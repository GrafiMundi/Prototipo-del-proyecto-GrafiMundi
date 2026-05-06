package servicios;

import model.*;
import java.io.*;

public class GraficaService {

    // metodo que lee el archivo de texto y carga los productos en memoria
    public static ListaGraficas cargarLista() {

        // se crea una lista vacía donde se guardarán las gráficas
        ListaGraficas lista = new ListaGraficas();

        // se abre el archivo en modo lectura
        try (BufferedReader br = new BufferedReader(new FileReader("dataGraficas/graficas.txt"))) {

            String linea;

            // se recorre el archivo línea por línea
            while ((linea = br.readLine()) != null) {

                // si la línea está vacía, se ignora
                if (linea.trim().isEmpty()) continue;

                // se separan los datos usando ";"
                String[] d = linea.split(";");

                // si la línea no tiene todos los datos necesarios, se ignora
                if (d.length < 7) continue;

                // se crea un nodo con los datos leídos
                nodoGraficas nodo = new nodoGraficas(
                        d[0].trim(), // codigo
                        d[1].trim(), // nombre
                        d[2].trim(), // precio
                        d[3].trim(), // descripcion
                        d[4].trim(), // marca
                        Integer.parseInt(d[5].trim()), // cantidad
                        d[6].trim()  // imagen
                );

                // se agrega el nodo a la lista
                lista.agregar(nodo);
            }

        } catch (Exception e) {
            // manejo de errores en caso de fallo al leer el archivo
            System.out.println("error al cargar archivo");
            e.printStackTrace();
        }

        // retorna la lista con todos los productos cargados
        return lista;
    }

    // metodo que guarda la lista actual en el archivo de texto
    public static void guardarLista(ListaGraficas lista) {

        // se abre el archivo en modo escritura
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("dataGraficas/graficas.txt"))) {
            nodoGraficas aux = lista.inicio;
            while (aux != null) {

                // se construye la línea con el mismo formato del archivo
                String linea = aux.codigo + ";" +
                               aux.nombre + ";" +
                               aux.precio + ";" +
                               aux.descripcion + ";" +
                               aux.marca + ";" +
                               aux.cantidad + ";" +
                               aux.imagen;

                // se escribe la línea en el archivo
                bw.write(linea);
                bw.newLine();

                // se pasa al siguiente nodo
                aux = aux.sig;
            }

        } catch (Exception e) {
            // manejo de errores en caso de fallo al guardar
            System.out.println("error al guardar archivo");
            e.printStackTrace();
        }
    }
}
