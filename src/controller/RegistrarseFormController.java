package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.application.Platform;

import servicios.UsuarioService;
import model.Usuario;

public class RegistrarseFormController implements Initializable {

    @FXML
    private VBox panelFormRegistro;

    @FXML
    private TextField txtEmailRegistro;

    @FXML
    private TextField txtUsuarioRegistro;

    @FXML
    private PasswordField txtContraseñaRegistro;

    @FXML
    private TextField txtContraseñaRegistroMask;

    @FXML
    private PasswordField txtConfirmarContraRegistro;

    @FXML
    private TextField txtConfirmarContraRegistroMask;

    @FXML
    private CheckBox checkVerContraseñaRegistro;

    @FXML
    private Button btnRegistrarse;

    @FXML
    private Button btnLimpiarRegistrarse;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // metodos para mostrar o ocultar contraseñas
        checkVerContraseñaRegistro.setOnAction(e -> {
            if (checkVerContraseñaRegistro.isSelected()) {

                txtContraseñaRegistroMask.setText(txtContraseñaRegistro.getText());
                txtContraseñaRegistroMask.setVisible(true);
                txtContraseñaRegistro.setVisible(false);

                txtConfirmarContraRegistroMask.setText(txtConfirmarContraRegistro.getText());
                txtConfirmarContraRegistroMask.setVisible(true);
                txtConfirmarContraRegistro.setVisible(false);

            } else {

                txtContraseñaRegistro.setText(txtContraseñaRegistroMask.getText());
                txtContraseñaRegistro.setVisible(true);
                txtContraseñaRegistroMask.setVisible(false);

                txtConfirmarContraRegistro.setText(txtConfirmarContraRegistroMask.getText());
                txtConfirmarContraRegistro.setVisible(true);
                txtConfirmarContraRegistroMask.setVisible(false);
            }
        });

        // accion boton registrar
        btnRegistrarse.setOnAction(e -> registrar());

        // accion boton limpiar
        btnLimpiarRegistrarse.setOnAction(e -> limpiar());
        Platform.runLater(() -> {
            txtEmailRegistro.requestFocus();
        });
    }

    // metodo principal que realiza todas las validaciones y guarda el usuario si todo esta correcto
    private void registrar() {

        String email = txtEmailRegistro.getText();
        String usuario = txtUsuarioRegistro.getText();

        String pass = txtContraseñaRegistro.isVisible()
                ? txtContraseñaRegistro.getText()
                : txtContraseñaRegistroMask.getText();

        String confirm = txtConfirmarContraRegistro.isVisible()
                ? txtConfirmarContraRegistro.getText()
                : txtConfirmarContraRegistroMask.getText();

        // validacion de campos vacíos
        if (email.isEmpty() || usuario.isEmpty() || pass.isEmpty()) {
            mostrarAlerta("Error", "Campos vacíos");
            return;
        }

        // validar el formato del email (que tenga su forma completa para evitar errores)
        if (!UsuarioService.emailValido(email)) {
            mostrarAlerta("Error", "Correo electrónico no válido");
            return;
        }

        // validar si el email ya existe
        if (UsuarioService.emailExiste(email)) {
            mostrarAlerta("Error", "El correo ya está registrado");
            return;
        }

        //  validar contraseñas (que sean iguales)
        if (!pass.equals(confirm)) {
            mostrarAlerta("Error", "Las contraseñas no coinciden");
            return;
        }

        // validar si el usuario ya existe
        if (UsuarioService.usuarioExiste(usuario)) {
            mostrarAlerta("Error", "El usuario ya existe");
            return;
        }

        // Creacion del nuevo usuario
        UsuarioService.agregarUsuario(new Usuario(email, usuario, pass, "cliente"));

        mostrarAlerta("Éxito", "Usuario registrado correctamente");
        limpiar();
    }

    // metodo que limpia todos los campos del formulario.
    private void limpiar() {
        txtEmailRegistro.clear();
        txtUsuarioRegistro.clear();

        txtContraseñaRegistro.clear();
        txtContraseñaRegistroMask.clear();

        txtConfirmarContraRegistro.clear();
        txtConfirmarContraRegistroMask.clear();
        txtEmailRegistro.requestFocus();
    }

    // metodo que muestra las alertas en caso que ocurra algun error en alguna de las validaciones
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
