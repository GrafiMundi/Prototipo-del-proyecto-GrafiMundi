package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import model.nodoGraficas;

public class ItemCarritoController {

    @FXML 
    private ImageView imgProducto;
    
    @FXML 
    private Label lblNombre;
    
    @FXML 
    private Label lblPrecio;
    
    @FXML 
    private Label lblCantidad;
    
    @FXML 
    private Label lblDisponibles;

    @FXML 
    private Button btnMas;
    
    @FXML 
    private Button btnMenos;
    
    @FXML 
    private Button btnEliminar;

    private nodoGraficas producto;
    private int cantidadSeleccionada = 1;
    private CarritoController carritoController;

    public void setProducto(nodoGraficas p) {
        this.producto = p;

        lblNombre.setText(p.getNombre());

        lblPrecio.setText("$ " + String.format("%,.0f", p.getPrecio()));

        lblDisponibles.setText("+" + p.getCantidad() + " disponibles");

        lblCantidad.setText(String.valueOf(cantidadSeleccionada));

        // carga de imagen
        try {
            String ruta = "/ImagenesGrafiMundi/" + p.getImagen();

            Image img = new Image(
                getClass().getResource(ruta).toExternalForm()
            );

            imgProducto.setImage(img);

        } catch (Exception e) {
            System.out.println("error cargando imagen carrito: " + p.getImagen());
        }

        configurarEventos();
    }
    
    public void setCarritoController(CarritoController controller) {
        this.carritoController = controller;
    }

    private void configurarEventos() {

        btnMas.setOnAction(e -> {
            if (cantidadSeleccionada < producto.getCantidad()) {
                cantidadSeleccionada++;
                lblCantidad.setText(String.valueOf(cantidadSeleccionada));
                
                producto.setCantidad(cantidadSeleccionada);
                carritoController.actualizarVista();
            }
        });

        btnMenos.setOnAction(e -> {
            if (cantidadSeleccionada > 1) {
                cantidadSeleccionada--;
                lblCantidad.setText(String.valueOf(cantidadSeleccionada));
                
                producto.setCantidad(cantidadSeleccionada);
                carritoController.actualizarVista();
            }
        });

        btnEliminar.setOnAction(e -> {

            // eliminar del carrito
            if (carritoController != null) {
                carritoController.eliminarProducto(producto.getCodigo());
                carritoController.actualizarVista();
            }
        });
    }
}