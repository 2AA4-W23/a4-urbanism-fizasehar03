package ca.mcmaster.cas.se2aa4.a3.island.extentionpoints;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a3.island.tiles.Beach;
import ca.mcmaster.cas.se2aa4.a3.island.tiles.Lake;

import java.util.ArrayList;
import java.util.Random;

public class Lakes {
    private ArrayList<Structs.Polygon> temp;
    private ArrayList<String> type;
    private int numLakes;

    public Lakes(ArrayList<Structs.Polygon> temp, ArrayList<String> type, int numLakes){
        this.temp = temp;
        this.type = type;
        this.numLakes = numLakes;
    }
    public ArrayList<Structs.Polygon> generateLakes(Structs.Mesh mesh, long seed){
        Random rand1 = new Random(seed);

        for(int l = 0; l < numLakes; l++) {
            String tileType;
            int rand = 0;
            for(int r = 0; r < temp.size(); r++){
                rand = rand1.nextInt(temp.size());
                tileType = type.get(rand);
                if(tileType.equals("land")){
                    break;
                }
            }


            Polygon p = mesh.getPolygonsList().get(rand);
            type.set(mesh.getPolygonsList().indexOf(p), "lake");
            Lake lake = new Lake();
            temp.set(mesh.getPolygonsList().indexOf(p), Structs.Polygon.newBuilder(p).clearProperties().addProperties(lake.setColourCode()).build());
            int lakeSize = (rand1.nextInt(p.getNeighborIdxsCount()));
            int lakeCounter = 0;
            do {
                for (int i : p.getNeighborIdxsList()) {
                    if (type.get(i).equals("land")) {
                        type.set(i, "lake");
                        Lake lakeN = new Lake();
                        temp.set(i, Structs.Polygon.newBuilder(temp.get(i)).clearProperties().addProperties(lakeN.setColourCode()).build());
                    }
                    lakeCounter++;
                }
            } while (lakeCounter < lakeSize);
        }
        return temp;
    }
    public ArrayList<Structs.Polygon> getTempMeshProperties(){
        return this.temp;
    }
    public ArrayList<String> getType(){
        return this.type;
    }
}
