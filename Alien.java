import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

public class Alien extends Sprite {

    protected int speed = 2; // normal speed
    private Bomb bomb;

    public Alien(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 2;

        bomb = new Bomb(x, y);
        // Image will be set later by Factory
    }

    public void act(int direction) {
        this.x += direction * speed;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }
}
