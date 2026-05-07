package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;
import servicios.GraficaService;
import servicios.UsuarioService;

public class CatalogoController implements Initializable {

    // contenedor donde se agregan las tarjetas de productos
    @FXML
    private FlowPane contenedorGraficas;

    // boton del usuario
    @FXML
    private Button btnUsuario;

    // lista que almacena las gráficas cargadas desde el archivo
    private ListaGraficas lista;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // mensaje de prueba
        System.out.println("inicializando catalogo");

        // carga las graficas desde el archivo
        lista = GraficaService.cargarLista();

        // verifica si la lista fue cargada correctamente
        if (lista == null || lista.inicio == null) {

            System.out.println("la lista esta vacia o no se cargo");
        }

        // verifica si hay un usuario logueado
        if (UsuarioService.getUsuarioActual() != null) {

            // obtiene el nombre del usuario
            String nombreUsuario
                    = UsuarioService.getUsuarioActual().getUsername();

            // cambia el texto del boton
            btnUsuario.setText(nombreUsuario);

            // crear menu flotante
            crearMenuUsuario(nombreUsuario);
        }

        // mostrar productos
        mostrarGraficas();
    }

    // crea el menu flotante del usuario
    private void crearMenuUsuario(String nombreUsuario) {

        // menu flotante
        ContextMenu menu = new ContextMenu();

        // agregar clase css al menu
        menu.getStyleClass().add("menu-usuario");

        // mensaje superior
        MenuItem saludo = new MenuItem(
                "Es un gusto volver a verte " + nombreUsuario
        );

        // desactivar clic del saludo
        saludo.setDisable(true);

        // clases css
        saludo.getStyleClass().add("saludo-menu");

        // item dinamico
        MenuItem opcionPrincipal = new MenuItem();

        opcionPrincipal.getStyleClass().add("item-menu");

        // obtiene el rol del usuario
        String rol = UsuarioService
                .getUsuarioActual()
                .getRol();

        // verifica si es admin
        if (rol.equalsIgnoreCase("admin")) {

            // cambia texto para admin
            opcionPrincipal.setText("Agregar Producto");

            // accion admin
            opcionPrincipal.setOnAction(e -> {

                System.out.println(
                        "abrir panel agregar producto"
                );
            });

        } else {

            // texto normal para clientes
            opcionPrincipal.setText("Historial");

            // accion historial
            opcionPrincipal.setOnAction(e -> {

                System.out.println("abrir historial");
            });
        }

        // opcion cerrar sesion
        MenuItem cerrar = new MenuItem("Cerrar sesión");

        cerrar.getStyleClass().add("item-cerrar");

        // accion cerrar sesion
        cerrar.setOnAction(e -> {

            cerrarSesion();
        });

        // agregar elementos
        menu.getItems().addAll(
                saludo,
                new SeparatorMenuItem(),
                opcionPrincipal,
                cerrar
        );

        // mostrar menu debajo del boton
        btnUsuario.setOnAction(e -> {

            menu.show(btnUsuario, Side.BOTTOM, 0, 5);
        });
    }

    // recorre la lista y crea las cards
    private void mostrarGraficas() {

        // limpia el contenedor
        contenedorGraficas.getChildren().clear();

        // verifica si la lista esta vacia
        if (lista == null || lista.inicio == null) {

            Label vacio = new Label(
                    "no hay productos disponibles"
            );

            contenedorGraficas.getChildren().add(vacio);

            return;
        }

        // recorre la lista doblemente enlazada
        nodoGraficas aux = lista.inicio;

        while (aux != null) {

            // crea la card del producto
            VBox card = crearCard(aux);

            // agrega la card al contenedor
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
            String ruta
                    = "/ImagenesGrafiMundi/" + g.imagen;

            // carga la imagen
            Image image = new Image(
                    getClass()
                            .getResource(ruta)
                            .toExternalForm()
            );

            img.setImage(image);

        } catch (Exception e) {

            System.out.println(
                    "error cargando imagen: " + g.imagen
            );
        }

        // tamaño imagen
        img.setFitWidth(120);
        img.setFitHeight(100);

        // nombre del producto
        Label nombre = new Label(g.nombre);

        nombre.setWrapText(true);

        // precio
        Label precio = new Label(
                "$ " + String.format("%,.0f", g.precio)
        );

        // stock disponible
        Label stock = new Label(
                "stock: " + g.cantidad
        );

        // boton comprar
        Button btn = new Button("Comprar");

        btn.getStyleClass().add("boton-comprar");

        // accion de compra
        btn.setOnAction(e -> {

            // verifica si hay stock
            if (g.cantidad > 0) {

                // reduce stock
                g.cantidad--;

                // guarda cambios en el archivo
                GraficaService.guardarLista(lista);

                // refresca el catalogo
                mostrarGraficas();

            } else {

                System.out.println("sin stock");
            }
        });

        // card principal
        VBox card = new VBox(8);

        card.getChildren().addAll(
                img,
                nombre,
                precio,
                stock,
                btn
        );

        // estilos css
        card.getStyleClass().add("card-producto");

        // ancho card
        card.setPrefWidth(180);

        return card;
    }

    // metodo para cerrar sesion
    private void cerrarSesion() {

        try {

            // elimina la sesion actual
            UsuarioService.setUsuarioActual(null);

            // carga la vista principal
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/GrafiMundiView.fxml")
            );

            Parent root = loader.load();

            // obtiene la ventana actual
            Stage stage = (Stage) contenedorGraficas
                    .getScene()
                    .getWindow();

            // cambia la escena
            stage.setScene(new Scene(root));

            // centrar ventana
            stage.centerOnScreen();

            stage.show();

        } catch (Exception e) {

            System.out.println("error al cerrar sesion");

            e.printStackTrace();
        }
    }
}
