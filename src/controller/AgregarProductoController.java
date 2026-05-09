package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import model.nodoGraficas;

public class AgregarProductoController {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<String> comboMarca;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtImagen;
    @FXML private Label lblModo;

    private CatalogoController catalogoController;

    public void setCatalogoController(CatalogoController controller) {
        this.catalogoController = controller;
    }

    @FXML
    public void initialize() {

        comboMarca.getItems().addAll("INTEL", "AMD", "NVIDIA");

        lblModo.setText("Modo: Nuevo producto");
        lblModo.setStyle("-fx-text-fill: green;");

        txtCodigo.textProperty().addListener((obs, oldText, newText) -> {
            buscarProducto(newText);
        });

        // validacion para evitar letras en cantidad
        txtCantidad.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtCantidad.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        // formato de tipo moneda en el precio
        txtPrecio.textProperty().addListener((obs, oldValue, newValue) -> {

            if (newValue == null || newValue.isEmpty()) return;

            String limpio = newValue.replaceAll("[^\\d]", "");

            try {
                if (!limpio.isEmpty()) {
                    double valor = Double.parseDouble(limpio);
                    String formateado = "$ " + String.format("%,.0f", valor);

                    if (!newValue.equals(formateado)) {
                        txtPrecio.setText(formateado);
                        txtPrecio.positionCaret(formateado.length());
                    }
                }
            } catch (Exception e) {
                txtPrecio.setText("");
            }
        });
    }

    private void buscarProducto(String codigo) {

        if (codigo.isEmpty() || catalogoController == null) return;

        nodoGraficas existente = catalogoController
                .getLista()
                .buscarPorCodigo(codigo);

        if (existente != null) {

            lblModo.setText("Modo: Actualizar stock");
            lblModo.setStyle("-fx-text-fill: orange;");

            txtNombre.setText(existente.nombre);
            txtPrecio.setText("$ " + String.format("%,.0f", existente.precio));
            txtDescripcion.setText(existente.descripcion);
            comboMarca.setValue(existente.marca);
            txtImagen.setText(existente.imagen);

            txtNombre.setDisable(true);
            txtPrecio.setDisable(true);
            txtDescripcion.setDisable(true);
            comboMarca.setDisable(true);
            txtImagen.setDisable(true);

        } else {

            lblModo.setText("Modo: Nuevo producto");
            lblModo.setStyle("-fx-text-fill: green;");

            txtNombre.clear();
            txtPrecio.clear();
            txtDescripcion.clear();
            comboMarca.setValue(null);
            txtImagen.clear();

            txtNombre.setDisable(false);
            txtPrecio.setDisable(false);
            txtDescripcion.setDisable(false);
            comboMarca.setDisable(false);
            txtImagen.setDisable(false);
        }
    }

    @FXML
    private void guardarProducto() {

        // validacion de los campos
        if (txtCodigo.getText().isEmpty()
                || txtNombre.getText().isEmpty()
                || txtPrecio.getText().isEmpty()
                || comboMarca.getValue() == null
                || txtCantidad.getText().isEmpty()) {

            mostrarAlerta("Completa todos los campos obligatorios");
            return;
        }

        try {
            // limpiar el precio antes de guardarlo en el archivo txt
            String precioLimpio = txtPrecio.getText()
            .replace("$", "")
            .replace(".", "")
            .replace(",", "")
            .trim();

            Double.parseDouble(precioLimpio);

            boolean existe = catalogoController
                    .getLista()
                    .buscarPorCodigo(txtCodigo.getText()) != null;

            catalogoController.agregarOActualizarProducto(
                    txtCodigo.getText(),
                    txtNombre.getText(),
                    precioLimpio,
                    txtDescripcion.getText(),
                    comboMarca.getValue(),
                    txtCantidad.getText(),
                    txtImagen.getText()
            );

            if (existe) {
                mostrarAlerta("Stock actualizado correctamente");
            } else {
                mostrarAlerta("Producto agregado correctamente");
            }

            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarAlerta("El precio debe ser válido");
        }
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtCodigo.getScene().getWindow();
        stage.close();
    }
}