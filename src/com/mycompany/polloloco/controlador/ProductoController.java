package com.mycompany.polloloco.controlador;

import com.mycompany.polloloco.dao.ProductoDAO;
import com.mycompany.polloloco.modelo.Producto;
import java.util.List;

/**
 * Controlador para gestionar operaciones sobre productos.
 */
public class ProductoController {

    private final ProductoDAO productoDAO;

    public ProductoController() {
        this.productoDAO = new ProductoDAO();
    }

    /**
     * Obtiene la lista completa de productos.
     * @return lista de productos
     */
    public List<Producto> listarProductos() {
        return productoDAO.listar();
    }

    /**
     * Registra un nuevo producto si los datos son válidos.
     * @param producto el producto a registrar
     * @return true si fue registrado correctamente
     */
    public boolean registrarProducto(Producto producto) {
        if (producto == null || producto.getNombre() == null || producto.getNombre().isEmpty()) {
            System.err.println("Error: El producto debe tener un nombre.");
            return false;
        }

        if (producto.getPrecio() <= 0) {
            System.err.println("Error: El precio debe ser mayor que cero.");
            return false;
        }

        if (producto.getStock() < 0) {
            System.err.println("Error: El stock no puede ser negativo.");
            return false;
        }

        return productoDAO.insertar(producto);
    }

    /**
     * Actualiza un producto existente.
     * @param producto producto con datos actualizados
     * @return true si fue actualizado correctamente
     */
    public boolean actualizarProducto(Producto producto) {
        if (producto == null || producto.getId() <= 0) {
            System.err.println("Error: Producto inválido para actualizar.");
            return false;
        }

        return productoDAO.actualizar(producto);
    }

    /**
     * Elimina un producto por ID.
     * @param idProducto ID del producto a eliminar
     * @return true si fue eliminado
     */
    public boolean eliminarProducto(int idProducto) {
        if (idProducto <= 0) {
            System.err.println("Error: ID de producto inválido.");
            return false;
        }

        return productoDAO.eliminar(idProducto);
    }

    /**
     * Busca productos por nombre (coincidencia parcial).
     * @param nombre nombre o parte del nombre
     * @return lista de coincidencias
     */
    public List<Producto> buscarPorNombre(String nombre) {
        return productoDAO.buscarPorNombre(nombre);
    }
}
