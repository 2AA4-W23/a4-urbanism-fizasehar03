package ca.mcmaster.cas.se2aa4.a3.island.elevationprofiles;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.ArrayList;

public interface Elevations {
    void computeElevations(Structs.Mesh mesh, ArrayList<String> type, double xcenter, double ycenter, double minDimension, long seed);

    ArrayList<Double> getElevations();
}
