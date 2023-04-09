#!/bin/bash

java -jar generator/generator.jar -k irregular -h 1080 -w 1920 -p 1000 -r 5 -o ireg.mesh -d
java -jar island/island.jar -i ireg.mesh -o ireg2.mesh -s square -lakes 6 -rivers 10 -altitude hills -soil wet -biome Arctic -cities 50
java -jar visualizer/visualizer.jar -i ireg2.mesh -o ireg_dbg2.svg
