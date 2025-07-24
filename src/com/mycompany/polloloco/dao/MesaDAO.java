package com.mycompany.polloloco.dao;

import com.mycompany.polloloco.modelo.Mesa;
import com.mycompany.polloloco.modelo.enums.EstadoMesa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO de acceso a la tabla <b>mesa</b>.
 * <p>
 * Estructura sugerida de la tabla:
 * <pre>
 * CREATE TABLE mesa (
 *   id         INT AUTO_INCREMENT PRIMARY KEY,
 *   numero     INT      NOT NULL UNIQUE,
 *   capacidad  INT      NOT NULL,
 *   estado     ENUM('Libre','Ocupada','Reservada') DEFAULT 'Libre'
 * );
 * </pre>
 */
public class MesaDAO {

    private static final Logger LOG = Logger.getLogger(MesaDAO.class.getName());

    /* ------------------------------------------------------ */
    /*                    Consultas básicas                    */
    /* ------------------------------------------------------ */

    public List<Mesa> listarTodas() {
        String sql = "SELECT * FROM mesa ORDER BY numero";
        List<Mesa> lista = new ArrayList<>();
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al listar mesas", ex);
        }
        return lista;
    }

    public List<Mesa> listarPorEstado(EstadoMesa estado) {
        String sql = "SELECT * FROM mesa WHERE estado = ? ORDER BY numero";
        List<Mesa> lista = new ArrayList<>();
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al filtrar mesas", ex);
        }
        return lista;
    }

    public Optional<Mesa> buscarPorId(int id) {
        String sql = "SELECT * FROM mesa WHERE id=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return Optional.empty();
    }

    /* ------------------------------------------------------ */
    /*                       Mutaciones                       */
    /* ------------------------------------------------------ */

    public boolean insertar(Mesa m) {
        String sql = "INSERT INTO mesa(numero, capacidad, estado) VALUES(?,?,?)";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, m.getNumero());
            ps.setInt(2, m.getCapacidad());
            ps.setString(3, m.getEstado().name());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al insertar mesa", ex);
        }
        return false;
    }

    public boolean actualizar(Mesa m) {
        String sql = "UPDATE mesa SET numero=?, capacidad=?, estado=? WHERE id=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, m.getNumero());
            ps.setInt(2, m.getCapacidad());
            ps.setString(3, m.getEstado().name());
            ps.setInt(4, m.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al actualizar mesa", ex);
        }
        return false;
    }

    public boolean cambiarEstado(int idMesa, EstadoMesa nuevoEstado) {
        String sql = "UPDATE mesa SET estado=? WHERE id=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, idMesa);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al cambiar estado de mesa", ex);
        }
        return false;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM mesa WHERE id=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al eliminar mesa", ex);
        }
        return false;
    }

    /* ------------------------------------------------------ */
    /*                 Helpers y mapeo de filas               */
    /* ------------------------------------------------------ */

    private Mesa mapRow(ResultSet rs) throws SQLException {
        return new Mesa(
                rs.getInt("id"),
                rs.getInt("numero"),
                rs.getInt("capacidad"),
                EstadoMesa.valueOf(rs.getString("estado"))
        );
    }
}