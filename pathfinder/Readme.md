#Author

[Fiza Sehar]

#Rationale

```
The goal of this project is to provide a module for working with graphs and finding the shortest path between two nodes in the graph. 
The module is implemented in Java and includes a generic Graph class and a DijkstraPathFinder class that implements Dijkstra's algorithm for finding the shortest path.

```

#Usage

```
To use the module, simply include the Graph and DijkstraPathFinder classes in your Java project and create an instance of the Graph class. 
Then add nodes to the graph using the addNode method and edges using the addEdge method. Finally, create an instance of the DijkstraPathFinder class and set the graph using the setGraph method.
 You can then compute the shortest path between two nodes in the graph using the computeShortestPathsFrom method and retrieve the shortest path using the shortestPath method.
 ```

#Extending the Library

```
The library is easily expandable due to its flexible and modular architecture that separates pathfinding logic from data structures. 
It can add new algorithms by implementing the PathFinder interface without changing existing code. 
The generic Graph class can represent various graph types, making it easy to customize for different use cases. 
This makes the library adaptable to a wide range of scenarios and extendable. To add a new algorithm to the provided library, 
a new Java class needs to be created that implements the PathFinder interface. This requires implementing three methods: setGraph, 
computeShortestPathsFrom, and shortestPath. setGraph sets the graph field of the class, computeShortestPathsFrom computes 
the shortest path using the chosen algorithm, and shortestPath returns the actual shortest path. Once the new calass is created ,
it can be used in the code like any other pathfinder.
```