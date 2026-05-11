package model;

public class PilaFavoritos {

    static final int cantNodos = 10;
    private int totalNodos;
    private nodo tope;

    public PilaFavoritos() {
        totalNodos = 0;
        tope = null;
    }

    public boolean pilaLlena() {
        return totalNodos == cantNodos;
    }

    public boolean pilaVacia() {
        return totalNodos == 0;
    }

    public void agregar(String cod) {
        if (!pilaLlena()) {
            nodo nuevo = new nodo();
            nuevo.setCod(cod);
            nuevo.sig = tope;
            tope = nuevo;
            totalNodos++;
        }
    }

    public void quitar() {
        nodo temp;
        if (!pilaVacia()) {
            temp = tope;
            tope = tope.sig;
            temp = null;
            totalNodos--;
        }
    }

    public void limpiarPila() {  // Vaciar lista de favoritos
        while (!pilaVacia()) {
            quitar();
        }
    }

}
