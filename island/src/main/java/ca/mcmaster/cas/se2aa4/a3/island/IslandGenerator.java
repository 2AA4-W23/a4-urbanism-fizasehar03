package ca.mcmaster.cas.se2aa4.a3.island;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a3.island.biomes.Biomes;
import ca.mcmaster.cas.se2aa4.a3.island.elevationprofiles.Elevations;
import ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Cities;
import ca.mcmaster.cas.se2aa4.a3.island.shapes.CircleIsland;
import ca.mcmaster.cas.se2aa4.a3.island.shapes.SquareIsland;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class IslandGenerator {

    public Mesh generateIsland(Mesh mesh, String shape, boolean lagoon, int lakes, int rivers, int aquifers, String altitude, String soil, String biomes, long seed){
        Mesh tempMesh = mesh;
        double xcenter = 0;
        double ycenter = 0;

        for (Structs.Vertex v : mesh.getVerticesList()) {
            xcenter += v.getX();
            ycenter += v.getY();
        }
        xcenter = xcenter / mesh.getVerticesCount();
        ycenter = ycenter / mesh.getVerticesCount();

        double max_x = Double.MIN_VALUE;
        double max_y = Double.MIN_VALUE;


        for (Structs.Vertex v: mesh.getVerticesList()) {
            max_x = (Double.compare(max_x, v.getX()) < 0? v.getX(): max_x);
            max_y = (Double.compare(max_y, v.getY()) < 0? v.getY(): max_y);
        }
        double minDimension = Math.min(max_x, max_y);

        long newSeed = System.currentTimeMillis();

        if((Long.valueOf(seed).equals(Long.valueOf(0)))){
            seed=newSeed;
            System.out.println(seed);
        }
        Mesh finalizedMesh;
        ArrayList<String> type;
        if (shape.equals("Circle") || shape.equals("circle")) {
            CircleIsland circleIsland = new CircleIsland();
            circleIsland.generateCircleIsland(mesh, xcenter, ycenter, lagoon, lakes, rivers, aquifers, altitude, soil, minDimension, biomes, seed);
            finalizedMesh = finalizeMesh(mesh, circleIsland.getTempMeshProperties(), circleIsland.getTempSeg());
            type = circleIsland.getType();
        } else {
            SquareIsland squareIsland = new SquareIsland();
            squareIsland.generateSquareIsland(mesh, xcenter, ycenter, lakes, rivers, aquifers, altitude, soil, minDimension, biomes, seed);
            finalizedMesh = finalizeMesh(mesh, squareIsland.getTempMeshProperties(), squareIsland.getTempSeg());
            type = squareIsland.getType();

        }
        return Cities.addCities(finalizedMesh, type, 100, seed);
    }
    public Mesh finalizeMesh(Mesh tempMesh, ArrayList<Polygon> temp, ArrayList<Segment> tempSeg) {
        return Mesh.newBuilder().addAllVertices(tempMesh.getVerticesList()).addAllSegments(tempSeg).addAllPolygons(temp).build();
    }
}
