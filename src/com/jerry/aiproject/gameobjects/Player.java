package com.jerry.aiproject.gameobjects;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.jerry.aiproject.ai.AIMovement;
import com.jerry.aiproject.ai.Path;
import com.jerry.aiproject.core.*;
import com.jerry.aiproject.data.Sprite;
import com.jerry.aiproject.data.TileMap;
import com.jerry.aiproject.utils.*;
import com.jerry.aiproject.ai.Node;

/**
 * This class defines the GameObject Player.
 * The player is controlled by KeyInput and
 * can interact with both weapons and enemies.
 * @author Jerry
 */
public class Player extends GameObject implements Movement, AIMovement {

    private final int SPEED = 2;

	// Object's movement variables.
    private int delX, delY;
    // Save the initial position for tile-based movement.
    private int initialPosX, initialPosY;
    private int targetPosX, targetPosY;
	// Walk images without weapons.
	private BufferedImage[] walkDown, walkUp, walkRight, walkLeft;
	// Animation objects for the animations.
	private Animation downAnim, upAnim, rightAnim, leftAnim; 
	public int health; //Player health.

	// Booleans needed to fix the Sticky Keys problem, solution from Java-Gaming.org.
	private boolean isUp, isDown, isRight, isLeft;
    // Boolean needed to implement tile-based movement.
    private boolean isMoving = false;

	public Player(int xPos, int yPos) {
		super(xPos, yPos, GameObjectType.PLAYER);
		
		init();
	}

	/**
	 * This method initializes all 
	 * player variables. 
	 */
	@Override
	public void init() {
		initialPosX = getX();
        initialPosY = getY();
        // Set the target values otherwise the movement is going to go towards 0.
        targetPosX = initialPosX;
        targetPosY = initialPosY;
		initialImage = SpriteLoader.loadSprite(new Sprite(1, 1));

        loadAnimationFrames();
		downAnim = new Animation(walkDown, 7);
		upAnim = new Animation(walkUp, 7);
		rightAnim = new Animation(walkRight, 7);
		leftAnim = new Animation(walkLeft, 7);
		
		health = 50; //FOR SIA
	}

	/**
	 * This method updates any player
	 * related variables. It is called
	 * in the main game loop. 
	 */
	@Override
	public void update() {
        // Tile-based movement implementation.
        if(isMoving)
        {
            checkCollisions();
            setX(getX() + getDelX());
            setY(getY() + getDelY());

            if(isUp) { moveUp(); }
            else if(isDown) { moveDown(); }
            else if(isRight) { moveRight(); }
            else if(isLeft) { moveLeft(); }
            else
            {
                // Keep moving if the target hasn't been reached.
                if(getY() > targetPosY) { moveUp(); }
                else if(getY() < targetPosY) { moveDown(); }
                else if(getX() < targetPosX) { moveRight(); }
                else if(getX() > targetPosX) { moveLeft(); }
                else // Stop all movement once the target is reached.
                {
                    setDelX(0);
                    setDelY(0);
                    initialPosX = getX();
                    initialPosY = getY();
                    isMoving = false;
                }
            }
        }
    }

    /**
     * This method handles the drawings
     *  of the player, it is called in
     *  the main game loop.
     */
    @Override
    public void render(Graphics2D g2d) {
        // Drawing Images: image, X-Position, Y-Position, width, height, ImageObserver.
        if(isMoving)
        {
            if(isUp || initialPosY > targetPosY)
            { upAnim.drawAnimation(g2d, getX(), getY(), Sprite.WIDTH, Sprite.HEIGHT); }
            else if(isDown || initialPosY < targetPosY)
            { downAnim.drawAnimation(g2d, getX(), getY(), Sprite.WIDTH, Sprite.HEIGHT); }
            else if(isRight || initialPosX < targetPosX)
            { rightAnim.drawAnimation(g2d, getX(), getY(), Sprite.WIDTH, Sprite.HEIGHT); }
            else if(isLeft || initialPosX > targetPosX)
            { leftAnim.drawAnimation(g2d, getX(), getY(), Sprite.WIDTH, Sprite.HEIGHT); }
        }
        else { g2d.drawImage(initialImage, getX(), getY(), Sprite.WIDTH, Sprite.HEIGHT, null); }

        // DRAW HEALTH BAR
        if(health <= 50)
            g2d.setColor(Color.RED);
        else
            g2d.setColor(Color.GREEN);
        g2d.fillRect(Game.WIDTH - 250, 10, health, 25);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(Game.WIDTH - 250, 10, 200, 25);
    }
	
	/**
	 * This method checks for any collisions
	 * The player might have with the wall, 
	 * or other game objects. 
	 */
	public void checkCollisions() {
        // Check for left and right collision.
        if(getX() <= 0) { setX(0); }
        else if(getX() >= Game.WIDTH - initialImage.getWidth()) { setX(Game.WIDTH - initialImage.getWidth()); }

        // Check for top and bottom collision.
        if(getY() <= 0) { setY(0); }
        else if(getY() >= Game.HEIGHT - initialImage.getHeight()) { setY(Game.HEIGHT - initialImage.getHeight()); }
	}

	@Override
	public void moveUp() {
        setDelY(-SPEED);
        upAnim.runAnimation();
        initialImage = walkUp[0];
        if(getY() == targetPosY)
        {
            // Once the tile is reached, update position values.
            initialPosY = getY();
            // Since the key is still being held down, update the target position too.
            targetPosY = initialPosY - Sprite.HEIGHT <= 0? 0 : initialPosY - Sprite.HEIGHT;
        }
    }

    @Override
    public void moveDown() {
        setDelY(SPEED);
        downAnim.runAnimation();
        initialImage = walkDown[0];
        if(getY() == targetPosY)
        {
            initialPosY = getY();
            targetPosY = initialPosY + Sprite.HEIGHT >= Game.HEIGHT? initialPosY : initialPosY + Sprite.HEIGHT;
        }
    }

    @Override
    public void moveRight() {
        setDelX(SPEED);
        rightAnim.runAnimation();
        initialImage = walkRight[0];
        if(getX() == targetPosX)
        {
            initialPosX = getX();
            targetPosX = initialPosX + Sprite.WIDTH >= Game.WIDTH? initialPosX: initialPosX + Sprite.WIDTH;
        }
    }

    @Override
    public void moveLeft() {
        setDelX(-SPEED);
        leftAnim.runAnimation();
        initialImage = walkLeft[0];
        if(getX() == targetPosX)
        {
            initialPosX = getX();
            targetPosX = initialPosX - Sprite.WIDTH <= 0? 0 : initialPosX - Sprite.WIDTH;
        }
    }

	/**
	 * This method loads all the animation images
	 * from the sprite sheet for the player animation.
	 */
	@Override
	public void loadAnimationFrames() {
		walkDown = new BufferedImage[]{
				SpriteLoader.loadSprite(new Sprite(1, 1)),
				SpriteLoader.loadSprite(new Sprite(1, 2)),
				SpriteLoader.loadSprite(new Sprite(1, 3)),
				SpriteLoader.loadSprite(new Sprite(1, 4))
		};
		walkRight = new BufferedImage[] {
				SpriteLoader.loadSprite(new Sprite(3, 1)),
				SpriteLoader.loadSprite(new Sprite(3, 2)),
				SpriteLoader.loadSprite(new Sprite(3, 3)),
				SpriteLoader.loadSprite(new Sprite(3, 4))
		};
        walkLeft = new BufferedImage[] {
				SpriteLoader.loadSprite(new Sprite(2, 1)),
				SpriteLoader.loadSprite(new Sprite(2, 2)),
				SpriteLoader.loadSprite(new Sprite(2, 3)),
				SpriteLoader.loadSprite(new Sprite(2, 4))
		};
        walkUp = new BufferedImage[] {
                SpriteLoader.loadSprite(new Sprite(4, 1)),
                SpriteLoader.loadSprite(new Sprite(4, 2)),
                SpriteLoader.loadSprite(new Sprite(4, 3)),
                SpriteLoader.loadSprite(new Sprite(4, 4))
        };
    }
	
	/**
	 * This method is required by the 
	 * AIMovemnt interface, it will moveToNode
	 * the player along the path passed 
	 * into the method. 
	 * @param path the path generated by an AI algorithm.
	 * @return whether the end of the path has been reached.
	 */
	@Override
	public boolean moveAlongPath(TileMap map, Path path) {
        Node currentNode = new Node(map.getRow(getY()), map.getCol(getX()));
        //Get the number of steps.
        int steps = path.getLength();
        boolean reachedTile = false;
        int dex = 0;
        while(currentNode.compareTo(path.getStep(steps - 1)) != 0)
        {
            Node step = path.getStep(dex);
            while(currentNode.compareTo(step) != 0)
            {
                targetPosX = map.getXCoord(step.getCol());
                targetPosY = map.getYCoord(step.getRow());
                if (getY() > targetPosY) {
                    this.keyPressed(KeyEvent.VK_UP);
                } else if (getY() < targetPosY) {
                    this.keyPressed(KeyEvent.VK_DOWN);
                } else if (getX() < targetPosX) {
                    this.keyPressed(KeyEvent.VK_RIGHT);
                } else if (getX() > targetPosX) {
                    this.keyPressed(KeyEvent.VK_LEFT);
                }
                currentNode = new Node(map.getRow(getY()), map.getCol(getX()));
            }
            dex++;
            this.keyReleased(KeyEvent.VK_UP);
            this.keyReleased(KeyEvent.VK_DOWN);
            this.keyReleased(KeyEvent.VK_RIGHT);
            this.keyReleased(KeyEvent.VK_LEFT);
        }
//        for(int i = 0; i < steps; i++)
//        {
//            Node step = path.getStep(i);
//            while(!reachedTile) { reachedTile = moveToNode(currentNode, step); }
//
//            this.keyReleased(KeyEvent.VK_UP);
//            this.keyReleased(KeyEvent.VK_DOWN);
//            this.keyReleased(KeyEvent.VK_RIGHT);
//            this.keyReleased(KeyEvent.VK_LEFT);
//
//            currentNode = step;
//            if(currentNode == path.getStep(steps - 1)) { return true; }
//        }
        return false;
    }

    /* Setters and getters required by the Movement Interface. */
    public void setDelX(int delX) {
        this.delX = delX;
    }

    public int getDelX() {
        return delX;
    }

    public void setDelY(int delY) {
        this.delY = delY;
    }

    public int getDelY() {
        return delY;
    }

	/**
	 * This method handles key input from
	 * The Game class's key listener. 
	 * @param key the KeyEvent.
	 */
	public void keyPressed(int key) {
        switch(key)
        {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                // This check prevents other input from being processed.
                if(!isMoving)
                {
                    isMoving = true;
                    isUp = true;
                }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if(!isMoving)
                {
                    isMoving = true;
                    isDown = true;
                }
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                if(!isMoving)
                {
                    isMoving = true;
                    isRight = true;
                }
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                if(!isMoving)
                {
                    isMoving = true;
                    isLeft = true;
                }
                break;
        }
	}
	
	/**
	 * This method handles key input from
	 * The Game class's key listener. 
	 * @param key the KeyEvent.
	 */
	public void keyReleased(int key) {
		switch(key)
        {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                isUp = false;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                isDown = false;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                isRight = false;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                isLeft = false;
                break;
        }
	}

    /**
     * This method is used to determine collisions
     * between the player and other GameObjects.
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), Sprite.WIDTH, Sprite.HEIGHT);
    }

}