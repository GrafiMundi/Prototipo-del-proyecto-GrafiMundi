package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.nodoGraficas;
import model.nodoHistorial;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistorialController {

    @FXML
    private VBox vxHistorial;

    @FXML
    private Label lblVacio;

    private CatalogoController catalogoController;

    // cola
    private nodoHistorial frente;
    private nodoHistorial fin;

    private int tamaño;

    // constructor
    public HistorialController() {
        frente = null;
        fin = null;
        tamaño = 0;
    }

    // metodo para agregar compra (conceptual aun no conectado al carrito de compras)
    public void agregarCompra(nodoGraficas producto) {

        String fecha = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        nodoHistorial nuevo = new nodoHistorial(producto, fecha);

        if (frente == null) {
            frente = fin = nuevo;
        } else {
            fin.sig = nuevo;
            fin = nuevo;
        }

        tamaño++;

        actualizarVista();
    }

    // metodo que actualizara la vista 
    private void actualizarVista() {

        vxHistorial.getChildren().clear();

        if (frente == null) {
            lblVacio.setVisible(true);
            return;
        }

        lblVacio.setVisible(false);

        nodoHistorial actual = frente;

        while (actual != null) {

            nodoGraficas p = actual.getProducto();

            VBox card = new VBox();
            card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: #ddd; -fx-border-radius: 8;");
            card.setSpacing(5);

            Label nombre = new Label("Producto: " + p.getNombre());
            Label precio = new Label("Precio: $" + p.getPrecio());
            Label cantidad = new Label("Cantidad: " + p.getCantidad());
            Label subtotal = new Label("Subtotal: $" + p.subtotal());
            Label fecha = new Label("Fecha: " + actual.getFecha());

            card.getChildren().addAll(nombre, precio, cantidad, subtotal, fecha);

            vxHistorial.getChildren().add(card);

            actual = actual.sig;
        }
    }

    @FXML
    private void volver() {
        if (catalogoController != null) {
            catalogoController.mostrarCatalogo();
        }
    }
}
