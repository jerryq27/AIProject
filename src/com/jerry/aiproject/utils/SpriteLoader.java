package com.jerry.aiproject.utils;

import com.jerry.aiproject.data.Sprite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class handles the manipulating the 
 * Sprite Sheet and getting sub images from
 * it. It uses a modified method from 
 * RealTutsGML.
 * @author Jerry.
 */
public class SpriteLoader {

	//Static, easier access from all classes once it has been initialized in the Game class.
	private static BufferedImage spriteSheet, spriteImage;
	
	public SpriteLoader(String fileName) {
		try {
			spriteSheet = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			System.out.print("Error loading image: ");
			e.printStackTrace();
		}
	}
	
	/**
	 * Utility method, purpose of class, which is why the method is static.
	 * The method belongs to the class and is the same for every instance
	 * after it has been created.
     *
     * Uses rows and columns in the loaded sprite sheet to get the right
     * Sub images. Since the game is constantly using 32x48 px sprites,
     * This method can consistently grab the appropriate images.
	 * @param sprite contains the information of the sprite to load.
	 * @return the sub image specified. 
	 */
	public static BufferedImage loadSprite(Sprite sprite) {
		spriteImage = spriteSheet.getSubimage(
				(sprite.getCol() * Sprite.WIDTH) - Sprite.WIDTH,
				(sprite.getRow() * Sprite.HEIGHT) - Sprite.HEIGHT,
				Sprite.WIDTH, Sprite.HEIGHT);
		
		return spriteImage;
	}
	
	//METHOD FOR TESTING.
	public static BufferedImage loadSprite(String path, Sprite sprite) {
		BufferedImage extraImage = null;
		try {
			extraImage = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return extraImage.getSubimage(
				(sprite.getCol() * Sprite.WIDTH) - Sprite.WIDTH,
				(sprite.getRow() * Sprite.HEIGHT) - Sprite.HEIGHT,
				Sprite.WIDTH, Sprite.HEIGHT);
	}
}