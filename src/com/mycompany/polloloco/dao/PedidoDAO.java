package com.mycompany.polloloco.dao;

import com.mycompany.polloloco.modelo.DetallePedido;
import com.mycompany.polloloco.modelo.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla pedido y su detalle.
 */
public class PedidoDAO {

    /* ---------- INSERTAR (cabecera + detalle en transacción) ---------- */
    public boolean guardar(Pedido pedido) {
        String sqlCab = """
            INSERT INTO pedido (id_mesa, id_usuario, fecha_pedido, total)
            VALUES (?,?,?,?)
            """;
        String sqlDet = """
            INSERT INTO detalle_pedido
            (id_pedido, id_producto, cantidad, precio_unitario)
            VALUES (?,?,?,?)
            """;

        try (Connection cn = DatabaseConnection.getConnection()) {
            cn.setAutoCommit(false);

            /* Cabecera */
            int idGen;
            try (PreparedStatement ps = cn.prepareStatement(sqlCab,
                    Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt    (1, pedido.getIdMesa());
                ps.setInt    (2, pedido.getIdUsuario());
                ps.setTimestamp(3, Timestamp.valueOf(pedido.getFechaPedido()));
                ps.setDouble (4, pedido.getTotal());

                if (ps.executeUpdate() == 0) { cn.rollback(); return false; }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) idGen = rs.getInt(1);
                    else { cn.rollback(); return false; }
                }
            }

            /* Detalle */
            try (PreparedStatement psDet = cn.prepareStatement(sqlDet)) {
                for (DetallePedido d : pedido.getDetalle()) {
                    psDet.setInt   (1, idGen);
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

    /* Alias para el controlador */
    public boolean insertar(Pedido p)            { return guardar(p); }

    /* ---------- SELECTS BÁSICOS (implementaciones mínimas) ---------- */

    /** Devuelve todos los pedidos con total y fecha; sin detalle para agilizar. */
    public List<Pedido> listar() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido ORDER BY id DESC";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId               (rs.getInt("id"));
                p.setIdMesa           (rs.getInt("id_mesa"));
                p.setIdUsuario        (rs.getInt("id_usuario"));
                p.setFechaPedido      (rs.getTimestamp("fecha_pedido").toLocalDateTime());
                p.setTotal            (rs.getDouble("total"));
                lista.add(p);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return lista;
    }

    /** Trae cabecera y detalle de un pedido. */
    public Pedido buscarPorId(int id) {
        // Implementar si lo necesitas en ReporteFrame o ComprobanteFrame
        return null;
    }

    public boolean eliminar(int id)   { /* opcional */ return false; }
    public boolean actualizar(Pedido p){ /* opcional */ return false; }

    /* ---------- Métodos de apoyo para Mozo/Cajero ---------- */

    /** Cambia el estado del pedido (Pendiente, Preparación, Entregado, Cancelado). */
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

    /** Verifica si existe un pedido activo en una mesa (para evitar duplicidad). */
    public boolean existePedidoActivoEnMesa(int idMesa) {
        String sql = "SELECT 1 FROM pedido WHERE id_mesa=? AND estado IN ('Pendiente','Preparación')";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idMesa);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }
}
