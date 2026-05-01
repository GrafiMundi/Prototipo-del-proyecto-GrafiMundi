package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import servicios.UsuarioService;

public class GrafiMundi extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        UsuarioService.cargarUsuarios();

        Parent root = FXMLLoader.load(getClass().getResource("/view/GrafiMundiView.fxml"));

        primaryStage.setTitle("GrafiMundi");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
