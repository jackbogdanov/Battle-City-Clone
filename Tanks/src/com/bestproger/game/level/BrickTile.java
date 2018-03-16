package com.bestproger.game.level;

import com.bestproger.game.mob.Entity;
import com.bestproger.game.Game;

import java.awt.*;
import java.awt.image.BufferedImage;


public class BrickTile extends Tile{

    private Level level;
    private int keyI;
    private int keyJ;
    private int[][] status;

    protected BrickTile(BufferedImage image, int scale, TileType type, Level level, int keyI, int keyJ) {
        super(image, scale, type);

        this.keyI = keyI;
        this.keyJ = keyJ;
        this.level = level;
        status = new int[2][2];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                status[i][j] = 1;
            }
        }
    }

    public void bulletCollapse(){
        Graphics g =  image.getGraphics();
        g.setColor(Color.black);


        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (status[i][j] == 0) {
                    g.fillRect(i * image.getWidth() / 2, j * image.getHeight() / 2, (i + 1) * image.getWidth() / 2, (j + 1) * image.getHeight() / 2);
                }
            }
        }

        checkStatus();
    }

    public boolean isCollapseBullet(Entity.Heading heading, int x, int y){

        x = x - (x / (Level.TILE_SCALE*2*(int)Game.SCALE))*(Level.TILE_SCALE*2*(int)Game.SCALE);
        x = x - (x / (Level.TILE_SCALE*(int)Game.SCALE))*(Level.TILE_SCALE*(int)Game.SCALE);
        y = y - y / (Level.TILE_SCALE*2*(int)Game.SCALE)*(Level.TILE_SCALE*2*(int)Game.SCALE);
        y = y - y / (Level.TILE_SCALE*(int)Game.SCALE)*(Level.TILE_SCALE*(int)Game.SCALE);
        boolean isSolid = false;

        int checkIndex;

        if (heading == Entity.Heading.NORTH || heading == Entity.Heading.SOUTH){
            checkIndex = solidCheckJ(x, y, heading);
            if (checkIndex != -1){
                status[0][checkIndex] = 0;
                status[1][checkIndex] = 0;
                isSolid = true;
            }
        }

        if (heading == Entity.Heading.EAST || heading == Entity.Heading.WEST){
            checkIndex = solidCheckI(x, y, heading);
            if (checkIndex != -1){
                status[checkIndex][0] = 0;
                status[checkIndex][1] = 0;
                isSolid = true;
            }
        }

        bulletCollapse();
        return isSolid;
    }


    private int solidCheckJ(int x, int y, Entity.Heading heading) {
        int checkIndex = -1;
        boolean needCheck = true;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (status[i][j] == 1) {
                    if (x <= (i + 1) * image.getWidth() / 2 && x >= i * image.getWidth() / 2
                            && y <= (j + 1) * image.getHeight() / 2 && y >= j * image.getHeight() / 2) {
                        checkIndex = j;
                        needCheck = false;
                    }
                } else {
                    if (heading == Entity.Heading.SOUTH && needCheck){
                        if (i == 1 && status[i - 1][j] == 1) {
                            checkIndex = j;
                            status[i][j] = 0;
                            needCheck = false;
                        }

                        if (i == 0 && status[i + 1][j] == 1){
                            checkIndex = j;
                            status[i][j] = 0;
                            needCheck = false;
                        }
                    }

                    if (heading == Entity.Heading.NORTH){
                        if (i == 1 && status[i - 1][j] == 1){
                            checkIndex = j;
                            status[i][j] = 0;
                        }

                        if (i == 0 && status[i + 1][j] == 1){
                            checkIndex = j;
                            status[i][j] = 0;
                        }

                    }
                }
            }
        }
        return checkIndex;
    }

    private int solidCheckI(int x, int y, Entity.Heading heading) {
        int checkIndex = -1;
        boolean needCheck = true;

        for (int i = 0; i < 2; i++) {
           for (int j = 0; j < 2; j++) {

                if (status[i][j] == 1) {
                    if (x <= (i + 1) * image.getWidth() / 2 && x >= i * image.getWidth() / 2
                            && y <= (j + 1) * image.getHeight() / 2 && y >= j * image.getHeight() / 2) {
                        checkIndex = i;
                        needCheck = false;
                    }
                } else {
                    if (heading == Entity.Heading.EAST && needCheck){
                        if (j == 1 && status[i][j - 1] == 1) {
                            checkIndex = i;
                            status[i][j] = 0;
                            needCheck = false;
                        }

                        if (j == 0 && status[i][j + 1] == 1){
                            checkIndex = i;
                            status[i][j] = 0;
                            needCheck = false;
                        }
                    }

                    if (heading == Entity.Heading.WEST){
                        if (j == 1 && status[i][j - 1] == 1){
                            checkIndex = i;
                            status[i][j] = 0;
                        }

                        if (j == 0 && status[i][j + 1] == 1){
                            checkIndex = i;
                            status[i][j] = 0;
                        }

                    }
                }
            }
        }

        return checkIndex;
    }

    public boolean isCollapseMob(int x, int y){
        x = x - (x / (Level.TILE_SCALE*2*Game.SCALE))*(Level.TILE_SCALE*2*Game.SCALE);
        x = x - (x / (Level.TILE_SCALE*Game.SCALE))*(Level.TILE_SCALE*Game.SCALE);
        y = y - y / (Level.TILE_SCALE*2*Game.SCALE)*(Level.TILE_SCALE*2*Game.SCALE);
        y = y - y / (Level.TILE_SCALE*Game.SCALE)*(Level.TILE_SCALE*Game.SCALE);
        boolean isSolid = false;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (status[i][j] == 1) {
                    if (x <= (i + 1) * image.getWidth() / 2 && x >= i * image.getWidth() / 2
                            && y <= (j + 1) * image.getHeight() / 2 && y >= j * image.getHeight() / 2) {
                        isSolid = true;
                    }
                }
            }
        }

        return isSolid;
    }

    public static BrickTile getBrick(int[] xyCoords, Level level){
        int i = level.getTileMasX(xyCoords[0]);
        int j = level.getTileMasY(xyCoords[1]);
        return level.getBrickTile(i, j);
    }


    private void checkStatus(){
        boolean needDestroy = true;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (status[i][j] == 1) needDestroy = false;
            }
        }

        if (needDestroy){
            level.removeTile(keyI, keyJ);
        }
    }

}
