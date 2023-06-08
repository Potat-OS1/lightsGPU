package com.example.test;

import com.aparapi.Kernel;

public class ImageProcessingKernel extends Kernel {
    int[][][] base;
    int[][][] overlay;
    int[][][] result;
    int x, y;
    int maxX, maxY;
    int shadowLevel;

    ImageProcessingKernel (int[][][] base, int[][][] overlay, int x, int y) {
        this.base = base;
        this.overlay = overlay;
        maxX = overlay.length;
        maxY = overlay[0].length;
        result = base;
        this.x = x;
        this.y = y;
        shadowLevel = App.shadowLevel;
    }

    public void assignWorkload (int[][][] base, int[][][] overlay, int x, int y) {
        this.base = base;
        this.overlay = overlay;
        maxX = overlay.length;
        maxY = overlay[0].length;
        result = base;
        this.x = x;
        this.y = y;
    }

    public int[][][] results () {
        return result;
    }

    @Override
    public void run() {
        int gid = getGlobalId();
        //this system.out is apparently loadbearing.
        //System.out.println();
        int a = gid % maxX;
        int b = gid / maxY;

        //x or y should not be more than the bounds of the screen.
        if ((a + x) >= base.length || (b + y) >= base[0].length) {
            return;
        }
        if ((a + x) <= 0 || (b + y) <= 0) {
            return;
        }
        result[a+x][b+y][0] = Math.min(Math.max(base[a+x][b+y][0], overlay[a][b][0]), shadowLevel);
        result[a+x][b+y][1] = Math.max(base[a+x][b+y][1], overlay[a][b][1]);
        result[a+x][b+y][2] = Math.max(base[a+x][b+y][2], overlay[a][b][2]);
        result[a+x][b+y][3] = Math.max(base[a+x][b+y][3], overlay[a][b][3]);
    }
}
