package ca.mcmaster.cas.se2aa4.a3.island.extentionpoints.Cities;

import java.util.Random;


    public enum CitySize {
        TOWN(1),
        SMALL(2),
        MEDIUM(3),
        LARGE(4),
        METROPOLIS(5);

        private final int index;

        private CitySize(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static CitySize getRandomCitySize(Random random) {
            return values()[random.nextInt(values().length)];
        }
    }

