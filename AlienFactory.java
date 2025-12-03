import java.awt.image.BufferedImage;

import core.ResourceManager;

public class AlienFactory {

    private static final String BASIC_ALIEN_IMAGE = "/img/alien.png";
    private static final String WEAK_ALIEN_IMAGE = "/img/alien.png"; // reuse same image for now

    /**
     * Create an Alien at (x, y).
     * @param weak true = create WeakAlien, false = normal Alien
     */
    public static Alien createAlien(boolean weak, int x, int y) {
        Alien alien;

        if (weak) {
            alien = new WeakAlien(x, y);
            alien.setImage(ResourceManager.getInstance().loadImage(WEAK_ALIEN_IMAGE));
        } else {
            alien = new Alien(x, y);
            alien.setImage(ResourceManager.getInstance().loadImage(BASIC_ALIEN_IMAGE));
        }

        return alien;
    }
}
