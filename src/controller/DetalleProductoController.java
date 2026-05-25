package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.PilaFavoritos;
import model.nodoGraficas;
import servicios.CarritoService;

public class DetalleProductoController {

    private CatalogoController catalogoController;

    public void setCatalogoController(CatalogoController c) {
        this.catalogoController = c;
    }

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblPrecio;

    @FXML
    private Label lblPrecioPanel;

    @FXML
    private Label lblDescripcion;

    @FXML
    private Label lblMarca;

    @FXML
    private Label lblCantidad;

    @FXML
    private ImageView imgProducto;

    @FXML
    private Button btnFav;

    @FXML
    private Button btnCarrito;

    @FXML
    private Button btnVolver;

    @FXML
    private void volverAlCatalogo() {

        if (catalogoController != null) {
            catalogoController.mostrarCatalogo();
        }
    }

    // validar si ya esta en carrito
    private boolean estaEnCarrito(String codigo) {

        nodoGraficas aux = CarritoService.getCarrito();

        while (aux != null) {
            if (aux.codigo.equals(codigo)) {
                return true;
            }
            aux = aux.sig;
        }

        return false;
    }

    public void setProducto(nodoGraficas p) {

        lblNombre.setText(p.nombre);
        lblPrecioPanel.setText(p.nombre);

        // formato de precio
        lblPrecio.setText("$ " + String.format("%,.0f", p.precio));
        lblPrecioPanel.setText("$ " + String.format("%,.0f", p.precio));

        // evitar null en descripcion
        lblDescripcion.setText(
                (p.descripcion != null && !p.descripcion.isEmpty())
                ? p.descripcion
                : "Sin descripción"
        );

        lblMarca.setText("Marca: " + p.marca);
        lblCantidad.setText("Stock: " + p.cantidad);

        // imagen del producto
        try {
            String ruta = "/ImagenesGrafiMundi/" + p.imagen;

            if (getClass().getResource(ruta) != null) {

                Image img = new Image(
                        getClass().getResource(ruta).toExternalForm()
                );

                imgProducto.setImage(img);

            } else {
                System.out.println("imagen no encontrada: " + p.imagen);
            }

        } catch (Exception e) {
            System.out.println("error cargando imagen: " + p.imagen);
        }

        // titulo de ventana
        try {
            Stage stage = (Stage) lblNombre.getScene().getWindow();
            if (stage != null) {
                stage.setTitle(p.nombre);
            }
        } catch (Exception e) {
        }

        // estado inicial del boton
        if (estaEnCarrito(p.codigo)) {
            btnCarrito.setText("Ya agregado al carrito");
            btnCarrito.setDisable(true);
        }

        // boton del carrito
        btnCarrito.setOnAction(e -> {

            if (p.cantidad > 0) {

                CarritoService.agregarProducto(p);

                btnCarrito.setText("Ya agregado al carrito");
                btnCarrito.setDisable(true);

                mostrarAlerta(
                        "Carrito",
                        p.nombre + " agregado al carrito"
                );

            } else {

                mostrarAlerta(
                        "Sin stock",
                        "No hay unidades disponibles de " + p.nombre
                );
            }
        });

        // favoritos
        btnFav.setOnAction(e -> {
            if (PilaFavoritos.repetido(p.codigo)) {
                mostrarAlerta("", "Esta gráfica ya está en tu lista de favoritos");
            } else {
                PilaFavoritos.agregar(p);
                mostrarAlerta("favoritos", p.nombre + " agregado a favoritos");
            }
        });
    }

    private void mostrarAlerta(
            String titulo,
            String mensaje
    ) {

        javafx.scene.control.Alert alert
                = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION
                );

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
