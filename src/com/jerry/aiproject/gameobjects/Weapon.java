package com.jerry.aiproject.gameobjects;

import java.awt.*;

import com.jerry.aiproject.utils.SpriteLoader;

public class Weapon extends GameObject {

	// Enum for the possible weapons.
	public enum WeaponType {
		AX,
		BOW,
		SWORD;
	}
	private WeaponType weapon;
	
	
	public Weapon(int x, int y, WeaponType type) {
		super(x, y, GameObjectType.ITEM);
		weapon = type;
		init();
	}

	@Override
	public void init() {
		if(weapon == WeaponType.AX)
			initialImage = SpriteLoader.loadImage("res/weapons.png", 1, 1, 32, 48);
		else if(weapon == WeaponType.BOW)
			initialImage = SpriteLoader.loadImage("res/weapons.png", 2, 1, 32, 48);
		else if(weapon == WeaponType.SWORD)
			initialImage = SpriteLoader.loadImage("res/weapons.png", 3, 1, 32, 48);
	}

	@Override
	public void update() {}

	@Override
	public void render(Graphics2D g2d) {
		g2d.drawImage(initialImage, getX(), getY(), 32, 48, null);
		
		// DEBUG
		//g2d.setColor(Color.RED);
		//g2d.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(getX() + 4, getY() + 5, initialImage.getWidth() - 8, initialImage.getHeight());
	}
}