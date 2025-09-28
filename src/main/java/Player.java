import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import core.ResourceManager;

/**
 * Player class representing the player's spaceship.
 */
public class Player extends Sprite implements Commons {

    private static final int START_Y = 400;
    private static final int START_X = 270;

    private static final String PLAYER_IMAGE_PATH = "/img/craft.png"; // constant for image path
    private int width;

    /*
     * Constructor
     */
    public Player() {
        // Load image using ResourceManager singleton
        BufferedImage playerImage = ResourceManager.getInstance().loadImage(PLAYER_IMAGE_PATH);

        width = playerImage.getWidth();
        setImage(playerImage);
        setX(START_X);
        setY(START_Y);
    }

    /*
     * Move the player according to current dx
     */
    public void act() {
        x += dx;
        if (x <= 2)
            x = 2;
        if (x >= BOARD_WIDTH - 2 * width)
            x = BOARD_WIDTH - 2 * width;
    }

    /*
     * Handle key press events
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
