package com.mycompany.polloloco.controlador;

import com.mycompany.polloloco.dao.PedidoDAO;
import com.mycompany.polloloco.modelo.Pedido;
import java.util.List;

/**
 * Controlador para la gestión de pedidos.
 * Aplica la lógica de negocio antes de interactuar con el DAO.
 */
public class PedidoController {

    private final PedidoDAO pedidoDAO;

    public PedidoController() {
        this.pedidoDAO = new PedidoDAO();
    }

    /**
     * Registra un nuevo pedido si cumple con las condiciones mínimas.
     * @param pedido el objeto Pedido a registrar
     * @return true si se registró exitosamente, false si falló por validación
     */
    public boolean registrarPedido(Pedido pedido) {
        if (pedido == null) {
            System.err.println("Error: El pedido es nulo.");
            return false;
        }

        if (pedido.getIdMesa() <= 0) {
            System.err.println("Error: La mesa no es válida.");
            return false;
        }

        if (pedido.getDetalle() == null || pedido.getDetalle().isEmpty()) {
            System.err.println("Error: El pedido no contiene productos.");
            return false;
        }

        return pedidoDAO.insertar(pedido);
    }

    /**
     * Lista todos los pedidos registrados en el sistema.
     * @return lista de pedidos
     */
    public List<Pedido> listarPedidos() {
        return pedidoDAO.listar();
    }

    /**
     * Busca un pedido por su ID.
     * @param idPedido ID del pedido
     * @return objeto Pedido o null si no se encuentra
     */
    public Pedido buscarPedidoPorId(int idPedido) {
        return pedidoDAO.buscarPorId(idPedido);
    }

    /**
     * Elimina un pedido existente.
     * @param idPedido ID del pedido a eliminar
     * @return true si fue eliminado correctamente
     */
    public boolean eliminarPedido(int idPedido) {
        return pedidoDAO.eliminar(idPedido);
    }

    /**
     * Actualiza un pedido existente.
     * @param pedido objeto con los datos actualizados
     * @return true si fue actualizado correctamente
     */
    public boolean actualizarPedido(Pedido pedido) {
        return pedidoDAO.actualizar(pedido);
    }
}
