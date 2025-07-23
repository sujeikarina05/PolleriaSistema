package com.mycompany.polloloco.controlador;

import com.mycompany.polloloco.dao.UsuarioDAO;

public class UsuarioController {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public boolean login(String usuario, String clave) {
        return usuarioDAO.validarCredenciales(usuario, clave);
    }
}
