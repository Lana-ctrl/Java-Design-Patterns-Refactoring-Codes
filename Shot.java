import java.awt.image.BufferedImage;
import core.ResourceManager;

public class Shot extends Sprite {

    private static final String SHOT_IMAGE_PATH = "/img/shot.png"; 
    private static final int H_SPACE = 6;
    private static final int V_SPACE = 1;

    // Strategy variable
    private ShotMovementStrategy movementStrategy;

    /*
     * Default constructor - THIS MUST WORK LIKE BEFORE!
     */
    public Shot() {
        // This is what Board.gameInit() calls
        BufferedImage shotImage = ResourceManager.getInstance().loadImage(SHOT_IMAGE_PATH);
        setImage(shotImage);
        setVisible(false); // Start as not visible
        // Default to straight movement
        this.movementStrategy = new StraightShotMovement();
    }

    /*
     * Method to fire a shot from player position
     * This is what ShootCommand should call, not a constructor!
     */
    public void fire(int x, int y) {
        setX(x + H_SPACE);
        setY(y - V_SPACE);
        setVisible(true);
    }

    /*
     * Method to fire with a specific strategy
     */
    public void fire(int x, int y, ShotMovementStrategy movementStrategy) {
        setX(x + H_SPACE);
        setY(y - V_SPACE);
        setVisible(true);
        this.movementStrategy = movementStrategy;
    }

    /*
     * Update method called every game cycle
     */
    public void update() {
        if (movementStrategy != null && isVisible()) {
            movementStrategy.move(this);  
        }
    }

    // Allow changing movement strategy at runtime
    public void setMovementStrategy(ShotMovementStrategy newStrategy) {
        this.movementStrategy = newStrategy;
    }
}