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
}
