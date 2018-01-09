package com.jerry.aiproject.states;

import javax.swing.*;
import java.awt.*;

/**
 * Parent abstract class for all
 * Game states, it won't be
 * directly instantiated.
 * @author Jerry
 */
public abstract class State extends JPanel {

    public State(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        setDoubleBuffered(true);
    }

    public abstract void init();
    public abstract void update();
    public abstract void render(Graphics g);

    public void paint(Graphics g) {
        super.paint(g);

        render(g);

//        g.setColor(Color.RED);
//        g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
        g.dispose();
    }

}
