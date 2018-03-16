package com.bestproger.game.level;

public enum TileType {

    EMPTY(0, false), BRICK(1, true), METAL(2, true),
    WATER(3, true), GRASS(4, false), ICE(5, false);

    private int n;
    private boolean isSolid;

    TileType(int n, boolean isSolid){
       this.n = n;
        this.isSolid = isSolid;
    }

    public int numeric(){
        return n;
    }

    public static TileType fromNumeric(int n){
        switch (n){
            case 1:
                return BRICK;
            case 2:
                return METAL;
            case 3:
                return WATER;
            case 4:
                return GRASS;
            case 5:
                return ICE;
            default:
                return EMPTY;
        }
    }

    public boolean isSolid(){
        return isSolid;
    }
}
