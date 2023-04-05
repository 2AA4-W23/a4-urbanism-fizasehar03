package ca.mcmaster.cas.se2aa4.a3.island.biomes;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a3.island.tiles.*;

import java.util.ArrayList;

public class Biomes {
    private ArrayList<String> biomes;

    public Biomes() {
        this.biomes = new ArrayList<>();
    }

    public void FindBiomes(Structs.Mesh mesh, ArrayList<Double> elevations, ArrayList<Double> humidity, ArrayList<String> type, String userInput) {

        for (int i = 0; i < mesh.getPolygonsList().size(); i++) {
            Structs.Polygon p = mesh.getPolygonsList().get(i);
            double elevation = elevations.get(i);
            double humid = humidity.get(i);

            // Determine biome based on elevation and humidity
            if (userInput.equals("Arctic")) {
                if (type.get(i).equals("land")) {
                    if (elevation < 5 && humid < 2) {
                        biomes.add(Arctic.ArcticTundra.toString());
                    } else if (elevation < 20 && humid < 15 || humid > 0.2) {
                        biomes.add(Arctic.Taiga.toString());

                    } else if (elevation < 30 && humid > 1 || humid < 10) {
                        biomes.add(Arctic.TemperateRainforest.toString());

                    } else if (elevation < 10 && humid < 0.2 || humid > 2) {
                        biomes.add(Arctic.ArcticDesert.toString());

                    } else {
                        biomes.add(Arctic.BorealForest.toString());
                    }
                } else {
                    biomes.add("Water");
                }
            } else if (userInput.equals("Tropical")) {
                if (type.get(i).equals("land")) {
                    if (elevation < 40 && humid < 30 || humid > 20) {
                        biomes.add(Tropical.TropicalRainforest.toString());
                    } else if (elevation < 40|| elevation>30 && humid < 25 || humid > 10) {
                        biomes.add(Tropical.TropicalSeasonalForest.toString());
                    } else if (elevation < 10 || elevation >5 && humid < 10) {
                        biomes.add(Tropical.Desert.toString());
                    } else {
                        biomes.add(Tropical.MangroveSwamp.toString());
                    }
                } else {
                    biomes.add("Land");
                }
            } else if (userInput.equals("Grassland")) {
                if (elevation < 20 && humid < 10 || humid > 0.2) {
                    biomes.add(Grassland.GrasslandPrairie.toString());
                } else if (elevation < 30 && humid < 15 || humid > 5) {
                    biomes.add(Grassland.GrasslandSavanna.toString());
                } else if (elevation < 10 && humid < 15 || humid > 10) {
                    biomes.add(Grassland.GrasslandPampas.toString());
                } else  {
                    biomes.add(Grassland.GrasslandVeld.toString());
                }
            }
                else {
                    biomes.add("Land");
            }
        }
    }
    public ArrayList<String> getBiomes() {
        return this.biomes;
    }
    public ArrayList<Structs.Polygon> assignColor(ArrayList<Structs.Polygon> temp, ArrayList<String> type) {

        for (int f = 0; f < temp.size(); f++) {
            if (type.get(f).equals("land")) {
                if (biomes.get(f).equals("Ice") ){
                    Ice ice = new Ice();
                    temp.set(f, Structs.Polygon.newBuilder(temp.get(f)).clearProperties().addProperties(ice.setColourCode()).build());
                }
                if (biomes.get(f).equals(Tropical.Desert.toString())) {
                    Desert desert = new Desert();
                    temp.set(f, Structs.Polygon.newBuilder(temp.get(f)).clearProperties().addProperties(desert.setColourCode()).build());
                }
                if (biomes.get(f).equals(Arctic.ArcticDesert.toString())) {
                    ArcticDesert arcticDesert = new ArcticDesert();
                    temp.set(f, Structs.Polygon.newBuilder(temp.get(f)).clearProperties().addProperties(arcticDesert.setColourCode()).build());
                }
                if (biomes.get(f).equals(Arctic.BorealForest.toString())) {
                    BorealForest borealForest = new BorealForest();
                    temp.set(f, Structs.Polygon.newBuilder(temp.get(f)).clearProperties().addProperties(borealForest.setColourCode()).build());
                }
                if (biomes.get(f).equals(Tropical.TropicalRainforest.toString())) {
                    RainForest rainForest = new RainForest();
                    temp.set(f, Structs.Polygon.newBuilder(temp.get(f)).clearProperties().addProperties(rainForest.setColourCode()).build());
                }
                if (biomes.get(f).equals(Arctic.Taiga.toString())) {
                    Taiga taiga = new Taiga();
                    temp.set(f, Structs.Polygon.newBuilder(temp.get(f)).clearProperties().addProperties(taiga.setColourCode()).build());
                }
                if (biomes.get(f).equals(Arctic.TemperateRainforest.toString())) {
                    TemperateRainforest temperateRainforest = new TemperateRainforest();
                    temp.set(f, Structs.Polygon.newBuilder(temp.get(f)).clearProperties().addProperties(temperateRainforest.setColourCode()).build());
                }
                if (biomes.get(f).equals(Tropical.MangroveSwamp.toString())) {
                    Swamp swamp = new Swamp();
                    temp.set(f, Structs.Polygon.newBuilder(temp.get(f)).clearProperties().addProperties(swamp.setColourCode()).build());
                }
                if (biomes.get(f).equals(Arctic.ArcticTundra.toString())) {
                    Tundra tundra = new Tundra();
                    temp.set(f, Structs.Polygon.newBuilder(temp.get(f)).clearProperties().addProperties(tundra.setColourCode()).build());
                }
                if (biomes.get(f).equals(Tropical.TropicalSeasonalForest.toString())) {
                    Forest forest = new Forest();
                    temp.set(f, Structs.Polygon.newBuilder(temp.get(f)).clearProperties().addProperties(forest.setColourCode()).build());
                }
                if (biomes.get(f).equals(Grassland.GrasslandPrairie.toString())) {
                    GrasslandPrairie grasslandPrairie = new GrasslandPrairie();
                    temp.set(f, Structs.Polygon.newBuilder(temp.get(f)).clearProperties().addProperties(grasslandPrairie.setColourCode()).build());
                }
                if (biomes.get(f).equals(Grassland.GrasslandSavanna.toString())) {
                    GrasslandSavanna grasslandSavanna = new GrasslandSavanna();
                    temp.set(f, Structs.Polygon.newBuilder(temp.get(f)).clearProperties().addProperties(grasslandSavanna.setColourCode()).build());
                }
                if (biomes.get(f).equals(Grassland.GrasslandPampas.toString())) {
                    GrasslandPampas grasslandPampas = new GrasslandPampas();
                    temp.set(f, Structs.Polygon.newBuilder(temp.get(f)).clearProperties().addProperties(grasslandPampas.setColourCode()).build());
                }
                if (biomes.get(f).equals(Grassland.GrasslandPrairie.toString())) {
                    GrasslandPrairie grasslandPrairie = new GrasslandPrairie();
                    temp.set(f, Structs.Polygon.newBuilder(temp.get(f)).clearProperties().addProperties(grasslandPrairie.setColourCode()).build());
                }
            }

        }
        return temp;
    }
}
