package com.example.test;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Light {
    int x, y;
    WritableImage lightImage;
    int[][][] lightImageMatrix;

    Light (Color c, int x, int y) {
        lightImage = App.imageGradientToBlack(c, 150, 150, 100, 0.0);
        lightImageMatrix = App.imageArray(lightImage);
        this.x = x;
        this.y = y;
    }

    public WritableImage getLightImage () {
        return lightImage;
    }

    public int[][][] getLightImageMatrix () {
        return lightImageMatrix;
    }

    public int getX () {
        return x;
    }

    public int getY () {
        return y;
    }
}
