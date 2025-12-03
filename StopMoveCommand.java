public class StopMoveCommand implements Command {
    
    private final Player player;
    
    public StopMoveCommand(Player player) {
        this.player = player;
    }
    
    @Override
    public void execute() {
        player.stop();
    }
}