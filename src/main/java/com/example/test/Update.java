package com.example.test;

import com.example.test.lighttype.RadialLight;
import com.example.test.raycasting_objects.RaycastRadialKernel;
import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Update extends AnimationTimer {
    private long lastUpdate;
    ImageProcessingKernel ipk = new ImageProcessingKernel(App.baseFlattenedArray.clone(), (int) App.base.getWidth(), (int) App.base.getHeight());
    RaycastRadialKernel rrk = new RaycastRadialKernel();
    int a = 0;

    @Override
    public void handle (long now) {
        if (now - lastUpdate >= 16_666_666) {
            App.lightPane.getChildren().clear();

            ipk.reset(App.baseFlattenedArray.clone(), (int) App.base.getWidth(), (int) App.base.getHeight());
            for (Light light : App.lightList) {
                ipk.addOverlay(light.getFlattenedImage(), light.width, light.height, light.getXOffset(), light.getYOffset());
                ipk.execute(light.getRange());
                if (light.mouseControlled) {
                    light.setXOffset(App.mouseX-(light.width/2));
                    light.setYOffset(App.mouseY-(light.height/2));
                    //light.updateRays();
                    if (light instanceof RadialLight) {
                        rrk.assignWorkload(((RadialLight) light).getRays(), App.mouseX, App.mouseY);
                        rrk.execute(((RadialLight) light).getRaysRange());

                        App.lightPane.getChildren().add(Tools.arrayListToPolygon(rrk.returnPoints(), Color.WHITE));
                    }
                }

            }
            ImageView iv = new ImageView(App.arrayToImage(ipk.getImage(), App.size, App.size));
            App.pane.getChildren().set(1, iv);

            a++;
            if (a > 3600) {
                a = 0;
                System.gc();
            }

            System.out.println(now - lastUpdate);
            lastUpdate = now;
        }
    }
}
