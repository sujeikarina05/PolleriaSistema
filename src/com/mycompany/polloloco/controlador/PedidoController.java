package com.mycompany.polloloco.controlador;

import com.mycompany.polloloco.dao.PedidoDAO;
import com.mycompany.polloloco.modelo.Pedido;

import java.util.List;

/**
 * Controlador para operaciones de Pedido.
 */
public class PedidoController {

    private final PedidoDAO pedidoDAO = new PedidoDAO();

    /** Valida y registra un nuevo pedido. */
    public boolean registrarPedido(Pedido pedido) {
        if (pedido == null) {
            System.err.println("Pedido nulo");
            return false;
        }
        if (pedido.getIdMesa() <= 0) {
            System.err.println("Mesa inválida");
            return false;
        }
        if (pedido.getDetalle() == null || pedido.getDetalle().isEmpty()) {
            System.err.println("Pedido sin productos");
            return false;
        }
        // asegúrate de que el total esté calculado
        pedido.recalcularTotal();
        return pedidoDAO.insertar(pedido);
    }

    public List<Pedido> listarPedidos() { return pedidoDAO.listar(); }

    /* ejemplos de delegación */
    public boolean actualizarEstado(int id, String estado) {
        return pedidoDAO.actualizarEstado(id, estado);
    }
}
