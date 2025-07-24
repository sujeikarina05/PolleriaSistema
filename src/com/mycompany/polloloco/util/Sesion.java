package com.mycompany.polloloco.util;

import com.mycompany.polloloco.modelo.Usuario;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Maneja la sesión del usuario autenticado en la aplicación.
 * <p>
 * ▸ Guarda al usuario actual<br>
 * ▸ Permite verificar expiración por tiempo inactivo<br>
 * ▸ Ofrece helpers para rol y cierre de sesión
 */
public final class Sesion {

    private static final Logger LOG = Logger.getLogger(Sesion.class.getName());

    /* ---------------- Campos de sesión ---------------- */
    private static Usuario usuarioActual;
    private static LocalDateTime instanteLogin;
    private static LocalDateTime ultimaActividad;

    /** Minutos de inactividad antes de expirar la sesión (0 = sin expiración). */
    private static int timeoutMinutos = 30; // configurable

    private Sesion() {}

    /* ---------------- API pública ---------------- */

    /**
     * Registra al usuario autenticado y reinicia el temporizador de actividad.
     */
    public static void iniciarSesion(Usuario usuario) {
        Objects.requireNonNull(usuario, "Usuario no puede ser null");
        usuarioActual   = usuario;
        instanteLogin   = LocalDateTime.now();
        ultimaActividad = instanteLogin;
        LOG.info(() -> "Usuario " + usuario.getUsuario() + " inició sesión a las " + instanteLogin);
    }

    /**
     * Devuelve el usuario autenticado, o {@code null} si no hay sesión.
     */
    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Marca actividad para evitar expiración.
     */
    public static void refreshActividad() {
        if (usuarioActual != null) {
            ultimaActividad = LocalDateTime.now();
        }
    }

    /**
     * Indica si existe una sesión válida y no expirada.
     */
    public static boolean isAutenticado() {
        if (usuarioActual == null) return false;
        if (timeoutMinutos <= 0)    return true; // sin expiración

        Duration inactivo = Duration.between(ultimaActividad, LocalDateTime.now());
        if (inactivo.toMinutes() >= timeoutMinutos) {
            LOG.info("Sesión expirada por inactividad (" + inactivo.toMinutes() + " min)");
            cerrarSesion();
            return false;
        }
        return true;
    }

    /**
     * Devuelve el nombre del rol del usuario autenticado (si existe).
     */
    public static Optional<String> getRolActual() {
        return Optional.ofNullable(usuarioActual).map(Usuario::getRol);
    }

    /**
     * Cierra la sesión actual.
     */
    public static void cerrarSesion() {
        if (usuarioActual != null) {
            LOG.info(() -> "Usuario " + usuarioActual.getUsuario() + " cerró sesión");
        }
        usuarioActual   = null;
        instanteLogin   = null;
        ultimaActividad = null;
    }

    /* ---------------- Configuración ---------------- */

    /**
     * Cambia el tiempo de expiración por inactividad.
     * @param minutos 0 = desactivar expiración.
     */
    public static void setTimeoutMinutos(int minutos) {
        timeoutMinutos = Math.max(minutos, 0);
    }

    /** Tiempo (en minutos) desde el login hasta ahora, si hay sesión. */
    public static long minutosDesdeLogin() {
        if (instanteLogin == null) return 0;
        return Duration.between(instanteLogin, LocalDateTime.now()).toMinutes();
    }

    /** Tiempo (en minutos) de inactividad actual. */
    public static long minutosInactivo() {
        if (ultimaActividad == null) return 0;
        return Duration.between(ultimaActividad, LocalDateTime.now()).toMinutes();
    }
}
