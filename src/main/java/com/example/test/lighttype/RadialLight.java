package com.example.test.lighttype;

import com.aparapi.Range;
import com.example.test.Light;
import com.example.test.raycasting_objects.Ray;
import javafx.scene.paint.Color;

public class RadialLight extends Light {
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
            //App.rayPane.getChildren().add(rays[a].getRay());
        }
        raysRange = Range.create(rayCount, 1);
    }

    public Ray[] getRays () {
        return rays;
    }

    public Range getRaysRange () {
        return raysRange;
    }
}
