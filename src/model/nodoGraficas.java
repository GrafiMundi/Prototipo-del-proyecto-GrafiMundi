package model;

public class nodoGraficas {
    public String codigo;
    public String nombre;
    public double precio;
    public String descripcion;
    public String marca;
    public int cantidad;
    public String imagen;

    public nodoGraficas sig;
    public nodoGraficas ant;

    public nodoGraficas(String c, String n, String p, String d, String m, int cant, String img) {
        codigo = c;
        nombre = n;
        // conversión del precio de texto a número
        precio = Double.parseDouble(p);
        descripcion = d;
        // se convierte la marca a mayúsculas para mantener consistencia
        marca = m.toUpperCase();
        cantidad = cant;
        imagen = img;
        sig = null;
        ant = null;
    }
}