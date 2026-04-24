package Attizos.Backend.Attizos;



public class Pizza extends Producto
{
    private String tamano;
    private String descripcionIngredientes;
    private boolean extraQueso;
    public Pizza(int id, String nombre, double precio, String categoria,
                 String tamano, String ingredientes, boolean extraQueso){
        super(id, nombre, precio, categoria, 0.0);
        this.tamano = tamano;
        this.descripcionIngredientes = ingredientes;
        this.extraQueso = extraQueso;
    }
    @Override
    public void mostrarDetalles() {
        System.out.println("\n🍕 INFO DEL PLATO: " + getNombre());
        System.out.println("   Tamaño: " + tamano);
        System.out.println("   Ingredientes: " + descripcionIngredientes);
        System.out.println("   Extra Queso: " + (extraQueso ? "Sí" : "No"));
        System.out.println("   Precio: Bs. " + getPrecio());
    }
    public String getTamaño(){
        return tamano;
    }
    public String getDescripcionIngredientes(){
        return descripcionIngredientes;
    }
    public boolean isExtraQueso(){
        return extraQueso;
    }
}