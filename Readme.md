# Mesh Generator (Assignment #4)

- Author: Fiza Sehar

## How to install?

```
seharf@mcmaster.ca```

It creates two jars:

  1. `generator/generator.jar` to generate meshes
  2. `visualizer/visualizer.jar` to visualize such meshes as SVG files

## Examples of execution

### Generating mesh, island, and visualizing everything
```
 This bash script can be used to generate everything:  ./generate.sh 
```
If you would like to test indiviually:

### Generating a mesh, grid or irregular

```
mosser@azrael A2 % java -jar generator/generator.jar -k grid -h 1080 -w 1920 -p 1000 -s 20 -o img/grid.mesh
mosser@azrael A2 % java -jar generator/generator.jar -k grid -h 1080 -w 1920 -p 1000 -s 20 -o img/irregular.mesh
```

One can run the generator with `-help` as option to see the different command line arguments that are available

### Island generation

```
java -jar island/island.jar -i ireg.mesh -o ireg2.mesh -s square -lakes 6 -rivers 10 -altitude hills -soil wet -biome Arctic -cities 50

```

### Visualizing a mesh, (regular or debug mode)

```
mosser@azrael A2 % java -jar visualizer/visualizer.jar -i img/grid.mesh -o img/grid.svg          
mosser@azrael A2 % java -jar visualizer/visualizer.jar -i img/grid.mesh -o img/grid_debug.svg -x
mosser@azrael A2 % java -jar visualizer/visualizer.jar -i img/irregular.mesh -o img/irregular.svg   
mosser@azrael A2 % java -jar visualizer/visualizer.jar -i img/irregular.mesh -o img/irregular_debug.svg -x
```

Note: PDF versions of the SVG files were created with `rsvg-convert`.

