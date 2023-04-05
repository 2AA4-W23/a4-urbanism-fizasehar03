import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a3.island.IslandGenerator;
import ca.mcmaster.cas.se2aa4.a3.island.configuration.Configuration;


public class Main {
    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration(args);
        Structs.Mesh aMesh = new MeshFactory().read(config.input());
        String shape = config.shape();
        Boolean lagoon = config.lagoon();
        int lakes = config.lakes();
        int rivers = config.rivers();
        int aquifers = config.aquifers();
        String altitude = config.altitude();
        String soil = config.soil();
        String biome = config.biomes();
        long seed = config.seed();
        IslandGenerator islandGenerator = new IslandGenerator();
        Structs.Mesh islandMesh = islandGenerator.generateIsland(aMesh, shape, lagoon, lakes, rivers, aquifers, altitude, soil, biome,seed);
        new MeshFactory().write(islandMesh, config.export(Configuration.OUTPUT));
    }
}
