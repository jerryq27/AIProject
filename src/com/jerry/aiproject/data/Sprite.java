package com.jerry.aiproject.data;

/**
 * This class is going to represent a sprite.
 * It will contain all the information of the
 * Sprite to be passed into the SpriteLoader.
 * @author Jerry
 */
public class Sprite {

    public static final int WIDTH = 32, HEIGHT = 48;
    private int spriteRow, spriteCol;

    public Sprite(int row, int col) {
        spriteRow = row;
        spriteCol = col;
    }

    public int getRow() {
        return spriteRow;
    }

    public int getCol() {
        return spriteCol;
    }

}
