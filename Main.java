import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner entrada = new Scanner(System.in);
        ArrayList<String> ciudades = new ArrayList<>();
        Map<String, Map<String, int[]>> tiempos = new HashMap<>();

        // Leer el archivo logistica.txt
        BufferedReader archivo = new BufferedReader(new FileReader("logistica.txt"));
        String linea;
        while ((linea = archivo.readLine()) != null) {
            if (linea.trim().isEmpty()) continue;

            String[] partes = linea.trim().split("\\s+");
            if (partes.length < 6) {
                System.out.println("Línea inválida en archivo: " + linea);
                continue;
            }

            String desde = partes[0].trim();
            String hasta = partes[1].trim();
            int[] tiemposClima = {
                Integer.parseInt(partes[2]), // clima normal
                Integer.parseInt(partes[3]), // lluvia
                Integer.parseInt(partes[4]), // calor (antes nieve)
                Integer.parseInt(partes[5])  // tormenta
            };

            if (!ciudades.contains(desde)) ciudades.add(desde);
            if (!ciudades.contains(hasta)) ciudades.add(hasta);

            tiempos.putIfAbsent(desde, new HashMap<>());
            tiempos.get(desde).put(hasta, tiemposClima);
        }
        archivo.close();

        Grafo grafo = new Grafo(ciudades);
        int climaActual = 0; // 0=normal, 1=lluvia, 2=calor, 3=tormenta

        actualizarGrafoConClima(grafo, tiempos, climaActual);

        int opcion = 0;
        while (opcion != 5) {
            System.out.println("\n-- MENÚ --");
            System.out.println("1. Ver ruta más corta entre dos ciudades");
            System.out.println("2. Ver ciudad centro del grafo");
            System.out.println("3. Cambiar conexiones del grafo");
            System.out.println("4. Cambiar condición climática");
            System.out.println("5. Salir");
            System.out.print("Elegí una opción: ");
            opcion = entrada.nextInt();
            entrada.nextLine();

            switch (opcion) {
                case 1 -> {
                    System.out.print("Ciudad de origen: ");
                    String desde = entrada.nextLine().trim();
                    System.out.print("Ciudad de destino: ");
                    String hasta = entrada.nextLine().trim();

                    if (!grafo.nombreAIndice.containsKey(desde) || !grafo.nombreAIndice.containsKey(hasta)) {
                        System.out.println("Una de las ciudades no existe en el grafo.");
                        break;
                    }

                    List<String> ruta = grafo.obtenerRuta(desde, hasta);
                    if (ruta == null) {
                        System.out.println("No se encontró ruta disponible.");
                    } else {
                        System.out.println("Ruta: " + String.join(" -> ", ruta));
                        int tiempo = grafo.matriz[grafo.nombreAIndice.get(desde)][grafo.nombreAIndice.get(hasta)];
                        System.out.println("Tiempo estimado: " + tiempo + " horas");
                    }
                }

                case 2 -> {
                    String centro = grafo.obtenerCentroDelGrafo();
                    System.out.println("La ciudad en el centro del grafo es: " + centro);
                }

                case 3 -> {
                    System.out.println("1. Quitar conexión");
                    System.out.println("2. Agregar conexión nueva");
                    System.out.print("Elegí: ");
                    int tipo = entrada.nextInt();
                    entrada.nextLine();

                    System.out.print("Ciudad 1: ");
                    String c1 = entrada.nextLine().trim();
                    System.out.print("Ciudad 2: ");
                    String c2 = entrada.nextLine().trim();

                    if (tipo == 1) {
                        grafo.eliminarArco(c1, c2);
                        tiempos.getOrDefault(c1, new HashMap<>()).remove(c2);
                        System.out.println("Se eliminó la conexión.");
                    } else if (tipo == 2) {
                        int[] nuevosTiempos = new int[4];
                        System.out.print("Tiempo con clima normal: ");
                        nuevosTiempos[0] = entrada.nextInt();
                        System.out.print("Tiempo con lluvia: ");
                        nuevosTiempos[1] = entrada.nextInt();
                        System.out.print("Tiempo con calor: ");
                        nuevosTiempos[2] = entrada.nextInt(); // reemplazo de "nieve"
                        System.out.print("Tiempo con tormenta: ");
                        nuevosTiempos[3] = entrada.nextInt();
                        entrada.nextLine();

                        tiempos.putIfAbsent(c1, new HashMap<>());
                        tiempos.get(c1).put(c2, nuevosTiempos);
                        grafo.agregarArco(c1, c2, nuevosTiempos[climaActual]);
                        System.out.println("Conexión agregada.");
                    }

                    actualizarGrafoConClima(grafo, tiempos, climaActual);
                    System.out.println("Se actualizaron las rutas.");
                }

                case 4 -> {
                    System.out.println("Seleccione condición climática:");
                    System.out.println("0. Normal");
                    System.out.println("1. Lluvia");
                    System.out.println("2. Calor"); 
                    System.out.println("3. Tormenta");
                    System.out.print("Opción: ");
                    climaActual = entrada.nextInt();
                    entrada.nextLine();
                    actualizarGrafoConClima(grafo, tiempos, climaActual);
                    System.out.println("Condición climática actualizada.");
                }

                case 5 -> System.out.println("Gracias por usar el programa!");
                default -> System.out.println("Opción no válida.");
            }
        }

        entrada.close();
    }

    private static void actualizarGrafoConClima(Grafo grafo, Map<String, Map<String, int[]>> tiempos, int climaActual) {
        for (String desde : tiempos.keySet()) {
            for (String hasta : tiempos.get(desde).keySet()) {
                int tiempo = tiempos.get(desde).get(hasta)[climaActual];
                grafo.agregarArco(desde, hasta, tiempo);
            }
        }
        grafo.floydWarshall();
    }
}
