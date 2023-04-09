package ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Cities;

import java.util.*;
import ca.mcmaster.cas.se2aa4.a2.io.*;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.DijkstraPathFinder;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.Edge;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.Graph;
public class Cities {
    private static HashSet<Coordinate> extractPolygonCoordinates(Structs.Mesh mesh) {
        List<Vertex> vertices = mesh.getVerticesList();
        HashSet<Coordinate> polygonCoordinates = new HashSet<>();
        for (Structs.Polygon p : mesh.getPolygonsList()) {
            Vertex v = vertices.get(p.getCentroidIdx());
            polygonCoordinates.add(new Coordinate(v));
        }
        return polygonCoordinates;
    }

    private static HashSet<Coordinate> findTouchingLandCoordinates(Structs.Mesh mesh, List<String> types) {
        List<Vertex> vertices = mesh.getVerticesList();
        HashSet<Coordinate> touchingLand = new HashSet<>();
        List<Structs.Polygon> polygons = mesh.getPolygonsList();
        for (int i = 0; i < polygons.size(); i++) {
            String type = types.get(i);
            if (!type.equals("land")) {
                continue;
            }

            Structs.Polygon p = polygons.get(i);
            List<Integer> segments = p.getSegmentIdxsList();
            for (Integer s : segments) {
                Structs.Segment segment = mesh.getSegments(s);
                Vertex v1 = vertices.get(segment.getV1Idx());
                Vertex v2 = vertices.get(segment.getV2Idx());
                Coordinate c1 = new Coordinate(v1);
                Coordinate c2 = new Coordinate(v2);
                touchingLand.add(c1);
                touchingLand.add(c2);
            }
        }
        return touchingLand;
    }

    private static HashSet<Coordinate> filterCities(List<Vertex> vertices, HashSet<Coordinate> polygonCoordinates, HashSet<Coordinate> touchingLand) {
        HashSet<Coordinate> allCities = new HashSet<>();
        for (Vertex v : vertices) {
            Coordinate c = new Coordinate(v);
            if (polygonCoordinates.contains(c)) continue;
            if (!touchingLand.contains(c)) continue;
            allCities.add(c);
        }
        return allCities;
    }

    private static HashSet<Coordinate> selectCities(HashSet<Coordinate> allCities, int numCities, long seed) {
        Random r = new Random(seed);
        ArrayList<Coordinate> shuffledCities = new ArrayList<>(allCities);
        for (int i = 0; i < shuffledCities.size(); i++) {
            int idx1 = i;
            int idx2 = r.nextInt(shuffledCities.size());
            Coordinate c1 = shuffledCities.get(idx1);
            Coordinate c2 = shuffledCities.get(idx2);
            shuffledCities.set(idx1, c2);
            shuffledCities.set(idx2, c1);
        }

        HashSet<Coordinate> cities = new HashSet<>();
        for (int i = 0; i < numCities; i++) {
            Coordinate chosenCity = shuffledCities.get(i);
            for (int j = shuffledCities.size() - 1; j >= 0; j--) {
                if (shuffledCities.get(j).getDistance(chosenCity) < 40) {
                    shuffledCities.remove(j);
                }
            }
            cities.add(chosenCity);
        }
        return cities;
    }

    private static Coordinate findMostCentralCity(HashSet<Coordinate> cities) {
        double x = 0;
        double y = 0;
        for (Coordinate city : cities) {
            x += city.getX();
            y += city.getY();
        }
        x /= (double) cities.size();
        y /= (double) cities.size();

        Coordinate central = new Coordinate(x, y);

        Coordinate mostCentralCity = null;
        Double bestDistance = Double.POSITIVE_INFINITY;
        for (Coordinate city : cities) {
            double distance = city.getDistance(central);
            if (distance < bestDistance) {
                mostCentralCity = city;
                bestDistance = distance;
            }
        }
        return mostCentralCity;
    }

    private static Graph<Coordinate, CityEdge> constructCityGraph(Structs.Mesh mesh ,HashSet<Coordinate> allCities, List<Structs.Polygon> polygons, List<Vertex> vertices) {
        Graph<Coordinate, CityEdge> graph = new Graph<>();
        for (Coordinate city : allCities) {
            graph.addNode(city);
        }

        for (Structs.Polygon p : polygons) {
            for (Integer i : p.getSegmentIdxsList()) {
                Structs.Segment s = mesh.getSegments(i);
                Vertex v1 = vertices.get(s.getV1Idx());
                Vertex v2 = vertices.get(s.getV2Idx());
                Coordinate c1 = new Coordinate(v1);
                Coordinate c2 = new Coordinate(v2);
                if (!allCities.contains(c1)) continue;
                if (!allCities.contains(c2)) continue;
                graph.addEdge(c1, new CityEdge(c1, c2));
                graph.addEdge(c2, new CityEdge(c2, c1));
            }
        }
        return graph;
    }

    private static DijkstraPathFinder<Coordinate, CityEdge> computeShortestPaths(Graph<Coordinate, CityEdge> graph, Coordinate mostCentralCity) {
        DijkstraPathFinder<Coordinate, CityEdge> pathfinder = new DijkstraPathFinder<>();
        pathfinder.setGraph(graph);
        pathfinder.computeShortestPathsFrom(mostCentralCity);
        return pathfinder;
    }

    private static List<Vertex> addCitySizeProperties(List<Vertex> vertices, HashSet<Coordinate> cities, Random r) {
        ArrayList<Vertex> newVertices = new ArrayList<>();
        for (Vertex v : vertices) {
            Vertex.Builder builder = Vertex.newBuilder().mergeFrom(v);
            Coordinate c = new Coordinate(v);
            if (cities.contains(c)) {
                CitySize size = CitySize.getRandomCitySize(r);
                builder.addProperties(Structs.Property.newBuilder().setKey("city_size").setValue(String.valueOf(size.getIndex())).build());
            }
            newVertices.add(builder.build());
        }
        return newVertices;
    }

    private static List<Structs.Segment> constructNewSegments(Structs.Mesh mesh ,HashSet<Coordinate> cities, Coordinate mostCentralCity, DijkstraPathFinder<Coordinate, CityEdge> pathfinder, List<Vertex> newVertices) {
        ArrayList<Structs.Segment> newSegments = new ArrayList<>(mesh.getSegmentsList());
        for (Coordinate city : cities) {
            if (city.equals(mostCentralCity)) continue;
            List<Coordinate> path = pathfinder.shortestPath(city);
            Coordinate previousCoordinate = mostCentralCity;
            for (Coordinate nextCoordinate : path) {
                Vertex v1 = previousCoordinate.makeVertex();
                Vertex v2 = nextCoordinate.makeVertex();
                Structs.Segment s = Structs.Segment.newBuilder().setV1Idx(newVertices.size()).setV2Idx(newVertices.size() + 1).addProperties(Structs.Property.newBuilder().setKey("rgb_color").setValue("255,0,0").build()).addProperties(Structs.Property.newBuilder().setKey("hasWeight").setValue("true").build()).build();
                newVertices.add(v1);
                newVertices.add(v2);
                newSegments.add(s);
                previousCoordinate = nextCoordinate;
            }
        }
        return newSegments;
    }

    private static Structs.Mesh updateMesh(Structs.Mesh mesh, List<Vertex> newVertices, List<Structs.Segment> newSegments) {
        return Structs.Mesh.newBuilder().addAllVertices(newVertices).addAllPolygons(mesh.getPolygonsList()).addAllSegments(newSegments).addAllProperties(mesh.getPropertiesList()).build();
    }

    public static Structs.Mesh addCities(Structs.Mesh mesh, ArrayList<String> types, int numCities, long seed) {
        Random r = new Random(seed);
        HashSet<Coordinate> polygonCoordinates = extractPolygonCoordinates(mesh);
        HashSet<Coordinate> touchingLand = findTouchingLandCoordinates(mesh, types);
        HashSet<Coordinate> allCities = filterCities(mesh.getVerticesList(), polygonCoordinates, touchingLand);
        HashSet<Coordinate> cities = selectCities(allCities, numCities, seed);
        Coordinate mostCentralCity = findMostCentralCity(cities);
        Graph<Coordinate, CityEdge> graph = constructCityGraph(mesh,allCities, mesh.getPolygonsList(), mesh.getVerticesList());
        DijkstraPathFinder<Coordinate, CityEdge> pathfinder = computeShortestPaths(graph, mostCentralCity);
        List<Vertex> newVertices = addCitySizeProperties(mesh.getVerticesList(), cities, r);
        List<Structs.Segment> newSegments = constructNewSegments(mesh,cities, mostCentralCity, pathfinder, newVertices);
        return updateMesh(mesh, newVertices, newSegments);
    }
}