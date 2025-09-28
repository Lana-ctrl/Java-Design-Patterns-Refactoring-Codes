import java.awt.image.BufferedImage;

import core.ResourceManager;

/**
 * Bomb class representing a bomb dropped by an Alien.
 */
public class Bomb extends Sprite {

    private static final String BOMB_IMAGE_PATH = "/img/bomb.png"; // constant for image path
    private boolean destroyed;

    /*
     * Constructor
     */
    public Bomb(int x, int y) {
        setDestroyed(true);
        this.x = x;
        this.y = y;

        // Load image using ResourceManager singleton
        BufferedImage bombImage = ResourceManager.getInstance().loadImage(BOMB_IMAGE_PATH);
        setImage(bombImage);
    }

    /*
     * Setters & Getters
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
