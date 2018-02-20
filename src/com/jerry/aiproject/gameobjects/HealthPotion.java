package com.jerry.aiproject.gameobjects;

import java.awt.*;

import com.jerry.aiproject.data.Sprite;
import com.jerry.aiproject.utils.SpriteLoader;

/**
 * This class creates the Health Potion
 * Game Object.
 * @author Jerry
 */
public class HealthPotion extends GameObject {
	
	public HealthPotion(int xPos, int yPos) {
		super(xPos, yPos, GameObjectType.ITEM);
		init();
	}

	@Override
	public void init() {
		initialImage = SpriteLoader.loadSprite("res/health_potion.png", new Sprite(1, 1));
	}
	@Override
	public void update() {}

	@Override
	public void render(Graphics2D g2d) {
		g2d.drawImage(initialImage, getX(), getY(), Sprite.WIDTH, Sprite.HEIGHT, null);
		
		// DEBUG TOOL
		//g2d.setColor(Color.RED);
		//g2d.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), Sprite.WIDTH, Sprite.HEIGHT);
	}
}