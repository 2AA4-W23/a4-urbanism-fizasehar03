package ca.mcmaster.cas.se2aa4.a3.island.shapes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a3.island.biomes.Biomes;
import ca.mcmaster.cas.se2aa4.a3.island.elevationprofiles.HillsElevation;
import ca.mcmaster.cas.se2aa4.a3.island.elevationprofiles.MountainElevation;
import ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Lakes;
import ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Aquifers;
import ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Rivers;
import ca.mcmaster.cas.se2aa4.a3.island.soilabsorption.DrySoil;
import ca.mcmaster.cas.se2aa4.a3.island.soilabsorption.WetSoil;
import ca.mcmaster.cas.se2aa4.a3.island.tiles.Beach;
import ca.mcmaster.cas.se2aa4.a3.island.tiles.Ice;
import ca.mcmaster.cas.se2aa4.a3.island.tiles.Land;
import ca.mcmaster.cas.se2aa4.a3.island.tiles.Ocean;

import java.util.ArrayList;

public class SquareIsland {
    private ArrayList<Structs.Polygon> temp;
    private ArrayList<String> type;
    private ArrayList<Boolean> isAquifer = new ArrayList<>();
    private ArrayList<Structs.Segment> tempSeg;
    private ArrayList<Double> elevations;
    private ArrayList<Double> humidity;

    private ArrayList<String> biomes;


    public SquareIsland(){
        this.temp = new ArrayList<>();
        this.type = new ArrayList<>();
        this.tempSeg = new ArrayList<>();
    }


    public void  generateSquareIsland(Structs.Mesh mesh, double xcenter, double ycenter, int numLakes, int numRivers, int numAquifers, String altitude, String soil, double minDimension, String biomeInput, long seed) {

        for (Structs.Polygon p : mesh.getPolygonsList()) {

            double pCenterx = mesh.getVerticesList().get(p.getCentroidIdx()).getX();
            double pCentery = mesh.getVerticesList().get(p.getCentroidIdx()).getY();

            // Check if the point is within the square island
            if (Math.abs(pCenterx - xcenter) < (minDimension * 0.38) && Math.abs(pCentery - ycenter) < (minDimension * 0.38)) {
                type.add("land");
                Land land = new Land();
                temp.add(Structs.Polygon.newBuilder(p).clearProperties().addProperties(land.setColourCode()).build());
            }
            // Otherwise, it is water
            else {
                type.add("ocean");
                Ocean ocean = new Ocean();
                temp.add(Structs.Polygon.newBuilder(p).clearProperties().addProperties(ocean.setColourCode()).build());
            }
        }
        for (Structs.Polygon p : mesh.getPolygonsList()) {
            if (type.get(mesh.getPolygonsList().indexOf(p)).equals("land")) {
                for (int i : p.getNeighborIdxsList()) {
                    if (type.get(i).equals("ocean")) {
                        if(biomeInput.equals("Arctic")) {
                            type.set(mesh.getPolygonsList().indexOf(p), "Ice");
                            Ice ice = new Ice();
                            temp.set(mesh.getPolygonsList().indexOf(p), Structs.Polygon.newBuilder(p).clearProperties().addProperties(ice.setColourCode()).build());
                            break;
                        }else{
                            type.set(mesh.getPolygonsList().indexOf(p), "beach");
                            Beach beach = new Beach();
                            temp.set(mesh.getPolygonsList().indexOf(p), Structs.Polygon.newBuilder(p).clearProperties().addProperties(beach.setColourCode()).build());
                            break;
                        }
                    }
                }
            }
        }
        for(Structs.Segment s: mesh.getSegmentsList()){
            tempSeg.add(s);
        }
        for(int i = 0; i < mesh.getPolygonsCount(); i++){
            isAquifer.add(false);
        }

        if(numLakes != 0) {
            Lakes lakes = new Lakes(temp, type, numLakes);
            lakes.generateLakes(mesh, seed);
            temp = lakes.getTempMeshProperties();
        }
        if(numAquifers != 0){
            Aquifers aquifers = new Aquifers(temp, type, isAquifer, numAquifers);
            aquifers.generateAquifers(mesh, seed);
            isAquifer = aquifers.getIsAquifer();

        }
        Rivers rivers = new Rivers(temp,type,numRivers, tempSeg);

        if (numRivers != 0){
            rivers.generateRivers(mesh,xcenter,ycenter, seed);
            temp = rivers.getTempMeshProperties();
            tempSeg = rivers.getTempSeg();
            type = rivers.getType();
        }
        if(altitude.equals("mountain")) {
            MountainElevation me = new MountainElevation();
            me.computeElevations(mesh, type, xcenter, ycenter, minDimension, seed);
            this.elevations = me.getElevations();
        }
        else if(altitude.equals("hills")) {
            HillsElevation he = new HillsElevation();
            he.computeElevations(mesh, type, xcenter, ycenter, minDimension, seed);
            this.elevations = he.getElevations();
        }

        if(soil.equals("wet")){
            WetSoil wetSoil = new WetSoil();
            wetSoil.computeHumidity(mesh, type, isAquifer, minDimension, rivers);
            this.humidity = wetSoil.getHumidity();
        }
        else if(soil.equals("dry")){
            DrySoil drySoil = new DrySoil();
            drySoil.computeHumidity(mesh, type, isAquifer, minDimension, rivers);
            this.humidity = drySoil.getHumidity();
        }

        Biomes biome = new Biomes();
        biome.FindBiomes(mesh, elevations, humidity, type,biomeInput);
        this.biomes = biome.getBiomes();
        temp = biome.assignColor(temp, type);


    }
    public ArrayList<Structs.Polygon> getTempMeshProperties(){
        return this.temp;
    }
    public ArrayList<String> getType(){
        return this.type;
    }
    public ArrayList<Structs.Segment> getTempSeg(){
        return this.tempSeg;
    }
}
