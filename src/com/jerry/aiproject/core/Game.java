package com.jerry.aiproject.core;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.JPanel;

import com.jerry.aiproject.core.TileMap.TileType;
import com.jerry.aiproject.gameobjects.*;
import com.jerry.aiproject.gameobjects.Weapon.WeaponType;
import com.jerry.aiproject.utils.*;
import com.jerry.aiproject.aialgorithms.*;

/**
 * This is the heart of the game and
 * runs the game loop. This will also 
 * be the panel which holds the game.
 * @author Jerry
 */
public class Game extends JPanel implements Runnable {
	
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

	//The dimensions of the panel. 
	public static final int WIDTH = 640, HEIGHT = 720; //20*32px X 15*48
	
	private TileMap tileMap; //The map of the game.
	private Path path;
	private Player player; //The player of the game. 
	private GameObjectSpawner spawner; //Controls the spawning of GameObjects. 
	private SpriteLoader loader; //Utility class that loads sprites. 
	
	private Thread thread; //The thread the game will be running on. 
	private Random rand; //TEST. Might need for enemies...
	private boolean inGame; //Flag for determining when the game starts/ends.
	
	private AStarSearch aStar;
	
	private boolean startWalk = false; //For recording purposes.
	private boolean generateNewPath; //USe to determine when to generate a new path. (Other idea, generate when and object has been removed from game.)
	
	public Game() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		setDoubleBuffered(true);
		
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
		while(inGame)
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
			repaint(); //Call paint, not in loop since we want our computer to draw as fast as possible.
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
	 * game related objects. 
	 */
	public void init() {
		inGame = true; //The game has started.
		addKeyListener(new KeyInput()); //Add custom inner class key listener for player movement.
		
		loader = new SpriteLoader("res/loveless_ritsuka.png"); //Load the sprite sheet. 
	
		//Create and fill the map, and create the lava river.
		tileMap = new TileMap(WIDTH, HEIGHT, 32, 48); 
		tileMap.fillMap(TileType.GRASS, true);  
		tileMap.fillCol(TileType.LAVA, 2, false); 
		
		//Create the player and the GameObjectSpawner to spawn GameObjects.
		player = new Player(tileMap.getXCoord(5), tileMap.getYCoord(5)); 
		spawner = new GameObjectSpawner();
		
		//Add the items.
		spawner.spawn(new HealthPotion(tileMap.getXCoord(15)/*(10)*/, tileMap.getYCoord/*(12)*/(14)));
		spawner.spawn(new Weapon(tileMap.getXCoord(7), tileMap.getYCoord(3), WeaponType.AX));
		spawner.spawn(new Weapon(tileMap.getXCoord(10), tileMap.getYCoord(3), WeaponType.SWORD));
		spawner.spawn(new Weapon(tileMap.getXCoord(13), tileMap.getYCoord(3), WeaponType.BOW));
		
		aStar = new AStarSearch(tileMap, 100, true);
		path = aStar.findPath(player, spawner.getObject(0));
//		BreadthFirstSearch breadthFirst = new BreadthFirstSearch(tileMap, 100);
//		path = breadthFirst.findPath(player, spawner.getObject(0));
	}
	
	public void update() {
		player.update();

		if(startWalk) //For recording purposes.
		{	
			generateNewPath = player.moveAlongPath(tileMap, path);
			System.out.println(generateNewPath);
			//Check if the player is colliding with any object.
			GameObject collidingObject = spawner.checkCollision(player);
			if(collidingObject != null) 
			{
				if(collidingObject instanceof Weapon)
				{
					System.err.println("Got a weapon!");
					spawner.removeObject(collidingObject);
				}
				else if(collidingObject instanceof HealthPotion) 
				{
					System.err.println("Got a potion!");
					spawner.removeObject(collidingObject);
					if(player.health != 200)
						player.health += 150;
				}
			}
			if(generateNewPath)
			{
				startWalk = false;
				path = aStar.findPath(player, spawner.getObject(0));
				generateNewPath = false;
			}
		}


//		//TEST: Works!!
//		if(spawner != null && player.getBounds().intersects(spawner.getObject(0).getBounds()))
//		{
//			if(player.health != 200)
//				player.health += 150;
//			spawner.removeObject(spawner.getObject(0));
//			spawner = null;
//
//			//Display a message and exit the game.
//			JOptionPane.showMessageDialog(this, "Player got the potion!");
//			System.exit(0);
//		}
	}
	
	public void render(Graphics g) {
		//Draw the map first, due to overlap.
		tileMap.render(g);  
		player.render(g);
		if(spawner != null)
			spawner.render(g);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		render(g);

        g.setColor(Color.RED);
        g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
		g.dispose();
	}

    /**
     * For frame collision detection.
     * @return a rectangle the size of the Game Window.
     */
    public Rectangle getBounds() {
        return new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT);
    }
	
	/**
	 * Private inner class for player movement.
	 */
	private class KeyInput extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			player.keyPressed(e.getKeyCode());
			
			//For recording purposes.
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
				startWalk = true;
		}
		@Override
		public void keyReleased(KeyEvent e){
			player.keyReleased(e.getKeyCode());
		}
	}
}