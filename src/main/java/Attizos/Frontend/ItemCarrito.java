package Attizos.Frontend;

import Attizos.Backend.Attizos.Producto;

public class ItemCarrito {
    private Producto producto;
    private int cantidad;
    private double subtotal;

    public ItemCarrito(Producto producto, int cantidad){
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = producto.getPrecio() * cantidad;
    }
    public String getNombreProducto(){
        return producto.getNombre();
    }
    public double getPrecioUnitario(){
        return producto.getPrecio();
    }
    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }
    public void aumentarCantidad(){
        this.cantidad ++;
        this.subtotal = this.producto.getPrecio() * this.cantidad;
    }
    public void setCantidad(int cantidad){
        this.cantidad=cantidad;
    }
    public void setSubtotal(double subtotal) {
            this.subtotal = subtotal;
    }
}
