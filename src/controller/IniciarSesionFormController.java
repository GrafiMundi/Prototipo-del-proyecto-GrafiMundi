package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import servicios.UsuarioService;
import model.Usuario;

public class IniciarSesionFormController implements Initializable {

    @FXML
    private VBox panelFormIniciarSesion;

    @FXML
    private TextField txtUsuarioIniciarSesion;

    @FXML
    private PasswordField txtContraseñaInicio;

    @FXML
    private TextField txtContraseñaInicioMask;

    @FXML
    private CheckBox checkVerContraseñaInicio;

    @FXML
    private Button btnIngresarInicioS;

    @FXML
    private Button btnLimpiarInicioS;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        checkVerContraseñaInicio.setOnAction(e -> {
            if (checkVerContraseñaInicio.isSelected()) {
                txtContraseñaInicioMask.setText(txtContraseñaInicio.getText());
                txtContraseñaInicioMask.setVisible(true);
                txtContraseñaInicio.setVisible(false);
            } else {
                txtContraseñaInicio.setText(txtContraseñaInicioMask.getText());
                txtContraseñaInicio.setVisible(true);
                txtContraseñaInicioMask.setVisible(false);
            }
        });

        btnIngresarInicioS.setOnAction(this::login);
        btnLimpiarInicioS.setOnAction(e -> limpiar());
    }

    private void login(ActionEvent event) {
        String usuario = txtUsuarioIniciarSesion.getText();
        String contraseña = txtContraseñaInicio.isVisible()
                ? txtContraseñaInicio.getText()
                : txtContraseñaInicioMask.getText();

        if (usuario.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Error", "Campos vacíos");
            return;
        }

        Usuario user = UsuarioService.login(usuario, contraseña);

        if (user != null) {
            if (user.getRol().equals("admin")) {
                mostrarAlerta("Bienvenido", "Ingresaste como ADMIN");
            } else {
                mostrarAlerta("Bienvenido", "Ingresaste como CLIENTE");
            }
        } else {
            mostrarAlerta("Error", "Usuario o contraseña incorrectos");
        }
    }

    private void limpiar() {
        txtUsuarioIniciarSesion.clear();
        txtContraseñaInicio.clear();
        txtContraseñaInicioMask.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();

    }
}
