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

    @FXML
    private StackPane containerForm;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarVista("IniciarSesionForm.fxml");
    }

    @FXML
    private void irLogin() {
        cargarVista("IniciarSesionForm.fxml");
    }

    @FXML
    private void irRegistro() {
        cargarVista("RegistrarseForm.fxml");
    }

    private void cargarVista(String fxml) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("/view/" + fxml));
            containerForm.getChildren().setAll(vista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}