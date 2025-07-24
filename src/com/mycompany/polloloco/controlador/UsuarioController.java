package com.mycompany.polloloco.controlador;

import com.mycompany.polloloco.dao.UsuarioDAO;
import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.Sesion;
import com.mycompany.polloloco.util.ValidadorCampos;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador responsable de la lógica de negocio asociada a {@link Usuario}.
 * Encapsula validaciones antes de delegar en {@link UsuarioDAO}.
 */
public class UsuarioController {

    private static final Logger LOG = Logger.getLogger(UsuarioController.class.getName());

    private final UsuarioDAO usuarioDAO;

    /* --------------------------------------------------
     *  Constructores
     * -------------------------------------------------- */

    /** Constructor por defecto que crea su propio DAO – uso producción. */
    public UsuarioController() {
        this(new UsuarioDAO());
    }

    /**
     * Constructor inyectable – útil para pruebas unitarias (mock DAO).
     *
     * @param usuarioDAO dependencia a inyectar (no nulo)
     */
    public UsuarioController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = Objects.requireNonNull(usuarioDAO, "UsuarioDAO no puede ser null");
    }

    /* --------------------------------------------------
     *  Autenticación
     * -------------------------------------------------- */

    /**
     * Valida las credenciales contra la base de datos. Si son correctas, la
     * sesión global se inicializa con el usuario devuelto.
     *
     * @param user  nombre de usuario (no vacío)
     * @param pass  contraseña en texto plano (no vacío)
     * @return      {@code true} si las credenciales son válidas
     */
    public boolean login(String user, String pass) {
        if (ValidadorCampos.esVacio(user) || ValidadorCampos.esVacio(pass)) {
            LOG.warning("Credenciales vacías");
            return false;
        }
        return usuarioDAO.login(user, pass)
                .map(u -> { Sesion.iniciarSesion(u); return true; })
                .orElse(false);
    }

    /** Finaliza la sesión actual. */
    public void logout() { Sesion.cerrarSesion(); }

    /** @return Usuario autenticado o {@code null} si nadie inició sesión. */
    public Usuario getUsuarioActual() { return Sesion.getUsuarioActual(); }

    /* --------------------------------------------------
     *  CRUD de usuarios
     * -------------------------------------------------- */

    public List<Usuario> listarUsuarios() { return usuarioDAO.listar(true); }

    public Optional<Usuario> buscarPorId(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido");
        return usuarioDAO.buscarPorId(id);
    }

    public boolean registrarUsuario(Usuario u) {
        validarNuevoUsuario(u);
        return usuarioDAO.insertar(u);
    }

    public boolean actualizarUsuario(Usuario u) {
        Objects.requireNonNull(u, "Usuario null");
        if (u.getId() <= 0) throw new IllegalArgumentException("ID inválido");
        validarCamposBasicos(u);
        return usuarioDAO.actualizar(u);
    }

    public boolean eliminarUsuario(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido");
        if (id == getUsuarioActual().getId()) {
            LOG.warning("Intento de auto‑eliminación impedido");
            return false;
        }
        return usuarioDAO.desactivar(id);
    }

    /** Cambio de contraseña con validación de fuerza mínima. */
    public boolean cambiarPassword(int id, String nuevaClave) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido");
        if (ValidadorCampos.esVacio(nuevaClave) || nuevaClave.length() < 6) {
            LOG.log(Level.WARNING, "Clave demasiado corta");
            return false;
        }
        return usuarioDAO.cambiarClave(id, nuevaClave);
    }

    /* --------------------------------------------------
     *  Validaciones privadas
     * -------------------------------------------------- */

    private void validarNuevoUsuario(Usuario u) {
        Objects.requireNonNull(u, "Usuario null");
        validarCamposBasicos(u);
        if (ValidadorCampos.esVacio(u.getClave()))
            throw new IllegalArgumentException("La contraseña es obligatoria");
        if (usuarioDAO.existeNombreUsuario(u.getUsuario()))
            throw new IllegalStateException("El nombre de usuario ya existe");
    }

    private void validarCamposBasicos(Usuario u) {
        if (ValidadorCampos.esVacio(u.getNombre()))
            throw new IllegalArgumentException("Nombre requerido");
        if (ValidadorCampos.esVacio(u.getUsuario()))
            throw new IllegalArgumentException("Nombre de usuario requerido");
    }

    /* --------------------------------------------------
     *  Acceso a DAO (p. ej. para diálogos de la vista)
     * -------------------------------------------------- */

    public UsuarioDAO getDao() { return usuarioDAO; }
}
