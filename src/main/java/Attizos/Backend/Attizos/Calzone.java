package Attizos.Backend.Attizos;



public class Calzone extends Producto
{
    private String ingredientesTexto;

    public Calzone(int id, String nombre, double precio, String categoria, String img,
                   String ingredientesTexto){
        super(id, nombre, precio, categoria, 0.0,img);
        this.ingredientesTexto = ingredientesTexto;
    }
    @Override
    public void mostrarDetalles() {
        System.out.println("\n🥟 INFO DEL PLATO: " + getNombre());
        System.out.println("   Ingredientes: " + ingredientesTexto);
        System.out.println("   Precio: Bs. " + getPrecio());
    }

    public String getIngredientes(){
        return ingredientesTexto;
    }
}
