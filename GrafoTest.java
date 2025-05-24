import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class GrafoTest {
    private Grafo grafo;

    @BeforeEach
    public void setUp() {
        ArrayList<String> ciudades = new ArrayList<>(Arrays.asList("A", "B", "C"));
        grafo = new Grafo(ciudades);
        grafo.agregarArco("A", "B", 3);
        grafo.agregarArco("B", "C", 4);
    }

    @Test
    public void testAgregarArco() {
        grafo.agregarArco("A", "C", 5);
        assertEquals(5, grafo.matriz[grafo.nombreAIndice.get("A")][grafo.nombreAIndice.get("C")]);
    }

    @Test
    public void testEliminarArco() {
        grafo.eliminarArco("A", "B");
        assertEquals(grafo.INF, grafo.matriz[grafo.nombreAIndice.get("A")][grafo.nombreAIndice.get("B")]);
    }

    @Test
    public void testFloydWarshallCalculaRutaCorta() {
        grafo.floydWarshall();
        int tiempo = grafo.matriz[grafo.nombreAIndice.get("A")][grafo.nombreAIndice.get("C")];
        assertEquals(7, tiempo); // A -> B (3) + B -> C (4)
    }

    @Test
    public void testObtenerRutaCorrecta() {
        grafo.floydWarshall();
        List<String> ruta = grafo.obtenerRuta("A", "C");
        assertEquals(Arrays.asList("A", "B", "C"), ruta);
    }

    @Test
    public void testCentroDelGrafo() {
        grafo.floydWarshall();
        String centro = grafo.obtenerCentroDelGrafo();
        assertNotNull(centro); // No hay ciudades desconectadas
    }
}
