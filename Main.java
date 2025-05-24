import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner entrada = new Scanner(System.in);

        ArrayList<String> ciudades = new ArrayList<>();
        ArrayList<String[]> conexiones = new ArrayList<>();

        BufferedReader archivo = new BufferedReader(new FileReader("logistica.txt"));
        String linea;
        while ((linea = archivo.readLine()) != null) {
            String[] partes = linea.split(" ");
            if (!ciudades.contains(partes[0])) {
                ciudades.add(partes[0]);
            }
            if (!ciudades.contains(partes[1])) {
                ciudades.add(partes[1]);
            }
            conexiones.add(partes);
        }
        archivo.close();

        Grafo grafo = new Grafo(ciudades);

        for (String[] conexion : conexiones) {
            String desde = conexion[0];
            String hasta = conexion[1];
            int tiempo = Integer.parseInt(conexion[2]);
            grafo.agregarArco(desde, hasta, tiempo);
        }

        grafo.floydWarshall();

        int opcion = 0;

        while (opcion != 4) {
            System.out.println("\n-- MENÚ --");
            System.out.println("1. Ver ruta más corta entre dos ciudades");
            System.out.println("2. Ver ciudad centro del grafo");
            System.out.println("3. Cambiar conexiones del grafo");
            System.out.println("4. Salir");
            System.out.print("Elegí una opción: ");
            opcion = entrada.nextInt();
            entrada.nextLine();

            if (opcion == 1) {
                System.out.print("Ciudad de origen: ");
                String desde = entrada.nextLine();
                System.out.print("Ciudad de destino: ");
                String hasta = entrada.nextLine();

                List<String> ruta = grafo.obtenerRuta(desde, hasta);
                if (ruta == null) {
                    System.out.println("No se encontró ruta disponible.");
                } else {
                    System.out.println("Ruta: " + String.join(" -> ", ruta));
                    int tiempo = grafo.matriz[grafo.nombreAIndice.get(desde)][grafo.nombreAIndice.get(hasta)];
                    System.out.println("Tiempo estimado: " + tiempo + " horas");
                }

            } else if (opcion == 2) {
                String centro = grafo.obtenerCentroDelGrafo();
                System.out.println("La ciudad en el centro del grafo es: " + centro);

            } else if (opcion == 3) {
                System.out.println("1. Quitar conexión");
                System.out.println("2. Agregar conexión nueva");
                System.out.print("Elegí: ");
                int tipo = entrada.nextInt();
                entrada.nextLine();

                System.out.print("Ciudad 1: ");
                String c1 = entrada.nextLine();
                System.out.print("Ciudad 2: ");
                String c2 = entrada.nextLine();

                if (tipo == 1) {
                    grafo.eliminarArco(c1, c2);
                    System.out.println("Se eliminó la conexión.");
                } else if (tipo == 2) {
                    System.out.print("¿Cuántas horas tarda?: ");
                    int tiempo = entrada.nextInt();
                    grafo.agregarArco(c1, c2, tiempo);
                    System.out.println("Conexión agregada.");
                }

                grafo.floydWarshall();
                System.out.println("Se actualizaron las rutas.");

            } else if (opcion == 4) {
                System.out.println("¡Gracias por usar el programa! Adiós.");
            } else {
                System.out.println("Opción no válida. Probá otra vez.");
            }
        }

        entrada.close();
    }
}
