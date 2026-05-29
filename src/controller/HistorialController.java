package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.nodoGraficas;
import model.nodoHistorial;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HistorialController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDesdeArchivo();
    }

    private void crearCard(String nombreP, double precioP, int cantidadP, String fechaP, String imagenP) {

        // estilo
        javafx.scene.layout.HBox card = new javafx.scene.layout.HBox();
        card.setStyle("""
        -fx-background-color: white;
        -fx-padding: 20;
        -fx-border-color: #ddd;
        -fx-border-radius: 12;
        -fx-background-radius: 12;
        """);

        card.setSpacing(20);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPrefWidth(Double.MAX_VALUE);

        // imagen
        ImageView img = new ImageView();

        try {
            Image image = new Image(getClass().getResourceAsStream("/ImagenesGrafiMundi/" + imagenP));
            img.setImage(image);

            img.setFitWidth(300);
            img.setFitHeight(300);
            img.setPreserveRatio(true);

        } catch (Exception e) {
            System.out.println("No se pudo cargar imagen: " + imagenP);
        }

        VBox info = new VBox();
        info.setSpacing(10);

        // textos
        Label nombre = new Label("Producto Comprado: " + nombreP);
        nombre.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label precio = new Label("Precio: $" + String.format("%,.0f", precioP));
        precio.setStyle("-fx-text-fill: #2e7d32; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label cantidad = new Label("Cantidad comprada: " + cantidadP);
        cantidad.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label fecha = new Label("Fecha y hora de compra: " + fechaP);
        fecha.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        info.getChildren().addAll(nombre, precio, cantidad, fecha);

        // agregar todo al card
        card.getChildren().addAll(img, info);

        vxHistorial.getChildren().add(card);
    }

    public void agregarCompra(nodoGraficas producto, int cantidadComprada) {

        String fecha = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        nodoHistorial nuevo = new nodoHistorial(producto, cantidadComprada, fecha);

        if (frente == null) {
            frente = fin = nuevo;
        } else {
            fin.setSig(nuevo);
            fin = nuevo;
        }

        tamaño++;

        actualizarVista();
    }

    public void cargarDesdeArchivo() {

        vxHistorial.getChildren().clear();

        try {

            java.io.File archivo = servicios.historialService.obtenerArchivoUsuario();

            if (archivo == null || !archivo.exists()) {
                mostrarVacio(true);
                return;
            }

            java.util.List<String> lineas = java.nio.file.Files.readAllLines(archivo.toPath());

            String nombre = "";
            int cantidad = 0;
            double precio = 0;
            String fecha = "";
            String imagen = "";

            for (String linea : lineas) {

                if (linea.startsWith("Producto:")) {
                    nombre = linea.replace("Producto: ", "").trim();
                }

                if (linea.startsWith("Cantidad:")) {
                    cantidad = Integer.parseInt(linea.replace("Cantidad: ", "").trim());
                }

                if (linea.startsWith("Precio:")) {
                    precio = Double.parseDouble(linea.replace("Precio: ", "").trim());
                }

                if (linea.startsWith("Fecha:")) {
                    fecha = linea.replace("Fecha: ", "").trim();
                }

                if (linea.startsWith("Imagen:")) {
                    imagen = linea.replace("Imagen: ", "").trim();
                }

                if (linea.startsWith("------------------")) {

                    crearCard(nombre, precio, cantidad, fecha, imagen);

                    // limpiar
                    nombre = "";
                    cantidad = 0;
                    precio = 0;
                    fecha = "";
                    imagen = "";
                }
            }

            mostrarVacio(vxHistorial.getChildren().isEmpty());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarVista() {

        vxHistorial.getChildren().clear();

        if (frente == null) {
            mostrarVacio(true);
            return;
        }

        mostrarVacio(false);

        nodoHistorial actual = frente;

        while (actual != null) {

            nodoGraficas p = actual.getProducto();

            crearCard(
                    p.getNombre(),
                    p.getPrecio(),
                    actual.getCantidadComprada(),
                    actual.getFecha(),
                    p.getImagen()
            );

            actual = actual.getSig();
        }
    }

    private void mostrarVacio(boolean estado) {
        lblVacio.setVisible(estado);
        lblVacio.setManaged(estado);
    }

    public void setCatalogoController(CatalogoController catalogoController) {
        this.catalogoController = catalogoController;
    }

    @FXML
    private void volver() {
        if (catalogoController != null) {
            catalogoController.mostrarCatalogo();
        }
    }
}
