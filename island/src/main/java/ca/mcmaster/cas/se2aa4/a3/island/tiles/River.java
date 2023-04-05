package ca.mcmaster.cas.se2aa4.a3.island.tiles;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class River {
    private final int red, green, blue;

    public River(){
        this.red = 70;
        this.green = 90;
        this.blue = 180;
    }
    public Structs.Property setColourCode(){
        String colorCode = red + "," + green + "," + blue;
        Structs.Property color = Structs.Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
        return color;
    }

    public Structs.Property addWeight(){
        Structs.Property weight = Structs.Property.newBuilder().setKey("hasWeight").setValue("true").build();
        return weight;
    }
}
