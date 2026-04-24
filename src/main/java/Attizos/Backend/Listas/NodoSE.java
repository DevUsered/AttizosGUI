package Attizos.Backend.Listas;

public class NodoSE <T>{
    private T dato;
    private NodoSE<T> sig;
    public NodoSE(T dt){
        dato = dt;
        sig = null;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoSE<T> getSig() {
        return sig;
    }

    public void setSig(NodoSE<T> sig) {
        this.sig = sig;
    }
}
