package com.mycompany.polloloco.controlador;

import com.mycompany.polloloco.dao.PedidoDAO;
import com.mycompany.polloloco.modelo.Pedido;
import com.mycompany.polloloco.modelo.Pedido.Estado;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Controlador para gestionar operaciones de {@link Pedido} aplicando reglas de negocio
 * antes de delegar en su respectivo {@link PedidoDAO}.
 */
public class PedidoController {

    /** DAO inyectable para facilitar pruebas unitarias */
    private final PedidoDAO pedidoDAO;

    /**
     * Construye el controlador usando la implementación por defecto de {@link PedidoDAO}.
     */
    public PedidoController() {
        this(new PedidoDAO());
    }

    public PedidoController(PedidoDAO pedidoDAO) {
        this.pedidoDAO = Objects.requireNonNull(pedidoDAO, "pedidoDAO no puede ser nulo");
    }

    /**
     * Valida y registra un nuevo pedido.
     * @param pedido entidad con cabecera + detalle
     * @return {@code true} si se insertó correctamente
     */
    public boolean registrarPedido(Pedido pedido) {
        if (!esPedidoValido(pedido)) return false;
        pedido.recalcularTotal();
        return pedidoDAO.insertar(pedido) > 0;
    }

    /**
     * Obtiene lista completa o filtrada por fecha.
     */
    public List<Pedido> listarPedidos()                   { return pedidoDAO.listar(); }
    public List<Pedido> listarPendientes()                { return pedidoDAO.listarPorEstado(Estado.PENDIENTE); }

    /**
     * Cambia el estado de un pedido.
     */
    public boolean actualizarEstado(int id, Estado estado) {
        return pedidoDAO.actualizarEstado(id, estado);
    }

    /** Marca un pedido como anulado (eliminación lógica). */
    public boolean anularPedido(int id) {
        return actualizarEstado(id, Estado.ANULADO);
    }

    /** Registra el pago de un pedido (estado → «Pagado» y registra caja). */
    public boolean registrarPago(int idPedido, double montoPagado) {
        Optional<Pedido> opt = pedidoDAO.buscarPorId(idPedido);
        if (opt.isEmpty()) return false;
        Pedido p = opt.get();
        // opcional: validar que monto sea >= total
        if (Double.compare(montoPagado, p.getTotal()) < 0) return false;
        p.setEstado(Estado.PAGADO);
        return pedidoDAO.actualizar(p);
    }

    /* -------------------------------------------------- */
    /*   MÉTODOS PRIVADOS                                 */
    /* -------------------------------------------------- */

    private boolean esPedidoValido(Pedido p) {
        if (p == null)                              return false;
        if (p.getIdMesa() <= 0)                     return false;
        if (p.getDetalle() == null || p.getDetalle().isEmpty()) return false;
        return true;
    }
}
