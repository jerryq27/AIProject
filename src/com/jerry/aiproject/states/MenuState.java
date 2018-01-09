package com.jerry.aiproject.states;

import java.awt.*;

/**
 * Main menu state of the game.
 * This will display the main
 * menu with the game options.
 * @author Jerry
 */
public class MenuState extends GameState {

    public MenuState(int width, int height) {
        super(width, height);
    }

    @Override
    public void init() {
//        JButton playButton = new JButton("Play");
//        JButton exitButton = new JButton("Exit");
//
//        add(playButton);
//        add(exitButton);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, 640, 720);
        //g.drawString("Hello world!", 0, 0);
    }
}
