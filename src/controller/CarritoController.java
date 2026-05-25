package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.ListaGraficas;
import model.nodoGraficas;
import servicios.CarritoService;
import servicios.GraficaService;

public class CarritoController implements Initializable {

    @FXML
    private VBox contenedorCarrito;

    @FXML
    private Label lblTotal;

    @FXML
    private Label lblCantidad;

    @FXML
    private Label lblVacio;

    private CatalogoController catalogoController;

    public void setCatalogoController(CatalogoController c) {
        this.catalogoController = c;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarItems();
    }

    public void cargarItems() {

        contenedorCarrito.getChildren().clear();

        nodoGraficas actual = CarritoService.getCarrito();

        while (actual != null) {

            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/view/ItemCarrito.fxml")
                );

                HBox item = loader.load();

                ItemCarritoController controller = loader.getController();
                controller.inicializar(actual, this);

                item.setUserData(controller);

                contenedorCarrito.getChildren().add(item);

            } catch (Exception e) {
                e.printStackTrace();
            }

            actual = actual.sig;
        }

        actualizarEstadoVacio();
        actualizarTotales();
    }

    public void actualizarTotales() {

        int cantidad = 0;
        double total = 0;

        for (Node node : contenedorCarrito.getChildren()) {

            ItemCarritoController item
                    = (ItemCarritoController) node.getUserData();

            cantidad += item.getCantidad();
            total += item.getCantidad() * item.getPrecio();
        }

        lblCantidad.setText(String.valueOf(cantidad));
        lblTotal.setText("$ " + String.format("%,.0f", total));

    }

    private void actualizarEstadoVacio() {

        if (contenedorCarrito.getChildren().isEmpty()) {
            lblVacio.setVisible(true);
            lblVacio.setManaged(true);
        } else {
            lblVacio.setVisible(false);
            lblVacio.setManaged(false);
        }
    }

    public void eliminarItem(ItemCarritoController itemCtrl) {

        Node nodoAEliminar = null;

        for (Node node : contenedorCarrito.getChildren()) {
            if (node.getUserData() == itemCtrl) {
                nodoAEliminar = node;
                break;
            }
        }

        if (nodoAEliminar != null) {
            contenedorCarrito.getChildren().remove(nodoAEliminar);
        }

        // eliminar del servicio
        CarritoService.eliminarProducto(
                itemCtrl.getProducto().getCodigo()
        );

        actualizarTotales();
        actualizarEstadoVacio();
    }

    public void realizarCompra() {

        // cargar lista desde TXT
        ListaGraficas lista = GraficaService.cargarLista();

        // validar stock
        for (Node node : contenedorCarrito.getChildren()) {

            ItemCarritoController item
                    = (ItemCarritoController) node.getUserData();

            nodoGraficas aux = lista.inicio;

            while (aux != null) {

                if (aux.getCodigo().equals(item.getProducto().getCodigo())) {

                    // si no hay suficiente stock
                    if (item.getCantidad() > aux.getCantidad()) {

                        mostrarAlerta(
                                "Error",
                                "No hay suficiente stock para: " + aux.getNombre()
                        );
                        return; // detener compra
                    }
                }

                aux = aux.sig;
            }
        }

        // descontar stock
        for (Node node : contenedorCarrito.getChildren()) {

            ItemCarritoController item
                    = (ItemCarritoController) node.getUserData();

            nodoGraficas aux = lista.inicio;

            while (aux != null) {

                if (aux.getCodigo().equals(item.getProducto().getCodigo())) {

                    aux.setCantidad(
                            aux.getCantidad() - item.getCantidad()
                    );
                }

                aux = aux.sig;
            }
        }

        // guardar cambios en el txt
        GraficaService.guardarLista(lista);

        // alerta de compra exitosa
        mostrarAlerta(
                "Compra exitosa",
                "La compra se realizó correctamente. Gracias por comprar con nosotros"
        );

        // limpiar carrito
        CarritoService.vaciarCarrito();
        contenedorCarrito.getChildren().clear();
        actualizarEstadoVacio();
        actualizarTotales();

        // recargar catalogo
        if (catalogoController != null) {
            catalogoController.recargarCatalogo();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {

        javafx.scene.control.Alert alert
                = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION
                );

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }

    @FXML
    private void volverAlCatalogo() {
        if (catalogoController != null) {
            catalogoController.mostrarCatalogo();
        }
    }
}
