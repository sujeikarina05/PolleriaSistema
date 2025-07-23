package com.mycompany.polloloco.modelo;

import java.util.List;

public class Pedido {
    private int id;
    private List<DetallePedido> detalles;

    public Pedido(int id, List<DetallePedido> detalles) {
        this.id = id;
        this.detalles = detalles;
    }

    public int getId() { return id; }
    public List<DetallePedido> getDetalles() { return detalles; }
}
