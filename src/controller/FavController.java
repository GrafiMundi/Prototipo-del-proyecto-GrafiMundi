package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.PilaFavoritos;
import model.nodoGraficas;



public class FavController {

    @FXML
    private Label nombre;
    @FXML
    private Label precio;
    @FXML
    private Label cantidad;
    @FXML
    private ImageView imgF;
    @FXML
    private Button Qtar;

    public void setDatos(nodoGraficas g){
     nombre.setText(g.nombre);
     precio.setText( "$ " + String.format("%,.0f", g.precio)); 
     cantidad.setText("Disponibles: "+String.valueOf(g.cantidad));
      imgF.setImage(new Image(getClass().getResourceAsStream("/imagenesGrafiMundi/"+g.imagen))); 
      Qtar.setUserData(g.codigo);
    }

    @FXML
    private void quitarFavorito(ActionEvent event) {
        Button b=(Button)event.getSource();
       String cod=(String) b.getUserData();
       PilaFavoritos.quitar(cod);
       
       Pane panel=(Pane)b.getParent();
       VBox vx =(VBox)panel.getParent();
       vx.getChildren().remove(panel);
    }
    
}
