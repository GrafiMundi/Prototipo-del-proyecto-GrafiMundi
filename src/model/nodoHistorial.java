package model;

public class nodoHistorial {

    public nodoGraficas producto;
    public String fecha;
    public nodoHistorial sig;

    public nodoHistorial(nodoGraficas producto, String fecha) {
        this.producto = producto;
        this.fecha = fecha;
        this.sig = null;
    }

    public nodoGraficas getProducto() {
        return producto;
    }

    public String getFecha() {
        return fecha;
    }

    public nodoHistorial getSig() {
        return sig;
    }

    public void setSig(nodoHistorial sig) {
        this.sig = sig;
    }

}
