package com.bestproger.game.bullets;

import java.awt.*;

public interface SimpleObject {

    void render(Graphics2D g);
    void update();
    boolean isActive();

    //---------------
    //void renderPix(Graphics2D g);
}
