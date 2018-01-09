package com.jerry.aiproject.states;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jerry on 1/9/18.
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

}
