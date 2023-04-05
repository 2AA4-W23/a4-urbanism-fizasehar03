package ca.mcmaster.cas.se2aa4.a3.island.configuration;

import org.apache.commons.cli.*;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    public static final String INPUT = "i";
    public static final String OUTPUT = "o";
    public static final String SHAPE = "s";
    public static final String RIVERS = "rivers";
    public static final String LAKES = "lakes";
    public static final String LAGOON = "lagoon";
    public static final String AQUIFERS = "aquifers";
    public static final String ALTITUDE = "altitude";
    public static final String SOIL = "soil";
    public static final String BIOME = "biome";

    public static final String SEED = "seed";


    public static final String HELP = "help";

    private CommandLine cli;

    public Configuration(String[] args) {
        try {
            this.cli = parser().parse(options(), args);
            if (cli.hasOption(HELP)) {
                help();
            }
        } catch (ParseException pe) {
            throw new IllegalArgumentException(pe);
        }
    }

    private void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar generator.jar", options());
        System.exit(0);
    }

    public Map<String, String> export() {
        Map<String, String> result = new HashMap<>();
        for(Option o: cli.getOptions()){
            result.put(o.getOpt(), o.getValue(""));
        }
        return result;
    }

    public String export(String key) {
        return cli.getOptionValue(key);
    }

    private CommandLineParser parser() {
        return new DefaultParser();
    }
    public String input() {
        return this.cli.getOptionValue(INPUT);
    }
    public String shape() {
        return this.cli.getOptionValue(SHAPE);
    }
    public boolean lagoon(){
        return this.cli.hasOption(LAGOON);
    }
    public int rivers() {
        if(cli.hasOption(RIVERS)) {
            return Integer.valueOf(this.cli.getOptionValue(RIVERS));
        }
        return 0;
    }
    public int lakes() {
        if(cli.hasOption(LAKES)) {
            return Integer.valueOf(this.cli.getOptionValue(LAKES));
        }
        return 0;
    }
    public int aquifers() {
        if(cli.hasOption(AQUIFERS)) {
            return Integer.valueOf(this.cli.getOptionValue(AQUIFERS));
        }
        return 0;
    }
    public String altitude() {
        if(cli.hasOption(ALTITUDE)) {
            return this.cli.getOptionValue(ALTITUDE);
        }
        return "";
    }
    public String soil() {
        if(cli.hasOption(SOIL)) {
            return this.cli.getOptionValue(SOIL);
        }
        return "";
    }

    public String biomes() {
        if(cli.hasOption(BIOME)) {
            return this.cli.getOptionValue(BIOME);
        }
        return "";
    }
    public long seed() {
        if (cli.hasOption(SEED)) {
            return Long.valueOf(this.cli.getOptionValue(SEED));
        }
        return 0;
    }
    private Options options() {
        Options options = new Options();
        options.addOption(new Option(INPUT, true, "Input file (SVG)"));
        options.addOption(new Option(OUTPUT, true, "Output file name"));
        options.addOption(new Option(SHAPE, true, "Shape of island to be generated"));
        options.addOption(new Option(RIVERS, true, "Amount of rivers to be generated"));
        options.addOption(new Option(LAKES, true, "Amount of lakes to be generated"));
        options.addOption(new Option(AQUIFERS, true, "Amount of aquifers to be generated"));
        options.addOption(new Option(ALTITUDE, true, "Altitude of island"));
        options.addOption(new Option(SOIL, true, "Type of soil on the island for absorption"));
        options.addOption(new Option(BIOME, true, "Type of biome that's on the island to be generated "));
        options.addOption(new Option(SEED, true, "The SEED for reproducibility"));


        options.addOption(new Option(LAGOON, false, "Whether or not island should be a lagoon island"));
        // Global help
        options.addOption(new Option(HELP, false, "print help message"));
        return options;
    }

}
