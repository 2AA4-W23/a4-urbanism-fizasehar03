import ca.mcmaster.cas.se2aa4.a4.pathfinder.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import ca.mcmaster.cas.se2aa4.a4.pathfinder.Edge;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.Graph;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.DijkstraPathFinder;

public class DijkstraPathFinderTest {
    public static class SimpleEdge implements Edge<String> {
        private String destination;
        private double weight;

        public SimpleEdge(String destination, double weight) {
            this.destination = destination;
            this.weight = weight;
        }

        public String getDestination() {
            return destination;
        }

        public double getWeight() {
            return weight;
        }
    }

    @Test
    public void testSingleNodeGraph() {
        Graph<String, SimpleEdge> graph = new Graph<>();
        graph.addNode("A");

        DijkstraPathFinder<String, SimpleEdge> pathFinder = new DijkstraPathFinder<>();
        pathFinder.setGraph(graph);
        pathFinder.computeShortestPathsFrom("A");

        List<String> path = pathFinder.shortestPath("A");
        assertEquals(1, path.size());
        assertEquals("A", path.get(0));
    }

    @Test
    public void testTwoNodesSingleEdge() {
        Graph<String, SimpleEdge> graph = new Graph<>();
        graph.addNode("A");
        graph.addNode("B");
        graph.addEdge("A", new SimpleEdge("B", 5));

        DijkstraPathFinder<String, SimpleEdge> pathFinder = new DijkstraPathFinder<>();
        pathFinder.setGraph(graph);
        pathFinder.computeShortestPathsFrom("A");

        List<String> path = pathFinder.shortestPath("B");
        assertEquals(2, path.size());
        assertEquals("A", path.get(0));
        assertEquals("B", path.get(1));
    }

    @Test
    public void testMultipleNodesSinglePath() {
        Graph<String, SimpleEdge> graph = new Graph<>();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", new SimpleEdge("B", 5));
        graph.addEdge("B", new SimpleEdge("C", 3));

        DijkstraPathFinder<String, SimpleEdge> pathFinder = new DijkstraPathFinder<>();
        pathFinder.setGraph(graph);
        pathFinder.computeShortestPathsFrom("A");

        List<String> path = pathFinder.shortestPath("C");
        assertEquals(3, path.size());
        assertEquals("A", path.get(0));
        assertEquals("B", path.get(1));
        assertEquals("C", path.get(2));
    }

    @Test
    public void testMultipleNodesMultiplePaths() {
        Graph<String, SimpleEdge> graph = new Graph<>();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addEdge("A", new SimpleEdge("B", 5));
        graph.addEdge("A", new SimpleEdge("C", 3));
        graph.addEdge("B", new SimpleEdge("D", 3));
        graph.addEdge("C", new SimpleEdge("D", 4));

        DijkstraPathFinder<String, SimpleEdge> pathFinder = new DijkstraPathFinder<>();
        pathFinder.setGraph(graph);
        pathFinder.computeShortestPathsFrom("A");

        List<String> path = pathFinder.shortestPath("D");
        assertEquals(3, path.size());
        assertEquals("A", path.get(0));
        assertEquals("C", path.get(1));
        assertEquals("D", path.get(2));
    }

    @Test
    public void testMultipleDestinations() {
        Graph<String, SimpleEdge> graph = new Graph<>();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addNode("E");
        graph.addNode("F");
        graph.addEdge("A", new SimpleEdge("B", 5));
        graph.addEdge("A", new SimpleEdge("C", 3));
        graph.addEdge("B", new SimpleEdge("D", 2));
        graph.addEdge("B", new SimpleEdge("C", 4));
        graph.addEdge("C", new SimpleEdge("E", 3));
        graph.addEdge("D", new SimpleEdge("F", 1));
        graph.addEdge("E", new SimpleEdge("F", 6));

        DijkstraPathFinder<String, SimpleEdge> pathFinder = new DijkstraPathFinder<>();
        pathFinder.setGraph(graph);
        pathFinder.computeShortestPathsFrom("A");

        List<String> pathToB = pathFinder.shortestPath("B");
        assertEquals(2, pathToB.size());
        assertEquals("A", pathToB.get(0));
        assertEquals("B", pathToB.get(1));

        List<String> pathToC = pathFinder.shortestPath("C");
        assertEquals(2, pathToC.size());
        assertEquals("A", pathToC.get(0));
        assertEquals("C", pathToC.get(1));

        List<String> pathToD = pathFinder.shortestPath("D");
        assertEquals(3, pathToD.size());
        assertEquals("A", pathToD.get(0));
        assertEquals("B", pathToD.get(1));
        assertEquals("D", pathToD.get(2));

        List<String> pathToE = pathFinder.shortestPath("E");
        assertEquals(3, pathToE.size());
        assertEquals("A", pathToE.get(0));
        assertEquals("C", pathToE.get(1));
        assertEquals("E", pathToE.get(2));

        List<String> pathToF = pathFinder.shortestPath("F");
        assertEquals(4, pathToF.size());
        assertEquals("A", pathToF.get(0));
        assertEquals("B", pathToF.get(1));
        assertEquals("D", pathToF.get(2));
        assertEquals("F", pathToF.get(3));
    }

}