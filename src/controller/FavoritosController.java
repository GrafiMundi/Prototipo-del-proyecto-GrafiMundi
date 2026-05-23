package controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.PilaFavoritos;
import model.nodoGraficas;

public class FavoritosController {

    private CatalogoController catalogoController;

    public void setCatalogoController(CatalogoController c) {
        this.catalogoController = c;
    }

    @FXML
    private VBox vxFav;
    
    @FXML
    private Label lblvacio;

    public void mostrarFavoritos() {
        while (PilaFavoritos.pos < PilaFavoritos.totalNodos) {
            PilaFavoritos.pos++;
            nodoGraficas g = PilaFavoritos.mostrar();
            agregarPanel(g);
        }
        PilaFavoritos.pos = 0;
        if (vxFav.getChildren().isEmpty()) {
            lblvacio.setText("No hay productos favoritos");
        } else {
            lblvacio.setText("");
        }
    }

    public void agregarPanel(nodoGraficas g) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fav.fxml"));
            Pane panel = loader.load();

            FavController controller = loader.getController();
            controller.setDatos(g);

            vxFav.getChildren().add(panel);
        } catch (IOException e) {
        }
    }

    @FXML
    private void vaciarFavoritos(ActionEvent event) {
        PilaFavoritos.limpiarPila();
        vxFav.getChildren().clear();
        lblvacio.setText("No hay productos favoritos");
    }

    @FXML
    private void salir(ActionEvent event) {
        if (catalogoController != null) {
            catalogoController.mostrarCatalogo();
        }
    }

}
