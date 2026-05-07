package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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

        // mostrar u ocultar contraseña
        checkVerContraseñaInicio.setOnAction(e -> {

            if (checkVerContraseñaInicio.isSelected()) {
                txtContraseñaInicioMask.setText(
                        txtContraseñaInicio.getText()
                );
                txtContraseñaInicioMask.setVisible(true);
                txtContraseñaInicio.setVisible(false);
            } else {

                txtContraseñaInicio.setText(
                        txtContraseñaInicioMask.getText()
                );

                txtContraseñaInicio.setVisible(true);
                txtContraseñaInicioMask.setVisible(false);
            }
        });

        // accion boton ingresar
        btnIngresarInicioS.setOnAction(this::login);

        // accion boton limpiar
        btnLimpiarInicioS.setOnAction(e -> limpiar());
        Platform.runLater(() -> {
            txtUsuarioIniciarSesion.requestFocus();
        });
    }

    // metodo que valida el login
    private void login(ActionEvent event) {

        // obtener usuario
        String usuario
                = txtUsuarioIniciarSesion.getText();

        // obtener contraseña dependiendo del campo visible
        String contraseña
                = txtContraseñaInicio.isVisible()
                ? txtContraseñaInicio.getText()
                : txtContraseñaInicioMask.getText();

        // validar campos vacios
        if (usuario.isEmpty() || contraseña.isEmpty()) {

            mostrarAlerta("Error", "Campos vacíos");

            return;
        }

        // validar credenciales
        Usuario user
                = UsuarioService.login(usuario, contraseña);

        // si las credenciales son correctas
        if (user != null) {

            // guardar sesion
            UsuarioService.setUsuarioActual(user);

            // mensaje personalizado
            mostrarAlerta(
                    "Bienvenido",
                    "Bienvenido " + user.getUsername()
            );

            // abrir catalogo maximizado
            cambiarVista(
                    "/view/Catalogo.fxml",
                    event
            );

        } else {

            // credenciales incorrectas
            mostrarAlerta("Error", "Usuario o contraseña incorrectos");
        }
    }

    // metodo para cambiar de ventana
    private void cambiarVista(
            String rutaFXML,
            ActionEvent event
    ) {

        try {

            FXMLLoader loader
                    = new FXMLLoader(
                            getClass().getResource(rutaFXML)
                    );

            Parent root = loader.load();
            Stage stage
                    = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // abrir maximizado
            stage.setMaximized(true);

            stage.show();

        } catch (Exception e) {

            System.out.println(
                    "error al cambiar de vista"
            );

            e.printStackTrace();
        }
    }

    // limpiar campos
    private void limpiar() {
        txtUsuarioIniciarSesion.clear();
        txtContraseñaInicio.clear();
        txtContraseñaInicioMask.clear();
        txtUsuarioIniciarSesion.requestFocus();
    }

    // mostrar alertas
    private void mostrarAlerta(
            String titulo,
            String mensaje
    ) {

        Alert alert
                = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
