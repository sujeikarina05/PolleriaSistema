package com.mycompany.polloloco.controlador;

import com.mycompany.polloloco.dao.ProductoDAO;
import com.mycompany.polloloco.modelo.Producto;
import com.mycompany.polloloco.util.ValidadorCampos;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Capa de orquestación entre la vista y el {@link ProductoDAO}. Implementa
 * reglas de negocio para altas, bajas y modificaciones de productos.
 */
public class ProductoController {

    /** DAO inyectable para pruebas unitarias */
    private final ProductoDAO dao;

    public ProductoController() {
        this.dao = new ProductoDAO();
    }

    public ProductoController(ProductoDAO dao) {
        this.dao = Objects.requireNonNull(dao);
    }

    /* --------------------------------------------------
     *  Operaciones de consulta
     * -------------------------------------------------- */

    public List<Producto> listarTodos() {
        return dao.listar();
    }

    public List<Producto> listarPorCategoria(int idCategoria) {
        return dao.listarPorCategoria(idCategoria);
    }

    public Optional<Producto> buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public List<Producto> buscarPorNombre(String texto) {
        return dao.buscarPorNombre(texto);
    }

    /* --------------------------------------------------
     *  Operaciones de mantenimiento
     * -------------------------------------------------- */

    public boolean crear(Producto p) {
        validarProducto(p, true);
        return dao.insertar(p).isPresent();
    }

    public boolean actualizar(Producto p) {
        validarProducto(p, false);
        return dao.actualizar(p);
    }

    public boolean eliminar(int idProducto) {
        if (idProducto <= 0) throw new IllegalArgumentException("ID de producto inválido");
        return dao.desactivar(idProducto);
    }

    /* --------------------------------------------------
     *  Reglas de negocio y validación
     * -------------------------------------------------- */

    private void validarProducto(Producto p, boolean isNuevo) {
        Objects.requireNonNull(p, "Producto nulo");
        if (!isNuevo && p.getId() <= 0)
            throw new IllegalArgumentException("ID de producto requerido para actualización");

        if (ValidadorCampos.esVacio(p.getNombre()))
            throw new IllegalArgumentException("El nombre es obligatorio");

        if (p.getPrecio() <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor a cero");

        if (p.getStock() < 0)
            throw new IllegalArgumentException("El stock no puede ser negativo");
    }
}
