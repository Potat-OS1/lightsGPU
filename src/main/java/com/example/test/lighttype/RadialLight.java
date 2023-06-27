package com.example.test.lighttype;

import com.aparapi.Range;
import com.example.test.App;
import com.example.test.Light;
import com.example.test.Obstacle;
import com.example.test.Tools;
import com.example.test.raycasting_objects.Ray;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class RadialLight extends Light {
    double[] points = new double[2];
    double x1, y1;
    int rayCount;
    int strength;
    Ray[] rays;
    Range raysRange;

    public RadialLight(int width, int height, Color c, int steps, double deadPercent, int xOffset, int yOffset, boolean mouseControlled, int rayCount, int strength) {
        super(width, height, c, steps, deadPercent, xOffset, yOffset, mouseControlled);
        this.rayCount = rayCount;
        this.strength = strength;
        rays = new Ray[rayCount];
        for (int a = 0; a < rayCount; a++) {
            rays[a] = new Ray(xOffset+(width/2), yOffset+(height/2), (360.0/rayCount)*a, strength);
            App.rayPane.getChildren().add(rays[a].getRay());
        }
        raysRange = Range.create(rayCount, 1);
    }

    public Ray[] getRays () {
        return rays;
    }

    public Range getRaysRange () {
        return raysRange;
    }

    @Override
    public ArrayList<Point2D> updateRays () {
        ArrayList<Point2D> lightPoints = new ArrayList<>();
        if (mouseControlled) {
            x1 = App.mouseX;
            y1 = App.mouseY;
        }
        double x2, y2;
        for (Ray ray : rays) {
            ray.updateRayOrigin(x1, y1);
            for (Obstacle ob : App.objList) {
                points[0] = 0;
                points[1] = 1;
                Point2D point = Tools.getShortestIntersection(ray.getRay(), ob.getDetectionLines());
                if (point != null) {
                    points[0] = point.getX();
                    points[1] = point.getY();
                    ray.updateRayEndPoints(point.getX(), point.getY());
                }
                else {
                    points[0] = ray.getRay().getEndX();
                    points[1] = ray.getRay().getEndY();
                }
            }
            lightPoints.add(new Point2D(points[0], points[1]));
        }
        return lightPoints;
    }
}
