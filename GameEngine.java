// GameEngine.java - updated with Command pattern
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Facade that provides a simple interface to the complex game system.
 * This version applies the Command design pattern to handle player input:
 * - Movement (left / right)
 * - Shooting
 * - Dash ability
 * - Toggle shot type (Z key)
 */
public class GameEngine {

    private Board board;

    // Commands for key press / release
    private Map<Integer, Command> pressCommands = new HashMap<>();
    private Map<Integer, Command> releaseCommands = new HashMap<>();

    // Track current shot type
    private ShotMovementStrategy currentShotStrategy = new StraightShotMovement();

    public GameEngine(Board board) {
        this.board = board;
        initCommands();
    }

    private void initCommands() {
        Player player = board.getPlayer();

        // Movement commands
        pressCommands.put(KeyEvent.VK_LEFT, new MoveLeftCommand(player));
        pressCommands.put(KeyEvent.VK_RIGHT, new MoveRightCommand(player));

        Command stopCommand = new StopMoveCommand(player);
        releaseCommands.put(KeyEvent.VK_LEFT, stopCommand);
        releaseCommands.put(KeyEvent.VK_RIGHT, stopCommand);

        // Shooting command - pass this GameEngine instance
        pressCommands.put(KeyEvent.VK_SPACE, new ShootCommand(board, this));

        // Dash ability
        pressCommands.put(KeyEvent.VK_SHIFT, new DashCommand(player));
        
        // NEW: Toggle shot type with 'Z' key
        pressCommands.put(KeyEvent.VK_Z, new Command() {
            @Override
            public void execute() {
                toggleShotType();
            }
        });
    }
    
    // NEW: Method to toggle between straight and zigzag shots
    public void toggleShotType() {
        if (currentShotStrategy instanceof StraightShotMovement) {
            currentShotStrategy = new ZigZagShotMovement();
            System.out.println("Switched to ZIGZAG shots!");
        } else {
            currentShotStrategy = new StraightShotMovement();
            System.out.println("Switched to STRAIGHT shots!");
        }
    }
    
    // NEW: Get current shot strategy
    public ShotMovementStrategy getCurrentShotStrategy() {
        return currentShotStrategy;
    }

    // Delegation methods to keep GameEngine as a Facade over Board
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

    public void drawGameEnd(Graphics g) {
        board.drawGameEnd(g);
    }

    // New input handling using Command pattern
    public void handleKeyPress(KeyEvent e) {
        Command command = pressCommands.get(e.getKeyCode());
        if (command != null) {
            command.execute();
        }
    }

    public void handleKeyRelease(KeyEvent e) {
        Command command = releaseCommands.get(e.getKeyCode());
        if (command != null) {
            command.execute();
        }
    }

    // Getters to access original Board state
    public boolean isIngame() { return board.isIngame(); }
    public boolean hasWon()   { return board.hasWon(); }
    public String getMessage(){ return board.getMessage(); }
}