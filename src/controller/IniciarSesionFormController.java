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

        // metodos para mostrar o ocultar contraseñas
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

    // metodo que valida los datos y verifica las credenciales del usuario
    private void login(ActionEvent event) {
        
        // metodo que obtiene los datos ingresados
        String usuario = txtUsuarioIniciarSesion.getText();
        
        // metodo que obtiene la contraseña dependiendo si esta oculta o no
        String contraseña = txtContraseñaInicio.isVisible()
                ? txtContraseñaInicio.getText()
                : txtContraseñaInicioMask.getText();

        // validacion de campos vacios
        if (usuario.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Error", "Campos vacíos");
            return;
        }

        Usuario user = UsuarioService.login(usuario, contraseña);

        if (user != null) {

            if (user.getRol().equals("admin")) {
                
                // mensaje de bienvenida para admin (temporal)
                mostrarAlerta("Bienvenido", "Ingresaste como ADMIN");

                // cambiar a vista admin (al mismo catálogo por ahora)
                cambiarVista("/view/Catalogo.fxml", event);

            } else {
                
                // mensaje de bienvenida para cliente (temporal)
                mostrarAlerta("Bienvenido", "Ingresaste como CLIENTE");

                // cambiar al catálogo
                cambiarVista("/view/Catalogo.fxml", event);
            }

        } else {
            mostrarAlerta("Error", "Usuario o contraseña incorrectos");
        }
    }
        
    // método que cambia de ventana cargando un archivo fxml
    private void cambiarVista(String rutaFXML, ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource(rutaFXML)
            );

            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource())
                    .getScene().getWindow();

            stage.setScene(new javafx.scene.Scene(root));
            stage.show();

        } catch (Exception e) {
            // imprime el error si falla la carga del fxml
            e.printStackTrace();
        }
    }
    
    // metodo que limpia todos los campos del formulario.
    private void limpiar() {
        txtUsuarioIniciarSesion.clear();
        txtContraseñaInicio.clear();
        txtContraseñaInicioMask.clear();
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
