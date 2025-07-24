package com.mycompany.polloloco.dao;

import com.mycompany.polloloco.modelo.DetallePedido;
import com.mycompany.polloloco.modelo.Pedido;
import com.mycompany.polloloco.modelo.enums.EstadoPedido;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO encargado de persistir la cabecera y el detalle de un {@link Pedido}.
 * <p>
 * Todas las operaciones que afecten a ambas tablas se ejecutan dentro de una
 * misma transacción para garantizar consistencia. El pool de conexiones es
 * provisto por {@link DatabaseConnection}.
 */
public class PedidoDAO {

    private static final Logger LOG = Logger.getLogger(PedidoDAO.class.getName());

    /* ====================================================================== */
    /* ==========   ALTAS   ================================================= */
    /* ====================================================================== */

    /**
     * Guarda cabecera + detalle en una única transacción.
     *
     * @param pedido entidad a persistir (con List<DetallePedido> ya cargado)
     * @return ID generado si todo fue correcto, <code>-1</code> en caso de error
     */
    public int insertar(Pedido pedido) {
        final String SQL_CAB =
                "INSERT INTO pedido (id_mesa, id_usuario, fecha_pedido, total, estado) " +
                "VALUES (?,?,?,?,?)";
        final String SQL_DET =
                "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio_unitario) " +
                "VALUES (?,?,?,?)";

        try (Connection cn = DatabaseConnection.getConnection()) {
            cn.setAutoCommit(false);
            int idGenerado;

            /* ---------- cabecera ---------- */
            try (PreparedStatement ps = cn.prepareStatement(SQL_CAB, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt      (1, pedido.getIdMesa());
                ps.setInt      (2, pedido.getIdUsuario());
                ps.setTimestamp(3, Timestamp.valueOf(pedido.getFechaPedido()));
                ps.setDouble   (4, pedido.getTotal());
                ps.setString   (5, pedido.getEstado().name());
                if (ps.executeUpdate() == 0) { cn.rollback(); return -1; }
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) { cn.rollback(); return -1; }
                    idGenerado = rs.getInt(1);
                }
            }

            /* ---------- detalle ---------- */
            try (PreparedStatement psDet = cn.prepareStatement(SQL_DET)) {
                for (DetallePedido d : pedido.getDetalle()) {
                    psDet.setInt   (1, idGenerado);
                    psDet.setInt   (2, d.getProducto().getId());
                    psDet.setInt   (3, d.getCantidad());
                    psDet.setDouble(4, d.getPrecioUnitario());
                    psDet.addBatch();
                }
                psDet.executeBatch();
            }

            cn.commit();
            return idGenerado;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al insertar pedido", ex);
            return -1;
        }
    }

    /* ====================================================================== */
    /* ==========   CONSULTAS   ============================================ */
    /* ====================================================================== */

    public List<Pedido> listar() {
        return listarPorEstado(null);
    }

    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        List<Pedido> lista = new ArrayList<>();
        StringBuilder sb = new StringBuilder("SELECT * FROM pedido");
        if (estado != null) sb.append(" WHERE estado=?");
        sb.append(" ORDER BY id DESC");

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sb.toString())) {
            if (estado != null) ps.setString(1, estado.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "listarPorEstado", ex); }
        return lista;
    }

    public List<Pedido> listarPorPeriodo(LocalDate ini, LocalDate fin) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE DATE(fecha_pedido) BETWEEN ? AND ? ORDER BY fecha_pedido DESC";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(ini));
            ps.setDate(2, Date.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "listarPorPeriodo", ex); }
        return lista;
    }

    public Optional<Pedido> buscarPorId(int id) {
        String sql = "SELECT * FROM pedido WHERE id=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "buscarPorId", ex); }
        return Optional.empty();
    }

    /* ====================================================================== */
    /* ==========   ACTUALIZACIONES   ====================================== */
    /* ====================================================================== */

    public boolean actualizar(Pedido p) {
        String sql = "UPDATE pedido SET id_mesa=?, id_usuario=?, total=?, estado=? WHERE id=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt   (1, p.getIdMesa());
            ps.setInt   (2, p.getIdUsuario());
            ps.setDouble(3, p.getTotal());
            ps.setString(4, p.getEstado().name());
            ps.setInt   (5, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "actualizar", ex); }
        return false;
    }

    public boolean actualizarEstado(int idPedido, EstadoPedido nuevoEstado) {
        String sql = "UPDATE pedido SET estado=? WHERE id=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setInt   (2, idPedido);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "actualizarEstado", ex); }
        return false;
    }

    /* ====================================================================== */
    /* ==========   BAJAS   ================================================= */
    /* ====================================================================== */

    /**
     * Eliminación lógica: cambia el estado a «Anulado».
     */
    public boolean eliminar(int idPedido) {
        return actualizarEstado(idPedido, EstadoPedido.Anulado);
    }

    /* ====================================================================== */
    /* ==========   HELPERS   ============================================== */
    /* ====================================================================== */

    private Pedido mapRow(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId          (rs.getInt("id"));
        p.setIdMesa      (rs.getInt("id_mesa"));
        p.setIdUsuario   (rs.getInt("id_usuario"));
        p.setFechaPedido (rs.getTimestamp("fecha_pedido").toLocalDateTime());
        p.setTotal       (rs.getDouble("total"));
        p.setEstado      (EstadoPedido.valueOf(rs.getString("estado")));
        return p; // detalle se carga aparte si se requiere
    }
}
