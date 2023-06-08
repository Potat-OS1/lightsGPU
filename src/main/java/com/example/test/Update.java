package com.example.test;

import com.aparapi.Range;
import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;

public class Update extends AnimationTimer {
    long lastUpdate = 0;
    ImageProcessingKernel ipk;
    Range range;
    @Override
    public void start (){
        super.start();
        // the 1 was necessary on my desktop but not my laptop. for whatever reason the local width is screwy on a NVIDIA 3060, but not integrated intel graphics.
        range = Range.create(150*150, 150);
        ipk = new ImageProcessingKernel(App.baseMatrix);
    }

    @Override
    public void handle(long now) {
        if ((now - lastUpdate) > 24_666_666) {
            for (Light light : App.lightList) {
                ipk.assignWorkload(ipk.results(), light.getLightImageMatrix(), light.getX(), light.getY());
                ipk.execute(range);
            }
            //App.lightPane.getChildren().clear();
            //App.lightPane.getChildren().add(new ImageView(App.arrayToImage(ipk.results())));
            //ipk.setResult(App.baseMatrix);
            System.out.println(now-lastUpdate);
            lastUpdate = now;
        }
    }
}
