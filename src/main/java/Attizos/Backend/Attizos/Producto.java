package Attizos.Backend.Attizos;


import java.util.Map;

public  class Producto
{
    protected int id;
    protected String nombre;
    protected double precio;
    protected String categoria;
    protected double stock;
    protected Receta receta;

    protected String imagenURL;
    public Producto(int id, String nombre, double precio, String categoria, double stock, String imagenURL){
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.stock = stock;
        this.receta = null;

        if(imagenURL == null || imagenURL.isEmpty()){
            this.imagenURL = "/images/default.png";
        } else {
            this.imagenURL = imagenURL;
        }
    }
    public int getId(){
        return id;
    }
    public String getNombre(){
    return nombre;
}
    public double getPrecio(){
        return precio;
    }
    public String getCategoria(){
        return categoria;
    }
    public double getStock(){
        return stock;
    }

    public void setPrecio(double precio){this.precio = precio;}

    public void setStock(double stock){this.stock = stock;}

    public Receta getReceta(){
        return receta;
    }
    public void setReceta(Receta receta){
        this.receta = receta;
    }
    public boolean tieneReceta(){
        return this.receta != null && !this.receta.esVacia();
    }
    public void setStock(Double stock) {
        this.stock = stock;
    }
    public boolean reducirStock(int cantidad){
        if(this.stock >= cantidad){
            this.stock -= cantidad;
            return true;
        }
        return false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void aumentarStock(int cantidad){
        this.stock += cantidad;
    }
    @Override
    public String toString() {
        return String.format("ID: %-4d | %-15s | %-10s | Bs. %-8.2f | Stock: %-6.2f",
                id, nombre, categoria, precio, stock);
    }
    public int calcularDisponibilidad(Inventario inventario){
        if(!tieneReceta()){
            return (int) this.stock;
        }
        Receta r = this.getReceta();
        int maxPlatosPosibles = Integer.MAX_VALUE;
        for(Map.Entry<String, Double> entry : r.getIngredientes().entrySet()){
            String codInsumoBase = entry.getKey();
            double cantNecesariaPorPlato = entry.getValue();
            double stockValido = 0;
            for(Insumo i : inventario.getInventarioInsumos().values()){
                if((i.getCodigo().equals(codInsumoBase) || i.getCodigo().startsWith(codInsumoBase + "-L")) && !i.isVencido()){
                    stockValido += i.getStockActual();
                }
            }
            int porciones = (int) (stockValido / cantNecesariaPorPlato);

            if(porciones < maxPlatosPosibles){
                maxPlatosPosibles = porciones;
            }
        }
        return maxPlatosPosibles == Integer.MAX_VALUE ? 0 : maxPlatosPosibles;
    }
    public void mostrarDetalles(){
        System.out.println("\n📦 DETALLES: " + getNombre());
        System.out.println("   Categoría: " + getCategoria());
        System.out.println("   Precio: Bs. " + getPrecio());
    }
}
