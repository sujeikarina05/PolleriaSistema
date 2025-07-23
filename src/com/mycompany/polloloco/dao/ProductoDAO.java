package com.mycompany.polloloco.dao;

import com.mycompany.polloloco.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD sobre la tabla producto.
 */
public class ProductoDAO {

    /* ---------- SELECT ---------- */
    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return productos;
    }

    /* ---------- INSERT ---------- */
    public boolean insertar(Producto p) {
        String sql = """
            INSERT INTO producto (nombre, descripcion, precio, stock, id_categoria)
            VALUES (?,?,?,?,?)
            """;
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString (1, p.getNombre());
            ps.setString (2, p.getDescripcion());
            ps.setDouble (3, p.getPrecio());
            ps.setInt    (4, p.getStock());
            ps.setInt    (5, p.getIdCategoria());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    /* ---------- UPDATE ---------- */
    public boolean actualizar(Producto p) {
        String sql = """
            UPDATE producto
            SET nombre=?, descripcion=?, precio=?, stock=?, id_categoria=?
            WHERE id_producto=?
            """;
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString (1, p.getNombre());
            ps.setString (2, p.getDescripcion());
            ps.setDouble (3, p.getPrecio());
            ps.setInt    (4, p.getStock());
            ps.setInt    (5, p.getIdCategoria());
            ps.setInt    (6, p.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    /* ---------- DELETE ---------- */
    public boolean eliminar(int idProducto) {
        String sql = "DELETE FROM producto WHERE id_producto=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    /* ---------- SEARCH ---------- */
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE nombre LIKE ?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
        }
        return lista;
    }

    /* ---------- util ---------- */
    private Producto mapRow(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId         (rs.getInt   ("id_producto"));      // cambia a "id" si tu columna se llama así
        p.setNombre     (rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio     (rs.getDouble("precio"));
        p.setStock      (rs.getInt   ("stock"));
        p.setIdCategoria(rs.getInt   ("id_categoria"));
        return p;
    }
}
