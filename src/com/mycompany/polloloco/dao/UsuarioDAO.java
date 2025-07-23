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
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("usuario"));
                u.setClave(rs.getString("clave"));
                u.setRol(rs.getString("rol"));
                return u;
            }

        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
        }

        return null; // no encontrado
    }
}
