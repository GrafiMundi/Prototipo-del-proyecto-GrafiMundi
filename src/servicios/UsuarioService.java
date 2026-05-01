package servicios;

import model.Usuario;
import java.util.ArrayList;
import java.io.*;

public class UsuarioService {

    private static final ArrayList<Usuario> usuarios = new ArrayList<>();
    private static final String ARCHIVO = "data/usuarios.txt";

    public static void cargarUsuarios() {
        usuarios.clear();

        File archivo = new File(ARCHIVO);

        try {
            File carpeta = archivo.getParentFile();
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }

            if (!archivo.exists()) {
                archivo.createNewFile();

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                    bw.write("admin,1234,admin");
                    bw.newLine();
                    bw.write("cliente,1234,cliente");
                    bw.newLine();
                }
            }

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");

                if (datos.length == 3) {
                    usuarios.add(new Usuario(datos[0], datos[1], datos[2]));
                }
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean usuarioExiste(String username) {
        return usuarios.stream()
                .anyMatch(u -> u.getUsername().equals(username));
    }

    public static void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);

        try {

            File archivo = new File(ARCHIVO);
            File carpeta = archivo.getParentFile();

            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true));
            bw.write(usuario.getUsername() + "," + usuario.getPassword() + "," + usuario.getRol());
            bw.newLine();
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Usuario login(String username, String password) {
        return usuarios.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}
