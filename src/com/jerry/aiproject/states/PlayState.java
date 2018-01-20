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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Random;

/**
 * This is the play state of the game.
 * This will display the game and
 * handle the main rendering of the game,
 * game events, and all other game logic.
 * @author Jerry
 */
public class PlayState extends GameState {

    private TileMap tileMap; // The map of the game.
    private Path path;
    private Player player; // The player of the game.
    private GameObjectSpawner spawner; // Controls the spawning of GameObjects.
    private SpriteLoader loader; // Utility class that loads sprites.

    private Random rand; // TEST. Might need for enemies...

    private AStarSearch aStar;

    private boolean startWalk = false; // For recording purposes.
    private boolean generateNewPath; // Used to determine when to generate a new path. (Other idea, generate when and object has been removed from game.)

    private PlayKeyInput playKeyInput;

    public PlayState(Game game) {
        super(game);
        init();
    }

    @Override
    public void init() {
        loader = new SpriteLoader("res/loveless_ritsuka.png"); // Load the sprite sheet.

        // Create and fill the map, and create the lava river.
        tileMap = new TileMap(Game.WIDTH, Game.HEIGHT, Game.WIDTH/20, Game.HEIGHT/15);
        tileMap.fillMap(TileMap.TileType.GRASS, true);
        tileMap.fillCol(TileMap.TileType.LAVA, 2, false);

        // Create the player and the GameObjectSpawner to spawn GameObjects.
        player = new Player(tileMap.getXCoord(5), tileMap.getYCoord(5));
        spawner = new GameObjectSpawner();

        // Add the items.
        spawner.spawn(new HealthPotion(tileMap.getXCoord(15)/*(10)*/, tileMap.getYCoord/*(12)*/(14)));
        spawner.spawn(new Weapon(tileMap.getXCoord(7), tileMap.getYCoord(3), Weapon.WeaponType.AX));
        spawner.spawn(new Weapon(tileMap.getXCoord(10), tileMap.getYCoord(3), Weapon.WeaponType.SWORD));
        spawner.spawn(new Weapon(tileMap.getXCoord(13), tileMap.getYCoord(3), Weapon.WeaponType.BOW));

        aStar = new AStarSearch(tileMap, 100, true);
        path = aStar.findPath(player, spawner.getObject(0));
//		BreadthFirstSearch breadthFirst = new BreadthFirstSearch(tileMap, 100);
//		path = breadthFirst.findPath(player, spawner.getObject(0));
        //addKeyListener(new KeyInput()); // Add custom inner class key listener for player movement.
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                System.out.println(mouseEvent.getX() + " " + mouseEvent.getY());
            }
        });
        playKeyInput = new PlayKeyInput(this);
        playKeyInput.setUpKeyBindings();
    }

    @Override
    public void update() {
        player.update();

        if(startWalk) // For recording purposes.
        {
            generateNewPath = player.moveAlongPath(tileMap, path);
            System.out.println(generateNewPath);
            // Check if the player is colliding with any object.
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


//		// TEST: Works!!
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
    public void render(Graphics2D g2d) {
        // Draw the map first, due to overlap.
        tileMap.render(g2d);
        player.render(g2d);
        if(spawner != null)
            spawner.render(g2d);
    }

    /**
     * Private inner class for player movement.
     */
    private class KeyInput extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("ARE WE EVEN HERE?");
            player.keyPressed(e.getKeyCode());

            // For recording purposes.
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                startWalk = true;
                System.out.println("pressing enter...");
            }
        }
        @Override
        public void keyReleased(KeyEvent e){
            player.keyReleased(e.getKeyCode());
        }
    }

    private class PlayKeyInput {

        private InputMap inputMap;
        private ActionMap actionMap;
        private HashMap<Integer, String> movementKeyMap;
        private HashMap<Integer, String> optionsKeyMap;

        /* Possible actions in the play state.*/
        // Movement actions.
        public static final String
                MOVE_UP = "Up",
                MOVE_LEFT = "Left",
                MOVE_DOWN = "Down",
                MOVE_RIGHT = "Right";
        // Other actions.
        public static final String
                GOTO_MENU = "SwitchToMenu",
                TEST = "StartWalk";

        public PlayKeyInput(JPanel playState) {
            inputMap = playState.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            actionMap = playState.getActionMap();

            createKeyMaps();
        }

        private void createKeyMaps() {
            movementKeyMap = new HashMap<>();
            optionsKeyMap = new HashMap<>();

            /* Movement keys. */
            movementKeyMap.put(KeyEvent.VK_W, MOVE_UP);
            movementKeyMap.put(KeyEvent.VK_A, MOVE_LEFT);
            movementKeyMap.put(KeyEvent.VK_S, MOVE_DOWN);
            movementKeyMap.put(KeyEvent.VK_D, MOVE_RIGHT);

            movementKeyMap.put(KeyEvent.VK_UP, MOVE_UP);
            movementKeyMap.put(KeyEvent.VK_LEFT, MOVE_LEFT);
            movementKeyMap.put(KeyEvent.VK_DOWN, MOVE_DOWN);
            movementKeyMap.put(KeyEvent.VK_RIGHT, MOVE_RIGHT);

            /* Option keys */
            optionsKeyMap.put(KeyEvent.VK_ESCAPE, GOTO_MENU);
            optionsKeyMap.put(KeyEvent.VK_ENTER, TEST);
        }

        public void setUpKeyBindings() {
            for(Integer key : movementKeyMap.keySet())
            {

            }

            for(Integer key : optionsKeyMap.keySet())
            {
                String action = optionsKeyMap.get(key);
                inputMap.put(KeyStroke.getKeyStroke(key, 0), action);
                actionMap.put(action, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String action = optionsKeyMap.get(key);
                        switch(action)
                        {
                            case GOTO_MENU:
                                game.switchStateTo("Menu");
                                break;
                            case TEST:
                                startWalk = true;
                                break;
                            default:
                                System.out.println(action);
                                break;
                        }
                    }
                });
            }
        }
    }
}
