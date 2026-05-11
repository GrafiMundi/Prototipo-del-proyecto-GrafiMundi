package controller;

import model.nodoGraficas;

public class HistorialController {

    // cola
    private nodoGraficas frente;
    private nodoGraficas fin;

    private int tamaño;

    // constructor
    public HistorialController() {

        frente = null;
        fin = null;

        tamaño = 0;
    }

    // metodo que agrega la compra al historial
    public void agregarCompra(
            nodoGraficas producto
    ) {

        producto.sig = null;

        // COLA VACÍA
        if (frente == null) {

            frente = fin = producto;

        } else {

            fin.sig = producto;

            fin = producto;
        }

        tamaño++;
    }

    // metodo que mostrara la primera compra
    public nodoGraficas verPrimeraCompra() {

        return frente;
    }

    // metodo que mostrara la ultima compra
    public nodoGraficas verUltimaCompra() {

        return fin;
    }

    // metodo que busca la compra
    public nodoGraficas buscarCompra(
            String codigo
    ) {

        nodoGraficas actual = frente;

        while (actual != null) {

            if (actual.getCodigo()
                    .equals(codigo)) {

                return actual;
            }

            actual = actual.sig;
        }

        return null;
    }

    // metodo que mostrara el historial
    public void mostrarHistorial() {

        if (frente == null) {

            System.out.println(
                    "No hay compras."
            );

            return;
        }

        nodoGraficas actual = frente;

        System.out.println(
                "\n===== HISTORIAL ====="
        );

        while (actual != null) {

            System.out.println(
                    "Código: "
                    + actual.getCodigo()
            );

            System.out.println(
                    "Nombre: "
                    + actual.getNombre()
            );

            System.out.println(
                    "Precio: $"
                    + actual.getPrecio()
            );

            System.out.println(
                    "Cantidad: "
                    + actual.getCantidad()
            );

            System.out.println(
                    "Subtotal: $"
                    + actual.subtotal()
            );

            System.out.println(
                    "---------------------"
            );

            actual = actual.sig;
        }
    }

    // metodo que calcula el total del historial
    public double calcularTotalHistorial() {

        double total = 0;

        nodoGraficas actual = frente;

        while (actual != null) {

            total += actual.subtotal();

            actual = actual.sig;
        }

        return total;
    }

    // metodo que obtiene la cantidad de compras
    public int obtenerTamaño() {

        return tamaño;
    }

    // metodo que verifica si la cola esta vacia
    public boolean estaVacia() {

        return frente == null;
    }

    // metodo que vacia el historial
    public void vaciarHistorial() {

        frente = null;

        fin = null;

        tamaño = 0;
    }
}
