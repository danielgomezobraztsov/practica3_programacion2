package dominio;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class Graph<V> {
    //Lista de adyacencia.
    private Map<V, Set<V>> adjacencyList = new HashMap<>();


    public boolean addVertex(V v){
        if (adjacencyList.containsKey(v)) {
            return false;
        } else {
            adjacencyList.put(v, new HashSet<>());
            return true;
        }
    }

    public boolean addEdge(V v1, V v2){
        boolean v1Exists = adjacencyList.containsKey(v1);
        boolean v2Exists = adjacencyList.containsKey(v2);

        if (!v1Exists) {
            addVertex(v1);
        }
        if (!v2Exists) {
            addVertex(v2);
        }

        Set<V> adjacents = adjacencyList.get(v1);

        if (adjacents.contains(v2)) {
            return false;
        } else {
            adjacents.add(v2);
            return true;
        }
    }

    public Set<V> obtainAdjacents(V v) throws Exception{
        if (!containsVertex(v)) {
            throw new Exception("El vértice no existe en el grafo.");
        }

        Set<V> adjacents = adjacencyList.get(v);

        Set<V> adjacentsCopy = new HashSet<>(adjacents);

        return adjacentsCopy;
    }

    public boolean containsVertex(V v){
        return adjacencyList.containsKey(v);
    }

    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder();
        for (V vertex : adjacencyList.keySet()) {
            sb.append(vertex.toString()).append(": ");
            Set<V> adjacents = adjacencyList.get(vertex);
            for (V adjacent : adjacents) {
                sb.append(adjacent.toString()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public List<V> onePath(V v1, V v2){
        Map<V, V> trace = new HashMap<>();
        Stack<V> open = new Stack<>();
        List<V> path = new ArrayList<>();

        open.push(v1);
        trace.put(v1, null);

        boolean found = false;
        while (!open.isEmpty() && !found) {
            V current = open.pop();

            found = current.equals(v2);
            if (!found) {
                Set<V> adjacents = adjacencyList.get(current);
                if (adjacents != null) {
                    for (V adjacent : adjacents) {
                        if (!trace.containsKey(adjacent)) {
                            open.push(adjacent);
                            trace.put(adjacent, current);
                        }
                    }
                }
            }
        }

        if (found) {
            V current = v2;
            while (current != null) {
                path.add(0, current);
                current = trace.get(current);
            }
        }

        return found ? path : null;
    }

    @Test
    public void onePathFindsAPath(){
        System.out.println("\nTest onePathFindsAPath");
        System.out.println("----------------------");
        // Se construye el grafo.
        Graph<Integer> g = new Graph<>();
        g.addEdge(1, 2);
        g.addEdge(3, 4);
        g.addEdge(1, 5);
        g.addEdge(5, 6);
        g.addEdge(6, 4);
        // Se construye el camino esperado.
        List<Integer> expectedPath = new ArrayList<>();
        expectedPath.add(1);
        expectedPath.add(5);
        expectedPath.add(6);
        expectedPath.add(4);
        //Se comprueba si el camino devuelto es igual al esperado.
        assertEquals(expectedPath, g.onePath(1, 4));
    }
}
