package com.example.test;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class Tools {

    public static Shape arrayListToPolygon (ArrayList<Point2D> points, Color c) {
        return arrayToPolygon(points.toArray(new Point2D[0]), c);
    }

    public static Shape arrayToPolygon (Point2D[] points, Color c) {
        Polygon polygon = new Polygon();
        polygon.setFill(c);
        ObservableList<Double> list = polygon.getPoints();
        for (Point2D point : points) {
            list.add(point.getX());
            list.add(point.getY());
        }
        return polygon;
    }

    public static Point2D getShortestIntersection (Line line, ArrayList<Line> otherLines) {
        Point2D shortestIntersection = null;
        double shortestDistance = Double.POSITIVE_INFINITY;

        for (Line otherLine : otherLines) {
            if (line != otherLine) {
                // Do not compare line with itself
                Point2D intersection = getIntersection(line, otherLine);
                if (intersection != null) {
                    // Lines intersect
                    double distance = Math.abs(line.getStartX() - intersection.getX());
                    if (distance >= 0 && distance < shortestDistance) {
                        // Shortest intersection so far
                        shortestIntersection = intersection;
                        shortestDistance = distance;
                    }
                }
            }
        }

        return shortestIntersection;
    }

    private static Point2D getIntersection (Line line1, Line line2) {
        double x1 = line1.getStartX();
        double y1 = line1.getStartY();
        double x2 = line1.getEndX();
        double y2 = line1.getEndY();
        double x3 = line2.getStartX();
        double y3 = line2.getStartY();
        double x4 = line2.getEndX();
        double y4 = line2.getEndY();

        double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (denom == 0) {
            // Lines are parallel
            return null;
        }

        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
        if (ua < 0 || ua > 1 || ub < 0 || ub > 1) {
            // Intersection is not on line segments
            return null;
        }

        double x = x1 + ua * (x2 - x1);
        double y = y1 + ua * (y2 - y1);

        return new Point2D(x, y);
    }

}
