package ca.mcmaster.cas.se2aa4.a4.pathfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Graph<NodeType,EdgeType extends Edge<NodeType>> {
    //generic constraint
    private HashMap<NodeType, HashSet<EdgeType>> edges; //edges equals to hashmap, key is from, value is an array list of everything it connects from to

    public Graph() {
        this.edges = new HashMap<>();
    }
    public void addNode(NodeType node) {
        edges.put(node, new HashSet<>());
    }
    public void addEdge(NodeType source, EdgeType edge) {
        HashSet<EdgeType> sourceEdges = edges.get(source);
        sourceEdges.add(edge);
    }
    public Set<NodeType> getNodes() {
        return edges.keySet();
    }
    public Set<EdgeType> getEdgesForNode(NodeType node){
        return edges.get(node);
    }
}
