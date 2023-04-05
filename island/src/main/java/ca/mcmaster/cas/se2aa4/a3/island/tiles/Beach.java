package ca.mcmaster.cas.se2aa4.a3.island.tiles;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class Beach {
    private final int red, green, blue;

    public Beach(){
        this.red = 180;
        this.green = 156;
        this.blue = 70;
    }

    public Structs.Property setColourCode(){
        String colorCode = red + "," + green + "," + blue;
        Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
        return color;
    }
}
