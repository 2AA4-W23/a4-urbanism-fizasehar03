package ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Cities;

import ca.mcmaster.cas.se2aa4.a4.pathfinder.Edge;

import java.util.Objects;


    public class CityEdge implements Edge<Coordinate> {
        Coordinate source;
        Coordinate destination;

        CityEdge(Coordinate source, Coordinate destination) {
            this.source = source;
            this.destination = destination;
        }

        @Override
        public Coordinate getDestination() {
            return destination;
        }

        @Override
        public double getWeight() {
            return source.getDistance(destination);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            CityEdge that = (CityEdge) obj;
            return this.source.equals(that.source) && this.destination.equals(that.destination);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.source, this.destination);
        }
    }

