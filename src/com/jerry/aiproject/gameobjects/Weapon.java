package com.jerry.aiproject.gameobjects;

import java.awt.*;

import com.jerry.aiproject.data.Sprite;
import com.jerry.aiproject.utils.SpriteLoader;

/**
 * This class represents a weapon game object.
 * There are three types of weapons used in the game:
 *      - A sword for basic enemies
 *      - A bow for unreachable enemies
 *      - An ax for large enemies
 * @author Jerry
 */
public class Weapon extends GameObject {

	// Enum for the possible weapons.
	public enum WeaponType {
		AX,
		BOW,
		SWORD;
	}
	private WeaponType weaponType;
	
	
	public Weapon(int x, int y, WeaponType type) {
		super(x, y, GameObjectType.ITEM);
		weaponType = type;
		init();
	}

	@Override
	public void init() {
		switch(weaponType)
        {
            case AX:
                initialImage = SpriteLoader.loadSprite("res/items.png", new Sprite(1, 2));
                break;
            case BOW:
                initialImage = SpriteLoader.loadSprite("res/items.png", new Sprite(1, 3));
                break;
            case SWORD:
                initialImage = SpriteLoader.loadSprite("res/items.png", new Sprite(2, 2));
                break;
        }
	}

	@Override
	public void update() {}

	@Override
	public void render(Graphics2D g2d) {
		g2d.drawImage(initialImage, getX(), getY(), Sprite.WIDTH, Sprite.HEIGHT, null);
		
		// DEBUG
		//g2d.setColor(Color.RED);
		//g2d.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(getX() + 4, getY() + 5, Sprite.WIDTH - 8, Sprite.HEIGHT);
	}
}