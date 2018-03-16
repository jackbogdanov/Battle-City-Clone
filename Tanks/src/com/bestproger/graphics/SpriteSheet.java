package com.bestproger.graphics;


import com.bestproger.utils.Utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class SpriteSheet {

    private BufferedImage sheet;
    private int spriteCount;
    private int spriteScale;
    private int spritesInWidth;

    private int[] imageData;

    public SpriteSheet(BufferedImage sheet, int spriteCount, int spriteScale, int scale) {
        this.sheet = Utils.resize(sheet, (sheet.getWidth()*scale),  (sheet.getHeight()*scale));
        this.spriteCount = spriteCount;
        this.spriteScale = spriteScale*scale;
        this.spritesInWidth = sheet.getWidth()/spriteScale;

        imageData = ((DataBufferInt) this.sheet.getAlphaRaster().getDataBuffer()).getData();
        Utils.removeBlack(imageData);
    }

    public BufferedImage getSprite(int index){

        index = index % spriteCount;

        int x = index % spritesInWidth*spriteScale;
        int y = index / spritesInWidth*spriteScale;
        return sheet.getSubimage(x, y, spriteScale, spriteScale);
    }
}
