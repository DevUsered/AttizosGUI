package Attizos.Backend.Attizos;

public enum TamanoBebida {
    PERSONAL(1,"Personal (355ml)"),
    MEDIO_LITRO(2,"Medio litro (500ml)"),
    UN_LITRO(3,"1 Litro"),
    DOS_LITROS(4, "2 Litros"),
    TRES_LITROS(5, "3 Litros");

    private final int opcion;
    private final String descripcion;

    TamanoBebida(int opcion, String descripcion) {
        this.opcion = opcion;
        this.descripcion = descripcion;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public static TamanoBebida obtenerPorNumero(int numeroSeleccionado){
        for(TamanoBebida tam : TamanoBebida.values()){
            if(tam.opcion == numeroSeleccionado){
                return tam;
            }
        }
        return null;
    }
}
