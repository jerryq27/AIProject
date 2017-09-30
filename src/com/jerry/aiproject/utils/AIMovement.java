package com.jerry.aiproject.utils;

import com.jerry.aiproject.core.TileMap;

/**
 * This interface is for GameObjects that
 * will be using AI algorithms generated 
 * paths to follow. It should be called
 * from the update method in the Game class.
 * @author Jerry
 */
public interface AIMovement {
	
	public void moveAlongPath(TileMap map, Path path);

}
