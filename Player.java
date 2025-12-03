import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import core.ResourceManager;

/**
 * Player class representing the player's spaceship.
 */
public class Player extends Sprite implements Commons {

    private static final int START_Y = 400;
    private static final int START_X = 270;

    private static final String PLAYER_IMAGE_PATH = "/img/craft.png";

    private int width;
    private int dashDistance = 40;
    private boolean dashing = false;
    private long dashStartTime = 0;

    /**
     * Constructor: loads the player sprite and sets the initial position.
     */
    public Player() {
        BufferedImage playerImage = ResourceManager.getInstance().loadImage(PLAYER_IMAGE_PATH);
        width = playerImage.getWidth();
        setImage(playerImage);
        setX(START_X);
        setY(START_Y);
    }

    /**
     * Move the player according to the current horizontal velocity dx.
     */
    public void act() {
        x += dx;
        if (x <= 2) {
            x = 2;
        }
        if (x >= BOARD_WIDTH - 2 * width) {
            x = BOARD_WIDTH - 2 * width;
        }
    }

    // Movement helpers used by Command objects
    public void moveLeft() {
        dx = -2;
    }

    public void moveRight() {
        dx = 2;
    }

    public void stop() {
        dx = 0;
    }

    /**
     * Dash ability: quickly move the player horizontally.
     * Uses current direction (dx) if available, otherwise dashes to the right.
     */
    public void dash() {
        if (dx < 0) {
            x -= dashDistance;
        } else {
            x += dashDistance;
        }

        // Clamp inside board
        if (x <= 2) {
            x = 2;
        }
        if (x >= BOARD_WIDTH - 2 * width) {
            x = BOARD_WIDTH - 2 * width;
        }

        // Start dash visual effect
        dashing = true;
        dashStartTime = System.currentTimeMillis();
    }

    /**
     * Helper used by the Board to know when to draw the dash glow effect.
     */
    public boolean isDashingEffectActive() {
        if (!dashing) {
            return false;
        }

        // Effect lasts for ~150 ms
        if (System.currentTimeMillis() - dashStartTime < 150) {
            return true;
        }

        dashing = false;
        return false;
    }

    /*
     * Handle key press events (legacy - input is now also handled via GameEngine
     * using Command objects, but we keep this for compatibility).
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -2;
        } else if (key == KeyEvent.VK_RIGHT) {
            dx = 2;
        }
    }

    /*
     * Handle key release events
     */
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }
}
