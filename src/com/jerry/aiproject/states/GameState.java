package com.jerry.aiproject.states;

import com.jerry.aiproject.core.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Parent abstract class for all
 * Game states, it won't be
 * directly instantiated.
 * @author Jerry
 */
public abstract class GameState extends JPanel {

    protected Game game; // Game object that the GameStates need for switching states.

    public GameState(Game game) {
        super();
        this.game = game;
        setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        setFocusable(true);
        setDoubleBuffered(true);
    }
    public abstract void init();
    public abstract void update();
    public abstract void render(Graphics2D g2d);

    /* Recommended to override paintComponent rather then Paint */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Using Graphics2D rather than Graphics for more 2D functionality.
        render((Graphics2D)g);

        g.dispose();
    }

}
