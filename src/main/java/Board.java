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
import java.util.Random;

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

    /*
     * Constructor
     */
    public Board() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGTH);
        setBackground(Color.black);

        gameInit();
        setDoubleBuffered(true);
    }

    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    public void gameInit() {
        aliens = new ArrayList<>();

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
            drawBombing(g);
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

    public void animationCycle() {
        if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
            ingame = false;
            message = "Parabéns! Você salvou a galáxia!";
        }

        // Player
        player.act();

        // Shot
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
                    }
                }
            }

            int y = shot.getY() - 8;
            if (y < 0) shot.die();
            else shot.setY(y);
        }

        // Aliens movement
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

        // Bombs
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

                    BufferedImage explosionImage = ResourceManager.getInstance().loadImage(EXPLOSION_IMAGE);
                    player.setImage(explosionImage);
                    player.setDying(true);
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
        public void keyReleased(KeyEvent e) { player.keyReleased(e); }
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
            int x = player.getX();
            int y = player.getY();

            if (ingame) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !shot.isVisible()) {
                    shot = new Shot(x, y);
                }
            }
        }
    }
}
