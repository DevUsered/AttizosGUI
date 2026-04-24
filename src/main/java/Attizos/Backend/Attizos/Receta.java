package Attizos.Backend.Attizos;


import java.util.HashMap;
import java.util.Map;

public class Receta {
    // Clave: Código del Insumo (ej. "I001" para harina)
    // Valor: Cantidad necesaria para 1 porción (ej. 0.25 kg)
    private HashMap<String, Double> ingredientes;

    public Receta() {
        this.ingredientes = new HashMap<>();
    }

    // Método para armar la receta ingrediente por ingrediente
    public void agregarIngrediente(String codigoInsumo, double cantidadNecesaria) {
        if (cantidadNecesaria > 0) {
            ingredientes.put(codigoInsumo, cantidadNecesaria);
        }
    }

    // Devuelve el mapa completo de ingredientes
    public HashMap<String, Double> getIngredientes() {
        return ingredientes;
    }

    // Verifica si la receta tiene ingredientes registrados
    public boolean esVacia() {
        return ingredientes.isEmpty();
    }

    // Muestra la receta en pantalla (útil para el Admin)
    public void mostrarReceta(Inventario inventarioPizzeria) {
        System.out.println("--- RECETA DEL PRODUCTO ---");
        for (Map.Entry<String, Double> entry : ingredientes.entrySet()) {
            String codInsumo = entry.getKey();
            double cant = entry.getValue();

            // Buscamos el nombre y la unidad en el inventario real
            Insumo insumo = inventarioPizzeria.buscarInsumo(codInsumo);
            if (insumo != null) {
                System.out.printf("- %s: %.2f %s\n", insumo.getNombre(), cant, insumo.getUnidad());
            } else {
                System.out.println("- Insumo Desconocido [" + codInsumo + "]: " + cant);
            }
        }
        System.out.println("---------------------------");
    }
}
