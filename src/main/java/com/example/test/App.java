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
        lightList.add(new Light(new Color(0.0, 1.0, 0.0, 1.0), 150, 150, true, 150, 150));
        lightList.add(new Light(new Color(1.0, 0.0, 0.0, 1.0), 125, 125, true, 150, 150));

        Pane p = new Pane(iv, lightPane);
        AnimationTimer timer = new Update();
        timer.start();

        Scene scene = new Scene(p);
        stage.setScene(scene);
        stage.show();
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
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                wi.getPixelWriter().setArgb(x, y, (info[(b)]<<24) | (info[(b)+1]<<16) | (info[(b)+2]<<8) | info[(b)+3]);
                //System.out.println(x + "  " + y + " color values: Alpha - " + info[b] + "  Red - " + info[b+1] + "  Blue - " + info[b+2] + "  Green - " + + info[b+3]);
            }
            b+=4;
        }
        return wi;
    }

    public static int[] imageArray(WritableImage wi, int width, int height) {
        int[] pixelArray = new int[(int) (wi.getWidth() * wi.getHeight())];
        int c = 0;
        System.out.println(pixelArray.length);
        for (int a = 0; a < pixelArray.length; a+=4) {
            c = wi.getPixelReader().getArgb(a/width, a%height);
            pixelArray[a] = ((c >> 24) & 0xff);
            pixelArray[a+1] = ((c >> 16) & 0xff);
            pixelArray[a+2] = ((c >> 8) & 0xff);
            pixelArray[a+3] = (c & 0xff);
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
        return pixelArray;
    }
}
