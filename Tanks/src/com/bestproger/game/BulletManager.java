package com.bestproger.game;

import com.bestproger.game.bullets.Bullet;
import com.bestproger.game.bullets.Explosion;
import com.bestproger.game.bullets.SimpleObject;
import com.bestproger.graphics.SpriteSheet;
import com.bestproger.graphics.TextureAtlas;

import java.awt.*;
import java.util.ArrayList;

import static com.bestproger.game.Game.SPRITE_SCALE;


public class BulletManager  {


    private ArrayList<SimpleObject> bullets;
    private SpriteSheet spriteSheet;
    private float scale;

    public BulletManager(TextureAtlas atlas, float scale) {
        this.scale = scale;
        bullets = new ArrayList<>();

        spriteSheet = new SpriteSheet(atlas.cut(16*SPRITE_SCALE, 8*SPRITE_SCALE, SPRITE_SCALE*3, SPRITE_SCALE), 3 , SPRITE_SCALE , (int) scale);
    }

    public void addNewBullet(Bullet bullet){
        bullets.add(bullet);
    }

    public void updateBullets(){
        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).isActive()) {
                bullets.get(i).update();
            } else{
                bullets.get(i).update();
                destroyBullet(i);
            }
        }
    }

    public void renderBullets(Graphics2D g){
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).render(g);
        }
    }

    private void destroyBullet(int i){
        if (bullets.get(i) instanceof Bullet){
            bullets.add(new Explosion(spriteSheet, ((Bullet) bullets.get(i)).getX(), ((Bullet) bullets.get(i)).getY(), ((Bullet) bullets.get(i)).getHeading()));
            bullets.remove(i);
        } else {
            bullets.remove(i);
        }
    }

}
