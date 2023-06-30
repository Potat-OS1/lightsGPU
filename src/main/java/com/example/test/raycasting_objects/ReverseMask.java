package com.example.test.raycasting_objects;

import com.aparapi.Kernel;

public class ReverseMask extends Kernel {
    int[] base;
    int width, height;
    int[] overlay;

    public int[] getImage () {
        return base;
    }

    public int getWidth () {
        return width;
    }

    public int getHeight () {
        return height;
    }

    public void assignWorkload (int[] base, int width, int height, int[] overlay) {
        this.base = base;
        this.overlay = overlay;
        this.width = width;
        this.height = height;
        //System.out.println(base.length + " " + overlay.length + "  " + overlayWidth + "  " + overlayHeight + "  " + baseWidth + "  " + baseHeight + "  " + a + "  " + b);
    }

    @Override
    public void run() {
        int gid = getGlobalId();
        int x = gid % width;
        int y = gid / height;

        int v = 4 * (x + (y * width));

        if (overlay[v+1] == 0 && overlay[v+2] == 0 && overlay[v+3] == 0) {
            base[v] = 0;
            base[v+1] = 0;
            base[v+2] = 0;
            base[v+3] = 0;
        }
    }
}
