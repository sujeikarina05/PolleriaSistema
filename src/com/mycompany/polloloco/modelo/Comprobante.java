package com.mycompany.polloloco.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa un comprobante de pago (Boleta, Factura, Nota de crédito, etc.) emitido por
 * la Pollería Pollo Loco.
 */
public class Comprobante implements Serializable {

    /* ------------------ Enumeraciones ------------------ */
    public enum Tipo {
        BOLETA, FACTURA, NOTA_CREDITO, NOTA_DEBITO;
        public static Tipo from(String s) { return valueOf(s.toUpperCase()); }
    }

    public enum MetodoPago { EFECTIVO, TARJETA, YAPE, PLIN, TRANSFERENCIA }

    /* ------------------ Atributos ------------------ */
    private int id;                          // PK
    private String serie;                    // E001, F001, etc.
    private int correlativo;                 // 1…n  (reinicia por serie)
    private LocalDateTime fechaEmision;
    private Tipo tipo;
    private int idPedido;                    // FK -> pedido(id)
    private double total;
    private String nombreCliente;
    private String rucDni;
    private MetodoPago metodoPago;
    private boolean activo = true;           // Soft‑delete

    /* ------------------ Constructores ------------------ */
    public Comprobante() { }

    public Comprobante(String serie, int correlativo, Tipo tipo, int idPedido, double total,
                       String nombreCliente, String rucDni, MetodoPago mp) {
        this.serie = serie;
        this.correlativo = correlativo;
        this.fechaEmision = LocalDateTime.now();
        this.tipo = tipo;
        this.idPedido = idPedido;
        this.total = total;
        this.nombreCliente = nombreCliente;
        this.rucDni = rucDni;
        this.metodoPago = mp;
    }

    /* ------------------ Getters & Setters ------------------ */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    public int getCorrelativo() { return correlativo; }
    public void setCorrelativo(int correlativo) { this.correlativo = correlativo; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getRucDni() { return rucDni; }
    public void setRucDni(String rucDni) { this.rucDni = rucDni; }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    /* ------------------ Utilidades ------------------ */
    public String getNumeroCompleto() { return serie + "-" + String.format("%08d", correlativo); }

    @Override
    public String toString() {
        return getNumeroCompleto() + " – " + tipo + " S/ " + total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comprobante that = (Comprobante) o;
        return id == that.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
