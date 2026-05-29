package servicios;

import model.Usuario;
import java.util.ArrayList;
import java.io.*;

// clase que gestiona los usuarios, carga desde el archivo (.txt), guarda nuevos usuarios y valida el login
public class UsuarioService {

    // lista en memoria donde se almacenan los usuarios
    private static final ArrayList<Usuario> usuarios = new ArrayList<>();

    // usuario que tiene la sesión iniciada actualmente
    private static Usuario usuarioActual;

    // retorna el usuario que tiene la sesion iniciada
    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // cambia el usuario actual de la sesion
    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    // ruta del archivo donde se guardan los usuarios
    private static final String ARCHIVO = "data/usuarios.txt";

    // carga los usuarios desde el archivo de texto, si el archivo o carpeta no existen, los crea automáticamente e inicializa con usuarios de prueba (admin y cliente)
    public static void cargarUsuarios() {
        usuarios.clear();

        File archivo = new File(ARCHIVO);

        try {

            File carpeta = archivo.getParentFile();

            // crear carpeta si no existe
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }

            // crear archivo con usuarios iniciales
            if (!archivo.exists()) {

                archivo.createNewFile();

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {

                    bw.write("admin@grafimundi.com,admin,1234,admin");
                    bw.newLine();

                    bw.write("cliente@gmail.com,cliente,1234,cliente");
                    bw.newLine();
                }
            }
            
            // leer archivo
            BufferedReader br = new BufferedReader(new FileReader(archivo));

            String linea;

            while ((linea = br.readLine()) != null) {

                String[] datos = linea.split(",");

                // validar que tenga todos los datos
                if (datos.length == 4) {

                    usuarios.add(
                            new Usuario(
                                    datos[0],
                                    datos[1],
                                    datos[2],
                                    datos[3]
                            )
                    );
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // metodo para validar el formato de email (que permita usar todos los caracteres de un email real)
    public static boolean emailValido(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }

    // metodo que verifica si el usuario ya existe en la lista, retorna true en caso que exista y false si no
    public static boolean usuarioExiste(String username) {
        return usuarios.stream()
                .anyMatch(u -> u.getUsername().equals(username));
    }

    // metodo para verificar que si el email ya existe
    public static boolean emailExiste(String email) {
        return usuarios.stream()
                .anyMatch(u -> u.getEmail().equals(email));
    }

    // metodo que agrega un nuevo usuario tanto a la lista en memoria como al archivo de texto
    public static void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);

        try {

            File archivo = new File(ARCHIVO);
            File carpeta = archivo.getParentFile();

            // asegura que la carpeta exista
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }

            // escribe el nuevo usuario al final del archivo
            BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true));
            bw.write(usuario.getEmail() + "," + usuario.getUsername() + "," + usuario.getPassword() + "," + usuario.getRol());
            bw.newLine();
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // este metodo verifica las credenciales de un usuario (Usuario y Contraseña) retorna un usuario si las credenciales son correctas y retorna null en caso que no
    public static Usuario login(String username, String password) {

        Usuario user = usuarios.stream()
                .filter(u
                        -> u.getUsername().equals(username)
                && u.getPassword().equals(password)
                )
                .findFirst()
                .orElse(null);

        // guardar usuario actual si el login es correcto
        if (user != null) {
            usuarioActual = user;
            
            historialService.inicializarHistorialUsuario();
        }

        return user;
    }

    // cerrar sesión
    public static void cerrarSesion() {
        usuarioActual = null;
    }
}
