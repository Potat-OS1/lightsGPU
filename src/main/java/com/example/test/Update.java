package com.example.test;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;

public class Update extends AnimationTimer {
    private long lastUpdate;
    int a = 0;
    ImageProcessingKernel ipk = new ImageProcessingKernel(App.shadowLevel);

    @Override
    public void handle (long now) {
        if (now - lastUpdate >= 8_666_666) {
            ipk.setResult(App.imageArray(App.base));
            for (Light light : App.lightList) {
                ipk.assignWorkload(ipk.results(), light.getImage(), light.getXOffset(), light.getYOffset());
                ipk.execute(light.getRange());
                //light.setXOffset(light.getXOffset()-1);
            }
            ((ImageView) App.pane.getChildren().get(1)).setImage(App.arrayToImage(ipk.results()));
            System.out.println(now - lastUpdate);
            lastUpdate = now;
            a++;
            if (a > 200) {
                System.gc();
                a = 0;
            }
        }
    }
}
