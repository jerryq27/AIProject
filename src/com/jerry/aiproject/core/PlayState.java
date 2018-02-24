package com.jerry.aiproject.core;

import com.jerry.aiproject.ai.AStarSearch;
import com.jerry.aiproject.data.TileMap;
import com.jerry.aiproject.gameobjects.GameObject;
import com.jerry.aiproject.gameobjects.HealthPotion;
import com.jerry.aiproject.gameobjects.Player;
import com.jerry.aiproject.gameobjects.Weapon;
import com.jerry.aiproject.utils.GameObjectSpawner;
import com.jerry.aiproject.ai.Path;
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
    private PlayStateKeyInput playStateKeyInput;

    private Random rand; // TEST. Might need for enemies...

    private AStarSearch aStar;
    private boolean startWalk = false; // For recording purposes.

    private boolean generateNewPath; // Used to determine when to generate a new path. (Other idea, generate when and object has been removed from game.)

    public PlayState(Game game) {
        super(game, GameStateType.PLAY);
        init();
    }

    @Override
    public void init() {
        loader = new SpriteLoader("res/loveless_ritsuka.png"); // Load the sprite sheet.

        // Create and fill the map, and create the lava river.
        tileMap = new TileMap(Game.WIDTH, Game.HEIGHT);
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

        playStateKeyInput = new PlayStateKeyInput(this);
        playStateKeyInput.setUpKeyBindings();
    }

    @Override
    public void update() {
        player.update();

        if(startWalk) // For recording purposes.
        {
            // Generate a path using one of the algorithms.
            //aStar = new AStarSearch(tileMap, 100, true);
            //path = aStar.findPath(player, spawner.getObject(0));
            if(path == null)
            {
                System.out.println("Starting search..");
                //BreadthFirstSearch pathFinder = new BreadthFirstSearch(tileMap);
                AStarSearch pathFinder = new AStarSearch(tileMap);
                long startTime = System.currentTimeMillis();
                path = pathFinder.findPath(player, spawner.getObject(0));
                long endTime = System.currentTimeMillis();
                double time = endTime - startTime;
                System.out.println("Path generation took: " + time/1000.0 + " seconds.\n");
                path.printPath();
            }
            // generateNewPath = player.moveAlongPath(tileMap, path);
            // System.out.println(generateNewPath);

            // Check if the player is colliding with any object.
//            GameObject collidingObject = spawner.checkCollision(player);
//            if(collidingObject != null)
//            {
//                if(collidingObject instanceof Weapon)
//                {
//                    System.err.println("Got a weapon!");
//                    spawner.removeObject(collidingObject);
//                }
//                else if(collidingObject instanceof HealthPotion)
//                {
//                    System.err.println("Got a potion!");
//                    spawner.removeObject(collidingObject);
//                    if(player.health != 200)
//                        player.health += 150;
//                }
//            }
//            if(generateNewPath)
//            {
//                startWalk = false;
//                // path = aStar.findPath(player, spawner.getObject(0));
//                generateNewPath = false;
//            }
        }


//		// TEST: Works!!
        for (int i = 0; i < spawner.getItems().size(); i++)
        {
            GameObject currentObject = spawner.getObject(i);

            if(player.getBounds().intersects(currentObject.getBounds()))
            {
                if(currentObject instanceof HealthPotion)
                {
                    player.health += 150;
                    System.out.println("Got the potion.");
                }
                else if(currentObject instanceof Weapon)
                {
                    System.out.println("Got a weapon.");
                }
                spawner.removeObject(spawner.getObject(i));
            }
        }
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
     *
     * Note:
     * It is recommended to use Key Bindings for
     * Swing applications rather than the KeyListener
     * Classes. Using the KeyListener classes
     * Requires focus, and with Swing applications,
     * Any JComponent can have focus, making it
     * Difficult to track which JComponent has focus.
     * @author Jerry
     */
    private class PlayStateKeyInput {

        private InputMap inputMap;
        private ActionMap actionMap;
        private HashMap<Integer, String> movementKeyMap;
        private HashMap<Integer, String> stopMovementKeyMap;
        private HashMap<Integer, String> optionsKeyMap;

        /* Possible actions in the play state.*/
        // Movement actions.
        public static final String
                MOVE_UP = "Up",
                MOVE_LEFT = "Left",
                MOVE_DOWN = "Down",
                MOVE_RIGHT = "Right";
        // Stop movement actions.
        public static final String
                STOP_UP = "Stop Up",
                STOP_LEFT = "Stop Left",
                STOP_DOWN = "Stop Down",
                STOP_RIGHT = "Stop Right";
        // Other actions.
        public static final String
                GOTO_MENU = "SwitchToMenu",
                TEST = "StartWalk";

        public PlayStateKeyInput(JPanel playState) {
            inputMap = playState.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            actionMap = playState.getActionMap();

            createKeyMaps();
        }

        private void createKeyMaps() {
            movementKeyMap = new HashMap<>();
            stopMovementKeyMap = new HashMap<>();
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

            /* Stop Movement keys. */
            stopMovementKeyMap.put(KeyEvent.VK_W, STOP_UP);
            stopMovementKeyMap.put(KeyEvent.VK_A, STOP_LEFT);
            stopMovementKeyMap.put(KeyEvent.VK_S, STOP_DOWN);
            stopMovementKeyMap.put(KeyEvent.VK_D, STOP_RIGHT);

            stopMovementKeyMap.put(KeyEvent.VK_UP, STOP_UP);
            stopMovementKeyMap.put(KeyEvent.VK_LEFT, STOP_LEFT);
            stopMovementKeyMap.put(KeyEvent.VK_DOWN, STOP_DOWN);
            stopMovementKeyMap.put(KeyEvent.VK_RIGHT, STOP_RIGHT);

            /* Option keys */
            optionsKeyMap.put(KeyEvent.VK_ESCAPE, GOTO_MENU);
            optionsKeyMap.put(KeyEvent.VK_ENTER, TEST);
        }

        public void setUpKeyBindings() {
            /* Set up the movement key mappings. */
            for(Integer key : movementKeyMap.keySet())
            {
                String action = movementKeyMap.get(key);
                // Third argument is asking if this is a key released event.
                inputMap.put(KeyStroke.getKeyStroke(key, 0, false), action);
                actionMap.put(action, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        player.keyPressed(key);
                    }
                });
            }

            /* Set up the stop movement key mappings. */
            for(Integer key : stopMovementKeyMap.keySet())
            {
                String action = stopMovementKeyMap.get(key);
                inputMap.put(KeyStroke.getKeyStroke(key, 0, true), action);
                actionMap.put(action, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        player.keyReleased(key);
                    }
                });
            }

            /* Set up the options key mappings. */
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
                                game.switchStateTo(GameStateType.MENU);
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
