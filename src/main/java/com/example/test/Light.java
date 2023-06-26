package com.example.test;

import com.aparapi.Range;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import static com.example.test.App.imageGradientToBlack;

public class Light {
    private WritableImage wi;
    private int[][][] image;
    private int[] flattenedImage;
    private int xOffset, yOffset;
    private Range range;
    public int width, height;
    boolean mouseControlled;

    Light (int width, int height, Color c, int steps, double deadPercent, int xOffset, int yOffset, boolean mouseControlled) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.width = width;
        this.height = height;
        wi = imageGradientToBlack(c, width, height, steps, deadPercent);
        image = App.imageArray(wi);
        range = Range.create(width*height, 100);
        flattenedImage = App.flattenArray(image, width, height, 4);
        this.mouseControlled = mouseControlled;
    }

    public int[] getFlattenedImage () {
        return flattenedImage;
    }

    public Range getRange () {
        return range;
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
