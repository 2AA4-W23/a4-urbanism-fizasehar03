package ca.mcmaster.cas.se2aa4.a4.pathfinder;

public interface Edge <NodeType> {

    public NodeType getDestination();
    public double getWeight();
}
