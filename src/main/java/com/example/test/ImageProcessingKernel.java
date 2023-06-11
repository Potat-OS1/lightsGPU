package com.example.test;

import com.aparapi.Kernel;

public class ImageProcessingKernel extends Kernel {
    int[] base;
    int[] overlay;
    int[] result;
    int x, y;
    int baseWidth;
    int baseHeight;
    int overlayWidth;
    int overlayHeight;
    int shadowLevel;

    ImageProcessingKernel (int[] base, int width) {
        this.result = base;
        this.baseWidth = width;
        this.baseHeight = base.length/width;
    }

    //this is to be used in the update loop in this project to "reset" without created a new object of this kernel. hopefully.
    public void setResult (int[] input) {
        this.result = input;
    }

    public void assignWorkload (Light base, Light overlay) {
        this.base = base.lightImageMatrix;
        this.overlay = overlay.lightImageMatrix;
        this.baseWidth = base.getWidth();
        this.baseHeight = base.getHeight();
        this.x = overlay.getX();
        this.y = overlay.getY();
        this.overlayWidth = overlay.getWidth();
        this.overlayHeight = overlay.getHeight();
    }

    public int[] results () {
        return result;
    }

    @Override
    public void run() {
        int gid = getGlobalId();
        int a = gid % baseWidth;
        int b = gid / baseHeight;
        if (a < x && a > baseWidth) {
            return;
        }
        if (b < y && b > baseHeight) {
            return;
        }
        //System.out.println(base[a+(b*baseWidth)] + " " + overlay[a+(b*overlayWidth)]);
        //result[a+(b*baseWidth)] = Math.max(base[a+(b*baseWidth)], overlay[a+(b*overlayWidth)]);
        result[(x+(y*baseWidth))+(a+(b*baseWidth))] = Math.max();
//        x or y should not be more than the bounds of the screen.
//        if (gid % 4 == 0) {
//            System.out.println("oi");
//            result[gid] = Math.min(Math.max(base[gid], overlay[gid]), shadowLevel);
//        }
//        else {
//            result[gid] = Math.max(base[gid], overlay[gid]);
//        }


//        if ((a + x) >= base.length || (b + y) >= base[0].length) {
//            return;
//        }
//        if ((a + x) <= 0 || (b + y) <= 0) {
//            return;
//        }
//        if (gid % 4 == 0) {
//            System.out.println("oi");
//            result[gid] = Math.min(Math.max(base[gid], overlay[gid]), shadowLevel);
//        }
//        else {
//            result[gid] = Math.max(base[gid], overlay[gid]);
//        }

//        result[a+x][b+y][0] = Math.min(Math.max(base[a+x][b+y][0], overlay[a][b][0]), shadowLevel);
//        result[a+x][b+y][1] = Math.max(base[a+x][b+y][1], overlay[a][b][1]);
//        result[a+x][b+y][2] = Math.max(base[a+x][b+y][2], overlay[a][b][2]);
//        result[a+x][b+y][3] = Math.max(base[a+x][b+y][3], overlay[a][b][3]);
    }
}
