package com.jerry.aiproject.gameobjects;

import java.awt.*;
import com.jerry.aiproject.utils.SpriteLoader;

/**
 * This class creates the Health Potion
 * Game Object.
 * @author Jerry
 */
public class HealthPotion extends GameObject {
	
	public HealthPotion(int xPos, int yPos) {
		super(xPos, yPos, GameObjectType.ITEM);
		initialImage = SpriteLoader.loadImage("res/smallhp.png", 1, 1, 48, 48);
	}

	@Override
	public void init() {}
	@Override
	public void update() {}

	@Override
	public void render(Graphics g) {
		g.drawImage(initialImage, getX(), getY(), 32, 32, null);
		
		//DEBUG TOOL
		//g.setColor(Color.RED);
		//g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(getX() + 5, getY(), initialImage.getWidth() - 27, initialImage.getHeight() - 17);
	}
}