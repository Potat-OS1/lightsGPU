package com.example.test;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Light {
    int x, y, width, height;
    WritableImage lightImage;
    int[] lightImageMatrix;

    Light (Color c, int x, int y, boolean useGradient, int width, int height) {
        this.width = width;
        this.height = height;
        if (useGradient) {
            lightImage = App.imageGradientToBlack(c, width, height, 100, 0.0);
        }
        else {
            lightImage = new WritableImage(width, height);
            for (int a = 0; a < lightImage.getWidth(); a++) {
                for (int b = 0; b < lightImage.getHeight(); b++) {
                    lightImage.getPixelWriter().setColor(a, b, c);
                }
            }
        }
        lightImageMatrix = App.imageArray(lightImage, width, height);
        this.x = x;
        this.y = y;
    }

    public WritableImage getLightImage () {
        return lightImage;
    }

    public int getWidth () {
        return width;
    }

    public int getHeight () {
        return height;
    }

    public int[] getLightImageMatrix () {
        return lightImageMatrix;
    }

    public int getX () {
        return x;
    }

    public int getY () {
        return y;
    }
}
