package com.bestproger.game.mob;


import com.bestproger.game.BulletManager;
import com.bestproger.game.level.Level;
import com.bestproger.graphics.TextureAtlas;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.bestproger.game.Game.SPRITE_SCALE;

public abstract class Entity {

    public final EntityType type;

    protected float x;
    protected float y;
    protected Heading heading;
    protected float scale;
    protected Level level;
    protected TextureAtlas atlas;
    protected BulletManager bulletManager;
    protected int fireRate;


    public enum Heading{
        NORTH(0* SPRITE_SCALE, 0* SPRITE_SCALE, 1 * SPRITE_SCALE, 2 * SPRITE_SCALE),
        EAST(6* SPRITE_SCALE, 0* SPRITE_SCALE, 1 * SPRITE_SCALE, 2 * SPRITE_SCALE),
        SOUTH(4* SPRITE_SCALE, 0* SPRITE_SCALE, 1 * SPRITE_SCALE, 2 * SPRITE_SCALE),
        WEST(2* SPRITE_SCALE, 0* SPRITE_SCALE, 1 * SPRITE_SCALE, 2 * SPRITE_SCALE);

        private int x, y, h, w;

        public void setHeading(int index, boolean type){
            if (type){
                y = y + SPRITE_SCALE*(index % 8);
            } else {
                x = x + SPRITE_SCALE*8;
                y = y + SPRITE_SCALE*(index % 8);
            }
        }

        Heading(int x, int y, int h, int w){
            this.x = x;
            this.y = y;
            this.h = h;
            this.w = w;
        }

        public static Heading fromNumeric(int n){
            switch (n){
                case 1:
                    return NORTH;
                case 2:
                    return SOUTH;
                case 3:
                    return EAST;
                case 4:
                    return WEST;
                default:
                    return null;
            }
        }

        protected BufferedImage texture(TextureAtlas atlas){
            return atlas.cut(x, y, w, h);
        }
    }

    public Entity(EntityType type, float x, float y, float scale, Level level, TextureAtlas atlas, BulletManager bulletManager) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.level = level;
        this.scale = scale;
        this.atlas = atlas;
        this.bulletManager = bulletManager;
    }

    public abstract void render(Graphics2D g);

}
