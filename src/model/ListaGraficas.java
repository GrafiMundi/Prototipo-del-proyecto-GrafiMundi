package model;

public class ListaGraficas {
    public nodoGraficas inicio;
    public nodoGraficas fin;

    public ListaGraficas() {
        inicio = null;
        fin = null;
    }

     // método para agregar un nuevo nodo al final de la lista
    public void agregar(nodoGraficas nuevo) {

        // si la lista está vacía, el nuevo nodo será el primero y el último
        if (inicio == null) {
            inicio = nuevo;
            fin = nuevo;
        } else {
            // se enlaza el último nodo actual con el nuevo
            fin.sig = nuevo;

            // el nuevo nodo apunta hacia atrás al nodo anterior
            nuevo.ant = fin;

            // se actualiza el puntero final al nuevo nodo
            fin = nuevo;
        }
    }
    
    // buscar un producto por codigo
    public nodoGraficas buscarPorCodigo(String codigo) {

        nodoGraficas aux = inicio;

        while (aux != null) {

            if (aux.codigo.equalsIgnoreCase(codigo)) {
                return aux;
            }

            aux = aux.sig;
        }

        return null;
    }
}