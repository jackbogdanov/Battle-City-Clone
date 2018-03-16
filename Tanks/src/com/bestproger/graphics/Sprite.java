package com.bestproger.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite {

    private SpriteSheet sheet;
    private BufferedImage image;
    private int count;
    private final int MAX_COUNT = 100000;

    public Sprite(SpriteSheet sheet){
        this.sheet = sheet;
        count = 0;
    }

    public void render(Graphics2D g, float x, float y){
        image = sheet.getSprite(count);
        g.drawImage(image, (int) x, (int) y, null);
    }

    public void anim(){
        count++;
        count = count % MAX_COUNT;
    }

}
