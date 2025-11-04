import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.LinkedList;
import core.ResourceManager;





import javax.swing.JPanel;

import core.ResourceManager;


/**
 * Board class: main game panel.
 */
public class Board extends JPanel implements Runnable, Commons {

    private static final long serialVersionUID = 1L;

    private Dimension d;
    private ArrayList<Alien> aliens;
    private Player player;
    private Shot shot;
    private GameOver gameend;
    private Won vunnet;

    private int alienX = 150;
    private int alienY = 25;
    private int direction = -1;
    private int deaths = 0;

    private boolean ingame = true;
    private boolean havewon = true;
    private static final String EXPLOSION_IMAGE = "/img/explosion.png";
    private static final String ALIEN_IMAGE = "/img/alien.png";
    private String message = "Seu planeta nos pertence agora...";

    private Thread animator;


    // for decorator pattern
    private List<PowerUp> powerUps;
    private static final int POWERUP_SPAWN_CHANCE_PERCENT = 80; // when alien dies, 20% chance to spawn powerup  

    //for facade pattern
    private GameEngine gameEngine;

    /*
     * Constructor
     */
    public Board() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGTH);
        setBackground(Color.black);
       // for facade 
        gameEngine = new GameEngine(this);

        gameInit();
        setDoubleBuffered(true);
    }

    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    public void gameInit() {
        aliens = new ArrayList<>();
        powerUps = new LinkedList<>();


        // Load the alien image once
        BufferedImage alienImage = ResourceManager.getInstance().loadImage(ALIEN_IMAGE);

        for (int i = 0; i < 4; i++) {
    for (int j = 0; j < 6; j++) {
        boolean weak = (i == 3); // make last row WeakAliens
        Alien alien = AlienFactory.createAlien(weak, alienX + 18 * j, alienY + 18 * i);
        aliens.add(alien);
    }
}



        player = new Player();
        shot = new Shot();

        // Force a PowerUp spawn at start for testing
        powerUps.add(new PowerUp(270, 300)); // adjust coordinates if needed


        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
    }

    public void drawAliens(Graphics g) {
        for (Alien alien : aliens) {
            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }
            if (alien.isDying()) {
                alien.die();
            }
        }
    }

    public void drawPlayer(Graphics g) {
    if (player.isVisible()) {
        g.drawImage(player.getImage(), player.getX(), player.getY(), this);
    }

    // --- BLUE SHIELD EFFECT when decorator is active ---
    if (player instanceof ShieldedPlayer) {
        int px = player.getX();
        int py = player.getY();
        int w = PLAYER_WIDTH;
        int h = PLAYER_HEIGHT;

        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                            java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // glowing blue ring
        g2.setColor(new java.awt.Color(0, 150, 255, 180)); // bright blue
        g2.setStroke(new java.awt.BasicStroke(4));
        g2.drawOval(px - 6, py - 6, w + 12, h + 12);

        // inner glow effect
        g2.setColor(new java.awt.Color(0, 200, 255, 80));
        g2.fillOval(px - 4, py - 4, w + 8, h + 8);
    }

    if (player.isDying()) {
        player.die();
        havewon = false;
        ingame = false;
    }
}



    public void drawGameEnd(Graphics g) {
        g.drawImage(gameend.getImage(), 0, 0, this);
    }

    public void drawShot(Graphics g) {
        if (shot.isVisible()) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    public void drawBombing(Graphics g) {
        for (Alien a : aliens) {
            Bomb b = a.getBomb();
            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }
// for decorator pattern 
    public void drawPowerUps(Graphics g) {
    if (powerUps == null) return;
    for (PowerUp pu : powerUps) {
        if (!pu.isCollected()) {
            if (pu.getImage() != null)
                g.drawImage(pu.getImage(), pu.getX(), pu.getY(), this);
        }
    }
}


    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (ingame) {
            g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            // for decorator pattern
            drawBombing(g);
            drawPowerUps(g); 

        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void gameOver() {
        Graphics g = this.getGraphics();

        gameend = new GameOver();
        vunnet = new Won();

        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGTH);
        if (havewon) {
            g.drawImage(vunnet.getImage(), 0, 0, this);
        } else {
            g.drawImage(gameend.getImage(), 0, 0, this);
        }
        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2,
                BOARD_WIDTH / 2);
    }

// adjusted for decorator pattern 
public void animationCycle() {

    if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
        ingame = false;
        message = "Parabéns! Você salvou a galáxia!";
    }

    // --- DECORATOR UNWRAP ---
    // If player is decorated (ShieldedPlayer), check if the shield expired
    if (player instanceof PlayerDecorator) {
        PlayerDecorator pd = (PlayerDecorator) player;
        if (pd.isExpired()) {
            player = pd.getWrappedPlayer(); // unwrap back to normal player
        }
    }

    // --- PLAYER MOVEMENT ---
    player.act();

    // --- SHOT LOGIC ---
    if (shot.isVisible()) {
        int shotX = shot.getX();
        int shotY = shot.getY();

        for (Alien alien : aliens) {
            if (alien.isVisible() && shot.isVisible()) {
                int alienX = alien.getX();
                int alienY = alien.getY();

                if (shotX >= alienX && shotX <= (alienX + ALIEN_WIDTH) &&
                    shotY >= alienY && shotY <= (alienY + ALIEN_HEIGHT)) {

                    BufferedImage explosionImage = ResourceManager.getInstance().loadImage(EXPLOSION_IMAGE);
                    alien.setImage(explosionImage);
                    alien.setDying(true);
                    deaths++;
                    shot.die();

                    // --- POWERUP SPAWN LOGIC (Decorator trigger) ---
                    int chance = new Random().nextInt(100);
                    if (chance < 80) { // 20% chance to spawn
                        PowerUp pu = new PowerUp(alien.getX(), alien.getY());
                        powerUps.add(pu);
                        System.out.println("PowerUp spawned at " + pu.getX() + "," + pu.getY());

                    }
                }
            }
        }

        int y = shot.getY() - 8;
        if (y < 0) shot.die();
        else shot.setY(y);
    }

    // --- ALIEN MOVEMENT ---
    for (Alien a : aliens) {
        int x = a.getX();
        if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
            direction = -1;
            for (Alien a2 : aliens) {
                a2.setY(a2.getY() + GO_DOWN);
            }
        }
        if (x <= BORDER_LEFT && direction != 1) {
            direction = 1;
            for (Alien a2 : aliens) {
                a2.setY(a2.getY() + GO_DOWN);
            }
        }
    }

    for (Alien alien : aliens) {
        if (alien.isVisible()) {
            int y = alien.getY();
            if (y > GROUND - ALIEN_HEIGHT) {
                havewon = false;
                ingame = false;
                message = "Aliens estão invadindo a galáxia!";
            }
            alien.act(direction);
        }
    }

    // --- BOMB LOGIC ---
    Random generator = new Random();
    for (Alien a : aliens) {
        int chance = generator.nextInt(15);
        Bomb b = a.getBomb();
        if (chance == CHANCE && a.isVisible() && b.isDestroyed()) {
            b.setDestroyed(false);
            b.setX(a.getX());
            b.setY(a.getY());
        }

        int bombX = b.getX();
        int bombY = b.getY();
        int playerX = player.getX();
        int playerY = player.getY();

        if (player.isVisible() && !b.isDestroyed()) {
            
           if (bombX >= playerX && bombX <= (playerX + PLAYER_WIDTH) &&
                 bombY >= playerY && bombY <= (playerY + PLAYER_HEIGHT)) {

                        BufferedImage explosionImage =
                            ResourceManager.getInstance().loadImage(EXPLOSION_IMAGE);

                        player.setImage(explosionImage);
                        player.setDying(true);     // ShieldedPlayer intercepts this call
                        b.setDestroyed(true);
                    }
        }


        if (!b.isDestroyed()) {
            b.setY(b.getY() + 1);
            if (b.getY() >= GROUND - BOMB_HEIGHT) {
                b.setDestroyed(true);
            }
        }
    }

    // --- POWERUP LOGIC (movement + pickup) ---
    if (powerUps != null && !powerUps.isEmpty()) {
        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUp pu = it.next();

            if (pu.isCollected()) {
                it.remove();
                continue;
            }

            pu.act(); // move down slowly

            // remove if it falls below ground
            if (pu.getY() > GROUND) {
                it.remove();
                continue;
            }

            // collision with player
            int puX = pu.getX();
            int puY = pu.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() &&
                puX >= playerX && puX <= (playerX + PLAYER_WIDTH) &&
                puY >= playerY && puY <= (playerY + PLAYER_HEIGHT)) {

                // collect the powerup
                pu.setCollected(true);
                it.remove();

                // apply shield decorator if not already shielded
                if (!(player instanceof PlayerDecorator)) {
                    player = new ShieldedPlayer(player);
                    System.out.println("Shield activated!");
                } else {
                    System.out.println("Already shielded!");
                }
            }
        }
    }
}


    public void run() {
        long beforeTime = System.currentTimeMillis();
        while (ingame) {
            repaint();
            animationCycle();

            long timeDiff = System.currentTimeMillis() - beforeTime;
            long sleep = DELAY - timeDiff;
            if (sleep < 0) sleep = 1;

            try { Thread.sleep(sleep); } catch (InterruptedException e) { System.out.println("interrupted"); }

            beforeTime = System.currentTimeMillis();
        }
        gameOver();
    }

        private class TAdapter extends KeyAdapter {
        public void keyReleased(KeyEvent e) { 
            gameEngine.handleKeyRelease(e); 
        }
        public void keyPressed(KeyEvent e) {
            gameEngine.handleKeyPress(e);
        }
    }
        // === FACADE PATTERN GETTERS ===
    public boolean isIngame() { return ingame; }
    public boolean hasWon() { return havewon; }
    public String getMessage() { return message; }
    public ArrayList<Alien> getAliens() { return aliens; }
    public Player getPlayer() { return player; }
    public Shot getShot() { return shot; }
    public void setShot(Shot shot) { this.shot = shot; }
}