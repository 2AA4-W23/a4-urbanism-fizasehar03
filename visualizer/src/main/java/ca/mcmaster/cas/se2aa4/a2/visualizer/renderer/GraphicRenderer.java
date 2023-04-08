package ca.mcmaster.cas.se2aa4.a2.visualizer.renderer;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.visualizer.renderer.properties.CityProperty;
import ca.mcmaster.cas.se2aa4.a2.visualizer.renderer.properties.ColorProperty;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Iterator;
import java.util.Optional;

public class GraphicRenderer implements Renderer {

    private static final int THICKNESS = 3;
    public void render(Mesh aMesh, Graphics2D canvas) {
        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(0.2f);
        canvas.setStroke(stroke);
        drawPolygons(aMesh,canvas);
        drawSegments(aMesh, canvas);
        drawDots(aMesh, canvas);
    }

    private void drawPolygons(Mesh aMesh, Graphics2D canvas) {
        for(Structs.Polygon p: aMesh.getPolygonsList()){
            drawAPolygon(p, aMesh, canvas);
        }
    }

    private void drawDots(Mesh aMesh, Graphics2D canvas) {
        canvas.setColor(Color.ORANGE);
        for (Vertex v : aMesh.getVerticesList()) {
            Optional<Integer> citySize = new CityProperty().extract(v.getPropertiesList());
            if (citySize.isPresent()) {
                Ellipse2D circle = new Ellipse2D.Float((float) v.getX()-2.5f, (float) v.getY()-2.5f, 5, 5);
                canvas.fill(circle);
            }
        }
        canvas.setColor(Color.BLACK);
    }

    private void drawAPolygon(Structs.Polygon p, Mesh aMesh, Graphics2D canvas) {
        Hull hull = new Hull();
        for(Integer segmentIdx: p.getSegmentIdxsList()) {
            hull.add(aMesh.getSegments(segmentIdx), aMesh);
        }
        Path2D path = new Path2D.Float();
        Iterator<Vertex> vertices = hull.iterator();
        Vertex current = vertices.next();
        path.moveTo(current.getX(), current.getY());
        while (vertices.hasNext()) {
            current = vertices.next();
            path.lineTo(current.getX(), current.getY());
        }
        path.closePath();
        canvas.draw(path);
        Optional<Color> fill = new ColorProperty().extract(p.getPropertiesList());
        if(fill.isPresent()) {
            Color old = canvas.getColor();
            canvas.setColor(fill.get());
            canvas.fill(path);
            canvas.setColor(old);
        }
    }
    private void drawSegments(Mesh aMesh, Graphics2D canvas){
        Line2D line;
        for(Structs.Segment s: aMesh.getSegmentsList()){
            line = new Line2D.Double(aMesh.getVerticesList().get(s.getV1Idx()).getX(),aMesh.getVerticesList().get(s.getV1Idx()).getY(), aMesh.getVerticesList().get(s.getV2Idx()).getX(), aMesh.getVerticesList().get(s.getV2Idx()).getY());
            canvas.draw(line);
            Optional<Color> fill = new ColorProperty().extract(s.getPropertiesList());
            Color old = canvas.getColor();
            if(fill.isPresent() && s.getPropertiesList().size() > 1){
                canvas.setColor(fill.get());
                Stroke strokeSegments = new BasicStroke(5f);
                canvas.setStroke(strokeSegments);
                canvas.draw(line);
            }
            else if(fill.isPresent()) {
                canvas.setColor(fill.get());
                Stroke strokeSegments = new BasicStroke(2f);
                canvas.setStroke(strokeSegments);
                canvas.draw(line);
            }

            canvas.setColor(old);
            Stroke stroke = new BasicStroke(0.2f);
            canvas.setStroke(stroke);
        }
    }

}
