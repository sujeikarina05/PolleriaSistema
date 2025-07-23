package com.mycompany.polloloco.dao;

public class UsuarioDAO {
    public boolean validarCredenciales(String usuario, String clave) {
        return "admin".equals(usuario) && "1234".equals(clave);
    }
}
