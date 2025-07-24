package com.mycompany.polloloco.dao;

import com.mycompany.polloloco.modelo.DetallePedido;
import com.mycompany.polloloco.modelo.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla pedido (cabecera) y detalle_pedido.
 */
public class PedidoDAO {

    /* ---------- INSERTAR (cabecera + detalle, en transacción) ---------- */
    public boolean guardar(Pedido pedido) {
        final String SQL_CAB = """
                INSERT INTO pedido (id_mesa, id_usuario, fecha_pedido, total)
                VALUES (?,?,?,?)
                """;
        final String SQL_DET = """
                INSERT INTO detalle_pedido
                       (id_pedido, id_producto, cantidad, precio_unitario)
                VALUES (?,?,?,?)
                """;

        try (Connection cn = DatabaseConnection.getConnection()) {
            cn.setAutoCommit(false);

            /* ----- cabecera ----- */
            int idGenerado;
            try (PreparedStatement ps = cn.prepareStatement(
                    SQL_CAB, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt      (1, pedido.getIdMesa());
                ps.setInt      (2, pedido.getIdUsuario());
                ps.setTimestamp(3, Timestamp.valueOf(pedido.getFechaPedido()));
                ps.setDouble   (4, pedido.getTotal());

                if (ps.executeUpdate() == 0) { cn.rollback(); return false; }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) { cn.rollback(); return false; }
                    idGenerado = rs.getInt(1);
                }
            }

            /* ----- detalle ----- */
            try (PreparedStatement psDet = cn.prepareStatement(SQL_DET)) {
                for (DetallePedido d : pedido.getDetalle()) {
                    psDet.setInt   (1, idGenerado);
                    psDet.setInt   (2, d.getProducto().getId());
                    psDet.setInt   (3, d.getCantidad());
                    psDet.setDouble(4, d.getProducto().getPrecio());
                    psDet.addBatch();
                }
                psDet.executeBatch();
            }

            cn.commit();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /* ---------- Métodos utilitarios / alias ---------- */

    /** Enlace “corto” para el controlador. */
    public boolean insertar(Pedido p) { return guardar(p); }

    /** Devuelve cabeceras de pedidos (uso en reportes). */
    public List<Pedido> listar() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido ORDER BY id DESC";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId          (rs.getInt("id"));
                p.setIdMesa      (rs.getInt("id_mesa"));
                p.setIdUsuario   (rs.getInt("id_usuario"));
                p.setFechaPedido (rs.getTimestamp("fecha_pedido").toLocalDateTime());
                p.setTotal       (rs.getDouble("total"));
                lista.add(p);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return lista;
    }

    /* Métodos de ejemplo no implementados todavía */
    public Pedido  buscarPorId(int id)   { return null; }
    public boolean eliminar(int id)      { return false; }
    public boolean actualizar(Pedido p)  { return false; }

    /* ---------- Extras útiles ---------- */

    public boolean actualizarEstado(int idPedido, String nuevoEstado) {
        String sql = "UPDATE pedido SET estado=? WHERE id=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt   (2, idPedido);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    public boolean existePedidoActivoEnMesa(int idMesa) {
        String sql = """
                SELECT 1 FROM pedido
                 WHERE id_mesa=? AND estado IN ('Pendiente','Preparación')
                """;
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idMesa);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }
}
