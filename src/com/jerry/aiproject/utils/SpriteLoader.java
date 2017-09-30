package com.jerry.aiproject.utils;

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
	private static BufferedImage spriteSheet, sprite;
	
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
	 * @param col starts count at 1. 
	 * @param row starts count at 1. 
	 * @param width the width of the sub image in the sheet. 
	 * @param height the height of the sub image in the sheet. 
	 * @return the sub image specified. 
	 */
	public static BufferedImage loadImage(int col, int row, int width, int height) {
		sprite = spriteSheet.getSubimage((col * width) - width, (row * height) - height, 
			width, height);
		
		return sprite;
	}
	
	//METHOD FOR TESTING.
	public static BufferedImage loadImage(String path, int col, int row, int width, int height) {
		BufferedImage extraImage = null;
		try {
			extraImage = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return extraImage.getSubimage((col * width) - width, (row * height) - height, 
				width, height);
	}
}