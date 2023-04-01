package ca.mcmaster.cas.se2aa4.a4.pathfinder;

import java.util.*;

public class DijkstraPathFinder<NodeType, EdgeType extends Edge<NodeType>> implements PathFinder<NodeType, EdgeType> {
    private Graph<NodeType, EdgeType> graph;
    private HashMap<NodeType, Double> cost;
    private HashMap<NodeType, NodeType> previous;
    private PriorityQueue<NodeType> queue;

    public void setGraph(Graph<NodeType, EdgeType> graph) {
        this.graph = graph;
    }
    public void computeShortestPathsFrom(NodeType source) {
        this.cost = new HashMap<>();
        this.previous = new HashMap<>();
        this.queue = new PriorityQueue<>(Comparator.comparingDouble(cost::get));

        // initialize distances
        for (NodeType node : graph.getNodes()) {
            cost.put(node, Double.POSITIVE_INFINITY);
            previous.put(node, null);
        }
        cost.put(source, 0.0);
        queue.offer(source);

        while (!queue.isEmpty()) {
            NodeType node = queue.poll();
            for (EdgeType edge : graph.getEdgesForNode(node)) {
                if ((cost.get(node) + edge.getWeight()) < cost.get(edge.getDestination())) {
                    previous.put(edge.getDestination(), node);
                    cost.put(edge.getDestination(), cost.get(node) + edge.getWeight());
                    queue.remove(edge.getDestination());
                    queue.add(edge.getDestination());
                }
            }
        }
    }
    public List<NodeType> shortestPath(NodeType destination) {
        List<NodeType> path = new ArrayList<>();
        NodeType current = destination;
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }
        Collections.reverse(path);
        return path;
    }
}