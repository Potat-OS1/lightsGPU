package com.example.test;

import com.aparapi.Range;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import static com.example.test.App.imageGradientToBlack;

public class Light {
    private WritableImage wi;
    private int[][][] image;
    private int xOffset, yOffset;
    private Range range;

    Light (int width, int height, Color c, int steps, double deadPercent, int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        wi = imageGradientToBlack(c, width, height, steps, deadPercent);
        image = App.imageArray(wi);
        range = Range.create(width*height, 100);
    }

    public Range getRange () {
        return range;
    }

    public int[][][] getImage () {
        return image;
    }

    public void setXOffset (int xOffset) {
        this.xOffset = xOffset;
    }

    public int getXOffset () {
        return xOffset;
    }

    public void setYOffset (int yOffset) {
        this.yOffset = yOffset;
    }

    public int getYOffset () {
        return yOffset;
    }
}
