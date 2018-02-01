package com.jerry.aiproject.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class is the super class for all GameObjects. 
 * It won't be directly instantiated which is why
 * it is labeled as abstract. 
 * @author Jerry
 */
public abstract class GameObject {

	// Possible object types.
	public enum GameObjectType {PLAYER, ENEMY, ITEM} 
	
	private int x, y; // Object's position.
	private GameObjectType gameObjectType; // The object's type.
	
	protected BufferedImage initialImage; // Protected for easer access in subclass, sprite image.
	
	public GameObject(int xPos, int yPos, GameObjectType objectType) {
		x = xPos;
		y = yPos;
		gameObjectType = objectType;
	}
	
	/* Abstract Methods */
	
	// Handles all initializations.
	public abstract void init();
	// Handles the updates.
	public abstract void update();
	// Handles all drawings.
	public abstract void render(Graphics2D g2d);
	// For collision detection among game objects.
	public abstract Rectangle getBounds();
	
	
	// Getters and setters. Some Should only be accessed by subclasses.
	public int getX() {
		return x;
	}
	protected void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	protected void setY(int y) {
		this.y = y;
	}
	public GameObjectType getGameObjectType() {
		return gameObjectType;
	}
	protected void setGameObjectType(GameObjectType gameObjectType) {
		this.gameObjectType = gameObjectType;
	}	
}