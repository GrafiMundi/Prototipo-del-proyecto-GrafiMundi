package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.PilaFavoritos;
import model.nodoGraficas;


public class DetalleProductoController {
    
    private CatalogoController catalogoController;

    public void setCatalogoController(CatalogoController c) {
        this.catalogoController = c;
    }

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblPrecio;
    
    @FXML
    private Label lblPrecioPanel;

    @FXML
    private Label lblDescripcion;

    @FXML
    private Label lblMarca;

    @FXML
    private Label lblCantidad;

    @FXML
    private ImageView imgProducto;

    @FXML
    private Button btnFav;

    @FXML
    private Button btnCarrito;
    
    @FXML
    private Button btnVolver;

    @FXML
private void volverAlCatalogo() {

    if (catalogoController != null) {
        catalogoController.mostrarCatalogo();
    }
}
    public void setProducto(nodoGraficas p) {

        lblNombre.setText(p.nombre);
        lblPrecioPanel.setText(p.nombre);
        
        // formato de precio
        lblPrecio.setText("$ " + String.format("%,.0f", p.precio));
        lblPrecioPanel.setText("$ " + String.format("%,.0f", p.precio));

        // evitar null en descripcion (evita una descipcion vacia)
        lblDescripcion.setText(
                (p.descripcion != null && !p.descripcion.isEmpty())
                ? p.descripcion
                : "Sin descripción"
        );

        lblMarca.setText("Marca: " + p.marca);
        lblCantidad.setText("Stock: " + p.cantidad);

        // imagen del producto
        try {
            String ruta = "/ImagenesGrafiMundi/" + p.imagen;

            if (getClass().getResource(ruta) != null) {

                Image img = new Image(
                        getClass().getResource(ruta).toExternalForm()
                );

                imgProducto.setImage(img);

            } else {
                System.out.println("imagen no encontrada: " + p.imagen);
            }

        } catch (Exception e) {
            System.out.println("error cargando imagen: " + p.imagen);
        }

        // cambiar titulo de ventana
        try {
            Stage stage = (Stage) lblNombre.getScene().getWindow();
            if (stage != null) {
                stage.setTitle(p.nombre);
            }
        } catch (Exception e) {}

        // boton del carrito
        btnCarrito.setOnAction(e -> {

            if (p.cantidad > 0) {

                p.cantidad--;

                //muestra conceptual de como debe disminuir el stock luego de comprar algo (por el momento solo se reduce para dar una idea de como se vera)
                lblCantidad.setText("Stock: " + p.cantidad);

                mostrarAlerta(
                    "carrito",
                    p.nombre + " agregado al carrito"
            );

            } else {

                System.out.println("Sin stock de " + p.nombre);
            }
        });

        // boton de favoritos
        btnFav.setOnAction(e -> {
             if(PilaFavoritos.repetido(p.codigo)) {mostrarAlerta( "","Esta gráfica ya está en tu lista de favoritos");}
            else{ PilaFavoritos.agregar(p);    
            mostrarAlerta( "favoritos",p.nombre + " agregado a favoritos");   }
        });
    }
    
    // metodo utilitario para mostrar alertas informativas al usuario, se reutiliza en acciones como agregar al carrito o favoritos
    private void mostrarAlerta(
            String titulo,
            String mensaje
    ) {

        // crea una alerta de tipo informacion
        javafx.scene.control.Alert alert
                = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION
                );
        
        alert.setTitle(titulo);
        alert.setHeaderText(null);

        // mensaje principal que se mostrara al usuario
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}