package com.bestproger.game;

import com.bestproger.game.level.BrickTile;
import com.bestproger.game.level.Level;
import com.bestproger.game.level.TileType;
import com.bestproger.game.mob.Entity;
import com.bestproger.game.mob.EntityType;
import com.bestproger.graphics.TextureAtlas;

import java.awt.*;

import static com.bestproger.game.Game.SCALE;
import static com.bestproger.game.Game.SPRITE_SCALE;

public abstract class Mob extends Entity {


    public Mob(EntityType type, float x, float y, float scale, Level level, TextureAtlas atlas, BulletManager bulletManager) {
        super(type, x, y, scale, level, atlas, bulletManager);
    }


    public abstract void render(Graphics2D g);

    protected boolean collision(int x, int y){
        boolean solid = false;
        int[] xyCoords;
        TileType tileType;

        for (int c = 0; c < 16; c++) {
            xyCoords = getCollusionDotCoords(x, y, c);
            tileType = level.getTile(xyCoords[0], xyCoords[1]);

            if (tileType == TileType.BRICK){
                BrickTile brick = BrickTile.getBrick(xyCoords, level);

                if (!solid){
                    solid = brick.isCollapseMob(xyCoords[0], xyCoords[1]);
                }
            } else if (tileType.isSolid()) {
                solid = true;
                break;
            }
        }


        return solid;
    }

    protected int[] getCollusionDotCoords(int x, int y, int index){
        int[] xyCollusionDot = new int[2];
        xyCollusionDot[0] = x + index % 2*41 + 1;
        xyCollusionDot[1] = y + index / 2*41 + 1;
        if (index == 4 || index == 5){
            xyCollusionDot[0] = x + index % 2*41 + 1;
            xyCollusionDot[1] = y + SPRITE_SCALE*SCALE/2;
        }  else
        if (index == 6 || index == 7){
            xyCollusionDot[0] = x + SPRITE_SCALE*SCALE/2;
            xyCollusionDot[1] = y + index % 2*41 + 1;
        } else
        if (index == 8 || index == 9) {
            xyCollusionDot[0] = x + index % 2*41 + 1;
            xyCollusionDot[1] = y + SPRITE_SCALE - 2;

        } else  if(index == 10 || index == 11){
            xyCollusionDot[0] = x + index % 2*41 + 1;
            xyCollusionDot[1] = y + SPRITE_SCALE*2 - 2;
        } else if (index == 12 || index ==13) {
            xyCollusionDot[0] = x + SPRITE_SCALE - 2;
            xyCollusionDot[1] = y + index % 2*41 + 1;
        }  else  if(index == 14 || index == 15){
            xyCollusionDot[0] = x + SPRITE_SCALE*2 - 2;
            xyCollusionDot[1] = y + index % 2*41 + 1;
        }


        return xyCollusionDot;
    }
}
