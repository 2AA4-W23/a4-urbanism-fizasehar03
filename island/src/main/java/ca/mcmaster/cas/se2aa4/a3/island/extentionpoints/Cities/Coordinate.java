package ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Cities;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.Objects;

public class Coordinate {
        Double x;
        Double y;

        Coordinate(Structs.Vertex v) {
            this.x = v.getX();
            this.y = v.getY();
        }

        Coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getDistance(Coordinate other) {
            double pointDifference = Math.sqrt((Math.pow(this.getX()-other.getX(),2)+Math.pow(this.getY()-other.getY(),2)));
            return pointDifference;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public Structs.Vertex makeVertex() {
            return Structs.Vertex.newBuilder().setX(x).setY(y).build();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Coordinate that = (Coordinate) obj;
            return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return this.x.toString() + "," + this.y.toString();
        }
}

