package Attizos.Backend.Attizos;

import Attizos.Backend.Listas.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pedido {
    private int idPedido;
    private String fechaHora;
    private ListaDE<DetalleFactura> productos;
    private double total;
    private String estado;
    private String cliente;


    public Pedido(int idPedido, String cliente, ListaDE<DetalleFactura> productos, double total) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.productos = productos;
        this.total = total;
        this.estado = "En Espera";


        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");
        this.fechaHora = LocalDateTime.now().format(formato);
    }

    public int getIdPedido() { return idPedido; }
    public String getFechaHora() { return fechaHora; }
    public ListaDE<DetalleFactura> getProductos() { return productos; }
    public double getTotal() { return total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCliente() { return cliente; }


    public void mostrarPedido() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("📋 Pedido Nro: %-3d | Hora: %s | Cliente: %-15s | Estado: [%s]\n",
                idPedido, fechaHora, cliente, estado));
        sb.append("   🍳 A PREPARAR:\n");
        NodoDE<DetalleFactura> actual = productos.getCabeza();
        while (actual != null) {
            DetalleFactura det = actual.getDato();
            sb.append(String.format("      👉 %d x %s\n", det.getCantidad(), det.getProducto().getNombre()));
            actual = actual.getSiguiente();
        }
        sb.append("----------------------------------------------------------------");

        return sb.toString();
    }
}