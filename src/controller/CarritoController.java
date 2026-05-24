package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.nodoGraficas;

public class CarritoController implements Initializable {

    // ui principal donde se renderizan los items del carrito
    @FXML
    private VBox contenedorCarrito;

    // labels de resumen
    @FXML
    private Label lblTotal;

    @FXML
    private Label lblSubtotal;

    @FXML
    private Label lblCantidad;

    // lista doble enlazada
    private nodoGraficas cabeza;
    private nodoGraficas cola;
    
    // tamaño del carrito
    private int tamaño;

    public CarritoController() {
        cabeza = null;
        cola = null;
        tamaño = 0;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizarVista();
    }

    // metodo para agregar producto a la lista
    public void agregarProducto(nodoGraficas producto) {

        // si la lista esta vacia se inicializa
        if (cabeza == null) {
            cabeza = cola = producto;
        } else {
            // se agrega al final
            cola.sig = producto;
            producto.ant = cola;
            cola = producto;
        }

        tamaño++;

        // se refresca la vista
        actualizarVista();
    }

    // metodo principal que renderiza el carrito usando fxml
    @FXML
    public void actualizarVista() {

        // limpiar contenedor antes de volver a dibujar
        contenedorCarrito.getChildren().clear();

        nodoGraficas actual = cabeza;

        int cantidadProductos = 0;
        double totalGeneral = 0;

        // recorrer la lista doble
        while (actual != null) {

            nodoGraficas productoActual = actual;

            try {

                // cargar el fxml de cada item del carrito
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/view/ItemCarrito.fxml")
                );

                // cargar la vista como un hbox
                HBox item = loader.load();

                // obtener el controller del item
                ItemCarritoController controller = loader.getController();

                // pasar el producto al item
                controller.setProducto(productoActual);
                
                // pasar referencia del carrito al item
                controller.setCarritoController(this);

                // agregar el item al contenedor principal
                contenedorCarrito.getChildren().add(item);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // acumular totales usando tu metodo subtotal
            totalGeneral += productoActual.subtotal();

            // acumular cantidad total de productos
            cantidadProductos += productoActual.getCantidad();

            actual = actual.sig;
        }

        // actualizar labels del resumen
        lblCantidad.setText(String.valueOf(cantidadProductos));
        lblTotal.setText("$" + String.format("%,.0f", totalGeneral));

        // actualizar subtotal si existe en el fxml
        if (lblSubtotal != null) {
            lblSubtotal.setText("$" + String.format("%,.0f", totalGeneral));
        }
    }

    // metodo para eliminar un producto por codigo
    public void eliminarProducto(String codigo) {

        nodoGraficas actual = cabeza;

        while (actual != null) {

            if (actual.getCodigo().equals(codigo)) {

                // caso: solo hay un elemento
                if (cabeza == cola) {
                    cabeza = cola = null;
                }
                // caso: eliminar cabeza
                else if (actual == cabeza) {
                    cabeza = cabeza.sig;
                    cabeza.ant = null;
                }
                // caso: eliminar cola
                else if (actual == cola) {
                    cola = cola.ant;
                    cola.sig = null;
                }
                // caso: nodo intermedio
                else {
                    actual.ant.sig = actual.sig;
                    actual.sig.ant = actual.ant;
                }

                tamaño--;
                return;
            }

            actual = actual.sig;
        }
    }

    // calcular total general del carrito
    public double calcularTotal() {

        double total = 0;
        nodoGraficas actual = cabeza;

        while (actual != null) {
            total += actual.subtotal();
            actual = actual.sig;
        }

        return total;
    }

    // vaciar completamente el carrito
    @FXML
    public void vaciarCarritoUI() {
        cabeza = null;
        cola = null;
        tamaño = 0;
        actualizarVista();
    }
}