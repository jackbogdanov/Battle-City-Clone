package com.bestproger.game.bullets;

import com.bestproger.game.mob.Entity;
import com.bestproger.game.Game;
import com.bestproger.graphics.Sprite;
import com.bestproger.graphics.SpriteSheet;

import java.awt.*;

public class Explosion implements SimpleObject {

    private Sprite sprite;
    private float x;
    private float y;
    private boolean isActive;
    private int count;

    public Explosion(SpriteSheet sheet, float x, float y, Entity.Heading heading) {

        if (heading == Entity.Heading.NORTH || heading == Entity.Heading.SOUTH ){
            this.x = x - Game.SPRITE_SCALE/2 - 3;
            this.y = y;
        } else {
            this.x = x;
            this.y = y - Game.SPRITE_SCALE/2 + 6;
        }


        sprite = new Sprite(sheet);
        isActive = true;
        count = 1;
    }

    public void render(Graphics2D g){
        if (count <= 12) {
            sprite.render(g, x, y);
            if (count % 4 == 0)sprite.anim();
            count++;
        } else isActive = false;
    }

    @Override
    public void update() {

    }

    public boolean isActive(){
        return isActive;
    }


     //--------------------------
//    @Override
//    public void renderPix(Graphics2D g) {
//
//    }

}
