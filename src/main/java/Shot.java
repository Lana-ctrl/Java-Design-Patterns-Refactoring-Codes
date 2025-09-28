import java.awt.image.BufferedImage;

import core.ResourceManager;

/**
 * Shot class representing the player's projectile.
 */
public class Shot extends Sprite {

    private static final String SHOT_IMAGE_PATH = "/img/shot.png"; // constant for image path
    private static final int H_SPACE = 6;
    private static final int V_SPACE = 1;

    /*
     * Default constructor
     */
    public Shot() {
    }

    /*
     * Constructor with initial position
     */
    public Shot(int x, int y) {
        // Load image using ResourceManager singleton
        BufferedImage shotImage = ResourceManager.getInstance().loadImage(SHOT_IMAGE_PATH);
        setImage(shotImage);

        setX(x + H_SPACE);
        setY(y - V_SPACE);
    }
}
