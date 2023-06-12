package com.example.test;

import com.aparapi.Range;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Objects;


public class App extends Application {
    int size = 300;
    public static int shadowLevel = 178;
    public static ArrayList<Light> lightList = new ArrayList<>();
    public static Pane lightPane = new Pane();
    public static Light background;

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ImageView iv = new ImageView(new Image(Objects.requireNonNull(App.class.getResourceAsStream("/image.jpg"))));
        iv.setFitHeight(300);
        iv.setFitWidth(300);
        background = new Light(new Color(0.0, 0.0, 0.0, 0.7), 0, 0, false, 300, 300);
        lightList.add(new Light(new Color(0.0, 0.0, 1.0, 1.0), 100, 100, true, 150, 150));
        lightList.add(new Light(new Color(0.0, 1.0, 0.0, 1.0), 100, 100, true, 150, 150));
        lightList.add(new Light(new Color(1.0, 0.0, 0.0, 1.0), 125, 125, true, 150, 150));

        Pane p = new Pane(iv, lightPane);
        AnimationTimer timer = new Update();
        //timer.start();
        int[] temp = background.getLightImageMatrix();
        //temp = stitchImages(background, lightList.get(0));
        temp = stitchArrays(temp, lightList.get(0).getLightImageMatrix(), lightList.get(0).getX(), lightList.get(0).getY(), 300, lightList.get(0).getWidth());
        temp = stitchArrays(temp, lightList.get(1).getLightImageMatrix(), lightList.get(1).getX(), lightList.get(1).getY(), 300, lightList.get(1).getWidth());
        lightPane.getChildren().add(new ImageView(arrayToImage(temp, 300, 300)));
        //lightPane.getChildren().add(new ImageView(arrayToImage(background.getLightImageMatrix(), 300, 300)));

        Scene scene = new Scene(p);
        stage.setScene(scene);
        stage.show();
    }

    private int[] stitchArrays (int[] base, int[] overlay, int a, int b, int baseWidth, int overlayWidth) {
        int x, y, u, v;
        int[] result = base;
        //System.out.println(result.length + "  " + overlay.getLightImageMatrix().length);

        for (int c = 0; c < base.length; c++) {
            y = c % baseWidth;
            x = c / baseWidth;
            u = 4 * (( x + a ) + ( ( y + b ) * baseWidth));
            v = 4 * ( x + ( y * overlayWidth));
//            if ((x < overlay.getX() && x > base.getWidth()) || (y < 100 && y > 300)) {
//                continue;
//            }
            if (x > overlayWidth) {
                continue;
            }
            try {
                if (c < 4) {
                    //System.out.println(u + "  " +v);
                }
                result[u] = Math.max(Math.min(base[u], overlay[v]), shadowLevel);
                result[u+1] = Math.max(base[u+1], overlay[v+1]);
                result[u+2] = Math.max(base[u+2], overlay[v+2]);
                result[u+3] = Math.max(base[u+3], overlay[v+3]);
//                System.out.println(base.lightImageMatrix[u] + "  " + base.lightImageMatrix[u+1] + "  " + base.lightImageMatrix[u+2] + "  " + base.lightImageMatrix[u+3]);
//                System.out.println(overlay.lightImageMatrix[v] + "  " + overlay.lightImageMatrix[v+1] + "  " + overlay.lightImageMatrix[v+2] + "  " + overlay.lightImageMatrix[v+3]);
//                System.out.println(result[u] + "  " + result[u+1] + "  " + result[u+1] + "  " + result[u+1]);

            }
            catch (Exception e) {
                continue;
            }
        }

//        for (int e = 0; e < result.length; e+=4) {
//            System.out.println("color values: Alpha - " + result[e] + "  Red - " + result[e+1] + "  Blue - " + result[e+2] + "  Green - " + result[e+3]);
//        }
        return result;
    }

    private int[] stitchImages (Light base, Light overlay) {
        int x, y, u, v;
        int[] result = new int[base.lightImageMatrix.length];
        //System.out.println(result.length + "  " + overlay.getLightImageMatrix().length);

        for (int c = 0; c < base.lightImageMatrix.length; c++) {
            y = c % base.getWidth();
            x = c / base.getWidth();
            u = 4 * (( x + overlay.getX() ) + ( ( y + overlay.getY() ) * base.getWidth()));
            v = 4 * ( x + ( y * overlay.getWidth()));
//            if ((x < overlay.getX() && x > base.getWidth()) || (y < 100 && y > 300)) {
//                continue;
//            }
            if (x > overlay.getWidth()) {
                continue;
            }
            try {
                if (c < 4) {
                    //System.out.println(u + "  " +v);
                }
                result[u] = Math.max(Math.min(base.lightImageMatrix[u], overlay.lightImageMatrix[v]), shadowLevel);
                result[u+1] = Math.max(base.lightImageMatrix[u+1], overlay.lightImageMatrix[v+1]);
                result[u+2] = Math.max(base.lightImageMatrix[u+2], overlay.lightImageMatrix[v+2]);
                result[u+3] = Math.max(base.lightImageMatrix[u+3], overlay.lightImageMatrix[v+3]);
//                System.out.println(base.lightImageMatrix[u] + "  " + base.lightImageMatrix[u+1] + "  " + base.lightImageMatrix[u+2] + "  " + base.lightImageMatrix[u+3]);
//                System.out.println(overlay.lightImageMatrix[v] + "  " + overlay.lightImageMatrix[v+1] + "  " + overlay.lightImageMatrix[v+2] + "  " + overlay.lightImageMatrix[v+3]);
//                System.out.println(result[u] + "  " + result[u+1] + "  " + result[u+1] + "  " + result[u+1]);

            }
            catch (Exception e) {
                continue;
            }
        }

//        for (int e = 0; e < result.length; e+=4) {
//            System.out.println("color values: Alpha - " + result[e] + "  Red - " + result[e+1] + "  Blue - " + result[e+2] + "  Green - " + result[e+3]);
//        }
        return result;
    }

    private WritableImage createImage (Color c, int width, int height) {
        WritableImage wi = new WritableImage(width, height);
        for (int a = 0; a < width; a++) {
            for (int b = 0; b < height; b++) {
                wi.getPixelWriter().setColor(a, b, c);
            }
        }
        return wi;
    }

    public static WritableImage imageGradientToBlack (Color c, int width, int height, int steps, double deadPercent) {
        int endSteps = (int) (steps / 4.0);
        int deadSteps = (int) (steps * deadPercent);
        int normalSteps = steps - endSteps - deadSteps;

        double deltaR = (c.getRed()) / (normalSteps);
        double deltaG = (c.getGreen()) / (normalSteps);
        double deltaB = (c.getBlue()) / (normalSteps);
        double centerX = Math.ceil(width/2.0);
        double centerY = Math.ceil(height/2.0);

        double currentLength;

        double stepSize = Math.sqrt(Math.pow(centerX - width, 2) + Math.pow(centerY - height, 2)) / (steps * 1.1);
        int currentStep;

        WritableImage wi = new WritableImage(width, height);
        PixelWriter pw = wi.getPixelWriter();

        double red;
        double blue;
        double green;
        double alpha;

        for (int x = 0; x < wi.getWidth(); x++) {
            for (int y = 0; y < wi.getHeight(); y++) {
                currentLength = Math.sqrt(Math.pow(centerX - (x+1), 2) + Math.pow(centerY - (y+1), 2));
                currentStep = (int) Math.floor(currentLength / stepSize);

                if (currentStep > normalSteps + deadSteps) {
                    //pw.setColor(x, y, Color.BLACK);
                    continue;
                }
                if (currentStep < deadSteps) {
                    pw.setColor(x, y, Color.TRANSPARENT);
                }
                else {
                    currentStep = currentStep - deadSteps;

                    red = Math.max((c.getRed() - (deltaR * currentStep)), 0);
                    blue = Math.max((c.getBlue() - (deltaB * currentStep)), 0);
                    green = Math.max((c.getGreen() - (deltaG * currentStep)), 0);
                    alpha = 0.15 + ((0.85/normalSteps) * currentStep);

                    if (red < 0) {
                        red = 0;
                    }
                    if (blue < 0) {
                        blue = 0;
                    }
                    if (green < 0) {
                        green = 0;
                    }

                    pw.setColor(x, y, new Color(red, green, blue, alpha));
                }

            }
        }
        return wi;
    }

    public static Image arrayToImage (int[] info, int height, int width) {
        WritableImage wi = new WritableImage(width, height);
        int b = 0;
        int u;
        for (int x = 0; x < width; x++) {
            //System.out.println(x + " color values: Alpha - " + info[b] + "  Red - " + info[b+1] + "  Green - " + info[b+2] + "  Blue - " + + info[b+3]);
            for (int y = 0; y < height; y++) {
                wi.getPixelWriter().setArgb(x, y, (info[b]<<24) | (info[b+1]<<16) | (info[b+2]<<8) | info[b+3]);
                //System.out.println(x + "  " + y + " color values: Alpha - " + info[b] + "  Red - " + info[b+1] + "  Green - " + info[b+2] + "  Blue - " + info[b+3]);
                b+=4;
            }
        }
        return wi;
    }

    public static int[] imageArray(WritableImage wi, int width, int height) {
        int[] pixelArray = new int[(int) (wi.getWidth() * wi.getHeight() * 4)];
        int c = 0;
        int d = 0;
        //System.out.println(pixelArray.length);
        for (int a = 0; a < (width * height); a++) {
            c = wi.getPixelReader().getArgb(a/width, a%height);
            pixelArray[d] = ((c >> 24) & 0xff);
            pixelArray[d+1] = ((c >> 16) & 0xff);
            pixelArray[d+2] = ((c >> 8) & 0xff);
            pixelArray[d+3] = (c & 0xff);
            d+=4;

            //System.out.println(pixelArray[a]);
        }
//        int[][][] pixelArray = new int[(int) wi.getWidth()][(int) wi.getHeight()][4];
//        int c;
//        for (int a = 0; a < wi.getWidth(); a++) {
//            for (int b = 0; b < wi.getHeight(); b++) {
//                c = wi.getPixelReader().getArgb(a, b);
//                pixelArray[a][b][0] = ((c >> 24) & 0xff);
//                pixelArray[a][b][1] = ((c >> 16) & 0xff);
//                pixelArray[a][b][2] = ((c >> 8) & 0xff);
//                pixelArray[a][b][3] = (c & 0xff);
//            }
//        }
//        for (int b = 0; b < pixelArray.length; b+=4) {
//            System.out.println(pixelArray[b] + "  " + pixelArray[b+1] + "  " + pixelArray[b+2] + "  " + pixelArray[b+3]);
//        }
        return pixelArray;
    }
}
