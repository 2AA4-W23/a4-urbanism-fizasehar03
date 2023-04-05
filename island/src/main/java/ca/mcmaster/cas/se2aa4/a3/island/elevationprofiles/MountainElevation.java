package ca.mcmaster.cas.se2aa4.a3.island.elevationprofiles;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a3.island.tiles.Beach;

import java.util.ArrayList;

public class MountainElevation implements Elevations{

    private ArrayList<Double> elevations;

    public MountainElevation(){
        this.elevations = new ArrayList<>();
    }

    public void computeElevations(Structs.Mesh mesh, ArrayList<String> type, double xcenter, double ycenter, double minDimension, long seed){
        double xCurrent, yCurrent, distance;
        int count = 0;
        for (Structs.Polygon p : mesh.getPolygonsList()) {
            if (!(type.get(mesh.getPolygonsList().indexOf(p)).equals("ocean"))) {
                xCurrent = mesh.getVerticesList().get(p.getCentroidIdx()).getX();
                yCurrent = mesh.getVerticesList().get(p.getCentroidIdx()).getY();

                distance = Math.sqrt(Math.pow(xCurrent - xcenter, 2) + Math.pow(yCurrent - ycenter, 2));

                if(distance < (minDimension * 0.03)){
                    elevations.add(100.0);
                }
                else if(distance < (minDimension * 0.07)){
                    elevations.add(90.0);
                }
                else if(distance < (minDimension * 0.11)){
                    elevations.add(80.0);
                }
                else if(distance < (minDimension * 0.15)){
                    elevations.add(65.0);
                }
                else if(distance < (minDimension * 0.19)){
                    elevations.add(50.0);
                }
                else if(distance < (minDimension * 0.24)){
                    elevations.add(35.0);
                }
                else if(distance < (minDimension * 0.30)){
                    elevations.add(20.0);
                }
                else if(distance < (minDimension * 0.4)){
                    elevations.add(10.0);
                }
                else{
                    elevations.add(5.0);
                }

            }
            // ocean level elevation, which will always be 0
            else{
                elevations.add(0.0);
            }
            count++;
        }
    }
    public ArrayList<Double> getElevations(){
        return this.elevations;
    }

}
