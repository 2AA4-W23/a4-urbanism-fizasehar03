package ca.mcmaster.cas.se2aa4.a4.pathfinder;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph<NodeType,EdgeType extends Edge<NodeType>>   {
    //generic constraint
    private ArrayList<NodeType> nodes;
    private HashMap<NodeType, ArrayList<EdgeType>> edges; //edges equals to hashmap, key is from, value is an array list of everything it connects from to

    public Graph(){
        this.nodes=new ArrayList<>();
        this.edges=new HashMap<>();
    }

    public void addNode(NodeType node) {
        edges.put(node, new ArrayList<>());
    }

    public void addEdge(NodeType source, EdgeType edge) {
        ArrayList<EdgeType> sourceEdges = edges.get(source);
        sourceEdges.add(edge);
    }
}
