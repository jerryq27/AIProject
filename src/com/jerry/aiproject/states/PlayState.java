package com.jerry.aiproject.states;

import com.jerry.aiproject.aialgorithms.AStarSearch;
import com.jerry.aiproject.core.Game;
import com.jerry.aiproject.core.TileMap;
import com.jerry.aiproject.gameobjects.GameObject;
import com.jerry.aiproject.gameobjects.HealthPotion;
import com.jerry.aiproject.gameobjects.Player;
import com.jerry.aiproject.gameobjects.Weapon;
import com.jerry.aiproject.utils.GameObjectSpawner;
import com.jerry.aiproject.utils.Path;
import com.jerry.aiproject.utils.SpriteLoader;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created by jerry on 1/9/18.
 */
public class PlayState extends GameState {

    private TileMap tileMap; //The map of the game.
    private Path path;
    private Player player; //The player of the game.
    private GameObjectSpawner spawner; //Controls the spawning of GameObjects.
    private SpriteLoader loader; //Utility class that loads sprites.

    private Random rand; //TEST. Might need for enemies...
    private boolean inGame; //Flag for determining when the game starts/ends.

    private AStarSearch aStar;

    private boolean startWalk = false; //For recording purposes.
    private boolean generateNewPath; //USe to determine when to generate a new path. (Other idea, generate when and object has been removed from game.)

    public PlayState(int width, int height) {
        super(width, height);
        init();
    }

    @Override
    public void init() {
        inGame = true; //The game has started.
        addKeyListener(new PlayState.KeyInput()); //Add custom inner class key listener for player movement.

        loader = new SpriteLoader("res/loveless_ritsuka.png"); //Load the sprite sheet.

        //Create and fill the map, and create the lava river.
        tileMap = new TileMap(Game.WIDTH, Game.HEIGHT, 32, 48);
        tileMap.fillMap(TileMap.TileType.GRASS, true);
        tileMap.fillCol(TileMap.TileType.LAVA, 2, false);

        //Create the player and the GameObjectSpawner to spawn GameObjects.
        player = new Player(tileMap.getXCoord(5), tileMap.getYCoord(5));
        spawner = new GameObjectSpawner();

        //Add the items.
        spawner.spawn(new HealthPotion(tileMap.getXCoord(15)/*(10)*/, tileMap.getYCoord/*(12)*/(14)));
        spawner.spawn(new Weapon(tileMap.getXCoord(7), tileMap.getYCoord(3), Weapon.WeaponType.AX));
        spawner.spawn(new Weapon(tileMap.getXCoord(10), tileMap.getYCoord(3), Weapon.WeaponType.SWORD));
        spawner.spawn(new Weapon(tileMap.getXCoord(13), tileMap.getYCoord(3), Weapon.WeaponType.BOW));

        aStar = new AStarSearch(tileMap, 100, true);
        path = aStar.findPath(player, spawner.getObject(0));
//		BreadthFirstSearch breadthFirst = new BreadthFirstSearch(tileMap, 100);
//		path = breadthFirst.findPath(player, spawner.getObject(0));
    }

    @Override
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

    @Override
    public void render(Graphics g) {
        //Draw the map first, due to overlap.
        tileMap.render(g);
        player.render(g);
        if(spawner != null)
            spawner.render(g);
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
