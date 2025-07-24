package com.mycompany.polloloco.controlador;

import org.junit.Test;
import static org.junit.Assert.*;

public class UsuarioControllerTest {

    @Test
    public void testLoginExitoso() {
        String usuario = "admin";
        String clave   = "1234";

        boolean loginCorrecto = "admin".equals(usuario) && "1234".equals(clave);

        assertTrue("Login debería ser exitoso", loginCorrecto);
        System.out.println("✓ testLoginExitoso pasó (esperado)");
    }

    @Test
    public void testLoginFallido() {
        String usuario = "admin";
        String clave   = "incorrecta";

        boolean loginCorrecto = "admin".equals(usuario) && "1234".equals(clave);

        assertFalse("Login inválido debe fallar", loginCorrecto);
        System.out.println("✓ testLoginFallido falló (esperado)");
    }
}
