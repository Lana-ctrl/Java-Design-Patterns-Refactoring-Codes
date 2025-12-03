public class ShootCommand implements Command {

    private final Board board;
    private final GameEngine gameEngine;

    public ShootCommand(Board board, GameEngine gameEngine) {
        this.board = board;
        this.gameEngine = gameEngine;
    }

    @Override
    public void execute() {
        // Fire a shot only if we are in game and there is no active shot
        if (board.isIngame() && !board.getShot().isVisible()) {
            int x = board.getPlayer().getX();
            int y = board.getPlayer().getY();
            
            // Get the shot and fire with current strategy
            Shot shot = board.getShot();
            shot.fire(x, y, gameEngine.getCurrentShotStrategy());
        }
    }
}