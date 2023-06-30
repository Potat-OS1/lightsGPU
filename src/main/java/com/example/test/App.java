package com.example.test;

import com.example.test.lighttype.RadialLight;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;


public class App extends Application {
    public static int size = 900;
    public static int shadowLevel = 118;
    public static ArrayList<Light> lightList = new ArrayList<>();
    public static int[][][] baseArray;
    public static int[] baseFlattenedArray;
    public static WritableImage base;
    public static Pane pane = new Pane();
    public static int mouseX, mouseY;
    public static Pane rayPane = new Pane();
    public static Pane lightPane = new Pane();
    public static ArrayList<Obstacle> objList = new ArrayList<>();

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ImageView iv = new ImageView(new Image(Objects.requireNonNull(App.class.getResourceAsStream("/image.jpg"))));
        iv.setFitHeight(size);
        iv.setFitWidth(size);
        pane.getChildren().add(iv);
        ImageView lights = new ImageView();
        lights.setFitHeight(size);
        lights.setFitWidth(size);
        pane.getChildren().addAll(lights, rayPane, lightPane);

        base = createImage(new Color(0.0, 0.0, 0.0, shadowLevel/255.0), size, size);
        baseArray = imageArray(base);
        baseFlattenedArray = flattenArray(baseArray, size, size, 4);

        lightList.add(new RadialLight(300, 300, new Color(0.0, 0.0, 1.0, 1.0), 100, 0, 100, 100, true, 100, 150));
        lightList.add(new RadialLight(300, 300, new Color(0.0, 1.0, 0.0, 1.0), 100, 0, 0, 0, false, 72, 150));
        lightList.add(new RadialLight(300, 300, new Color(1.0, 0.0, 0.0, 1.0), 100, 0, 0, 200, false, 72, 150));
        //lightList.add(new Light(150, 150, new Color(1.0, 0.0, 0.0, 1.0), 100, 0, 200, 100, false));
        AnimationTimer timer = new Update();
        timer.start();

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();

        obstacles(rayPane);
        mouseTracking(scene);
    }

    public static Image arrayToImage (int[] info, int height, int width) {
        WritableImage wi = new WritableImage(width, height);
        int b = 0;
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

    public static int[] flattenArray(int[][][] arr, int width, int height, int depth) {
        int flattenedSize = width * height * depth;
        int[] flattenedArray = new int[flattenedSize];
        int index = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < depth; k++) {
                    flattenedArray[index] = arr[i][j][k];
                    index++;
                }
            }
        }

        return flattenedArray;
    }

    public static WritableImage createImage (Color c, int width, int height) {
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

    public static int[][][] imageArray(WritableImage wi) {
        int[][][] pixelArray = new int[(int) wi.getWidth()][(int) wi.getHeight()][4];
        int c;
        for (int a = 0; a < wi.getWidth(); a++) {
            for (int b = 0; b < wi.getHeight(); b++) {
                c = wi.getPixelReader().getArgb(a, b);
                pixelArray[a][b][0] = ((c >> 24) & 0xff);
                pixelArray[a][b][1] = ((c >> 16) & 0xff);
                pixelArray[a][b][2] = ((c >> 8) & 0xff);
                pixelArray[a][b][3] = (c & 0xff);
            }
        }
        return pixelArray;
    }

    public static int[] image1DArray(WritableImage wi, int width, int height) {
        int[] pixelArray = new int[(int) (width*height*4)];
        int c;
        int d = (int) Math.ceil((width - wi.getWidth())/2.0);
        int e = (int) Math.ceil((height - wi.getHeight())/2.0);
        int index = 0;
        for (int a = 0; a < width; a++) {
            for (int b = 0; b < height; b++) {
                try {
                    c = wi.getPixelReader().getArgb(a-d, b-e);
                    pixelArray[index] = ((c >> 24) & 0xff);
                    pixelArray[index+1] = ((c >> 16) & 0xff);
                    pixelArray[index+2] = ((c >> 8) & 0xff);
                    pixelArray[index+3] = (c & 0xff);
                }
                catch (Exception ex) {
                    pixelArray[index] = 256;
                    pixelArray[index+1] = 0;
                    pixelArray[index+2] = 0;
                    pixelArray[index+3] = 0;
                }

                index+=4;
            }
        }
        return pixelArray;
    }

    private void mouseTracking (Scene scene) {
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            mouseX = (int) e.getX();
            mouseY = (int) e.getY();
        });
    }

    private void obstacles (Pane p) {
        Point2D[] points1 = {
                new Point2D(100,200),
                new Point2D(150, 250),
                new Point2D(200,200),
                new Point2D(200,100),
                new Point2D(100,100),
        };
        Obstacle obs1 = new Obstacle(points1, Color.RED);
        objList.add(obs1);
        obs1.getObs().setOpacity(0.1);
        p.getChildren().add(obs1.getObs());

        Point2D[] points2 = {
                new Point2D(300,400),
                new Point2D(350, 450),
                new Point2D(400,400),
                new Point2D(400,300),
                new Point2D(300,300),
        };
        Obstacle obs2 = new Obstacle(points2, Color.RED);
        objList.add(obs2);
        obs2.getObs().setOpacity(0.1);
        p.getChildren().add(obs2.getObs());

        Point2D[] points3 = {
                new Point2D(500, 800),
                new Point2D(500, 0),
                new Point2D(550, 0),
                new Point2D(550, 800),
        };
        Obstacle obs3 = new Obstacle(points3, Color.RED);
        objList.add(obs3);
        obs3.getObs().setOpacity(0.1);
        p.getChildren().add(obs3.getObs());
    }
}
