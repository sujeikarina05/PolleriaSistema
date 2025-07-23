package com.mycompany.polloloco.util;

import com.mycompany.polloloco.modelo.Usuario;

/** Maneja el usuario autenticado actual. */
public final class Sesion {
    private static Usuario usuarioActual;

    private Sesion() {
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }
}
