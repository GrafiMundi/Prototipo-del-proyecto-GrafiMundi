package model;

public class nodoHistorial {

    public nodoGraficas producto;
    public int cantidadComprada;
    public String fecha;
    public nodoHistorial sig;

    public nodoHistorial(nodoGraficas producto, int cantidadComprada, String fecha) {
        this.producto = producto;
        this.cantidadComprada = cantidadComprada;
        this.fecha = fecha;
        this.sig = null;
    }

    public nodoGraficas getProducto() {
        return producto;
    }

    public int getCantidadComprada() {
        return cantidadComprada;
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
