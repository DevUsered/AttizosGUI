package Attizos.Backend.Attizos;

public class Postre extends Producto
{
    private String tamano;
    private String ingredientes;
    private String tipo;

    public Postre(int id, String nombre, double precio, String categoria,String img,
                  String tamano, String ingredientes, String tipo){
        super(id, nombre, precio, categoria, 0.0,img);
        this.tamano = tamano;
        this.ingredientes = ingredientes;
        this.tipo = tipo;
    }
    @Override
    public void mostrarDetalles() {
        System.out.println("\n🍰 INFO DEL PLATO: " + getNombre());
        System.out.println("   Tipo: " + tipo);
        System.out.println("   Tamaño: " + tamano);
        System.out.println("   Ingredientes: " + ingredientes);
        System.out.println("   Precio: Bs. " + getPrecio());
    }

    public String getTamaño(){
        return tamano;
    }

    public String getIngredientes(){
        return ingredientes;
    }

    public String getTipo(){
        return tipo;
    }
}
