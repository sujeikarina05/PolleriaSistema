package com.mycompany.polloloco.controlador;

import com.mycompany.polloloco.dao.UsuarioDAO;
import com.mycompany.polloloco.modelo.Usuario;

/** Controlador para operaciones de usuario. */
public class UsuarioController {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Retorna el usuario autenticado o null si las credenciales no son válidas.
     */
    public Usuario login(String usuario, String clave) {
        return usuarioDAO.validarLogin(usuario, clave);
    }
}
