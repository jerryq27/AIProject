package com.jerry.aiproject.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * This class creates an animation 
 * from a series of BufferedImages
 * @author Jerry
 */
public class Animation {

	private int animSpeed; //Animation speed. 
	private int count = 0; //Works with the animSpeed, to control the animation speed. 
	private int frames; //Number of frames in the animation. 
	private int index = 0; //Current frame. 
	private BufferedImage[] animation;
	private BufferedImage currentImage; //The current image in the animation. 
	
	public Animation(BufferedImage[] images, int speed) {
		animation = images;
		animSpeed = speed;
		frames = animation.length;
		currentImage = animation[0]; //Fixed bug: animation hiccup!
		index++; //Fixed Animation, hiccup fix caused a 'long slide' effect in the animation.
	}
	
	/**
	 * This method is the "update" for the animation,
	 * it updates the current index.
	 */
	public void runAnimation() {
		count++;
		
		if(count > animSpeed)
		{
			count = 0; 
			currentImage = animation[index % frames]; //Change the image for drawAnimation. 
		    index++; //For next image. 	
		}
	}
	
	/**
	 * This method is the "render" for the animation,
	 * it draws the current image index. 
	 * @param g the drawing object. 
	 * @param x the x position.
	 * @param y the y position.
	 * @param scaleX the width of the image in pixels. 
	 * @param scaleY the height of the image in pixels.
	 */
	public void drawAnimation(Graphics g, int x, int y, int scaleX, int scaleY) {
		g.drawImage(currentImage, x, y, scaleX, scaleY, null);
	}	
}