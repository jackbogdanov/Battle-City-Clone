package com.bestproger.game;

import com.bestproger.IO.Input;
import com.bestproger.display.Display;
import com.bestproger.game.level.Level;
import com.bestproger.game.mob.Enemy;
import com.bestproger.game.mob.EntityType;
import com.bestproger.game.mob.Player;
import com.bestproger.graphics.TextureAtlas;
import com.bestproger.utils.Time;

import java.awt.*;

public class Game implements Runnable{

    public static int WIDTH = 1056;
    public static int HEIGHT = 672;
    public static String TITLE = "Tanks";
    public static int CLEAN_COLOR = 0xff000000;
    public static int NUM_BUFFERS = 3;

    public static final float UPDATE_RATE = 60.0f;
    public static final float UPDATE_INTERVAL = Time.SECOND / UPDATE_RATE;
    public static final long IDLE_TIME = 1;

    public static final String ATLAS_FILE_NAME = "texture_atlas.png";
    //=======moveToGameManagerClass================
    public static final int SCALE = 3;
    public static final int SPRITE_SCALE = 16;

    private boolean isRunning;
    private Thread gameTread;
    private Graphics2D graphics2D;
    private Input input;

    private TextureAtlas atlas;


    private Player player;
    private Enemy enemy;

    private Level lvl;
    private BulletManager bulletManager;

    public Game() {

        isRunning = false;
        Display.create(WIDTH, HEIGHT, TITLE, CLEAN_COLOR, NUM_BUFFERS);
        graphics2D = Display.getGraphics2D();
        input = new Input();
        Display.addInputListener(input);

        atlas = new TextureAtlas(ATLAS_FILE_NAME);

        lvl = new Level(atlas);

        bulletManager = new BulletManager(atlas, SCALE);
        player = new Player(Level.X_END_INDENT - SPRITE_SCALE*SCALE, 0, SCALE, 3, atlas, lvl, bulletManager);
        enemy = new Enemy(EntityType.DEFAULT_ENEMY, Level.X_INDENT, Level.Y_INDENT, SCALE, lvl, atlas, bulletManager);
    }

    public synchronized void start(){

        if (isRunning) return;
        isRunning = true;

        gameTread = new Thread(this);
        gameTread.start();
    }

    public synchronized void stop(){
        if (!isRunning) return;
        isRunning = false;

        try {
            gameTread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cleanUp();
    }

    private void update(){
        player.update(input);
        enemy.update();
        bulletManager.updateBullets();
        lvl.update();
    }

    private void render(){
        Display.clear();
        lvl.render(graphics2D);
        player.render(graphics2D);
        enemy.render(graphics2D);
        bulletManager.renderBullets(graphics2D);
        lvl.renderGrass(graphics2D);
        Display.swapBuffers();
    }

    @Override
    public void run() {

        int fps  = 0;
        int upd = 0;
        int updl = 0;

        long count = 0;

        float delta = 0;
        long lastTime = Time.get();

        while (isRunning){
            long now = Time.get();
            long elapsedTime = now - lastTime;
            lastTime = now;

            count += elapsedTime;

            delta += (elapsedTime/UPDATE_INTERVAL);

            boolean render = false;
            while (delta > 1){
                update();
                upd++;
                delta--;

                if (render) updl++; else
                    render = true;



            }

            if (render){
                render();
                fps++;
            }  else {
                try {
                    Thread.sleep(IDLE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


         if (count >= Time.SECOND){
             Display.setTitle(TITLE + " || fps: " + fps +  " | update: "+ upd + " | updl: " + updl + " ||");
             upd = 0;
             fps = 0;
             updl = 0;
             count = 0;
         }
        }
    }

    private void cleanUp(){
        Display.destroy();
    }

}
