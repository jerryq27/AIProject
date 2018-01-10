package com.jerry.aiproject.core;

import java.awt.*;
import javax.swing.*;

import com.jerry.aiproject.states.GameState;
import com.jerry.aiproject.states.PlayState;
import com.jerry.aiproject.states.MenuState;

/**
 * This class contains the main method.
 * It is also the JFrame container for
 * the whole game. This is also the heart
 * of the game and runs the game loop.
 * This will also be the panel which
 * holds the game.
 * @author Jerry
 */
public class Game extends JFrame implements Runnable {
	
	//TODO Make diagonal animation smoother.
	//TODO Allow for BFS to find a diagonal path.
	//FIXME Fix up frame collision detection, use the Rectangles?
	//Try this in Game, render menu instead of everything else.
	//Current approach to get two items (Player kept stopping before 2nd), made the rectangle height longer, I don't like this approach.

    //NOTE: JOptionPanes, Bad for the game!!
    //POTENTIAL PROBLEMS: Path generating along the game objects.
    /*STOPPING POINT SINCE LAST EDIT: Player can move up and down correctly (left and right still a problem)
    * Enter is being pressed after getting the first item to generate a new path and get the third item,
    * had to increase height of second item to generate collision.*/

	//The dimensions of the window.
	public static final int WIDTH = 640, HEIGHT = 720; //20*32px X 15*48
	
	private Thread thread; //The thread the game will be running on.
	private boolean isRunning; //Flag for determining when the game starts/ends.
    private boolean inMenu = true; //Flag to determine if the game is in the MenuState.
    private boolean inGame = false; //Flag to determine if the game is in the PlayState.
    private GameState currentState; //The current GameState being used.

	public Game() {
		thread = new Thread(this); //Create a new Thread, can use 'this' since Runnable is implemented.
		thread.start(); //Start the thread AKA call run. 
	}
	
	/**
	 * This method contains the main game loop.
	 */
	@Override
	public void run() {
		init(); //Initialize variables.
		
		//Variables for the Game Loop.
		long lastTime = System.nanoTime(); //Get the current time.
		double numberOfUpdates = 60.0; //We want 60 updates per second.
		double ns = 1000000000/numberOfUpdates; //1000000000 = 1 second.
		double delta = 0; //To help determine when to update.
		
		//Variables for the print out message. 
		long timer = System.currentTimeMillis(); //Begin the timer before the loop. 
		int updates = 0; //Counts the number of updates. 
		int frames = 0; //Counts the number of frames. 
		
		//Game Loop
		while(isRunning)
		{
			long currentTime = System.nanoTime(); //Get the time.
			delta += (currentTime - lastTime)/ns; //Subtract the differences of time and divide by '1 second'
			lastTime = currentTime; //Reset the lastTime value.
			
			while(delta >= 1) //When delta is equal to 1 second.
			{
				update(); //Call update() 60 times a second.
				updates++; //Increment for print out message. 
				delta--; //Decrement delta to test for 1 second again. 
			}
			//repaint(); //Calls paint, not in loop since we want our computer to draw as fast as possible.
			render();
            frames++; //Increment for print out message.
					
			if(System.currentTimeMillis() - timer > 1000) //Print every second, not in loop so we can get render counts.
			{
				timer += 1000; //Add second.
				System.out.println("FPS: " + frames + " Updates: " + updates);
				frames = 0; //Reset value for next count.
				updates = 0; //Reset value for next count.
			}
		}
	}
	
	/**
	 * This method initializes the 
	 * core variables and MenuState.
	 */
	public void init() {
        // Set values for the JFrame and game loop.
        setTitle("AI Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        isRunning = true; //The game has running.

        // By default, the game should start with the menu state.
        currentState = new MenuState(this, WIDTH, HEIGHT); // By default the menu should show up first.
        add(currentState);
        pack();

        // Fix for centering the window: First pack, then set the location, then set the visibility.
        setLocationRelativeTo(null);
        //setResizable(false);
        setVisible(true);
	}

	public void switchStateTo(GameState newState) {
        currentState = newState;
    }
	
	public void update() {
        currentState.update();
	}
	
	public void render() {
        currentState.repaint();
    }

    /**
     * For frame collision detection.
     * @return a rectangle the size of the Game Window.
     */
    public Rectangle getBounds() {
        return new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT);
    }


    public static void main(String[] args) {
        new Game();
    }
}