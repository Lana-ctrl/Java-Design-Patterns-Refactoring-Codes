public class DashCommand implements Command {

    private final Player player;

    public DashCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        player.dash();
    }
}
