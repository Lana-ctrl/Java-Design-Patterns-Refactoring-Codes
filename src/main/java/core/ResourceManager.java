package core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;

/**
 * Simple Singleton ResourceManager (no cache, no multithreading).
 * Usage:
 *   ResourceManager rm = ResourceManager.getInstance();
 *   BufferedImage img = rm.loadImage("/screens/player.png");
 *
 * NOTE: loadImage reads the image from resources each call (no caching).
 */
public final class ResourceManager {
    // single instance holder
    private static ResourceManager instance;

    // private constructor prevents "new ResourceManager()" outside this class
    private ResourceManager() {
        // Useful debugging line while testing:
        // System.out.println("ResourceManager: constructor called");
    }

    // public global access point (lazy initialization)
    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    /**
     * Loads an image from the classpath and returns it.
     * This method does NOT cache results — it reads each time it's called.
     *
     * @param resourcePath path relative to classpath (e.g. "/screens/player.png")
     * @return BufferedImage or throws RuntimeException if not found/error
     */
    public BufferedImage loadImage(String resourcePath) {
        try (InputStream is = ResourceManager.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image: " + resourcePath, e);
        }
    }

    // Optional: prevent cloning (small safeguard)
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton - clone not allowed");
    }
}
