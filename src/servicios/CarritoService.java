package servicios;

import model.nodoGraficas;

public class CarritoService {

    public static nodoGraficas cabeza = null;

    public static nodoGraficas getCarrito() {
        return cabeza;
    }

    public static void agregarProducto(nodoGraficas producto) {

        nodoGraficas aux = cabeza;

        while (aux != null) {
            if (aux.codigo.equals(producto.codigo)) {

                if (aux.cantidad < 5) {
                    aux.cantidad++;
                }
                return;
            }
            aux = aux.sig;
        }

        nodoGraficas nuevo = new nodoGraficas(
                producto.codigo,
                producto.nombre,
                String.valueOf(producto.precio),
                producto.descripcion,
                producto.marca,
                1,
                producto.imagen
        );

        nuevo.sig = cabeza;
        if (cabeza != null) {
            cabeza.ant = nuevo;
        }

        cabeza = nuevo;
    }

    public static void eliminarProducto(String codigo) {

        nodoGraficas actual = cabeza;

        while (actual != null) {

            if (actual.codigo.equals(codigo)) {

                if (actual == cabeza) {
                    cabeza = cabeza.sig;
                    if (cabeza != null) {
                        cabeza.ant = null;
                    }
                } else {
                    actual.ant.sig = actual.sig;
                    if (actual.sig != null) {
                        actual.sig.ant = actual.ant;
                    }
                }
                return;
            }

            actual = actual.sig;
        }
    }

    public static void vaciarCarrito() {
        cabeza = null;
    }

    public static boolean existeProducto(String codigo) {

        nodoGraficas aux = cabeza;

        while (aux != null) {
            if (aux.codigo.equals(codigo)) {
                return true;
            }
            aux = aux.sig;
        }

        return false;
    }

}
