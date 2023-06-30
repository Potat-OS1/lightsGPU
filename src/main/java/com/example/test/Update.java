package com.example.test;

import com.aparapi.Range;
import com.example.test.lighttype.RadialLight;
import com.example.test.raycasting_objects.RaycastRadialKernel;
import com.example.test.raycasting_objects.ReverseMask;
import javafx.animation.AnimationTimer;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Update extends AnimationTimer {
    private long lastUpdate;
    ImageProcessingKernel imageProcessor = new ImageProcessingKernel(App.baseFlattenedArray.clone(), (int) App.base.getWidth(), (int) App.base.getHeight());
    RaycastRadialKernel radialRaycast = new RaycastRadialKernel();
    ReverseMask reverseMask = new ReverseMask();
    int[] temp;
    Shape tempShape;
    SnapshotParameters sp = new SnapshotParameters();
    int a = 0;

    @Override
    public void handle (long now) {
        if (now - lastUpdate >= 32_666_666) {
            //App.lightPane.getChildren().clear();

            imageProcessor.reset(App.baseFlattenedArray.clone(), (int) App.base.getWidth(), (int) App.base.getHeight());
            for (Light light : App.lightList) {
                if (light.mouseControlled) {
                    light.setXOffset(App.mouseX-(light.width/2));
                    light.setYOffset(App.mouseY-(light.height/2));
                    if (light instanceof RadialLight) {
                        updateRadial((RadialLight) light, App.mouseX, App.mouseY);
                    }
                }
                else {
                    light.setXOffset(light.getXOffset() + 1);
                    updateRadial((RadialLight) light, light.getXOffset()+(light.width/2), light.getYOffset()+(light.height/2));
                }
                reverseMask.assignWorkload(light.getFlattenedImage().clone(), light.width, light.height, temp);
                reverseMask.execute(Range.create(light.width * light.height, 1));
                imageProcessor.addOverlay(reverseMask.getImage(), reverseMask.getWidth(), reverseMask.getHeight(), light.getXOffset(), light.getYOffset());
                imageProcessor.execute(light.getRange());
            }
            ImageView iv = new ImageView(App.arrayToImage(imageProcessor.getImage(), App.size, App.size));
            App.pane.getChildren().set(1, iv);

            a++;
            if (a > 120) {
                a = 0;
                System.gc();
            }

            System.out.println(now - lastUpdate);
            lastUpdate = now;
        }
    }

    private void updateRadial (RadialLight light, int x, int y) {
        radialRaycast.assignWorkload(light.getRays(), x, y);
        radialRaycast.execute(light.getRaysRange());

        tempShape = Tools.arrayListToPolygon(radialRaycast.returnPoints(), Color.WHITE);

        //App.lightPane.getChildren().add(tempShape);
        Shape shape = new Rectangle(light.width, light.height);
        shape.setLayoutX(light.getXOffset());
        shape.setLayoutY(light.getYOffset());
        temp = App.image1DArray(shape.subtract(shape, tempShape).snapshot(sp, null), light.width, light.height);
        //App.lightPane.getChildren().add(new ImageView(App.arrayToImage(temp, light.width, light.height)));
        //App.lightPane.getChildren().add(new ImageView((shape.subtract(shape, tempShape).snapshot(sp, null))));
    }
}
