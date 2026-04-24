package Attizos.Backend.Listas;

public class NodoDE <T> {
    private T dato;
    private NodoDE<T> siguiente;
    private NodoDE<T> anterior;

    public NodoDE(T dato){
        this.dato = dato;
        siguiente = null;
        anterior = null;
    }
    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoDE<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoDE<T> siguiente) {
        this.siguiente = siguiente;
    }

    public NodoDE<T> getAnterior() {
        return anterior;
    }

    public void setAnterior(NodoDE<T> anterior) {
        this.anterior = anterior;
    }
}
