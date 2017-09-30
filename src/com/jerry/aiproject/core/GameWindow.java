package com.jerry.aiproject.core;

import javax.swing.JFrame;

/**
 * This class contains the main method. 
 * It is also the JFrame container for 
 * the Game panel.
 * @author Jerry
 */
public class GameWindow extends JFrame {

	private Game game;

	public GameWindow() {
		game = new Game();
		add(game);
		pack();

		setTitle("AI Project");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args) {
		new GameWindow();
	}
}