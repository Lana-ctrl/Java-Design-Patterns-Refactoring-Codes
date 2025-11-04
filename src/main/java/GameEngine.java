// GameEngine.java
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * Facade that provides a simple interface to the complex game system
 * WITHOUT changing any game logic or functionality
 */
public class GameEngine {
    private Board board;
    
    public GameEngine(Board board) {
        this.board = board;
    }
    
    // Simple delegation methods - NO LOGIC CHANGES
    public void gameInit() {
        board.gameInit();
    }
    
    public void animationCycle() {
        board.animationCycle();
    }
    
    public void drawAliens(Graphics g) {
        board.drawAliens(g);
    }
    
    public void drawPlayer(Graphics g) {
        board.drawPlayer(g);
    }
    
    public void drawShot(Graphics g) {
        board.drawShot(g);
    }
    
    public void drawBombing(Graphics g) {
        board.drawBombing(g);
    }
    
    public void drawPowerUps(Graphics g) {
        board.drawPowerUps(g);
    }
    
    public void handleKeyPress(KeyEvent e) {
        // Original key handling logic
        board.getPlayer().keyPressed(e);
        
        if (board.isIngame()) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE && !board.getShot().isVisible()) {
                int x = board.getPlayer().getX();
                int y = board.getPlayer().getY();
                board.setShot(new Shot(x, y));
            }
        }
    }
    
    public void handleKeyRelease(KeyEvent e) {
        board.getPlayer().keyReleased(e);
    }
    
    // Getters to access original Board state
    public boolean isIngame() { return board.isIngame(); }
    public boolean hasWon() { return board.hasWon(); }
    public String getMessage() { return board.getMessage(); }
}