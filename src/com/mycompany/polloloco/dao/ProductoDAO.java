package com.mycompany.polloloco.dao;

import com.mycompany.polloloco.modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO encargado del acceso a la tabla <b>producto</b>.
 * <p>
 * Implementa operaciones CRUD y búsquedas auxiliares, utilizando el pool
 * provisto por {@link DatabaseConnection}.
 */
public class ProductoDAO {

    private static final Logger LOG = Logger.getLogger(ProductoDAO.class.getName());

    /* --------------------------------------------------------------------- */
    /*                               QUERIES                                 */
    /* --------------------------------------------------------------------- */

    private static final String Q_SELECT_ALL =
            "SELECT * FROM producto WHERE activo = 1 ORDER BY id DESC";

    private static final String Q_SELECT_BY_ID =
            "SELECT * FROM producto WHERE id = ? AND activo = 1";

    private static final String Q_SELECT_BY_CAT =
            "SELECT * FROM producto WHERE id_categoria = ? AND activo = 1 ORDER BY nombre";

    private static final String Q_SEARCH_BY_NAME =
            "SELECT * FROM producto WHERE nombre LIKE ? AND activo = 1 ORDER BY nombre";

    private static final String Q_DUPLICATE_NAME =
            "SELECT 1 FROM producto WHERE nombre = ? AND activo = 1";

    private static final String Q_INSERT =
            "INSERT INTO producto (nombre, descripcion, precio, stock, id_categoria, activo)\n" +
            "VALUES (?,?,?,?,?,1)";

    private static final String Q_UPDATE =
            "UPDATE producto SET nombre=?, descripcion=?, precio=?, stock=?, id_categoria=? WHERE id=?";

    private static final String Q_SOFT_DELETE =
            "UPDATE producto SET activo = 0 WHERE id = ?";

    private static final String Q_UPDATE_STOCK =
            "UPDATE producto SET stock = ? WHERE id = ?";

    /* --------------------------------------------------------------------- */
    /*                               MÉTODOS                                  */
    /* --------------------------------------------------------------------- */

    /** Devuelve todos los productos activos. */
    public List<Producto> listar() {
        return listar(Q_SELECT_ALL, ps -> {});
    }

    /** Devuelve todos los productos de una categoría. */
    public List<Producto> listarPorCategoria(int idCategoria) {
        return listar(Q_SELECT_BY_CAT, ps -> ps.setInt(1, idCategoria));
    }

    public Optional<Producto> buscarPorId(int id) {
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(Q_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "buscarPorId", ex); }
        return Optional.empty();
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return listar(Q_SEARCH_BY_NAME, ps -> ps.setString(1, "%" + nombre + "%"));
    }

    /** Inserta un producto válido. Devuelve el ID generado. */
    public Optional<Integer> insertar(Producto p) {
        if (nombreDuplicado(p.getNombre())) return Optional.empty();
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(Q_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setInt   (4, p.getStock());
            ps.setInt   (5, p.getIdCategoria());
            if (ps.executeUpdate() == 0) return Optional.empty();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return Optional.of(rs.getInt(1));
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "insertar", ex); }
        return Optional.empty();
    }

    public boolean actualizar(Producto p) {
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(Q_UPDATE)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setInt   (4, p.getStock());
            ps.setInt   (5, p.getIdCategoria());
            ps.setInt   (6, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "actualizar", ex); }
        return false;
    }

    /** Desactiva lógicamente un producto. */
    public boolean desactivar(int id) {
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(Q_SOFT_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "desactivar", ex); }
        return false;
    }

    public boolean actualizarStock(int id, int nuevoStock) {
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(Q_UPDATE_STOCK)) {
            ps.setInt(1, nuevoStock);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "actualizarStock", ex); }
        return false;
    }

    /* --------------------------------------------------------------------- */
    /*                            MÉTODOS PRIVADOS                           */
    /* --------------------------------------------------------------------- */

    private List<Producto> listar(String sql, SQLConsumer<PreparedStatement> binder) {
        List<Producto> lista = new ArrayList<>();
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            binder.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "listar", ex); }
        return lista;
    }

    private boolean nombreDuplicado(String nombre) {
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(Q_DUPLICATE_NAME)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "nombreDuplicado", ex); }
        return true; // por seguridad, considerar duplicado si hay error
    }

    private Producto mapRow(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId         (rs.getInt   ("id"));
        p.setNombre     (rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio     (rs.getDouble("precio"));
        p.setStock      (rs.getInt   ("stock"));
        p.setIdCategoria(rs.getInt   ("id_categoria"));
        return p;
    }

    /* --------------------------------------------------------------------- */
    /*                               INTERFAZ                                */
    /* --------------------------------------------------------------------- */

    @FunctionalInterface
    private interface SQLConsumer<T> { void accept(T t) throws SQLException; }
}
