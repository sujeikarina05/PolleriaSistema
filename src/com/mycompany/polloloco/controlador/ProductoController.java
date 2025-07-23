package com.mycompany.polloloco.controlador;

import com.mycompany.polloloco.dao.ProductoDAO;
import com.mycompany.polloloco.modelo.Producto;
import java.util.List;

/** Controlador para operaciones con productos. */
public class ProductoController {
    private final ProductoDAO productoDAO = new ProductoDAO();

    /**
     * Obtiene la lista de productos registrados en la base de datos.
     * @return lista de productos
     */
    public List<Producto> listarProductos() {
        return productoDAO.listar();
    }
}
