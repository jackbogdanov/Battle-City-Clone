package com.bestproger.game.level;

import com.bestproger.game.Game;
import com.bestproger.graphics.TextureAtlas;
import com.bestproger.utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Level {

    public static final int TILE_SCALE = 8;
    public static final int TILE_IN_GAME_SCALE = 3;
    public static final int SCALE_TILE_SIZE = TILE_SCALE*TILE_IN_GAME_SCALE;
    public static final int TILES_IN_WIDTH = Game.WIDTH/(SCALE_TILE_SIZE );
    public static final int TILES_IN_HEIGHT = Game.HEIGHT/(SCALE_TILE_SIZE );

    public static final int X_INDENT = 16*TILE_IN_GAME_SCALE*4;
    public static final int X_END_INDENT = X_INDENT + 16*TILE_IN_GAME_SCALE*13;

    public static final int Y_INDENT = 8*TILE_IN_GAME_SCALE;
    public static final int Y_END_INDENT = Y_INDENT + 16*TILE_IN_GAME_SCALE*13;

    private Integer[][] tileMap;
    private Map<TileType, Tile> tiles;
    private Map<Integer, BrickTile> brickTiles;
    private List<Point> grassCords;
    //private BufferedImage backImage;
    //private BufferedImage blackBackImage;
    private Integer[][] brickKey;

    public Level(TextureAtlas atlas){

        tiles = new HashMap<>();
        brickTiles = new HashMap<>();
        brickKey = new Integer[26][26];

        tiles.put(TileType.BRICK, new Tile(atlas.cut(32*TILE_SCALE,0*TILE_SCALE, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE, TileType.BRICK));
        tiles.put(TileType.METAL, new Tile(atlas.cut(32*TILE_SCALE,2*TILE_SCALE, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE, TileType.METAL));
        tiles.put(TileType.WATER, new Tile(atlas.cut(33*TILE_SCALE,6*TILE_SCALE, TILE_SCALE*2, TILE_SCALE), TILE_IN_GAME_SCALE, TileType.WATER));
        tiles.put(TileType.GRASS, new Tile(atlas.cut(34*TILE_SCALE,4*TILE_SCALE, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE, TileType.GRASS));
        tiles.put(TileType.ICE, new Tile(atlas.cut(36*TILE_SCALE,4*TILE_SCALE, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE, TileType.ICE));
        tiles.put(TileType.EMPTY, new Tile(atlas.cut(36*TILE_SCALE,0*TILE_SCALE, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE, TileType.EMPTY));

        tileMap = Utils.levelParser("lvl_2.png");

        grassCords = new ArrayList<>();
        int k = 0;

        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap.length; j++) {
                brickKey[i][j] = k;
                k++;
            }
        }
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {

                Tile tile = tiles.get(TileType.fromNumeric(tileMap[i][j]));

                if (tile.type() == TileType.BRICK){
                    brickTiles.put(brickKey[i][j], new BrickTile(atlas.cut(32*TILE_SCALE,0*TILE_SCALE, TILE_SCALE, TILE_SCALE),
                            TILE_IN_GAME_SCALE, TileType.BRICK, this, i, j));
                }
                if (tile.type() == TileType.GRASS) {
                    grassCords.add(new Point(i * SCALE_TILE_SIZE + X_INDENT, j * SCALE_TILE_SIZE + Y_INDENT));
                }

            }
        }

        //-------------------------------------
      //  backImage = atlas.cut(47*TILE_SCALE,0*TILE_SCALE, TILE_SCALE, TILE_SCALE);
       // backImage = Utils.resize(backImage,Game.WIDTH - 16*Level.TILE_IN_GAME_SCALE*6,Game.HEIGHT);

       // blackBackImage = atlas.cut(45*TILE_SCALE,0*TILE_SCALE, TILE_SCALE, TILE_SCALE);
        //blackBackImage = Utils.resize(blackBackImage,Game.WIDTH - 16*Level.TILE_IN_GAME_SCALE*9,Game.HEIGHT - Y_INDENT*2);
    }

    public void update(){

    }

    public void render(Graphics2D g){

        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {

                Tile tile = tiles.get(TileType.fromNumeric(tileMap[i][j]));

                if (tile.type() != TileType.GRASS && tile.type() != TileType.BRICK && tile.type() != TileType.WATER) {
                    tile.render(g, i * SCALE_TILE_SIZE + X_INDENT , j * SCALE_TILE_SIZE + Y_INDENT );
                }

                if (tile.type() == TileType.WATER){
                   tile.renderWater(g, i * SCALE_TILE_SIZE + X_INDENT , j * SCALE_TILE_SIZE + Y_INDENT);
                }

                if (tile.type() == TileType.BRICK){
                    brickTiles.get(brickKey[i][j]).render(g,i * SCALE_TILE_SIZE + X_INDENT, j * SCALE_TILE_SIZE + Y_INDENT);
                }
            }
        }

        tiles.get(TileType.WATER).animWater();
    }


    public void renderGrass(Graphics2D g){
        for (Point p: grassCords) {
            tiles.get(TileType.GRASS).render(g, p.x, p.y);
        }
    }

    public TileType getTile(int x, int y){
        return TileType.fromNumeric(tileMap[(x - X_INDENT) / SCALE_TILE_SIZE][(y - Y_INDENT) / SCALE_TILE_SIZE]);
    }

    public int getTileX(int x){
        return (x / SCALE_TILE_SIZE)*SCALE_TILE_SIZE;
    }

    public int getTileY(int y){
        return (y / SCALE_TILE_SIZE)*SCALE_TILE_SIZE;
    }

    public int getTileMasX(int x){
        return  (x - X_INDENT) / SCALE_TILE_SIZE;
    }

    public int getTileMasY(int y){
        return  (y - Y_INDENT) / SCALE_TILE_SIZE;
    }

    public BrickTile getBrickTile(int i, int j){
        return brickTiles.get(brickKey[i][j]);
    }

    protected void removeTile(int i, int j){
        brickTiles.remove(brickKey[i][j]);
        tileMap[i][j] = 0;
    }

}
