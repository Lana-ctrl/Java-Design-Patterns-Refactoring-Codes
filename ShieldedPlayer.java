
public class ShieldedPlayer extends PlayerDecorator {

    private boolean shieldActive = true;
    private long shieldStartTime;
    private static final long SHIELD_DURATION_MS = 8000; 

    public ShieldedPlayer(Player wrappedPlayer) {
        super(wrappedPlayer);
        shieldStartTime = System.currentTimeMillis();
        System.out.println("ShieldedPlayer: Shield activated!");
    }

    @Override
    public void act() {
        wrappedPlayer.act();
    }

    @Override
    public void setDying(boolean dying) {
    if (shieldActive) {
        System.out.println("💥 Shield absorbed the hit! Player survives.");
        shieldActive = false;  // turn shield off
    } else {
        wrappedPlayer.setDying(dying);  // now allow normal death
    }
}


    @Override
    public boolean isDying() {
        // only dying if shield is gone and underlying player is dying
        return !shieldActive && wrappedPlayer.isDying();
    }




    @Override
    public boolean isVisible() {
        return wrappedPlayer.isVisible();
    }

    @Override
    public int getX() {
        return wrappedPlayer.getX();
    }

    @Override
    public int getY() {
        return wrappedPlayer.getY();
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        wrappedPlayer.keyPressed(e);
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        wrappedPlayer.keyReleased(e);
    }

    @Override
    public void die() {
        wrappedPlayer.die();
    }

    // for automatic expiration (optional)
    public boolean isExpired() {
        long elapsed = System.currentTimeMillis() - shieldStartTime;
        return elapsed > SHIELD_DURATION_MS && !shieldActive;
    }

    public Player getWrappedPlayer() {
        return wrappedPlayer;
    }
}

