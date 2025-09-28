import java.awt.image.BufferedImage;

/**
 * Base class for all game sprites.
 */
public class Sprite {

    private boolean visible;
    private BufferedImage image;
    protected int x;
    protected int y;
    protected boolean dying;
    protected int dx;

    /*
     * Constructor
     */
    public Sprite() {
        visible = true;
    }

    /*
     * Marks the sprite as dead/invisible
     */
    public void die() {
        visible = false;
    }

    /*
     * Visibility
     */
    public boolean isVisible() {
        return visible;
    }

    protected void setVisible(boolean visible) {
        this.visible = visible;
    }

    /*
     * Image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    /*
     * Position
     */
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /*
     * Dying state
     */
    public void setDying(boolean dying) {
        this.dying = dying;
    }

    public boolean isDying() {
        return dying;
    }
}
