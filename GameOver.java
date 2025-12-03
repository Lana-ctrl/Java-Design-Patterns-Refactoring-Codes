import java.awt.image.BufferedImage;

import core.ResourceManager;

/**
 * GameOver screen sprite.
 */
public class GameOver extends Sprite implements Commons {

    private static final String GAME_OVER_IMAGE_PATH = "/img/gameover.png"; // constant for image path
    private int width;

    /*
     * Constructor
     */
    public GameOver() {
        // Load image using ResourceManager singleton
        BufferedImage gameOverImage = ResourceManager.getInstance().loadImage(GAME_OVER_IMAGE_PATH);

        setWidth(gameOverImage.getWidth());
        setImage(gameOverImage);
        setX(0);
        setY(0);
    }

    /*
     * Getters & Setters
     */
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

