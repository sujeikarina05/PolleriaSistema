package com.mycompany.polloloco.controlador;

import com.mycompany.polloloco.dao.UsuarioDAO;
import com.mycompany.polloloco.modelo.Usuario;

/** Controlador para operaciones de usuario. */
public class UsuarioController {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Devuelve true si las credenciales son válidas. Utiliza el DAO para
     * consultar la base de datos.
     */
    public boolean login(String usuario, String clave) {
        Usuario u = usuarioDAO.validarLogin(usuario, clave);
        return u != null;
    }
}
