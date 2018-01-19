package com.jerry.aiproject.states;

import com.jerry.aiproject.core.Game;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

/**
 * Main menu state of the game.
 * This will display the main
 * menu with the game options.
 * @author Jerry
 */
public class MenuState extends GameState implements MouseListener, MouseMotionListener {

    // Rectangles surrounding the texts.
    private FontMetrics fontMetrics;
    // Metrics for getting the rectanlges surrounding the drawn strings.
    private Rectangle2D titleRect, playButtonRect, quitButtonRect;
    private final int PADDING = 8; // Padding for the rectangles surrounding the boxes.
    // Values to check if the mouse is over the buttons.
    private boolean hoveringPlay = false, hoveringQuit = false;

    public MenuState(Game game) {
        super(game);
        init();
    }

    @Override
    public void init() {
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2d) {
        // Set up anti-aliasing.
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        // The border gets draw right on the edge, subtracting 1 ensures the full stroke is within the JFrame.
        g2d.drawRect(0, 0, Game.WIDTH - 1, Game.HEIGHT - 1);

        // Set up the text values for drawing strings.
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 50);
        g2d.setFont(font);
        // Setup the FontMetrics to get values for drawn strings.
        fontMetrics = g2d.getFontMetrics(g2d.getFont());

        drawMainTitle(g2d);
        drawPlayButton(g2d);
        drawQuitButton(g2d);
    }

    private void drawMainTitle(Graphics2D g2d) {
        String mainTitle = "AI Game";

        titleRect = fontMetrics.getStringBounds(mainTitle, g2d);
        int width = (int)titleRect.getWidth();
        int height = (int)titleRect.getHeight();

        // Calculatations to center the text on the screen.
        int x = (Game.WIDTH/2) - (width/2);
        int y = (Game.HEIGHT/2) - height;
        g2d.drawString(mainTitle, x, y);

        // Set the new rectangle values to define the title's area.
        titleRect = new Rectangle(
                x - PADDING,
                y - fontMetrics.getAscent(),
                width + PADDING * 2,
                height
        );
        // g2d.draw(titleRect);

    }

    private void drawPlayButton(Graphics2D g2d) {
        if(hoveringPlay)
            g2d.setColor(Color.CYAN);
        else if(!hoveringPlay)
            g2d.setColor(Color.WHITE);

        String playButtonText = "Play";
        playButtonRect = fontMetrics.getStringBounds(playButtonText, g2d);
        int width = (int)playButtonRect.getWidth();
        int height = (int)playButtonRect.getHeight();
        // Calculate the play button's draw location.
        int x = (Game.WIDTH/2) - (width/2);
        int y = (Game.HEIGHT/2) + height;

        g2d.drawString(playButtonText, x, y);
        // Fonts are rendered from a baseline that runs along the bottom of the text.
        // Subtracting the 'ascent' removes that displacement.
        playButtonRect = new Rectangle(
                x - PADDING,
                y - fontMetrics.getAscent(),
                width + PADDING * 2,
                height
        );
        g2d.draw(playButtonRect);
    }

    private void drawQuitButton(Graphics2D g2d) {
        if(hoveringQuit)
            g2d.setColor(Color.RED);
        else if(!hoveringQuit)
            g2d.setColor(Color.WHITE);

        String playButtonText = "Quit";
        quitButtonRect = fontMetrics.getStringBounds(playButtonText, g2d);
        int width = (int)quitButtonRect.getWidth();
        int height = (int)quitButtonRect.getHeight();
        // Calculate the play button's draw location.
        int x = (Game.WIDTH/2) - (width/2);
        int y = (Game.HEIGHT/2) + height * 3;

        g2d.drawString(playButtonText, x, y);
        // Fonts are rendered from a baseline that runs along the bottom of the text.
        // Subtracting the 'ascent' removes that displacement.
        quitButtonRect = new Rectangle(
                x - PADDING,
                y - fontMetrics.getAscent(),
                width + PADDING * 2,
                height
        );
        g2d.draw(quitButtonRect);
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {}

    /**
     * This method will detect if the mouse is over
     * the buttons and add an animation on hover.
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        int mouseX = mouseEvent.getX();
        int mouseY = mouseEvent.getY();
        if(playButtonRect.contains(mouseX, mouseY))
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            hoveringPlay = true;
        }
        else if(quitButtonRect.contains(mouseX, mouseY))
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            hoveringQuit = true;
        }
        else
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            hoveringPlay = false;
            hoveringQuit = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int mouseX = mouseEvent.getX();
        int mouseY = mouseEvent.getY();
        if(playButtonRect.contains(mouseX, mouseY))
        {
            game.switchStateTo("Play");
        }
        else if(quitButtonRect.contains(mouseX, mouseY))
        {
            System.exit(0);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}
}
