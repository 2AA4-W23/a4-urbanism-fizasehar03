package ca.mcmaster.cas.se2aa4.a3.island.soilabsorption;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Rivers;

import java.util.ArrayList;

public interface Soil {
    void computeHumidity(Structs.Mesh mesh, ArrayList<String> type, ArrayList<Boolean> isAquifer, double minDimension, Rivers rivers);
}
