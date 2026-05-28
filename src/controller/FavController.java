package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.PilaFavoritos;
import model.nodoGraficas;
import servicios.CarritoService;

public class FavController {

    @FXML
    private Label nombre;

    @FXML
    private Label precio;

    @FXML
    private Label cantidad;

    @FXML
    private ImageView imgF;

    @FXML
    private Button Qtar;

    @FXML
    private Button btnAgregar;

    private FavoritosController fc;
    private nodoGraficas producto;

    public void setDatos(nodoGraficas g) {
        this.producto = g;

        nombre.setText(g.nombre);
        precio.setText("$ " + String.format("%,.0f", g.precio));
        cantidad.setText("Disponibles: " + String.valueOf(g.cantidad));
        imgF.setImage(new Image(getClass().getResourceAsStream("/imagenesGrafiMundi/" + g.imagen)));
        Qtar.setUserData(g.codigo);
        actualizarBoton();
    }

    @FXML
    private void quitarFavorito(ActionEvent event) {
        Button b = (Button) event.getSource();
        String cod = (String) b.getUserData();
        PilaFavoritos.quitar(cod);

        Pane panel = (Pane) b.getParent();
        VBox vx = (VBox) panel.getParent();
        vx.getChildren().remove(panel);

        if (PilaFavoritos.totalNodos == 0) {
            fc.labelvacio();
        }
    }

    @FXML
    private void agregarAlCarrito(ActionEvent event) {

        if (producto != null) {
            CarritoService.agregarProducto(producto);
            actualizarBoton();
            mostrarAlerta(
                    "carrito",
                    producto.nombre + " agregado al carrito"
            );

        }
    }

    private void actualizarBoton() {

        if (producto == null) {
            return;
        }

        if (CarritoService.existeProducto(producto.codigo)) {
            btnAgregar.setText("Ya agregado");
            btnAgregar.setDisable(true);
        } else {
            btnAgregar.setText("Agregar al carrito");
            btnAgregar.setDisable(false);
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

    public void setFC(FavoritosController f) {
        fc = f;
    }
}
