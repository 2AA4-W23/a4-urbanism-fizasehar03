package ca.mcmaster.cas.se2aa4.a4.pathfinder;

import java.util.List;

public interface PathFinder<NodeType, EdgeType extends Edge<NodeType>> {
    public void setGraph(Graph<NodeType, EdgeType> graph);//standard way for a path finder to find the graph that it needs to pathfind

    public void computeShortestPathsFrom(NodeType source);

    public List<NodeType> shortestPath(NodeType destination);
}
