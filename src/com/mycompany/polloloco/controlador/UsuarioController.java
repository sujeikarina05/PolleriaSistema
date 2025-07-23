package com.mycompany.polloloco.controlador;

import com.mycompany.polloloco.dao.UsuarioDAO;
import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.Sesion;

/**
 * Controlador para la gestión de usuarios.
 */
public class UsuarioController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Valida las credenciales del usuario.
     * @param usuario nombre de usuario
     * @param clave contraseña
     * @return true si las credenciales son correctas, false en caso contrario
     */
    public boolean login(String usuario, String clave) {
        Usuario u = usuarioDAO.validarCredenciales(usuario, clave);
        if (u != null) {
            // Guardar en sesión
            Sesion.setUsuarioActual(u);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retorna el usuario autenticado actual desde la sesión.
     * @return objeto Usuario logueado
     */
    public Usuario getUsuarioActual() {
        return Sesion.getUsuarioActual();
    }
}
