package com.bestproger.game.bullets;

import com.bestproger.game.mob.Entity;
import com.bestproger.game.Game;
import com.bestproger.game.level.BrickTile;
import com.bestproger.game.level.Level;
import com.bestproger.game.level.TileType;
import com.bestproger.graphics.TextureAtlas;
import com.bestproger.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Bullet implements SimpleObject {

    private static final int BULLET_SCALE = 8;
    public static final int FIRE_RATE = 25;

    private float xa;
    private float ya;
    private float x;
    private float y;
    private float speed;
    private float scale;
    private Level level;
    private boolean canMove;
    private Entity.Heading h;

    private BulletType type;

    private BufferedImage image;


    public Bullet(TextureAtlas atlas, float x, float y, Entity.Heading h, BulletType type, float scale, Level level) {

        this.type = type;
        this.scale = scale;
        this.level = level;
        this.h = h;
        canMove = true;
        speed = 6;

        switch (h){
            case NORTH: image = atlas.cut(40*BULLET_SCALE, 12*BULLET_SCALE, BULLET_SCALE, BULLET_SCALE*2);
                this.x = x + (Game.SPRITE_SCALE /2) + 1;
                this.y = y - BULLET_SCALE*scale;
                xa = 0;
                ya = -speed;
                break;
            case SOUTH: image = atlas.cut(42*BULLET_SCALE, 12*BULLET_SCALE, BULLET_SCALE, BULLET_SCALE*2);
                this.x = x + (Game.SPRITE_SCALE /2) + 1;
                this.y = y + BULLET_SCALE*scale;
                xa = 0;
                ya = speed;
                break;
            case WEST:  image = atlas.cut(41*BULLET_SCALE, 12*BULLET_SCALE, BULLET_SCALE, BULLET_SCALE*2);
                this.x = x - BULLET_SCALE*scale/2;
                this.y = y;
                xa = -speed;
                ya = 0;
                break;
            case EAST:  image = atlas.cut(43*BULLET_SCALE, 12*BULLET_SCALE, BULLET_SCALE, BULLET_SCALE*2);
                this.x = x + Game.SPRITE_SCALE *scale - BULLET_SCALE*scale/2;
                this.y = y;
                xa = speed;
                ya = 0;
                break;
        }

        image = Utils.resize(image, BULLET_SCALE*scale,  BULLET_SCALE*scale*2);
        Utils.removeBlack(( (DataBufferInt) image.getRaster().getDataBuffer()).getData());
    }


    public void update() {
        move(x + xa, y + ya);
    }

    public void render(Graphics2D g) {
        g.drawImage(image, (int) x, (int) y, null);
    }


     private int[] getCollusionDotCoords(int x, int y, int index){
         int[] xyCollusionDot = new int[2];
         if (h == Entity.Heading.NORTH || h == Entity.Heading.SOUTH){
             xyCollusionDot[0] = x + index % 2*9 + 9;
             xyCollusionDot[1] = y + index / 2*9 + 22;
         } else {
             xyCollusionDot[0] = x + index / 2*9 + 12;
             xyCollusionDot[1] = y + index % 2*9 + 18;
         }
         return xyCollusionDot;
    }

    private void move(float newX, float newY)  {

        if (newX < Level.X_INDENT - (BULLET_SCALE*scale/2 - 2*scale) ||
                newX >= Level.X_END_INDENT - BULLET_SCALE*scale + 2*scale - 1 ||
                newY <  Level.Y_INDENT - (BULLET_SCALE*scale - 2*scale) ||
                newY >= Level.Y_END_INDENT - BULLET_SCALE*scale - 2*scale) {
            canMove = false;
        }

        if (canMove && !collision((int) newX,(int) newY)){
            x = newX;
            y = newY;
        } else canMove = false;
    }

    public boolean collision(int x, int y){
        boolean solid = false;
        boolean b;
        int[] xyCoords;
        TileType type;
        BrickTile lastBrick = null;

        for (int c = 0; c < 2; c++) {
            xyCoords = getCollusionDotCoords(x, y, c);

            type = level.getTile(xyCoords[0],xyCoords[1]);

            if (type == TileType.BRICK){
                BrickTile brick = BrickTile.getBrick(xyCoords,level);

                if (lastBrick != brick) {
                    b = brick.isCollapseBullet(h, xyCoords[0], xyCoords[1]);
                    if (!solid) solid = b;
                }
                lastBrick = brick;
            } else {
                if (type.isSolid()){
                   if (!solid) solid = true;
                }
            }
        }

        return solid;
    }

    public boolean isActive(){
        return canMove;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Entity.Heading getHeading(){
        return h;
    }

     //--------------
//    public void renderPix(Graphics2D g){
//        g.setColor(Color.red);
//        int[] xyCoords;
//
//        for (int i = 0; i < 2; i++) {
//            xyCoords = getCollusionDotCoords((int) x,(int) y, i);
//            g.fillOval(xyCoords[0], xyCoords[1], 1, 1);
//        }
//    }
}
