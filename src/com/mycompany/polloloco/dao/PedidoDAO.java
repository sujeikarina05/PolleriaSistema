package com.mycompany.polloloco.dao;

import com.mycompany.polloloco.modelo.Pedido;
import com.mycompany.polloloco.modelo.DetallePedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DAO para operaciones de la tabla Pedido.
 */
public class PedidoDAO {

    /**
     * Guarda un pedido y sus detalles en la base de datos.
     * @param pedido el objeto pedido con detalles
     * @return true si se guarda correctamente
     */
    public boolean guardar(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedido (id_mesa, id_usuario, fecha, total) VALUES (?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // transacción

            // Insertar pedido
            PreparedStatement psPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setInt(1, pedido.getIdMesa());
            psPedido.setInt(2, pedido.getIdUsuario());
            psPedido.setTimestamp(3, new java.sql.Timestamp(pedido.getFecha().getTime()));
            psPedido.setDouble(4, pedido.getTotal());

            int filas = psPedido.executeUpdate();
            if (filas == 0) {
                conn.rollback();
                return false;
            }

            // Obtener ID del pedido generado
            int idPedidoGenerado;
            try (var rs = psPedido.getGeneratedKeys()) {
                if (rs.next()) {
                    idPedidoGenerado = rs.getInt(1);
                } else {
                    conn.rollback();
                    return false;
                }
            }

            // Insertar detalles
            PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle);
            for (DetallePedido det : pedido.getDetalle()) {
                psDetalle.setInt(1, idPedidoGenerado);
                psDetalle.setInt(2, det.getProducto().getId());
                psDetalle.setInt(3, det.getCantidad());
                psDetalle.setDouble(4, det.getPrecioUnitario());
                psDetalle.addBatch();
            }
            psDetalle.executeBatch();

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al guardar pedido: " + e.getMessage());
            return false;
        }
    }

    // --- Métodos simplificados utilizados por el controlador ---

    /** Inserta un pedido (alias de guardar). */
    public boolean insertar(Pedido pedido) {
        return guardar(pedido);
    }

    /** Lista todos los pedidos. Implementación pendiente. */
    public java.util.List<Pedido> listar() {
        return new java.util.ArrayList<>();
    }

    /** Busca un pedido por ID. Implementación pendiente. */
    public Pedido buscarPorId(int id) {
        return null;
    }

    /** Elimina un pedido. Implementación pendiente. */
    public boolean eliminar(int id) {
        return false;
    }

    /** Actualiza un pedido. Implementación pendiente. */
    public boolean actualizar(Pedido pedido) {
        return false;
    }
}
