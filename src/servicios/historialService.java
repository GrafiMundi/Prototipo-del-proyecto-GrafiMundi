package servicios;

import model.Usuario;
import model.nodoGraficas;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class historialService {

    private static final String CARPETA = "dataHistorial/";

    public static File obtenerArchivoUsuario() {

        Usuario usuario = UsuarioService.getUsuarioActual();

        if (usuario == null || usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            return null;
        }

        String ruta = CARPETA + usuario.getUsername() + ".txt";

        return new File(ruta);
    }

    public static void inicializarHistorialUsuario() {

        try {

            Usuario usuario = UsuarioService.getUsuarioActual();

            if (usuario == null || usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                return;
            }

            File carpeta = new File(CARPETA);

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            File archivo = new File(CARPETA + usuario.getUsername() + ".txt");

            if (!archivo.exists()) {
                archivo.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void registrarCompra(nodoGraficas carrito) {

        try {

            inicializarHistorialUsuario();

            File archivo = obtenerArchivoUsuario();

            if (archivo == null) {
                return;
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            nodoGraficas actual = carrito;

            while (actual != null) {

                bw.write("----- ITEM -----");
                bw.newLine();

                bw.write("Fecha: " + LocalDateTime.now().format(formatter));
                bw.newLine();

                bw.write("Producto: " + actual.getNombre());
                bw.newLine();

                bw.write("Cantidad: " + actual.getCantidad());
                bw.newLine();

                bw.write("Precio: " + actual.getPrecio());
                bw.newLine();

                bw.write("Imagen: " + actual.getImagen());
                bw.newLine();

                bw.write("------------------");
                bw.newLine();
                bw.newLine();

                actual = actual.sig;
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void registrarCompraIndividual(nodoGraficas producto, int cantidad) {

        try {

            inicializarHistorialUsuario();

            File archivo = obtenerArchivoUsuario();

            if (archivo == null) {
                return;
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            bw.write("----- ITEM -----");
            bw.newLine();

            bw.write("Fecha: " + LocalDateTime.now().format(formatter));
            bw.newLine();

            bw.write("Producto: " + producto.getNombre());
            bw.newLine();

            bw.write("Cantidad: " + cantidad);  
            bw.newLine();

            bw.write("Precio: " + String.format("%.0f", producto.getPrecio()));
            bw.newLine();

            bw.write("Imagen: " + producto.getImagen());
            bw.newLine();

            bw.write("------------------");
            bw.newLine();
            bw.newLine();

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
