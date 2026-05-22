package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import model.*;
import servicios.GraficaService;
import servicios.UsuarioService;

public class CatalogoController implements Initializable {

    @FXML
    private StackPane rootContainer;

    @FXML
    private Parent vistaCatalogoRoot;

    private Parent vistaCatalogo;

    private boolean invertido = false;

    // contenedor donde se agregan las tarjetas de productos
    @FXML
    private FlowPane contenedorGraficas;

    // boton del usuario
    @FXML
    private Button btnUsuario;

    // lista que almacena las gráficas cargadas desde el archivo
    private ListaGraficas lista;

    // combobox para filtrar productos por precio (actualmente no funcional)
    @FXML
    private ComboBox<String> comboPrecio;

    // combobox para filtrar productos por marca (actualmente no funcional)
    @FXML
    private ComboBox<String> comboMarca;

    @FXML
    private Button btnLimpiarFiltros;

    // contenedor con scroll que permite visualizar las tarjetas de productos
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button btnInvertir;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        scrollPane.setFitToWidth(true);

        // mensaje de prueba
        System.out.println("inicializando catalogo");

        // carga las graficas desde el archivo
        lista = GraficaService.cargarLista();

        // llenar filtros
        comboPrecio.getItems().addAll(
                "Sin orden",
                "Menor a mayor",
                "Mayor a menor"
        );

        comboMarca.getItems().addAll(
                "Todas",
                "AMD",
                "INTEL",
                "NVIDIA"
        );

        comboPrecio.setOnAction(e -> mostrarGraficasFiltradas());
        comboMarca.setOnAction(e -> mostrarGraficasFiltradas());
        btnLimpiarFiltros.setOnAction(e -> limpiarFiltros());

        // verifica si la lista fue cargada correctamente
        if (lista == null || lista.inicio == null) {

            System.out.println("la lista esta vacia o no se cargo");
        }

        // verifica si hay un usuario logueado
        if (UsuarioService.getUsuarioActual() != null) {

            // obtiene el nombre del usuario
            String nombreUsuario
                    = UsuarioService.getUsuarioActual().getUsername();
            String rol = UsuarioService.getUsuarioActual().getRol();

            if (rol.equalsIgnoreCase("admin")) {
                btnInvertir.setVisible(true);
                btnInvertir.setManaged(true);
            }

            // cambia el texto del boton
            btnUsuario.setText(nombreUsuario);

            // crear menu flotante
            crearMenuUsuario(nombreUsuario);
        }

        vistaCatalogo = vistaCatalogoRoot;

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

            // accion que se ejecuta cuando el admin selecciona "agregar producto"
            opcionPrincipal.setOnAction(e -> {

                try {

                    // carga el archivo fxml del formulario de agregar producto
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/view/agregarProducto.fxml")
                    );

                    // carga la vista en memoria
                    Parent root = loader.load();

                    // obtiene el controlador asociado al fxml
                    AgregarProductoController controller = loader.getController();

                    // pasa la referencia del catalogo al otro controlador
                    // esto permite acceder a la lista de productos y actualizarla
                    controller.setCatalogoController(this);

                    // crea una nueva ventana
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Agregar Producto");
                    stage.show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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

    private void abrirDetalleProducto(nodoGraficas grafica) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/detalleProducto.fxml")
            );

            Parent vistaDetalle = loader.load();

            DetalleProductoController controller = loader.getController();
            controller.setProducto(grafica);
            controller.setCatalogoController(this);

            rootContainer.getChildren().setAll(vistaDetalle);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarGraficasFiltradas() {

        contenedorGraficas.getChildren().clear();

        if (lista == null || lista.inicio == null) {
            return;
        }

        String ordenPrecio = comboPrecio.getValue() != null 
        ? comboPrecio.getValue() 
        : "Sin orden";
        String marcaSeleccionada = comboMarca.getValue();

        final String marcaFiltro
                = ("Todas".equals(marcaSeleccionada)) ? null : marcaSeleccionada;

        ArrayList<nodoGraficas> productos = convertirALista();

        // filtro por marca
        if (marcaFiltro != null && !marcaFiltro.isEmpty()) {

            productos.removeIf(g
                    -> !g.marca.equalsIgnoreCase(marcaFiltro)
            );
        }

        // filtro de orden por precio
        if ("Menor a mayor".equals(ordenPrecio)) {

            productos.sort((a, b)
                    -> Double.compare(a.precio, b.precio)
            );

        } else if ("Mayor a menor".equals(ordenPrecio)) {

            productos.sort((a, b)
                    -> Double.compare(b.precio, a.precio)
            );
        }

        // mostrar
        for (nodoGraficas g : productos) {
            contenedorGraficas.getChildren().add(crearCard(g));
        }
    }

    private void mostrarGraficasInverso() {

        contenedorGraficas.getChildren().clear();

        if (lista == null || lista.fin == null) {
            return;
        }

        nodoGraficas actual = lista.fin;
        String marcaSeleccionada = comboMarca.getValue();
        String marcaFiltro = ("Todas".equals(marcaSeleccionada)) ? null : marcaSeleccionada;

        while (actual != null) {

            // aplicar filtros
            if (marcaFiltro == null || actual.marca.equalsIgnoreCase(marcaFiltro)) {
                contenedorGraficas.getChildren().add(crearCard(actual));
            }

            // avanzar hacia atras
            actual = actual.ant;
        }
    }

    private void limpiarFiltros() {

        comboPrecio.getSelectionModel().clearSelection();
        comboMarca.getSelectionModel().clearSelection();

        comboPrecio.setValue(null);
        comboMarca.setValue(null);

        // metodo que pone el promtext original del comboBox
        comboPrecio.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Precio" : item);
            }
        });

        // metodo que pone el promtext original del comboBox
        comboMarca.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Marca" : item);
            }
        });

        if (invertido) {
            mostrarGraficasInverso();
        } else {
            mostrarGraficas();
        }
    }

    @FXML
    private void invertirLista() {

        invertido = !invertido;

        if (invertido) {
            btnInvertir.setText("Orden normal");
            mostrarGraficasInverso();
        } else {
            btnInvertir.setText("Invertir lista");

            if (comboPrecio.getValue() == null && comboMarca.getValue() == null) {
                mostrarGraficas();
            } else {
                mostrarGraficasFiltradas();
            }
        }
    }

    private ArrayList<nodoGraficas> convertirALista() {

        ArrayList<nodoGraficas> listaArray = new ArrayList<>();

        if (lista == null || lista.inicio == null) {
            return listaArray;
        }

        // recorrido hacia adelante
        nodoGraficas actual = lista.inicio;

        while (actual != null) {
            listaArray.add(actual);
            actual = actual.sig;
        }

        // recorrido hacia atras
        nodoGraficas ultimo = lista.inicio;

        while (ultimo.sig != null) {
            ultimo = ultimo.sig;
        }

        while (ultimo != null) {
            ultimo = ultimo.ant;
        }

        return listaArray;
    }

    public void mostrarCatalogo() {
        rootContainer.getChildren().setAll(vistaCatalogo);
    }

    //  metodo que permite agregar un nuevo producto o aumentar el stock si ya existe
    public void agregarOActualizarProducto(
            String codigo,
            String nombre,
            String precio,
            String descripcion,
            String marca,
            String cantidad,
            String imagen
    ) {

        try {

            // busca si ya existe un producto con el mismo codigo
            nodoGraficas existente = lista.buscarPorCodigo(codigo);

            if (existente != null) {

                // si el producto ya existe, se incrementa la cantidad (stock)
                existente.cantidad += Integer.parseInt(cantidad);

                System.out.println("stock actualizado");

            } else {

                // si no existe, se crea un nuevo nodo con los datos ingresados
                nodoGraficas nuevo = new nodoGraficas(
                        codigo,
                        nombre,
                        precio,
                        descripcion,
                        marca,
                        Integer.parseInt(cantidad),
                        imagen
                );

                // se agrega el nuevo producto a la lista enlazada
                lista.agregar(nuevo);

                System.out.println("producto agregado");
            }

            // guarda la lista actualizada en el archivo txt
            GraficaService.guardarLista(lista);

            // actualiza la interfaz para reflejar los cambios
            limpiarFiltros();

        } catch (Exception e) {

            System.out.println("error al agregar o actualizar producto");
            e.printStackTrace();
        }
    }

    public ListaGraficas getLista() {
        return lista;
    }

    // crea la tarjeta visual de cada producto
    private VBox crearCard(nodoGraficas g) {

        // contenedor principal
        VBox card = new VBox(10);
        card.getStyleClass().add("card-producto");

        card.setPrefWidth(328);
        card.setMinWidth(328);
        card.setMaxWidth(328);
        card.setPrefHeight(592);
        card.setMaxHeight(592);

        // imagen del producto
        ImageView img = new ImageView();

        try {

            String ruta = "/ImagenesGrafiMundi/" + g.imagen;

            Image image = new Image(
                    getClass()
                            .getResource(ruta)
                            .toExternalForm()
            );

            img.setImage(image);

        } catch (Exception e) {

            System.out.println("error cargando imagen: " + g.imagen);
        }

        // tamaño imagen 
        img.setFitWidth(302);
        img.setFitHeight(242);
        img.setPreserveRatio(false);

        //nombre
        Label nombre = new Label(g.nombre);
        nombre.setWrapText(true);
        nombre.setMinHeight(40);
        nombre.setMaxHeight(40);

        VBox.setMargin(nombre, new Insets(43, 0, 0, 0));

        //precio
        Label precio = new Label(
                "$ " + String.format("%,.0f", g.precio)
        );
        precio.getStyleClass().add("precio");

        VBox.setMargin(precio, new Insets(32, 0, 0, 0));

        // stock
        Label stock = new Label(
                "Stock: " + g.cantidad
        );
        stock.getStyleClass().add("stock");

        // descripcion
        String desc = (g.descripcion != null) ? g.descripcion : "";

        Label descripcion = new Label(desc);

        descripcion.setWrapText(true);
        descripcion.setMaxWidth(280);
        descripcion.setMinHeight(60);
        descripcion.setMaxHeight(60);

        descripcion.setStyle(
                "-fx-text-overrun: ellipsis;"
        );

        descripcion.getStyleClass().add("descripcion-producto");

        // boton comprar
        Button btn = new Button("Agregar al carrito");
        btn.getStyleClass().add("boton-comprar");

        btn.setOnAction(e -> {

            // verifica si hay stock disponible
            if (g.cantidad > 0) {

                // reduce el stock en 1
                g.cantidad--;

                // guarda los cambios en el archivo
                GraficaService.guardarLista(lista);

                // refresca la interfaz
                if (invertido) {
                    mostrarGraficasInverso();
                } else {
                    mostrarGraficas();
                }
                // alerta con nombre del producto
                mostrarAlerta(
                        "carrito",
                        g.nombre + " agregado al carrito"
                );

            } else {

                // alerta si no hay stock
                mostrarAlerta(
                        "sin stock",
                        "no hay unidades disponibles de " + g.nombre
                );
            }
        });

        // cotenedor interno para ordenar mejor
        VBox info = new VBox(6);
        info.getChildren().addAll(
                nombre,
                precio,
                stock,
                descripcion
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // boton favoritos
        Button favBtn = new Button("Favoritos");
        favBtn.getStyleClass().add("boton-favorito");

        favBtn.setOnAction(e -> {

            // aqui ira la logica de favoritos
            
            // alerta con nombre del producto
            mostrarAlerta(
                    "favoritos",
                    g.nombre + " agregado a favoritos"
            );
        });

        // contenedor de botones
        HBox botones = new HBox(10);
        botones.setAlignment(Pos.CENTER_LEFT);
        botones.getChildren().addAll(favBtn, btn);

        // agregar todo
        card.getChildren().addAll(
                img,
                info,
                spacer,
                botones
        );

        card.setOnMouseClicked(e -> {

            if (e.getTarget() instanceof Button) {
                return;
            }

            abrirDetalleProducto(g);
        });

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

    @FXML
    private void favoritos(ActionEvent event) {
    }
    
}