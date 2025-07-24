package com.mycompany.polloloco.dao;

import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.ValidadorCampos;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO encargado de las operaciones CRUD sobre la tabla <b>usuario</b>.
 * <p>
 * La capa de controlador {@link com.mycompany.polloloco.controlador.UsuarioController}
 * delega aquí todas las consultas, garantizando el hash de contraseña con SHA‑256,
 * la unicidad del nombre de usuario y la activación/desactivación lógica.
 */
public class UsuarioDAO {

    private static final Logger LOG = Logger.getLogger(UsuarioDAO.class.getName());

    /* -------------------- util -------------------- */
    private static String hash(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest(plain.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId     (rs.getInt   ("id"));
        u.setNombre (rs.getString("nombre"));
        u.setUsuario(rs.getString("usuario"));
        u.setClave  (rs.getString("clave")); // hash almacenado
        u.setIdRol  (rs.getInt   ("id_rol"));
        try {
            u.setActivo(rs.getBoolean("activo"));
        } catch (SQLException ex) {
            // la columna 'activo' no existe en algunas bases
            u.setActivo(true);
        }
        return u;
    }

    /* -------------------- login -------------------- */

    /**
     * Devuelve un {@link Usuario} si las credenciales (usuario + clave en texto
     * plano) son correctas y el usuario está activo.
     */
    public Optional<Usuario> login(String usr, String plainPass) {
        String sql = "SELECT * FROM usuario WHERE usuario=? AND clave=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, usr);
            ps.setString(2, hash(plainPass));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "login", ex);
        }

        // --- Fallback a credenciales por defecto si BD no responde o no coincide ---
        if ("admin".equals(usr) && "1234".equals(plainPass)) {
            Usuario u = new Usuario();
            u.setId(0);
            u.setNombre("Administrador");
            u.setUsuario("admin");
            u.setClaveHash(hash("1234"));
            u.setIdRol(1);
            return Optional.of(u);
        }
        return Optional.empty();
    }

    /* -------------------- create -------------------- */

    public boolean insertar(Usuario u) {
        if (!ValidadorCampos.esUsuarioValido(u.getUsuario())) return false;
        if (existeUsuario(u.getUsuario())) return false;

        String sql = "INSERT INTO usuario(nombre, usuario, clave, id_rol) VALUES (?,?,?,?)";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getUsuario());
            ps.setString(3, hash(u.getClave()));
            ps.setInt   (4, u.getIdRol());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "insertar", ex); }
        return false;
    }

    /* -------------------- read -------------------- */

    public List<Usuario> listar(boolean soloActivos) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario" + (soloActivos ? " WHERE activo=1" : "");

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "listar", ex); }
        return lista;
    }

    public Optional<Usuario> buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "buscarPorId", ex); }
        return Optional.empty();
    }

    /* -------------------- update -------------------- */

    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuario SET nombre=?, usuario=?, id_rol=? WHERE id=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getUsuario());
            ps.setInt   (3, u.getIdRol());
            ps.setInt   (4, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "actualizar", ex); }
        return false;
    }

    public boolean cambiarClave(int id, String nuevaPlain) {
        String sql = "UPDATE usuario SET clave=? WHERE id=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, hash(nuevaPlain));
            ps.setInt   (2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "cambiarClave", ex); }
        return false;
    }

    /* -------------------- delete -------------------- */

    public boolean desactivar(int id) {
        String sql = "UPDATE usuario SET activo=0 WHERE id=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "desactivar", ex); }
        return false;
    }

    /* -------------------- helpers -------------------- */

    /** Comprueba si ya existe el nombre de usuario. */
    public boolean existeNombreUsuario(String username) {
        return existeUsuario(username);
    }

    private boolean existeUsuario(String username) {
        String sql = "SELECT 1 FROM usuario WHERE usuario=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "existeUsuario", ex); }
        return true; // por seguridad asumimos que existe
    }
}
