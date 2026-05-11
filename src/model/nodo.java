package model;

//lista auxiliar para pila de favoritos
public class nodo {

    private String cod;
    public nodo sig;

    public nodo() {
        cod = "";
        sig = null;
    }

    public void setCod(String c) {
        cod = c;
    }

    public String getCod() {
        return cod;
    }

}
