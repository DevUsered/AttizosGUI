package Attizos.Backend.Attizos;

import Attizos.Backend.Listas.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Factura {
    private int numeroFactura;
    private LocalDateTime fecha;
    private String nombreCliente;
    private ListaDE<DetalleFactura> detalles;
    private double total;

    public Factura(int numeroFactura, String nombreCliente) {
        this.numeroFactura = numeroFactura;
        this.nombreCliente = nombreCliente;
        this.fecha = LocalDateTime.now(); // Guarda la fecha y hora exacta de la creación
        this.detalles = new ListaDE<>();
        this.total = 0.0;
    }

    // Método para agregar un producto a la factura
    // Método para agregar un producto a la factura
    // Método para agregar un producto a la factura
    public boolean agregarProducto(Producto producto, int cantidad, Inventario inventario) {
        if(producto != null && cantidad > 0){
            // CASO A: EL PRODUCTO SE COCINA (TIENE RECETA)
            if(producto.tieneReceta()){
                Receta receta = producto.getReceta();

                // 1. VALIDACIÓN ESTRICTA: Verificamos si hay stock VÁLIDO (no vencido)
                for(Map.Entry<String, Double> entry : receta.getIngredientes().entrySet()){
                    String codInsumoBase = entry.getKey();
                    double cantidadNecesariaTotal = entry.getValue() * cantidad;

                    double stockValidoDisponible = 0;
                    String nombreInsumo = codInsumoBase;

                    for (Insumo ins : inventario.getInventarioInsumos().values()) {
                        if (ins.getCodigo().equals(codInsumoBase) || ins.getCodigo().startsWith(codInsumoBase + "-L")) {
                            nombreInsumo = ins.getNombre().split(" \\(Lote")[0];
                            // Aquí está la clave: Solo sumamos si NO está vencido
                            if (ins.getStockActual() > 0 && !ins.isVencido()) {
                                stockValidoDisponible += ins.getStockActual();
                            }
                        }
                    }

                    if(stockValidoDisponible < cantidadNecesariaTotal){
                        System.out.println("❌ Error de Venta: No hay suficiente stock VÁLIDO de [" + nombreInsumo +
                                "] para preparar " + cantidad + "x " + producto.getNombre());
                        return false; // Cancelamos la venta antes de tocar nada
                    }
                }

                // 2. DESCUENTO EN COCINA (Ahora sabemos que es 100% seguro)
                for (Map.Entry<String, Double> entry : receta.getIngredientes().entrySet()) {
                    boolean exito = inventario.consumirInsumoFEFO(entry.getKey(), entry.getValue() * cantidad);
                    if (!exito) return false; // Por si acaso ocurre un error extraordinario
                }

                // 3. AGREGAMOS A LA FACTURA
                DetalleFactura nuevoDetalle = new DetalleFactura(producto, cantidad);
                detalles.insertarAlFinal(nuevoDetalle);
                calcularTotal();
                System.out.println("✅ " + cantidad + "x " + producto.getNombre() + " agregado (Ingredientes descontados por lotes).");
                return true;
            }
            // CASO B: PRODUCTO DIRECTO (EJ. BEBIDA CERRADA)
            else {
                if (producto.reducirStock(cantidad)) {
                    DetalleFactura nuevoDetalle = new DetalleFactura(producto, cantidad);
                    detalles.insertarAlFinal(nuevoDetalle);
                    calcularTotal();
                    System.out.println("✅ " + cantidad + "x " + producto.getNombre() + " agregado.");
                    return true;
                } else {
                    System.out.println("❌ Error: No hay stock suficiente. Quedan " + producto.getStock() + " unidades.");
                    return false;
                }
            }
        }
        System.out.println("Error: Producto no válido o cantidad no válida.");
        return false;
    }
    // Método para eliminar un producto de la factura
    public void eliminarProducto(Producto producto, Inventario inventario) {
        if (producto != null) {
            NodoDE<DetalleFactura> ac = detalles.getCabeza();
            while (ac != null) {
                DetalleFactura detalle = ac.getDato();

                if (detalle.getProducto().getId() == producto.getId()) {
                    int cantDevolver = detalle.getCantidad();

                    // Si tiene receta, devolvemos los ingredientes al inventario
                    if (producto.tieneReceta()) {
                        for (Map.Entry<String, Double> entry : producto.getReceta().getIngredientes().entrySet()) {
                            // Usamos registrarCompra para sumar el stock de vuelta
                            inventario.registrarCompra(entry.getKey(), entry.getValue() * cantDevolver);
                        }
                    } else {
                        // Si es directo, devolvemos la botella a la nevera
                        producto.aumentarStock(cantDevolver);
                    }

                    detalles.eliminarPorValor(detalle);
                    calcularTotal();
                    System.out.println("✅ Producto eliminado. Stock devuelto al almacén.");
                    return;
                }
                ac = ac.getSiguiente();
            }
            System.out.println("❌ Error: El producto no se encuentra en la factura.");
        }
    }
    public boolean modificarCantidad(Producto producto, int nuevaCantidad, Inventario inventario) {
        if (producto == null || nuevaCantidad < 0) {
            System.out.println("❌ Error: Producto o cantidad no válida.");
            return false;
        }

        NodoDE<DetalleFactura> ac = detalles.getCabeza();
        while (ac != null) {
            DetalleFactura detalle = ac.getDato();
            if (detalle.getProducto().getId() == producto.getId()) {
                int cantidadActual = detalle.getCantidad();

                if (nuevaCantidad == 0) {
                    eliminarProducto(producto, inventario);
                    return true;
                } else if (nuevaCantidad > cantidadActual) {
                    // Quiere MÁS. Simulamos agregar la diferencia.
                    int diferencia = nuevaCantidad - cantidadActual;

                    if (producto.tieneReceta()) {
                        // Verificar si hay stock para la diferencia
                        for (Map.Entry<String, Double> entry : producto.getReceta().getIngredientes().entrySet()) {
                            Insumo ins = inventario.buscarInsumo(entry.getKey());
                            if (ins == null || ins.getStockActual() < (entry.getValue() * diferencia)) {
                                System.out.println("❌ Error: No hay suficientes ingredientes para aumentar la cantidad.");
                                return false;
                            }
                        }
                        // Si hay, los consumimos
                        for (Map.Entry<String, Double> entry : producto.getReceta().getIngredientes().entrySet()) {
                            inventario.consumirInsumo(entry.getKey(), entry.getValue() * diferencia);
                        }
                        detalle.setCantidad(nuevaCantidad);
                        calcularTotal();
                        System.out.println("✅ Cantidad modificada a " + nuevaCantidad + ".");
                        return true;

                    } else { // Sin receta
                        if (producto.reducirStock(diferencia)) {
                            detalle.setCantidad(nuevaCantidad);
                            calcularTotal();
                            System.out.println("✅ Cantidad modificada a " + nuevaCantidad + ".");
                            return true;
                        } else {
                            System.out.println("❌ Error: No hay stock suficiente para aumentar.");
                            return false;
                        }
                    }

                } else if (nuevaCantidad < cantidadActual) {
                    // Quiere MENOS. Devolvemos la diferencia al almacén.
                    int diferencia = cantidadActual - nuevaCantidad;

                    if (producto.tieneReceta()) {
                        for (Map.Entry<String, Double> entry : producto.getReceta().getIngredientes().entrySet()) {
                            inventario.registrarCompra(entry.getKey(), entry.getValue() * diferencia);
                        }
                    } else {
                        producto.aumentarStock(diferencia);
                    }

                    detalle.setCantidad(nuevaCantidad);
                    calcularTotal();
                    System.out.println("✅ Cantidad reducida a " + nuevaCantidad + ". Ingredientes devueltos.");
                    return true;
                } else {
                    System.out.println("⚠️ La cantidad es la misma, no se realizaron cambios.");
                    return true;
                }
            }
            ac = ac.getSiguiente();
        }

        // Si no estaba en la factura y la cantidad es mayor a 0, lo agregamos nuevo
        if (nuevaCantidad > 0) {
            return agregarProducto(producto, nuevaCantidad, inventario);
        }
        return false;
    }

    // Método privado que suma los precios recorriendo tu ListaDE
    private void calcularTotal() {
        this.total = 0.0;
        NodoDE<DetalleFactura> actual = detalles.getCabeza();

        while (actual != null) {
            DetalleFactura det = actual.getDato();
            this.total += det.getSubtotal();
            actual = actual.getSiguiente();
        }
    }

    // Método para imprimir la factura en la consola (puro texto)
    public void imprimirFactura() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String fechaStr = fecha.format(formato);
        System.out.println("\n=========================================");
        System.out.println("             RESTAURANTE ATTIZOS         ");
        System.out.println("=========================================");
        System.out.printf("                  %03d\n", numeroFactura);
        System.out.println("-----------------------------------------");
        System.out.println("Fecha:       " + fechaStr);
        System.out.println("Cliente:     " + nombreCliente);
        System.out.println("-----------------------------------------");
        System.out.printf("%-5s | %-20s | %-10s\n", "CANT", "PRODUCTO", "SUBTOTAL");
        System.out.println("-----------------------------------------");

        NodoDE<DetalleFactura> actual = detalles.getCabeza();
        if (actual == null) {
            System.out.println("        (Sin productos registrados)      ");
        } else {
            while (actual != null) {
                DetalleFactura det = actual.getDato();
                System.out.printf("%-5d | %-20.20s | Bs.%8.2f\n",
                        det.getCantidad(), det.getProducto().getNombre(), det.getSubtotal());
                actual = actual.getSiguiente();
            }
        }
        System.out.println("-----------------------------------------");
        System.out.printf("TOTAL A PAGAR:               Bs.%8.2f\n", total);
        System.out.println("=========================================");
    }

    // --- Getters ---

    public int getNumeroFactura() {
        return numeroFactura;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public ListaDE<DetalleFactura> getDetalles() {
        return detalles;
    }

    public double getTotal() {
        return total;
    }
}