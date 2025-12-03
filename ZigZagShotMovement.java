public class ZigZagShotMovement implements ShotMovementStrategy {

    private int direction = 1;  // 1 = right, -1 = left

    @Override
    public void move(Shot shot) {
        // Move upward
        shot.setY(shot.getY() - 5);

        // Zig-zag movement
        shot.setX(shot.getX() + (direction * 3));

        // Change direction at boundaries
        if (shot.getX() < 20 || shot.getX() > 760) {
            direction *= -1; 
        }
    }
}