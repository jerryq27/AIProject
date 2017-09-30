package com.jerry.aiproject.utils;

import java.util.ArrayList;
import java.awt.Graphics;
import com.jerry.aiproject.gameobjects.*;

/**
 * This class handles the spawning of
 * GameObjects as well as the removal
 * of GameObjects. 
 * STATUS: INCOMPLETE, additions and removals 
 * should be handled in this class.
 * @author Jerry 
 */
public class GameObjectSpawner {
	
	private ArrayList<GameObject> enemies, items;
	
	public GameObjectSpawner() {
		items = new ArrayList<GameObject>();
	}
	
	public void spawn(GameObject object) {
		if(object instanceof HealthPotion || object instanceof Weapon)
			items.add(object);
	}
	
	public GameObject getObject(int index) {
		return items.get(index);
	}
	
	public void removeObject(GameObject object) {
		items.remove(object);
	}
	
	/**
	 * To handle the updating of
	 * the Game Objects. 
	 */
	public void update() {
		for(int dex = 0; dex < items.size(); dex++)
		{
			items.get(dex).update();
		}
	}
	
	/**
	 * to handle the rendering of
	 * the GameObjects. 
	 */
	public void render(Graphics g) {
		for(int dex = 0; dex < items.size(); dex++)
		{
			items.get(dex).render(g);
		}
	}
	
	public GameObject checkCollision(GameObject objectColliding) {
		boolean collision = false;
		
		//Check if there is a collison with an item, otherwise return null.
		for(int dex = 0; dex < items.size(); dex++)
		{
			collision = objectColliding.getBounds().intersects(items.get(dex).getBounds());
			if(collision)
				return items.get(dex);
		}
		return null;
	}
}