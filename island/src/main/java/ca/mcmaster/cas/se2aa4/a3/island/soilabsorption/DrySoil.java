package ca.mcmaster.cas.se2aa4.a3.island.soilabsorption;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Rivers;

import java.util.ArrayList;

public class DrySoil implements Soil{
    private ArrayList<Double> humidity;

    public DrySoil(){
        this.humidity = new ArrayList<>();
    }

    public void computeHumidity(Structs.Mesh mesh, ArrayList<String> type, ArrayList<Boolean> isAquifer, double minDimension, Rivers rivers){
        double xCurrentWater, yCurrentWater, xCurrentLand, yCurrentLand, distance;
        for(int s = 0; s < mesh.getPolygonsList().size(); s++){
            humidity.add(0,0.0);
        }
        for (Structs.Polygon p : mesh.getPolygonsList()) {
            if (type.get(mesh.getPolygonsList().indexOf(p)).equals("lake") || isAquifer.get(mesh.getPolygonsList().indexOf(p)).equals(true)) {
                xCurrentWater = mesh.getVerticesList().get(p.getCentroidIdx()).getX();
                yCurrentWater = mesh.getVerticesList().get(p.getCentroidIdx()).getY();
                for (Structs.Polygon i : mesh.getPolygonsList()) {
                    if (type.get(mesh.getPolygonsList().indexOf(i)).equals("land")) {
                        xCurrentLand = mesh.getVerticesList().get(i.getCentroidIdx()).getX();
                        yCurrentLand = mesh.getVerticesList().get(i.getCentroidIdx()).getY();

                        distance = Math.sqrt(Math.pow(xCurrentLand - xCurrentWater, 2) + Math.pow(yCurrentLand - yCurrentWater, 2));
                        if(distance < (minDimension * 0.01)){
                            humidity.set(mesh.getPolygonsList().indexOf(i), humidity.get(mesh.getPolygonsList().indexOf(i)) + 2);
                        }
                        else if(distance < (minDimension * 0.04)){
                            humidity.set(mesh.getPolygonsList().indexOf(i), humidity.get(mesh.getPolygonsList().indexOf(i)) + 1.4);
                        }
                        else if(distance < (minDimension * 0.06)){
                            humidity.set(mesh.getPolygonsList().indexOf(i), humidity.get(mesh.getPolygonsList().indexOf(i)) + 1);
                        }
                        else if(distance < (minDimension * 0.09)){
                            humidity.set(mesh.getPolygonsList().indexOf(i), humidity.get(mesh.getPolygonsList().indexOf(i)) + 0.5);
                        }
                        else if(distance < (minDimension * 0.11)){
                            humidity.set(mesh.getPolygonsList().indexOf(i), humidity.get(mesh.getPolygonsList().indexOf(i)) + 0.25);
                        }
                    }
                }
            }
        }
        for(int i = 0; i < rivers.getTempSeg().size(); i++){
            if(rivers.getDischarge().get(i) > 0){
                System.out.println("helloooooo");
                int currentDischarge = rivers.getDischarge().get(i);

                double xRiver = (mesh.getVerticesList().get(rivers.getTempSeg().get(i).getV1Idx()).getX() + mesh.getVerticesList().get(rivers.getTempSeg().get(i).getV2Idx()).getX())/2;
                double yRiver = (mesh.getVerticesList().get(rivers.getTempSeg().get(i).getV1Idx()).getY() + mesh.getVerticesList().get(rivers.getTempSeg().get(i).getV2Idx()).getY())/2;

                for(Structs.Polygon p: mesh.getPolygonsList()){
                    if (type.get(mesh.getPolygonsList().indexOf(p)).equals("land")) {
                        xCurrentLand = mesh.getVerticesList().get(p.getCentroidIdx()).getX();
                        yCurrentLand = mesh.getVerticesList().get(p.getCentroidIdx()).getY();

                        distance = Math.sqrt(Math.pow(xCurrentLand - xRiver, 2) + Math.pow(yCurrentLand - yRiver, 2));
                        if(distance < (minDimension * 0.02)){
                            humidity.set(mesh.getPolygonsList().indexOf(p), humidity.get(mesh.getPolygonsList().indexOf(p)) + 0.6*(currentDischarge));
                        }
                        else if(distance < (minDimension *0.04)){
                            humidity.set(mesh.getPolygonsList().indexOf(p), humidity.get(mesh.getPolygonsList().indexOf(p)) + 0.4*(currentDischarge));
                        }
                        else if(distance < (minDimension* 0.07)){
                            humidity.set(mesh.getPolygonsList().indexOf(p), humidity.get(mesh.getPolygonsList().indexOf(p)) + 0.2*(currentDischarge));
                        }
                    }
                }
            }
        }
    }
    public ArrayList<Double> getHumidity(){
        return this.humidity;
    }
}
