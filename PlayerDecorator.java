import java.awt.image.BufferedImage;

public abstract class PlayerDecorator extends Player {

    protected final Player wrappedPlayer;
    protected boolean expired = false;

    public PlayerDecorator(Player player) {
        this.wrappedPlayer = player;

        this.x = player.getX();
        this.y = player.getY();
        BufferedImage img = player.getImage();
        if (img != null) {
            this.setImage(img);
        }
    }


    public Player getWrappedPlayer() {
        return wrappedPlayer;
    }

    
  
    public boolean isExpired() {
        return expired;
    }

 
    @Override
    public void act() {
        //c oordinates
        wrappedPlayer.act();
        this.x = wrappedPlayer.getX();
        this.y = wrappedPlayer.getY();
        this.dx = wrappedPlayer.dx;
        this.setImage(wrappedPlayer.getImage());
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        wrappedPlayer.keyPressed(e);
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        wrappedPlayer.keyReleased(e);
    }

}
