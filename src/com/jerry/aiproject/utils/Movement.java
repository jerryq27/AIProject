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
    boolean moveAlongPath(TileMap map, Path path);
    int getDelX();
    void setDelX(int delX);
    int getDelY();
    void setDelY(int delY);
    void loadAnimation();	//All moving game objects will have animation.
}
