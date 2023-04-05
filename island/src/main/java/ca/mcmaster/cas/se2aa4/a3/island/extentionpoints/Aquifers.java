package ca.mcmaster.cas.se2aa4.a3.island.extentionpoints;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a3.island.tiles.Aquifer;

import java.util.ArrayList;
import java.util.Random;

public class Aquifers {
    private ArrayList<Structs.Polygon> temp;
    private ArrayList<String> type;
    private ArrayList<Boolean> isAquifer;
    private int numAquifers;

    public Aquifers(ArrayList<Structs.Polygon> temp, ArrayList<String> type, ArrayList<Boolean> isAquifer, int numAquifers){
        this.temp = temp;
        this.type = type;
        this.isAquifer = isAquifer;
        this.numAquifers = numAquifers;
    }

    public ArrayList<Structs.Polygon> generateAquifers(Structs.Mesh mesh, long seed){
        Random rand1 = new Random(seed);

        for(int l = 0; l < numAquifers; l++) {
            String tileType;
            int rand = 0;
            for(int r = 0; r < temp.size(); r++){
                rand = rand1.nextInt(temp.size());
                tileType = type.get(rand);
                if((tileType.equals("land") || tileType.equals("beach")) && isAquifer.get(rand).equals(false)){
                    break;
                }
            }

            Structs.Polygon p = mesh.getPolygonsList().get(rand);
            isAquifer.set(mesh.getPolygonsList().indexOf(p), true);
          //  Aquifer aquifer = new Aquifer();
          //  temp.set(mesh.getPolygonsList().indexOf(p), Structs.Polygon.newBuilder(p).clearProperties().addProperties(aquifer.setColourCode()).build());
            int aquiferSize = rand1.nextInt(p.getNeighborIdxsCount());
            int aquiferCounter = 0;
            do {
                for (int i : p.getNeighborIdxsList()) {
                    if ((type.get(i).equals("land") || type.get(i).equals("beach")) && isAquifer.get(i).equals(false)) {
                        isAquifer.set(i, true);
                      //  Aquifer lakeN = new Aquifer();
                      //  temp.set(i, Structs.Polygon.newBuilder(temp.get(i)).clearProperties().addProperties(lakeN.setColourCode()).build());
                    }
                    aquiferCounter++;
                }
            } while (aquiferCounter < aquiferSize);
        }
        return temp;
    }

    public ArrayList<Structs.Polygon> getTempMeshProperties(){
        return this.temp;
    }
    public ArrayList<String> getType(){
        return this.type;
    }
    public ArrayList<Boolean> getIsAquifer(){
        return this.isAquifer;
    }
}
