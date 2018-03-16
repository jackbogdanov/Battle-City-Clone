package com.bestproger.game.mob;

import com.bestproger.IO.Input;
import com.bestproger.game.BulletManager;
import com.bestproger.game.Mob;
import com.bestproger.game.bullets.Bullet;
import com.bestproger.game.bullets.BulletType;
import com.bestproger.game.level.BrickTile;
import com.bestproger.game.level.Level;
import com.bestproger.game.level.TileType;
import com.bestproger.graphics.Sprite;
import com.bestproger.graphics.SpriteSheet;
import com.bestproger.graphics.TextureAtlas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import static com.bestproger.game.Game.SPRITE_SCALE;


public class Player extends Mob {


    static final int SPRITES_PER_HEADING = 2;

    private Map<Heading, Sprite> spriteMap;
    private float speed;

    private boolean wasPressed;

    public Player(float x, float y, float scale, float speed, TextureAtlas atlas, Level level, BulletManager bulletManager) {
        super(EntityType.PLAYER, x, y, scale, level, atlas, bulletManager);

        this.scale = scale;
        this.speed = speed;

        fireRate = Bullet.FIRE_RATE;
        heading = Heading.NORTH;
        spriteMap = new HashMap<>();
        wasPressed = false;

        for (Heading h : Heading.values()){
            SpriteSheet spriteSheet = new SpriteSheet(h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE, (int) scale);
            Sprite sprite = new Sprite(spriteSheet);
            spriteMap.put(h, sprite);
        }
    }


    public void update(Input input) {

        float newX = x;
        float newY = y;

        boolean isNeedRotation = true;

        if (fireRate > 0) fireRate --;


        if (input.getKey(KeyEvent.VK_W)){
            heading = Heading.NORTH;
            newY -= speed;
            isNeedRotation = false;
        }
        if(input.getKey(KeyEvent.VK_D) && isNeedRotation){
            newX += speed;
            heading = Heading.EAST;
            isNeedRotation = false;
        }
        if(input.getKey(KeyEvent.VK_S) && isNeedRotation){
            newY += speed;
            heading = Heading.SOUTH;
            isNeedRotation = false;
        }
        if(input.getKey(KeyEvent.VK_A) && isNeedRotation) {
            newX -= speed;
            heading = Heading.WEST;
            isNeedRotation = false;
        }

        if (input.getKey(KeyEvent.VK_SPACE) && !wasPressed && fireRate <= 0){
            bulletManager.addNewBullet(new Bullet(atlas, x, y, heading, BulletType.DEFAULT_BULLET, scale, level));
            fireRate = Bullet.FIRE_RATE;
            wasPressed = true;
        }
        if (!input.getKey(KeyEvent.VK_SPACE)){
            wasPressed = false;
        }


        if (!isNeedRotation) spriteMap.get(heading).anim();

        turning((int) newX,(int) newY, heading);

        if (!(newX == x && newY == y)){
            move(newX, newY);
        }
}

    @Override
    public void render(Graphics2D g) {
        spriteMap.get(heading).render(g, x, y);
    }

    private void turning(int x, int y, Heading h){
        int[] xyCoords;
        final int MOVING_CORRECTION = 7;

        switch (h){
            case NORTH:
                xyCoords = getCollusionDotCoords(x, y, 0);

                if (!(collisionForTurning(xyCoords[0] + MOVING_CORRECTION, xyCoords[1])) && collisionForTurning(xyCoords[0],xyCoords[1])){
                    this.x = x + ((xyCoords[0] + MOVING_CORRECTION) - (level.getTileX(xyCoords[0]) + level.SCALE_TILE_SIZE));
                    this.y = y;
                } else{
                    xyCoords = getCollusionDotCoords(x, y, 1);

                    if (!(collisionForTurning(xyCoords[0] - MOVING_CORRECTION, xyCoords[1])) && collisionForTurning(xyCoords[0],xyCoords[1])){
                        this.x = x - ((xyCoords[0]) - level.getTileX(xyCoords[0]) + 1);
                        this.y = y;
                    }
                }
                break;
            case SOUTH:
                xyCoords = getCollusionDotCoords(x, y, 2);
                if (!(collisionForTurning(xyCoords[0] + MOVING_CORRECTION, xyCoords[1])) && collisionForTurning(xyCoords[0],xyCoords[1])){
                    this.x = x + ((xyCoords[0] + MOVING_CORRECTION) - (level.getTileX(xyCoords[0]) + level.SCALE_TILE_SIZE));
                    this.y = y;
                } else{

                    xyCoords = getCollusionDotCoords(x, y, 3);
                    if (!(collisionForTurning(xyCoords[0] - MOVING_CORRECTION, xyCoords[1])) && collisionForTurning(xyCoords[0],xyCoords[1])){
                        this.x = x - ((xyCoords[0]) - level.getTileX(xyCoords[0]) + 1);
                        this.y = y;
                    }
                }
                break;

            case EAST:
                xyCoords = getCollusionDotCoords(x, y, 1);
                if (!(collisionForTurning(xyCoords[0], xyCoords[1] + MOVING_CORRECTION)) && collisionForTurning(xyCoords[0],xyCoords[1])){
                    this.x = x;
                    this.y = y  - ((xyCoords[1]) - (level.getTileY(xyCoords[1]) + level.SCALE_TILE_SIZE));;
                }
                else{
                    xyCoords = getCollusionDotCoords(x, y, 3);
                    if (!(collisionForTurning(xyCoords[0], xyCoords[1] - MOVING_CORRECTION)) && collisionForTurning(xyCoords[0],xyCoords[1])){
                        this.x = x;
                        this.y = y - ((xyCoords[1]) - (level.getTileY(xyCoords[1]) - 1));
                    }
                }
                break;

            case WEST:
                xyCoords = getCollusionDotCoords(x, y, 0);
                if (!(collisionForTurning(xyCoords[0], xyCoords[1] + MOVING_CORRECTION)) && collisionForTurning(xyCoords[0],xyCoords[1])){
                    this.x = x;
                    this.y = y  - ((xyCoords[1]) - (level.getTileY(xyCoords[1]) + level.SCALE_TILE_SIZE));;
                }
                else{
                    xyCoords = getCollusionDotCoords(x, y, 2);
                    if (!(collisionForTurning(xyCoords[0], xyCoords[1] - MOVING_CORRECTION)) && collisionForTurning(xyCoords[0],xyCoords[1])){
                        this.x = x;
                        this.y = y - ((xyCoords[1]) - (level.getTileY(xyCoords[1]) - 1));
                    }
                }
                break;
            default:
                break;
        }
    }

    private void move(float newX, float newY){
        if (newX < Level.X_INDENT){
            newX = Level.X_INDENT;
        } else if (newX >= Level.X_END_INDENT - SPRITE_SCALE *scale) {
            newX = Level.X_END_INDENT - SPRITE_SCALE *scale;
        }

        if (newY < Level.Y_INDENT){
            newY = Level.Y_INDENT;
        } else if (newY >= Level.Y_END_INDENT - SPRITE_SCALE *scale) {
            newY = Level.Y_END_INDENT - SPRITE_SCALE *scale;
        }


        if (!collision((int) newX,(int) newY)){
            x = newX;
            y = newY;
        }
    }


    private boolean collisionForTurning(int x, int y){
        boolean solid = false;
        int[] xyCoords = {x , y};
        TileType tileType;

        tileType = level.getTile(x,y);

        if (tileType == TileType.BRICK){
            BrickTile brick = BrickTile.getBrick(xyCoords, level);
            if (!solid) solid = brick.isCollapseMob(xyCoords[0], xyCoords[1]);

        } else if (tileType.isSolid()) solid = true;


        return solid;
    }

    //-------------
//    public void renderPix(Graphics2D g){
//        g.setColor(Color.red);
//        int[] xyCoords;
//
//        for (int i = 0; i < 16; i++) {
//            xyCoords = getCollusionDotCoords((int) x,(int) y, i);
//            g.fillOval(xyCoords[0], xyCoords[1], 1, 1);
//        }
//    }
}