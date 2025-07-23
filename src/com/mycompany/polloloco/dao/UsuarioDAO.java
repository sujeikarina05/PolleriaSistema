package com.mycompany.polloloco.dao;

import com.mycompany.polloloco.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO para validar credenciales de usuario contra la base de datos.
 */
public class UsuarioDAO {

    /**
     * Valida las credenciales del usuario.
     * @param usuario nombre de usuario
     * @param clave contraseña
     * @return objeto Usuario si es válido, null si no existe
     */
    public Usuario validarCredenciales(String usuario, String clave) {
        String sql = "SELECT * FROM usuario WHERE usuario = ? AND clave = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, clave);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Obtener el nombre del rol desde la tabla "rol"
                String rol = obtenerNombreRol(conn, rs.getInt("id_rol"));

                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("usuario"));
                u.setClave(rs.getString("clave"));
                u.setRol(rol); // nombre del rol (Administrador, Mozo, etc.)

                return u;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al validar usuario: " + e.getMessage());
        }

        return null; // Usuario no encontrado
    }

    /**
     * Obtiene el nombre del rol a partir de su ID.
     * @param conn conexión activa
     * @param idRol ID del rol
     * @return nombre del rol
     */
    private String obtenerNombreRol(Connection conn, int idRol) throws SQLException {
        String sql = "SELECT nombre FROM rol WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("nombre");
            }
        }
        return "Desconocido";
    }

    /* ------------------------------------------------------------------ */
    /*  Métodos CRUD adicionales para la gestión de usuarios              */
    /* ------------------------------------------------------------------ */

    /**
     * Lista todos los usuarios registrados en la base de datos.
     */
    public java.util.List<Usuario> listar() {
        java.util.List<Usuario> lista = new java.util.ArrayList<>();
        String sql = "SELECT u.id, u.nombre, u.usuario, u.clave, r.nombre AS rol "
                   + "FROM usuario u JOIN rol r ON u.id_rol = r.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("usuario"));
                u.setClave(rs.getString("clave"));
                u.setRol(rs.getString("rol"));
                lista.add(u);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Inserta un nuevo usuario.
     */
    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuario(nombre, usuario, clave, id_rol) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getUsuario());
            ps.setString(3, u.getClave());
            // suponer que el rol viene como nombre -> obtener id
            ps.setInt(4, obtenerIdRol(conn, u.getRol()));
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los datos de un usuario existente.
     */
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuario SET nombre=?, usuario=?, clave=?, id_rol=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getUsuario());
            ps.setString(3, u.getClave());
            ps.setInt(4, obtenerIdRol(conn, u.getRol()));
            ps.setInt(5, u.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    /** Elimina un usuario por ID. */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    /** Obtiene el ID del rol a partir de su nombre. */
    private int obtenerIdRol(Connection conn, String nombreRol) throws SQLException {
        String sql = "SELECT id FROM rol WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreRol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return 0;
    }
}
