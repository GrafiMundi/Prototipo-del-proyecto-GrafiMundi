package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import model.*;
import servicios.GraficaService;

public class CatalogoController implements Initializable {

    // contenedor donde se agregan las tarjetas de productos
    @FXML
    private FlowPane contenedorGraficas;

    // lista que almacena las gráficas cargadas desde el archivo
    private ListaGraficas lista;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // mensaje de prueba para verificar que el controlador se ejecuta
        System.out.println("inicializando catalogo");

        // carga los productos desde el archivo de texto
        lista = GraficaService.cargarLista();

        // verifica si la lista está vacía o no se cargó correctamente
        if (lista == null || lista.inicio == null) {
            System.out.println("la lista está vacía o no se cargó");
        }

        // muestra los productos en pantalla
        mostrarGraficas();
    }

    // recorre la lista y crea una tarjeta visual por cada producto
    private void mostrarGraficas() {

        // limpia el contenedor antes de volver a mostrar
        contenedorGraficas.getChildren().clear();

        // si no hay datos, muestra un mensaje
        if (lista == null || lista.inicio == null) {
            Label vacio = new Label("no hay productos disponibles");
            contenedorGraficas.getChildren().add(vacio);
            return;
        }

        // recorre la lista enlazada
        nodoGraficas aux = lista.inicio;

        while (aux != null) {

            // mensaje de prueba para ver qué productos se están mostrando
            System.out.println("mostrando: " + aux.nombre);

            // crea la tarjeta del producto y la agrega al contenedor
            VBox card = crearCard(aux);
            contenedorGraficas.getChildren().add(card);

            aux = aux.sig;
        }
    }

    // crea la tarjeta visual de cada producto
    private VBox crearCard(nodoGraficas g) {

        // imagen del producto
        ImageView img = new ImageView();

        try {
            // construye la ruta de la imagen
            String ruta = "/ImagenesGrafiMundi/" + g.imagen;

            // carga la imagen 
            Image image = new Image(
                getClass().getResource(ruta).toExternalForm()
            );

            img.setImage(image);

        } catch (Exception e) {
            // mensaje en caso de error al cargar la imagen
            System.out.println("error cargando imagen: " + g.imagen);
        }

        // tamaño de la imagen
        img.setFitWidth(120);
        img.setFitHeight(100);

        // nombre del producto
        Label nombre = new Label(g.nombre);
        nombre.setWrapText(true);

        // precio del producto
        Label precio = new Label("$" + String.format("%,.0f", g.precio));

        // cantidad disponible
        Label stock = new Label("stock: " + g.cantidad);

        // botón para comprar
        Button btn = new Button("comprar");

        // acción del botón
        btn.setOnAction(e -> {

            // verifica si hay stock disponible
            if (g.cantidad > 0) {

                // reduce la cantidad en uno
                g.cantidad--;

                System.out.println("compraste: " + g.nombre);
                System.out.println("stock restante: " + g.cantidad);

                // guarda los cambios en el archivo
                GraficaService.guardarLista(lista);

                // actualiza la vista del catálogo
                mostrarGraficas();

            } else {
                System.out.println("sin stock");
            }
        });

        VBox card = new VBox(8);

        card.getChildren().addAll(img, nombre, precio, stock, btn);

        card.setStyle(
            "-fx-border-color: black;" +
            "-fx-padding: 10;" +
            "-fx-background-color: #f5f5f5;"
        );

        card.setPrefWidth(150);

        return card;
    }
}