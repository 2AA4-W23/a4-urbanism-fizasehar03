package ca.mcmaster.cas.se2aa4.a3.island.extentionpoints;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a3.island.tiles.Lake;
import ca.mcmaster.cas.se2aa4.a3.island.tiles.River;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Rivers {
    private ArrayList<Structs.Polygon> temp;
    private ArrayList<String> type;
    private int numRivers;
    private ArrayList<Structs.Segment> tempSeg;
    private ArrayList<Integer> discharge;

    public Rivers(ArrayList<Structs.Polygon> temp, ArrayList<String> type, int numRivers, ArrayList<Structs.Segment> tempSeg){
        this.temp = temp;
        this.type = type;
        this.numRivers = numRivers;
        this.tempSeg = tempSeg;
        this.discharge = new ArrayList<>();

    }
    public ArrayList<Structs.Segment> generateRivers(Structs.Mesh mesh, double xcenter, double ycenter, long seed){

        ArrayList<Structs.Segment> visited = new ArrayList<>();
        HashSet<Structs.Polygon> visitedPolygons = new HashSet<>();
        Random rand1 = new Random(seed);

        for(int d = 0; d < tempSeg.size(); d++){
            discharge.add(0);
        }

        for(int l = 0; l < numRivers; l++){
            String tileType;
            int rand = 0;
            for(int r = 0; r < temp.size(); r++){
                rand = rand1.nextInt(temp.size());
                tileType = type.get(rand);
                if(tileType.equals("land") && !visitedPolygons.contains(mesh.getPolygonsList().get(rand))){
                    break;
                }
            }
            Structs.Polygon pCurrent = mesh.getPolygonsList().get(rand);
            Structs.Polygon pNext = pCurrent;

            double xCurrent = mesh.getVerticesList().get(pCurrent.getCentroidIdx()).getX();
            double yCurrent = mesh.getVerticesList().get(pCurrent.getCentroidIdx()).getY();

            boolean topLeft = (xCurrent <= xcenter && yCurrent <= ycenter);
            boolean topRight = (xCurrent >= xcenter && yCurrent <= ycenter);
            boolean bottomLeft = (xCurrent <= xcenter && yCurrent >= ycenter);
            boolean bottomRight = (xCurrent >= xcenter && yCurrent >= ycenter);

            int currentDischarge = 1+rand1.nextInt(3);

            boolean check = false;

            for (int i = 0; i < pCurrent.getNeighborIdxsCount(); i++) {
                if(topLeft || topRight){
                    if (mesh.getVerticesList().get(mesh.getPolygonsList().get(pCurrent.getNeighborIdxs(i)).getCentroidIdx()).getY() < yCurrent) {
                        check = true;
                    }
                }
                else if(bottomLeft || bottomRight){
                    if (mesh.getVerticesList().get(mesh.getPolygonsList().get(pCurrent.getNeighborIdxs(i)).getCentroidIdx()).getY() > yCurrent) {
                        check = true;
                    }
                }
                if(check){
                    if(type.get(pCurrent.getNeighborIdxsList().get(i)).equals("ocean") || type.get(pCurrent.getNeighborIdxsList().get(i)).equals("river")){
                        visitedPolygons.add(pCurrent);
                        River river1 = new River();
                        pNext = mesh.getPolygonsList().get(pCurrent.getNeighborIdxs(i));
                        visitedPolygons.add(pNext);
                        if(!visited.contains(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river1.setColourCode()).build())){
                            tempSeg.add(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river1.setColourCode()).build());
                            discharge.add(currentDischarge);
                        } else {
                            tempSeg.add(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river1.setColourCode()).addProperties(river1.addWeight()).build());
                            discharge.add(discharge.get(tempSeg.indexOf(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river1.setColourCode()).build())) + currentDischarge);
                            discharge.set(tempSeg.indexOf(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river1.setColourCode()).build()), discharge.get(tempSeg.indexOf(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river1.setColourCode()).build())) + currentDischarge);
                        }
                        visited.add(tempSeg.get(tempSeg.size()-1));
                        break;
                    }
                    else if (type.get(pCurrent.getNeighborIdxsList().get(i)).equals("land") || type.get(pCurrent.getNeighborIdxsList().get(i)).equals("beach")) {
                        visitedPolygons.add(pCurrent);
                        pNext = mesh.getPolygonsList().get(pCurrent.getNeighborIdxs(i));
                        visitedPolygons.add(pNext);
                        River river = new River();
                        if(!visited.contains(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river.setColourCode()).build())){
                            tempSeg.add(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river.setColourCode()).build());
                            discharge.add(currentDischarge);
                        }
                        else{
                            tempSeg.add(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river.setColourCode()).addProperties(river.addWeight()).build());
                            discharge.add(discharge.get(tempSeg.indexOf(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river.setColourCode()).build())) + currentDischarge);
                            discharge.set(tempSeg.indexOf(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river.setColourCode()).build()), discharge.get(tempSeg.indexOf(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river.setColourCode()).build())) + currentDischarge);
                        }
                        visited.add(tempSeg.get(tempSeg.size()-1));
                        pCurrent = pNext;
                        xCurrent = mesh.getVerticesList().get(pCurrent.getCentroidIdx()).getX();
                        yCurrent = mesh.getVerticesList().get(pCurrent.getCentroidIdx()).getY();
                        i = 0;
                    }
                    else if(i >= pCurrent.getNeighborIdxsCount() - 1){
                        visitedPolygons.add(pCurrent);
                        River river1 = new River();
                        pNext = mesh.getPolygonsList().get(pCurrent.getNeighborIdxs(i));
                        visitedPolygons.add(pNext);
                        if(!visited.contains(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river1.setColourCode()).build())){
                            tempSeg.add(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river1.setColourCode()).build());
                            discharge.add(currentDischarge);
                        }
                        else{
                            tempSeg.add(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river1.setColourCode()).addProperties(river1.addWeight()).build());
                            discharge.add(discharge.get(tempSeg.indexOf(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river1.setColourCode()).build())) + currentDischarge);
                            discharge.set(tempSeg.indexOf(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river1.setColourCode()).build()), discharge.get(tempSeg.indexOf(Structs.Segment.newBuilder().setV1Idx(pCurrent.getCentroidIdx()).setV2Idx(pNext.getCentroidIdx()).addProperties(river1.setColourCode()).build())) + currentDischarge);
                        }
                        visited.add(tempSeg.get(tempSeg.size()-1));
                        break;
                    }
                    check = false;
                }
            }
        }


        return tempSeg;
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
    public ArrayList<Integer> getDischarge(){
        return this.discharge;
    }
}
