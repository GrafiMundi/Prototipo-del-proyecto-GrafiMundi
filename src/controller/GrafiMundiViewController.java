package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;

public class GrafiMundiViewController implements Initializable {

    // contenedor dinámico donde se cargan las vistas (login y registro)
    @FXML
    private StackPane containerForm;
    
    @FXML
    private Button btnRegistro;
    
    @FXML
    private Button btnLogin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarVista("IniciarSesionForm.fxml");
    }

    // metodo ligado a el boton que lleva al login
    @FXML
    private void irLogin() {
        cargarVista("IniciarSesionForm.fxml");
    }

    // metodo ligado a el boton que lleva al registro
    @FXML
    private void irRegistro() {
        cargarVista("RegistrarseForm.fxml");
    }

    // metodo que carga y cambia la vista de los fxml mostrados dentro del contenedor dinamico
    private void cargarVista(String fxml) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("/view/" + fxml));
            containerForm.getChildren().setAll(vista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}