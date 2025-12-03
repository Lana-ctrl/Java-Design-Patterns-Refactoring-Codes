// PowerUp.java
import java.awt.image.BufferedImage;
import core.ResourceManager;

public class PowerUp extends Sprite {
    private static final String POWERUP_IMAGE_PATH = "/img/powerup_shield.png";
    private int speed = 1;
    private boolean collected = false;

    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;
        
        // Load image safely - don't let exceptions break the game
        try {
            BufferedImage img = ResourceManager.getInstance().loadImage(POWERUP_IMAGE_PATH);
            setImage(img);
        } catch (Exception e) {
            System.out.println("PowerUp image not found, but power-up will still work");
            // Continue without image - the power-up functionality should still work
        }
    }

    public void act() {
        this.y += speed;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean c) {
        this.collected = c;
    }
}
