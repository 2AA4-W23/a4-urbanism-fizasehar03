package ca.mcmaster.cas.se2aa4.a3.island.extentionpoints;

import java.util.*;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.DijkstraPathFinder;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.Edge;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.Graph;

public class Cities {

    private ArrayList<String> type;

    private static class Coordinate {
        Double x;
        Double y;

        Coordinate(Vertex v) {
            this.x = v.getX();
            this.y = v.getY();
        }

        Coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getDistance(Coordinate other) {
            double pointDifference = Math.sqrt((Math.pow(this.getX()-other.getX(),2)+Math.pow(this.getY()-other.getY(),2)));
            return pointDifference;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public Vertex makeVertex() {
            return Vertex.newBuilder().setX(x).setY(y).build();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Coordinate that = (Coordinate) obj;
            return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return this.x.toString() + "," + this.y.toString();
        }
    }

    public enum CitySize {
        TOWN(1),
        SMALL(2),
        MEDIUM(3),
        LARGE(4),
        METROPOLIS(5);

        private final int index;

        private CitySize(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static CitySize getRandomCitySize(Random random) {
            return values()[random.nextInt(values().length)];
        }
    }

    public static class CityEdge implements Edge<Coordinate> {
        Coordinate source;
        Coordinate destination;

        CityEdge(Coordinate source, Coordinate destination) {
            this.source = source;
            this.destination = destination;
        }

        @Override
        public Coordinate getDestination() {
            return destination;
        }

        @Override
        public double getWeight() {
            return source.getDistance(destination);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            CityEdge that = (CityEdge) obj;
            return this.source.equals(that.source) && this.destination.equals(that.destination);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.source, this.destination);
        }
    }

    public static Structs.Mesh addCities(Structs.Mesh mesh, ArrayList<String> types, int numCities, long seed) {
        Random r = new Random(seed);

        List<Vertex> vertices = mesh.getVerticesList();

        HashSet<Coordinate> polygonCoordinates = new HashSet<>();
        for (Structs.Polygon p : mesh.getPolygonsList()) {
            Vertex v = vertices.get(p.getCentroidIdx());
            polygonCoordinates.add(new Coordinate(v));
        }

        HashSet<Coordinate> touchingLand = new HashSet<>();
        List<Structs.Polygon> polygons = mesh.getPolygonsList();
        for (int i =0; i<polygons.size();i++) {
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

        HashSet<Coordinate> allCities = new HashSet<>();
        for (Vertex v : vertices) {
            Coordinate c = new Coordinate(v);
            if (polygonCoordinates.contains(c)) continue;
            if (!touchingLand.contains(c)) continue;
            allCities.add(c);
        }

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

        DijkstraPathFinder<Coordinate, CityEdge> pathfinder = new DijkstraPathFinder<>();
        pathfinder.setGraph(graph);
        pathfinder.computeShortestPathsFrom(mostCentralCity);

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
        return Structs.Mesh.newBuilder().addAllVertices(newVertices).addAllPolygons(mesh.getPolygonsList()).addAllSegments(newSegments).addAllProperties(mesh.getPropertiesList()).build();
    }
}
