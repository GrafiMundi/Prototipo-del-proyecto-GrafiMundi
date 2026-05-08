package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import servicios.UsuarioService;

// clase principal de la tienda virtual GrafiMundi  
public class GrafiMundi extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        UsuarioService.cargarUsuarios();

        Parent root = FXMLLoader.load(getClass().getResource("/view/GrafiMundiView.fxml"));

        primaryStage.setTitle("GrafiMundi");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/* Autores: Coolin Jimenez, Alejandro Ballesteros, Pedro Sierra, Juan Arteaga, Mariana Lopez*/
