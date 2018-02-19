package com.jerry.aiproject.utils;

import com.jerry.aiproject.core.TileMap;

/**
 * This interface is for GameObjects that
 * will be using AI algorithms generated 
 * paths to follow. It should be called
 * from the update method in the Game class.
 * @author Jerry
 */
public interface Movement {
    void moveUp();
    void moveDown();
    void moveRight();
    void moveLeft();
    int getDelX();
    void setDelX(int delX);
    int getDelY();
    void setDelY(int delY);
    void loadAnimationFrames();	// All moving game objects will have animation.
}
