package Attizos.Backend.Attizos;

import Attizos.Backend.Listas.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Reporte {

    public static void generarReporteDiario(Restaurante restaurante) {
        LocalDate hoy = LocalDate.now();
        System.out.println("\n=================================================");
        System.out.println("        REPORTE DE CAJA DIARIO - " + hoy);
        System.out.println("=================================================");

        double totalIngresos = 0.0;
        double totalEgresos = 0.0;
        int cantidadPedidos = 0;
        HashMap<String, Integer> productosVendidos = new HashMap<>();

        // 1. Calcular Ventas
        NodoDE<Factura> auxF = restaurante.getHistorialVentas().getCabeza();
        while (auxF != null) {
            Factura f = auxF.getDato();
            // Solo procesamos las facturas de HOY
            if (f.getFecha().toLocalDate().isEqual(hoy)) {
                totalIngresos += f.getTotal();
                cantidadPedidos++;

                // Contabilizar productos vendidos de hoy
                NodoDE<DetalleFactura> auxD = f.getDetalles().getCabeza();
                while(auxD != null){
                    String nombreP = auxD.getDato().getProducto().getNombre();
                    int cant = auxD.getDato().getCantidad();
                    productosVendidos.put(nombreP, productosVendidos.getOrDefault(nombreP,0) + cant);
                    auxD = auxD.getSiguiente();
                }
            }
            auxF = auxF.getSiguiente();
        }
        System.out.println("DETALLE DE GASTOS DE HOY:");
        boolean hayGastosHoy = false;
        NodoDE<Egreso> auxE = restaurante.getExpenseHistory().getCabeza();
        while(auxE != null) {
            if(auxE.getDato().getDate().isEqual(hoy)) {
                System.out.printf("  - %-25s : Bs. %.2f\n", auxE.getDato().getDescription(), auxE.getDato().getTotalAmount());
                totalEgresos += auxE.getDato().getTotalAmount();
                hayGastosHoy = true;
            }
            auxE = auxE.getSiguiente();
        }
        if (!hayGastosHoy) {
            System.out.println("  (No se registraron gastos hoy)");
        }
        System.out.println("-------------------------------------------------");
        System.out.printf("Pedidos despachados hoy: %d\n", cantidadPedidos);
        System.out.printf("📈 INGRESOS TOTALES:        Bs. %.2f\n", totalIngresos);
        System.out.printf("📉 EGRESOS (COMPRAS):       Bs. %.2f\n", totalEgresos);
        System.out.printf("💰 UTILIDAD BRUTA DEL DÍA:  Bs. %.2f\n", (totalIngresos - totalEgresos));
        System.out.println("-------------------------------------------------");
        System.out.println("RESUMEN DE PRODUCTOS VENDIDOS HOY:");

        if(productosVendidos.isEmpty()){
            System.out.println("  (No se registraron ventas hoy)");
        }else{
            // Un pequeño ordenamiento rápido para mostrar los más vendidos del día arriba
            ArrayList<Map.Entry<String, Integer>> listaVentas = new ArrayList<>(productosVendidos.entrySet());
            listaVentas.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            for (Map.Entry<String, Integer> entry : listaVentas) {
                System.out.printf("  - %-20s : %d unidades\n", entry.getKey(), entry.getValue());
            }
        }
        System.out.println("=================================================");
    }
    public static void mostrarDetalleEgresosMensual(Restaurante restaurante, int mes, int anio){
        System.out.println("\n=================================================");
        System.out.println("      DETALLE DE EGRESOS - " + mes + "/" + anio);
        System.out.println("=================================================");

        double totalEgresos = 0;
        boolean hayEgresos = false;

        NodoDE<Egreso> aux = restaurante.getExpenseHistory().getCabeza();
        while(aux != null){
            Egreso e = aux.getDato();
            if(e.getDate().getMonthValue() == mes && e.getDate().getYear() == anio){
                System.out.printf("[%s] %-30s | Bs. %.2f\n",
                        e.getDate().toString(), e.getDescription(), e.getTotalAmount());
                totalEgresos += e.getTotalAmount();
                hayEgresos = true;
            }
            aux = aux.getSiguiente();
        }
        if (!hayEgresos) {
            System.out.println("  (No se registraron gastos en este mes)");
        }
        System.out.println("-------------------------------------------------");
        System.out.printf("TOTAL GASTADO EN EL MES: Bs. %.2f\n", totalEgresos);
        System.out.println("=================================================");
    }
    public static void generarReporteMensual(Restaurante restaurante, int mes, int anio) {
        System.out.println("\n=================================================");
        System.out.println("       REPORTE FINANCIERO MENSUAL - " + mes + "/" + anio);
        System.out.println("=================================================");

        double totalIngresosMes = 0.0;
        double totalEgresosMes = 0.0;
        int cantidadPedidosMes = 0;
        HashMap<String, Integer> productosVendidosMes = new HashMap<>();

        NodoDE<Factura> auxF = restaurante.getHistorialVentas().getCabeza();
        while (auxF != null) {
            Factura f = auxF.getDato();
            // Filtramos por mes y año
            if (f.getFecha().getMonthValue() == mes && f.getFecha().getYear() == anio) {
                totalIngresosMes += f.getTotal();
                cantidadPedidosMes++;

                NodoDE<DetalleFactura> auxD = f.getDetalles().getCabeza();
                while (auxD != null) {
                    String nombreP = auxD.getDato().getProducto().getNombre();
                    int cant = auxD.getDato().getCantidad();
                    productosVendidosMes.put(nombreP, productosVendidosMes.getOrDefault(nombreP, 0) + cant);
                    auxD = auxD.getSiguiente();
                }
            }
            auxF = auxF.getSiguiente();
        }
        NodoDE<Egreso> auxE = restaurante.getExpenseHistory().getCabeza();
        while(auxE != null){
            Egreso e = auxE.getDato();
            if (e.getDate().getMonthValue() == mes && e.getDate().getYear() == anio) {
                totalEgresosMes += e.getTotalAmount();
            }
            auxE = auxE.getSiguiente();
        }

        System.out.printf("Total de pedidos en el mes: %d\n", cantidadPedidosMes);
        System.out.println("-------------------------------------------------");
        System.out.printf("📈 INGRESOS TOTALES DEL MES: Bs. %.2f\n", totalIngresosMes);
        System.out.printf("📉 EGRESOS TOTALES DEL MES:  Bs. %.2f\n", totalEgresosMes);
        System.out.printf("💰 GANANCIA NETA DEL MES:    Bs. %.2f\n", (totalIngresosMes - totalEgresosMes));
        System.out.println("-------------------------------------------------");
        System.out.println("PRODUCTOS MÁS VENDIDOS EN EL MES:");

        if (productosVendidosMes.isEmpty()) {
            System.out.println("  (No se registraron ventas en este mes)");
        } else {
            ArrayList<Map.Entry<String, Integer>> listaVentasMes = new ArrayList<>(productosVendidosMes.entrySet());
            // Ordenar de mayor a menor
            listaVentasMes.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            // Mostrar solo el Top 5 para no saturar la pantalla
            int limite = Math.min(5, listaVentasMes.size());
            for (int i = 0; i < limite; i++) {
                System.out.printf("  %d. %-20s : %d unidades\n",
                        (i+1), listaVentasMes.get(i).getKey(), listaVentasMes.get(i).getValue());
            }
        }
        System.out.println("=================================================");
    }
}
