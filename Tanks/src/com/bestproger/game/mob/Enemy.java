package com.bestproger.game.mob;

import com.bestproger.game.BulletManager;
import com.bestproger.game.Mob;
import com.bestproger.game.bullets.Bullet;
import com.bestproger.game.bullets.BulletType;
import com.bestproger.game.level.Level;
import com.bestproger.graphics.Sprite;
import com.bestproger.graphics.SpriteSheet;
import com.bestproger.graphics.TextureAtlas;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.bestproger.game.Game.SPRITE_SCALE;
import static com.bestproger.game.mob.Player.SPRITES_PER_HEADING;


public class Enemy extends Mob {

    private Map<Heading, Sprite> spriteMap;
    private int distance;
    private int speed;
    private Random random;
    private boolean needNewCourse;
    private Heading previousHeading;

    public Enemy(EntityType type, float x, float y, float scale, Level level, TextureAtlas atlas, BulletManager bulletManager) {
        super(type, x, y, scale, level, atlas, bulletManager);

        spriteMap = new HashMap<>();
        random = new Random();

        boolean typeOfTank = random.nextBoolean();
        distance = ((int) (Math.random()*12 + 1))*SPRITE_SCALE;
        heading = Heading.fromNumeric(random.nextInt(4) + 1);
        previousHeading = null;
        needNewCourse = false;
        speed = 3;
        fireRate = Bullet.FIRE_RATE;

        for (Heading h : Heading.values()){
            h.setHeading(7, typeOfTank);
            SpriteSheet spriteSheet = new SpriteSheet(h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE, (int) scale);
            Sprite sprite = new Sprite(spriteSheet);
            spriteMap.put(h, sprite);
        }
    }

    public void update() {

        if (fireRate > 0) fireRate --;

        if (distance > 0 && !needNewCourse){
            float newX = x;
            float newY = y;

            switch (heading){
                case NORTH:
                    newY -= speed;
                    break;
                case SOUTH:
                    newY += speed;
                    break;
                case EAST:
                    newX += speed;
                    break;
                case WEST:
                    newX -= speed;
                    break;
            }

            if (newX < Level.X_INDENT){
                newX = Level.X_INDENT;
                needNewCourse = true;
            } else if (newX >= Level.X_END_INDENT - SPRITE_SCALE *scale) {
                newX = Level.X_END_INDENT - SPRITE_SCALE *scale;
                needNewCourse = true;
            }

            if (newY < Level.Y_INDENT){
                newY = Level.Y_INDENT;
                needNewCourse = true;
            } else if (newY >= Level.Y_END_INDENT - SPRITE_SCALE *scale) {
                newY = Level.Y_END_INDENT - SPRITE_SCALE *scale;
                needNewCourse = true;
            }


            if (!collision((int) newX,(int) newY)){
                x = newX;
                y = newY;
            } else needNewCourse = true;

            if (fireRate <= 0) shoot();

            distance--;

        } else newCourse();

    }

    private void newCourse(){
        shoot();

        distance  =  ((int) (Math.random()*12 + 1))*SPRITE_SCALE;

        Heading newHeading = heading;

        while (true){
            if (newHeading != heading && newHeading != previousHeading) break;
            newHeading = Heading.fromNumeric(random.nextInt(4) + 1);
        }

        previousHeading = heading;
        heading = newHeading;
        needNewCourse = false;
    }

    private void shoot(){
        bulletManager.addNewBullet(new Bullet(atlas, x, y, heading, BulletType.DEFAULT_BULLET, scale, level));
        fireRate = Bullet.FIRE_RATE;
    }
    @Override
    public void render(Graphics2D g) {
        spriteMap.get(heading).render(g, x, y);
    }

}
