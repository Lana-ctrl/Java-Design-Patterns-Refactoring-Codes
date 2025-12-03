public class StraightShotMovement implements ShotMovementStrategy {

    @Override
    public void move(Shot shot) {
        // Just move the shot upward
        // The Board class will handle boundary checking and hiding
        shot.setY(shot.getY() - 5);
    }
}