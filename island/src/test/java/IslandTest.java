import static org.junit.jupiter.api.Assertions.*;

import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a3.island.elevationprofiles.HillsElevation;
import ca.mcmaster.cas.se2aa4.a3.island.elevationprofiles.MountainElevation;
import ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Aquifers;
import ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Cities.Cities;
import ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Cities.CityEdge;
import ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Cities.Coordinate;
import ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Lakes;
import ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Rivers;
import ca.mcmaster.cas.se2aa4.a3.island.shapes.CircleIsland;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.DijkstraPathFinder;
import ca.mcmaster.cas.se2aa4.a4.pathfinder.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.io.IOException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class IslandTest {
    private ArrayList<Structs.Polygon> temp;
    private ArrayList<String> type;
    private int numRivers;
    private ArrayList<Structs.Segment> tempSeg;
    private Rivers rivers;
    private ArrayList<Structs.Polygon> polygons;
    private ArrayList<Structs.Segment> segments;
    private ArrayList<Structs.Vertex> vertices;
    private Structs.Mesh aMesh;
    private ArrayList<Boolean> isAquifer;

    @BeforeEach
    void setUp() throws IOException {
        temp = new ArrayList<>();
        type = new ArrayList<>();
        numRivers = 3;
        tempSeg = new ArrayList<>();
        rivers = new Rivers(temp, type, numRivers, tempSeg);
        polygons = new ArrayList<>();
        segments = new ArrayList<>();
        vertices = new ArrayList<>();
        isAquifer = new ArrayList<>();
        try {
            aMesh = new MeshFactory().read("../ireg.mesh");
        } catch (IOException io) {
            System.out.println("couldnt find file");
        }
        for (int i = 0; i < aMesh.getPolygonsCount(); i++) {
            isAquifer.add(false);
        }
    }

    @Test
    void testGenerateCircleIsland() {
        // Test that the two islands with the same parameters and seeds have the same type for each tile
        CircleIsland circleIsland1 = new CircleIsland();
        circleIsland1.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);
        CircleIsland circleIsland2 = new CircleIsland();
        circleIsland2.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);

        assertEquals(circleIsland1.getType(), circleIsland2.getType());
        assertEquals(circleIsland1.getTempSeg(), circleIsland2.getTempSeg());
        assertEquals(circleIsland1.getTempMeshProperties(), circleIsland2.getTempMeshProperties());

        // Test that the two islands with the different parameters and seeds have a different type for the tiles
        CircleIsland circleIsland3 = new CircleIsland();
        circleIsland3.generateCircleIsland(aMesh, 700, 700, false, 5, 8, 5, "mountain", "wet", 1400, "Arctic", 995343222);

        assertNotEquals(circleIsland1.getType(), circleIsland3.getType());
        assertNotEquals(circleIsland1.getTempSeg(), circleIsland3.getTempSeg());
        assertNotEquals(circleIsland1.getTempMeshProperties(), circleIsland3.getTempMeshProperties());
    }

    @Test
    void testGenerateLakes() {
        // Test that the generated rivers have non-zero length
        CircleIsland circleIsland1 = new CircleIsland();
        circleIsland1.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);
        CircleIsland circleIsland2 = new CircleIsland();
        circleIsland2.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);

        Lakes lakes1 = new Lakes(circleIsland1.getTempMeshProperties(), circleIsland1.getType(), 5);
        Lakes lakes2 = new Lakes(circleIsland2.getTempMeshProperties(), circleIsland2.getType(), 5);

        lakes1.generateLakes(aMesh, 1234567);
        lakes2.generateLakes(aMesh, 1234567);

        assertEquals(lakes1.getType(), lakes2.getType());
        assertEquals(lakes1.getTempMeshProperties(), lakes2.getTempMeshProperties());

        CircleIsland circleIsland3 = new CircleIsland();
        circleIsland3.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);

        Lakes lakes3 = new Lakes(circleIsland3.getTempMeshProperties(), circleIsland3.getType(), 5);
        lakes3.generateLakes(aMesh, 111);

        assertNotEquals(lakes1.getType(), lakes3.getType());
        assertNotEquals(lakes1.getTempMeshProperties(), lakes3.getTempMeshProperties());
    }

    @Test
    void testGenerateAquifers() {
        // Test that the generated rivers have non-zero length
        CircleIsland circleIsland1 = new CircleIsland();
        circleIsland1.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);
        CircleIsland circleIsland2 = new CircleIsland();
        circleIsland2.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);

        Aquifers aquifers1 = new Aquifers(circleIsland1.getTempMeshProperties(), circleIsland1.getType(), isAquifer, 4);
        Aquifers aquifers2 = new Aquifers(circleIsland2.getTempMeshProperties(), circleIsland2.getType(), isAquifer, 4);


        CircleIsland circleIsland3 = new CircleIsland();
        circleIsland3.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);

    }

    @Test
    void testGenerateRivers() {
        // Test that the generated rivers have non-zero length
        CircleIsland circleIsland1 = new CircleIsland();
        circleIsland1.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);
        CircleIsland circleIsland2 = new CircleIsland();
        circleIsland2.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);

        Rivers rivers1 = new Rivers(circleIsland1.getTempMeshProperties(), circleIsland1.getType(), 5, circleIsland1.getTempSeg());
        Rivers rivers2 = new Rivers(circleIsland2.getTempMeshProperties(), circleIsland2.getType(), 5, circleIsland2.getTempSeg());

        rivers1.generateRivers(aMesh, 700, 700, 123456789);
        rivers2.generateRivers(aMesh, 700, 700, 123456789);

        assertEquals(rivers1.getDischarge(), rivers2.getDischarge());
        assertEquals(rivers1.getType(), rivers2.getType());
        assertEquals(rivers1.getTempSeg(), rivers2.getTempSeg());
        assertEquals(rivers1.getTempMeshProperties(), rivers2.getTempMeshProperties());

        CircleIsland circleIsland3 = new CircleIsland();
        circleIsland3.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);

    }

    @Test
    void testHillsElevation() {
        // Test that the generated rivers have non-zero length
        CircleIsland circleIsland1 = new CircleIsland();
        circleIsland1.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);
        CircleIsland circleIsland2 = new CircleIsland();
        circleIsland2.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);

        HillsElevation he1 = new HillsElevation();
        he1.computeElevations(aMesh, circleIsland1.getType(), 700, 700, 1400, 12345);
        HillsElevation he2 = new HillsElevation();
        he2.computeElevations(aMesh, circleIsland2.getType(), 700, 700, 1400, 12345);

        assertEquals(he1.getElevations(), he2.getElevations());

        CircleIsland circleIsland3 = new CircleIsland();
        circleIsland3.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);
        HillsElevation he3 = new HillsElevation();
        he3.computeElevations(aMesh, circleIsland1.getType(), 700, 700, 1400, 12);

        assertNotEquals(he1.getElevations(), he3.getElevations());

    }

    @Test
    void testMountainElevation() {
        // Test that the generated rivers have non-zero length
        CircleIsland circleIsland1 = new CircleIsland();
        circleIsland1.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);
        CircleIsland circleIsland2 = new CircleIsland();
        circleIsland2.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);

        MountainElevation me1 = new MountainElevation();
        me1.computeElevations(aMesh, circleIsland1.getType(), 700, 700, 1400, 12345);
        MountainElevation me2 = new MountainElevation();
        me2.computeElevations(aMesh, circleIsland2.getType(), 700, 700, 1400, 12345);

        assertEquals(me1.getElevations(), me2.getElevations());

        CircleIsland circleIsland3 = new CircleIsland();
        circleIsland3.generateCircleIsland(aMesh, 700, 700, false, 3, 2, 3, "hills", "dry", 1400, "Arctic", 123456789);
        MountainElevation me3 = new MountainElevation();
        me3.computeElevations(aMesh, circleIsland1.getType(), 600, 600, 1200, 12345);

        assertNotEquals(me1.getElevations(), me3.getElevations());

    }

    @Test
    public void testExtractPolygonCoordinates() {
        // Create a sample Mesh object with some vertices and polygons
        Structs.Mesh.Builder meshBuilder = Structs.Mesh.newBuilder();
        meshBuilder.addVertices(Structs.Vertex.newBuilder().setX(1).setY(1).build());
        meshBuilder.addVertices(Structs.Vertex.newBuilder().setX(2).setY(2).build());
        meshBuilder.addVertices(Structs.Vertex.newBuilder().setX(3).setY(3).build());
        meshBuilder.addPolygons(Structs.Polygon.newBuilder().setCentroidIdx(0).build());
        meshBuilder.addPolygons(Structs.Polygon.newBuilder().setCentroidIdx(1).build());
        Structs.Mesh mesh = meshBuilder.build();

        // Call the extractPolygonCoordinates method
        HashSet<Coordinate> polygonCoordinates = Cities.extractPolygonCoordinates(mesh);

        // Assert that the returned HashSet contains the expected coordinates
        assertEquals(2, polygonCoordinates.size());
        assertTrue(polygonCoordinates.contains(new Coordinate(1.0, 1.0)));
        assertTrue(polygonCoordinates.contains(new Coordinate(2.0, 2.0)));
    }

    @Test
    public void testFindTouchingLandCoordinates() {
        // Create a sample Mesh object with some vertices, polygons, and segments
        Structs.Mesh.Builder meshBuilder = Structs.Mesh.newBuilder();
        meshBuilder.addVertices(Structs.Vertex.newBuilder().setX(1).setY(1).build());
        meshBuilder.addVertices(Structs.Vertex.newBuilder().setX(2).setY(2).build());
        meshBuilder.addVertices(Structs.Vertex.newBuilder().setX(3).setY(3).build());
        meshBuilder.addPolygons(Structs.Polygon.newBuilder().addSegmentIdxs(0).build());
        meshBuilder.addPolygons(Structs.Polygon.newBuilder().addSegmentIdxs(1).build());
        meshBuilder.addSegments(Structs.Segment.newBuilder().setV1Idx(0).setV2Idx(1).build());
        meshBuilder.addSegments(Structs.Segment.newBuilder().setV1Idx(1).setV2Idx(2).build());
        Structs.Mesh mesh = meshBuilder.build();

        // Create a list of polygon types
        List<String> types = new ArrayList<>();
        types.add("land");
        types.add("water");

        // Call the findTouchingLandCoordinates method
        HashSet<Coordinate> touchingLand = Cities.findTouchingLandCoordinates(mesh, types);

        // Assert that the returned HashSet contains the expected coordinates
        assertEquals(2, touchingLand.size());
        assertTrue(touchingLand.contains(new Coordinate(1.0, 1.0)));
        assertTrue(touchingLand.contains(new Coordinate(2.0, 2.0)));
    }

    @Test
    public void testFilterCities() {
        // Create a list of vertices
        List<Structs.Vertex> vertices = new ArrayList<>();
        vertices.add(Structs.Vertex.newBuilder().setX(1).setY(1).build());
        vertices.add(Structs.Vertex.newBuilder().setX(2).setY(2).build());
        vertices.add(Structs.Vertex.newBuilder().setX(3).setY(3).build());

        // Create a HashSet of polygonCoordinates
        HashSet<Coordinate> polygonCoordinates = new HashSet<>();
        polygonCoordinates.add(new Coordinate(1.0, 1.0));

        // Create a HashSet of touchingLand
        HashSet<Coordinate> touchingLand = new HashSet<>();
        touchingLand.add(new Coordinate(3.0, 3.0));

        // Call the filterCities method
        HashSet<Coordinate> filteredCities = Cities.filterCities(vertices, polygonCoordinates, touchingLand);

        // Assert that the returned HashSet contains the expected coordinates
        assertEquals(1, filteredCities.size());
        assertTrue(filteredCities.contains(new Coordinate(3.0, 3.0)));
    }

    @Test
    public void testFindMostCentralCity() {
        // Create a HashSet of cities
        HashSet<Coordinate> cities = new HashSet<>();
        cities.add(new Coordinate(1.0, 1.0));
        cities.add(new Coordinate(2.0, 2.0));
        cities.add(new Coordinate(3.0, 3.0));

        // Call the findMostCentralCity method
        Coordinate mostCentralCity = Cities.findMostCentralCity(cities);

        // Assert that the returned Coordinate is the expected most central city
        assertEquals(new Coordinate(2.0, 2.0), mostCentralCity);
    }

    @Test
    public void testComputeShortestPathsAndConstructCityGraph() {
        // Create a sample Mesh object with some vertices, polygons, and segments
        Structs.Mesh.Builder meshBuilder = Structs.Mesh.newBuilder();
        meshBuilder.addVertices(Structs.Vertex.newBuilder().setX(1).setY(1).build());
        meshBuilder.addVertices(Structs.Vertex.newBuilder().setX(2).setY(2).build());
        meshBuilder.addVertices(Structs.Vertex.newBuilder().setX(3).setY(3).build());
        meshBuilder.addPolygons(Structs.Polygon.newBuilder().addSegmentIdxs(0).build());
        meshBuilder.addPolygons(Structs.Polygon.newBuilder().addSegmentIdxs(1).build());
        meshBuilder.addSegments(Structs.Segment.newBuilder().setV1Idx(0).setV2Idx(1).build());
        meshBuilder.addSegments(Structs.Segment.newBuilder().setV1Idx(1).setV2Idx(2).build());
        Structs.Mesh mesh = meshBuilder.build();

        // Create a HashSet of allCities
        HashSet<Coordinate> allCities = new HashSet<>();
        allCities.add(new Coordinate(1.0, 1.0));
        allCities.add(new Coordinate(2.0, 2.0));
        allCities.add(new Coordinate(3.0, 3.0));

        // Call the constructCityGraph method
        Graph<Coordinate, CityEdge> graph = Cities.constructCityGraph(mesh, allCities, mesh.getPolygonsList(), mesh.getVerticesList());

        // Define the most central city
        Coordinate mostCentralCity = new Coordinate(2.0, 2.0);

        // Call the computeShortestPaths method
        DijkstraPathFinder<Coordinate, CityEdge> pathfinder = Cities.computeShortestPaths(graph, mostCentralCity);

        // Assert that the shortest paths are as expected
        List<Coordinate> pathToCity1 = pathfinder.shortestPath(new Coordinate(1.0, 1.0));
        List<Coordinate> pathToCity3 = pathfinder.shortestPath(new Coordinate(3.0, 3.0));

        assertEquals(2, pathToCity1.size());
        assertEquals(mostCentralCity, pathToCity1.get(0));
        assertEquals(new Coordinate(1.0, 1.0), pathToCity1.get(1));

        assertEquals(2, pathToCity3.size());
        assertEquals(mostCentralCity, pathToCity3.get(0));
        assertEquals(new Coordinate(3.0, 3.0), pathToCity3.get(1));
    }
}