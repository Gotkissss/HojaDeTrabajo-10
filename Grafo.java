import java.util.*;

public class Grafo {
    public int INF = 999999;
    public int[][] matriz;
    public int[][] caminos;
    public String[] ciudades;
    public HashMap<String, Integer> nombreAIndice;
    public HashMap<Integer, String> indiceANombre;
    public int cantidad;

    public Grafo(ArrayList<String> nombres) {
        cantidad = nombres.size();
        matriz = new int[cantidad][cantidad];
        caminos = new int[cantidad][cantidad];
        ciudades = new String[cantidad];
        nombreAIndice = new HashMap<>();
        indiceANombre = new HashMap<>();

        for (int i = 0; i < cantidad; i++) {
            ciudades[i] = nombres.get(i).trim();
            nombreAIndice.put(ciudades[i], i);
            indiceANombre.put(i, ciudades[i]);
        }

        for (int i = 0; i < cantidad; i++) {
            for (int j = 0; j < cantidad; j++) {
                matriz[i][j] = (i == j) ? 0 : INF;
            }
        }
    }

    public void agregarArco(String desde, String hasta, int tiempo) {
        int i = nombreAIndice.get(desde);
        int j = nombreAIndice.get(hasta);
        matriz[i][j] = tiempo;
    }

    public void eliminarArco(String desde, String hasta) {
        int i = nombreAIndice.get(desde);
        int j = nombreAIndice.get(hasta);
        matriz[i][j] = INF;
    }

    public void floydWarshall() {
        int[][] nuevaMatriz = new int[cantidad][cantidad];
        caminos = new int[cantidad][cantidad];

        for (int i = 0; i < cantidad; i++) {
            for (int j = 0; j < cantidad; j++) {
                nuevaMatriz[i][j] = matriz[i][j];
                caminos[i][j] = (i != j && matriz[i][j] != INF) ? i : -1;
            }
        }

        for (int k = 0; k < cantidad; k++) {
            for (int i = 0; i < cantidad; i++) {
                for (int j = 0; j < cantidad; j++) {
                    if (nuevaMatriz[i][k] + nuevaMatriz[k][j] < nuevaMatriz[i][j]) {
                        nuevaMatriz[i][j] = nuevaMatriz[i][k] + nuevaMatriz[k][j];
                        caminos[i][j] = caminos[k][j];
                    }
                }
            }
        }

        matriz = nuevaMatriz;
    }

    public List<String> obtenerRuta(String desde, String hasta) {
        int i = nombreAIndice.get(desde);
        int j = nombreAIndice.get(hasta);
        if (matriz[i][j] == INF) return null;

        LinkedList<String> ruta = new LinkedList<>();
        while (j != i) {
            ruta.addFirst(ciudades[j]);
            j = caminos[i][j];
            if (j == -1) return null;
        }
        ruta.addFirst(ciudades[i]);
        return ruta;
    }

    public String obtenerCentroDelGrafo() {
        int mejorCiudad = -1;
        int mejorTiempo = INF;

        for (int j = 0; j < cantidad; j++) {
            int tiempoMasLargo = 0;
            for (int i = 0; i < cantidad; i++) {
                if (matriz[i][j] < INF) {
                    tiempoMasLargo = Math.max(tiempoMasLargo, matriz[i][j]);
                }
            }
            if (tiempoMasLargo < mejorTiempo) {
                mejorTiempo = tiempoMasLargo;
                mejorCiudad = j;
            }
        }

        return (mejorCiudad == -1) ? "No hay centro" : ciudades[mejorCiudad];
    }
}
