package ca.mcmaster.cas.se2aa4.a3.island.extentionpoints;

import java.util.*;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;

public class Cities {

    private ArrayList<String> type;

    private static class Coordinate {
        Double x;
        Double y;

        Coordinate(Vertex v){
            this.x=v.getX();
            this.y=v.getY();
        }
        public double getDistance(Coordinate other){
            double pointDifference = Math.sqrt((Math.pow(this.getX()-other.getX(),2)+Math.pow(this.getY()-other.getY(),2)));
            return pointDifference;
        }
        public double getX() {
            return x;
        }

        public double getY() {
            return y;
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

        HashSet<Coordinate> cities = new HashSet<>();
        for (Vertex v : vertices) {
            Coordinate c = new Coordinate(v);
            if (polygonCoordinates.contains(c)) continue;
            if (!touchingLand.contains(c)) continue;
            cities.add(c);
        }

        ArrayList<Coordinate> allCities = new ArrayList<>(cities);
        for (int i = 0; i < allCities.size(); i++) {
            int idx1 = r.nextInt(allCities.size());
            int idx2 = r.nextInt(allCities.size());
            Coordinate c1 = allCities.get(idx1);
            Coordinate c2 = allCities.get(idx2);
            allCities.set(idx1, c2);
            allCities.set(idx2, c1);
        }
        cities.clear();
        for (int i = 0; i < numCities; i++) {
            Coordinate chosenCity = allCities.get(i);
            for (int j = allCities.size() - 1; j >= 0; j--) {
                if (allCities.get(j).getDistance(chosenCity) < 40) {
                    allCities.remove(j);
                }
            }
            cities.add(chosenCity);
        }
        System.out.println(cities.size());


        ArrayList<Vertex> newVertices = new ArrayList<>();
        for (Vertex v : vertices) {
            Vertex.Builder builder = Vertex.newBuilder().mergeFrom(v);
            Coordinate c = new Coordinate(v);
            if (cities.contains(c)) {
                builder.addProperties(Structs.Property.newBuilder().setKey("city_size").setValue("1").build());
            }
            newVertices.add(builder.build());
        }
        return Structs.Mesh.newBuilder().addAllVertices(newVertices).addAllPolygons(mesh.getPolygonsList()).addAllSegments(mesh.getSegmentsList()).addAllProperties(mesh.getPropertiesList()).build();
    }
}
