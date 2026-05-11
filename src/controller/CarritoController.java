package controller;

import model.nodoGraficas;

public class CarritoController {

    // inicio y final de la lista
    private nodoGraficas cabeza;
    private nodoGraficas cola;

    // tamaño del carrito
    private int tamaño;

    // constructor
    public CarritoController() {

        cabeza = null;
        cola = null;

        tamaño = 0;
    }

    // metodo que agregara un producto al carrito
    public void agregarProducto(nodoGraficas producto) {

        // SI EL CARRITO ESTÁ VACÍO
        if (cabeza == null) {

            cabeza = cola = producto;

        } else {

            cola.sig = producto;

            producto.ant = cola;

            cola = producto;
        }

        tamaño++;

        System.out.println(
                producto.getNombre()
                + " agregado al carrito."
        );
    }

    // mostrar carrito
    public void mostrarCarrito() {

        if (cabeza == null) {

            System.out.println(
                    "El carrito está vacío."
            );

            return;
        }

        nodoGraficas actual = cabeza;

        System.out.println(
                "\n======= CARRITO ======="
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
                    "----------------------"
            );

            actual = actual.sig;
        }

        System.out.println(
                "TOTAL: $"
                + calcularTotal()
        );
    }

    // metodo que eliminara un producto
    public void eliminarProducto(String codigo) {

        nodoGraficas actual = cabeza;

        while (actual != null) {

            if (actual.getCodigo().equals(codigo)) {

                // nodo unico
                if (cabeza == cola) {

                    cabeza = cola = null;
                } // eliminar cabeza
                else if (actual == cabeza) {

                    cabeza = cabeza.sig;

                    cabeza.ant = null;
                } // eliminar cola
                else if (actual == cola) {

                    cola = cola.ant;

                    cola.sig = null;
                } // eliminar
                else {

                    actual.ant.sig = actual.sig;

                    actual.sig.ant = actual.ant;
                }

                tamaño--;

                System.out.println(
                        "Producto eliminado."
                );

                return;
            }

            actual = actual.sig;
        }

        System.out.println(
                "Producto no encontrado."
        );
    }

    // metodo que buscar el producto
    public nodoGraficas buscarProducto(
            String codigo
    ) {

        nodoGraficas actual = cabeza;

        while (actual != null) {

            if (actual.getCodigo().equals(codigo)) {

                return actual;
            }

            actual = actual.sig;
        }

        return null;
    }

    // metodo que actualiza la cantidad
    public void actualizarCantidad(
            String codigo,
            int nuevaCantidad
    ) {

        nodoGraficas producto
                = buscarProducto(codigo);

        if (producto != null) {

            producto.setCantidad(
                    nuevaCantidad
            );

            System.out.println(
                    "Cantidad actualizada."
            );

        } else {

            System.out.println(
                    "Producto no encontrado."
            );
        }
    }

    // metodo que calcula el total
    public double calcularTotal() {

        double total = 0;

        nodoGraficas actual = cabeza;

        while (actual != null) {

            total += actual.subtotal();

            actual = actual.sig;
        }

        return total;
    }

    // metodo que vacia el carrito
    public void vaciarCarrito() {

        cabeza = null;

        cola = null;

        tamaño = 0;

        System.out.println(
                "Carrito vaciado."
        );
    }

    // metodo que cambiara el orden en que se ve el carrito
    public void mostrarInverso() {

        if (cola == null) {

            System.out.println(
                    "Carrito vacío."
            );

            return;
        }

        nodoGraficas actual = cola;

        System.out.println(
                "\n=== CARRITO INVERSO ==="
        );

        while (actual != null) {

            System.out.println(
                    actual.getNombre()
                    + " - $"
                    + actual.getPrecio()
            );

            actual = actual.ant;
        }
    }

    // metodo para obtener el tamaño del carrito 
    public int obtenerTamaño() {

        return tamaño;
    }
}
