package com.jerry.aiproject.core;

import javax.swing.*;
import java.awt.*;

/**
 * Parent abstract class for all
 * Game states, it won't be
 * directly instantiated.
 * @author Jerry
 */
public abstract class GameState extends JPanel {

    // Used to identify the states.
    public enum GameStateType {
        PLAY {
            @Override
            public String toString() { return "AI Game"; }
        },
        MENU {
            @Override
            public String toString() { return "Menu"; }
        }
    }
    protected Game game; // Game object that the GameStates need for switching states.
    protected GameStateType gameStateType;
    private int frames, updates; // Values to display stats on the window.
    private Font infoFont; // Font to use for the FPS/Updates counter.

    public GameState(Game g, GameStateType type) {
        super();
        game = g;
        gameStateType = type;
        infoFont = new Font(Font.MONOSPACED, Font.BOLD, 16);

        setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        setFocusable(true);
        setDoubleBuffered(true);
    }

    public void updateInfo(int f, int u) {
        frames = f;
        updates = u;
    }

    /* Recommended to override paintComponent rather then Paint */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Using Graphics2D rather than Graphics for more 2D functionality.
        // Passing a copy rather than a reference, since the graphics object
        // Is being modified constantly, it's better that each state modifies
        // Their own graphics object.
        render((Graphics2D)g.create());

        // Update the FPS/Updates counter.
        g.setColor(Color.WHITE);
        g.setFont(infoFont);
        g.drawString("FPS: " + frames + " Updates: " + updates, 16, 24);

        g.dispose();
    }

    public abstract void init();
    public abstract void update();
    public abstract void render(Graphics2D g2d);

}
