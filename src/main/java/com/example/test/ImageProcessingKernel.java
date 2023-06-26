package com.example.test;

import com.aparapi.Kernel;

public class ImageProcessingKernel extends Kernel {
    int[] base;
    int baseWidth, baseHeight;
    int[] overlay;
    int overlayWidth, overlayHeight;
    int a, b;
    int shadowLevel;

    ImageProcessingKernel (int[] base, int baseWidth, int baseHeight) {
        this.base = base;
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
        this.shadowLevel = App.shadowLevel;
    }

    public int[] getImage () {
        return base;
    }

    public void addOverlay (int[] overlay, int overlayWidth, int overlayHeight, int xOffset, int yOffset) {
        this.overlay = overlay;
        this.overlayWidth = overlayWidth;
        this.overlayHeight = overlayHeight;
        a = xOffset;
        b = yOffset;
    }

    public void reset (int[] base, int baseWidth, int baseHeight) {
        this.base = base;
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
    }


    @Override
    public void run() {
        int gid = getGlobalId();
        int x = gid % overlayWidth;
        int y = gid / overlayHeight;

        //x or y should not be more than the bounds of the screen.
        if ((a + x) >= baseWidth || (b + y) >= baseHeight) {
            return;
        }
        if ((a + x) <= 0 || (b + y) <= 0) {
            return;
        }

        int u = 4 * ((x+a) + ((y+b) * baseWidth));
        int v = 4 * (x + (y * overlayWidth));

        base[u] = Math.min(Math.max(base[u], overlay[v]), shadowLevel);
        for (int z = 1; z < 4; z++) {
            base[u+z] = Math.max(base[u+z], overlay[v+z]);
        }
    }
}
