package com.bestproger.game.level;

import com.bestproger.graphics.Sprite;
import com.bestproger.graphics.SpriteSheet;
import com.bestproger.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Tile {

    protected BufferedImage image;
    private int[] imageData;
    private TileType type;
    private Sprite sprite;
    private int count;

    protected Tile(BufferedImage image, int scale, TileType type){
        this.type = type;

        if ( type == TileType.WATER){
            sprite = new Sprite(new SpriteSheet(image, 2, Level.TILE_SCALE, 3));
            count = 0;
        }
        else {
            this.image = Utils.resize(image, image.getWidth()*scale, image.getHeight()*scale);
            imageData = ((DataBufferInt) this.image.getAlphaRaster().getDataBuffer()).getData();
            if (type != TileType.EMPTY ) Utils.removeBlack(imageData);
        }
    }

    protected TileType type(){
        return type;
    }

    protected void render(Graphics2D g, int x, int y){
        g.drawImage(image, x, y, null);
    }

    protected void renderWater(Graphics2D g, int x, int y){
        sprite.render(g, x, y);
    }

    protected void animWater(){
        if (count == 30){
            sprite.anim();
            count = 0;
        }
        count++;
    }

}
