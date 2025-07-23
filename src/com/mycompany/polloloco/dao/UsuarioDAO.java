package com.mycompany.polloloco.dao;

import com.mycompany.polloloco.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/** DAO para operaciones sobre la tabla usuario. */
public class UsuarioDAO {
    private static final String SQL_LOGIN =
            "SELECT u.id, u.nombre, u.usuario, u.clave, u.id_rol, r.nombre AS rol_nombre " +
            "FROM usuario u JOIN rol r ON u.id_rol=r.id WHERE u.usuario=? AND u.clave=?";

    /**
     * Valida las credenciales del usuario. Si son correctas retorna el
     * objeto Usuario con los datos obtenidos; de lo contrario retorna null.
     */
    public Usuario validarLogin(String usuario, String clave) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_LOGIN)) {
            ps.setString(1, usuario);
            ps.setString(2, clave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setUsuario(rs.getString("usuario"));
                    u.setClave(rs.getString("clave"));
                    u.setIdRol(rs.getInt("id_rol"));
                    u.setRolNombre(rs.getString("rol_nombre"));
                    return u;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
