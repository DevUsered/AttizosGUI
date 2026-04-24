package Attizos.Backend.Listas;

public class ListaSE<T> {
    private NodoSE<T> cabeza;
    private int longitud;

    public ListaSE(){
        cabeza = null;
        longitud = 0;
    }

    public boolean esVacia(){
        return cabeza == null;
    }

    public void insertarAlInicio(T dato){
        NodoSE<T> nuevo = new NodoSE<>(dato);
        nuevo.setSig(cabeza);
        cabeza = nuevo;
        longitud++;
    }

    public void insertarAlFinal(T dato){
        NodoSE<T> nuevo = new NodoSE<>(dato);

        if (esVacia()) {
            cabeza = nuevo;
        } else {
            NodoSE<T> ac = cabeza;
            while(ac.getSig() != null){
                ac = ac.getSig();
            }
            ac.setSig(nuevo);
        }
        longitud++;
    }

    public void insertarEnPosicion(T dato, int pos){
        if(pos < 0 || pos > longitud){
            System.out.println("Posición no válida");
            return;
        }

        if(pos == 0){
            insertarAlInicio(dato);
            return;
        }

        NodoSE<T> nuevo = new NodoSE<>(dato);
        NodoSE<T> ac = cabeza;

        for(int i = 0; i < pos - 1; i++){
            ac = ac.getSig();
        }

        nuevo.setSig(ac.getSig());
        ac.setSig(nuevo);
        longitud++;
    }

    public void recorrer(){
        NodoSE<T> ac = cabeza;

        while(ac != null){
            System.out.println(ac.getDato());
            ac = ac.getSig();
        }
    }

    public int getLongitud(){
        return longitud;
    }

    public T eliminarAlInicio(){
        if(esVacia()){
            System.out.println("Lista vacía");
            return null;
        }

        T dato = cabeza.getDato();
        cabeza = cabeza.getSig();
        longitud--;
        return dato;
    }

    public T eliminarAlFinal(){
        if(esVacia()){
            System.out.println("Lista vacía");
            return null;
        }

        if(cabeza.getSig() == null){
            return eliminarAlInicio();
        }

        NodoSE<T> ac = cabeza;

        while(ac.getSig().getSig() != null){
            ac = ac.getSig();
        }

        T dato = ac.getSig().getDato();
        ac.setSig(null);
        longitud--;
        return dato;
    }

    public T eliminarEnPosicion(int pos){
        if(pos < 0 || pos >= longitud){
            System.out.println("Posición no válida");
            return null;
        }

        if(pos == 0){
            return eliminarAlInicio();
        }

        NodoSE<T> ac = cabeza;

        for(int i = 0; i < pos - 1; i++){
            ac = ac.getSig();
        }

        NodoSE<T> eliminar = ac.getSig();
        ac.setSig(eliminar.getSig());
        longitud--;

        return eliminar.getDato();
    }

    public T obtener(int pos){
        if(pos < 0 || pos >= longitud){
            System.out.println("Posición no válida");
            return null;
        }

        NodoSE<T> ac = cabeza;

        for(int i = 0; i < pos; i++){
            ac = ac.getSig();
        }

        return ac.getDato();
    }

    public void reemplazar(int pos, T dato){
        if(pos < 0 || pos >= longitud){
            System.out.println("Posición no válida");
            return;
        }

        NodoSE<T> ac = cabeza;

        for(int i = 0; i < pos; i++){
            ac = ac.getSig();
        }

        ac.setDato(dato);
    }

    public int indiceDe(T dato){
        NodoSE<T> ac = cabeza;
        int i = 0;

        while(ac != null){
            if(dato == null ? ac.getDato() == null : dato.equals(ac.getDato())){
                return i;
            }
            ac = ac.getSig();
            i++;
        }

        return -1;
    }

    public boolean contiene(T dato){
        return indiceDe(dato) != -1;
    }

    public void limpiar(){
        cabeza = null;
        longitud = 0;
    }
    public void invertir(){
        NodoSE<T> anterior = null;
        NodoSE<T> actual = cabeza;
        NodoSE<T> siguiente;

        while(actual != null){
            siguiente = actual.getSig();   // guardar siguiente
            actual.setSig(anterior);       // invertir enlace
            anterior = actual;             // avanzar anterior
            actual = siguiente;            // avanzar actual
        }

        cabeza = anterior; // nueva cabeza
    }
    public boolean eliminarPorValor(T dato){
        if(esVacia()){
            System.out.println("Lista vacía");
            return false;
        }

        // 🔹 Caso: el dato está en la cabeza
        if(dato == null ? cabeza.getDato() == null : dato.equals(cabeza.getDato())){
            cabeza = cabeza.getSig();
            longitud--;
            return true;
        }

        NodoSE<T> anterior = cabeza;
        NodoSE<T> actual = cabeza.getSig();

        while(actual != null){
            if(dato == null ? actual.getDato() == null : dato.equals(actual.getDato())){
                anterior.setSig(actual.getSig());
                longitud--;
                return true;
            }
            anterior = actual;
            actual = actual.getSig();
        }

        System.out.println("Dato no encontrado");
        return false;
    }
}
