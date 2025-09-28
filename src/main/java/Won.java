import java.awt.image.BufferedImage;

import core.ResourceManager;

/**
 * Won screen sprite.
 */
public class Won extends Sprite implements Commons {

    private static final String WON_IMAGE_PATH = "/img/won.jpg"; // constant for image path
    private int width;

    /*
     * Constructor
     */
    public Won() {
        // Load image using ResourceManager singleton
        BufferedImage wonImage = ResourceManager.getInstance().loadImage(WON_IMAGE_PATH);

        width = wonImage.getWidth();
        setImage(wonImage);
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

