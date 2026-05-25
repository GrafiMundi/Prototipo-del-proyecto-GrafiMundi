package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.nodoGraficas;
import servicios.GraficaService;

public class ItemCarritoController {

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblPrecio;

    @FXML
    private Label lblCantidad;

    @FXML
    private Label lblDisponibles;

    @FXML
    private ImageView imgProducto;

    private nodoGraficas producto;
    private int cantidad;

    // stock real disponible
    private int stockDisponible;

    private CarritoController carritoController;

    public void inicializar(nodoGraficas producto, CarritoController carritoController) {

        this.producto = producto;
        this.carritoController = carritoController;
        this.cantidad = 1;

        lblNombre.setText(producto.getNombre());
        lblPrecio.setText("$ " + String.format("%,.0f", producto.getPrecio()));

        // cargar imagen
        cargarImagen(producto.getImagen());

        // leer stock
        stockDisponible = obtenerStockDesdeArchivo(producto.getCodigo());

        actualizarVista();
    }

    // carga de imagen
    private void cargarImagen(String nombreImagen) {

        try {
            Image img = new Image(
                    getClass().getResourceAsStream("/ImagenesGrafiMundi/" + nombreImagen)
            );

            imgProducto.setImage(img);

        } catch (Exception e) {
            System.out.println("Error cargando imagen: " + nombreImagen);
        }
    }

    private int obtenerStockDesdeArchivo(String codigo) {

        var lista = GraficaService.cargarLista();
        var aux = lista.inicio;

        while (aux != null) {
            if (aux.getCodigo().equals(codigo)) {
                return aux.getCantidad();
            }
            aux = aux.sig;
        }

        return 0;
    }

    private void actualizarVista() {

        lblCantidad.setText(String.valueOf(cantidad));
        lblDisponibles.setText(stockDisponible + " disponibles");
    }

    // boton para aumentar cantidad
    @FXML
    private void aumentarCantidad() {

        if (cantidad < stockDisponible) {
            cantidad++;
            actualizarVista();
            carritoController.actualizarTotales();
        }
    }

    // boton para disminuir cantidad
    @FXML
    private void disminuirCantidad() {

        if (cantidad > 1) {
            cantidad--;
            actualizarVista();
            carritoController.actualizarTotales();
        }
    }

    @FXML
    private void eliminarItem() {
        carritoController.eliminarItem(this);
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return producto.getPrecio();
    }

    public nodoGraficas getProducto() {
        return producto;
    }
}
