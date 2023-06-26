package com.example.test;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;

public class Update extends AnimationTimer {
    private long lastUpdate;
    ImageProcessingKernel ipk = new ImageProcessingKernel(App.baseFlattenedArray.clone(), (int) App.base.getWidth(), (int) App.base.getHeight());

    @Override
    public void handle (long now) {
        if (now - lastUpdate >= 12_666_666) {
            ipk.reset(App.baseFlattenedArray.clone(), (int) App.base.getWidth(), (int) App.base.getHeight());
            for (Light light : App.lightList) {
                ipk.addOverlay(light.getFlattenedImage(), light.width, light.height, light.getXOffset(), light.getYOffset());
                ipk.execute(light.getRange());
                if (light.mouseControlled) {

                }
                //light.setXOffset(light.getXOffset() - 1);
            }
            ImageView iv = new ImageView(App.arrayToImage(ipk.getImage(), 300, 300));
            App.pane.getChildren().set(1, iv);
            System.out.println(now - lastUpdate);
            lastUpdate = now;
        }
    }
}
