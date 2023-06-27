package com.example.test.raycasting_objects;

import com.aparapi.Kernel;
import com.example.test.App;
import com.example.test.Obstacle;
import com.example.test.Tools;
import javafx.geometry.Point2D;

import java.util.ArrayList;

public class RaycastRadialKernel extends Kernel {
    Ray[] rayList;
    int originX, originY;
    ArrayList<Point2D> lightPoints = new ArrayList<>();

    public void assignWorkload (Ray[] rayList, int xOffset, int yOffset) {
        lightPoints.clear();
        this.rayList = rayList;
        originX = xOffset;
        originY = yOffset;
    }

    public ArrayList<Point2D> returnPoints () {
        return lightPoints;
    }

    @Override
    public void run () {
        int gid = getGlobalId();
        rayList[gid].updateRayOrigin(originX, originY);
        double[] points = new double[2];

        for (Obstacle ob : App.objList) {
            points[0] = 0;
            points[1] = 0;
            Point2D point = Tools.getShortestIntersection(rayList[gid].getRay(), ob.getDetectionLines());
            if (point != null) {
                points[0] = point.getX();
                points[1] = point.getY();
                rayList[gid].updateRayEndPoints(point.getX(), point.getY());
            }
            else {
                points[0] = rayList[gid].getRay().getEndX();
                points[1] = rayList[gid].getRay().getEndY();
            }
        }
        lightPoints.add(new Point2D(points[0], points[1]));
    }
}
