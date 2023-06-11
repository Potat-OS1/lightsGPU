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
        // the 1500 was necessary on my desktop but not my laptop. for whatever reason the local width is screwy on a NVIDIA 3060, but not integrated intel graphics.
        range = Range.create(150*150, 150);
        ipk = new ImageProcessingKernel(App.background.getLightImageMatrix(), App.background.getWidth());
    }

    @Override
    public void handle(long now) {
        if ((now - lastUpdate) > 1_666_666_666) {
            for (Light light : App.lightList) {
                ipk.assignWorkload(App.background, light);
                ipk.execute(range);
            }
            App.lightPane.getChildren().clear();
            App.lightPane.getChildren().add(new ImageView(App.arrayToImage(ipk.results(), App.background.getWidth(), App.background.getHeight())));
            ipk.setResult(App.background.lightImageMatrix);
            System.out.println(now-lastUpdate);
            lastUpdate = now;
        }
    }
}
