/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MazeRunner;

/**
 *
 * @author Brandon Tan
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private Dimension d;
    private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);

    private Image ii;

    private int inGame = 0;
    private boolean dying = false;
    private boolean visibility = false;

    private final int BLOCK_SIZE = 32;
    private final int N_BLOCKS = 30;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int JOHNNY_ANIM_DELAY = 3;
    private final int JOHNNY_ANIM_COUNT = 3;
    private final int TRIBE_ANIM_DELAY = 5;
    private final int TRIBE_ANIM_COUNT = 3;
    private final int ENTER_ANIM_DELAY = 10;
    private final int ENTER_ANIM_COUNT = 2;

    private int johnnyAnimCount = JOHNNY_ANIM_DELAY;
    private int tribeAnimCount = TRIBE_ANIM_DELAY;
    private int enterAnimCount = ENTER_ANIM_DELAY;
    private final int NUM_OF_TREASURES = 5;
    private int johnnyAnimDir = 1;
    private int johnnyAnimPos = 1;
    private int tribeAnimDir = 1;
    private int tribeAnimPos = 1;
    private int enterAnimDir = 1;
    private int enterAnimPos = 1;
    private int johnnyLeft, power, treasureLeft;
    private Image tribe1, tribe2, tribe3;
    private Image johnny2up, johnny2left, johnny2right, johnny2down;
    private Image johnny3up, johnny3down, johnny3left, johnny3right;
    private Image johnny4up, johnny4down, johnny4left, johnny4right;
    private Image block1, ground1, bg1, gameOver1;
    private Image diamond1, goldBar, chest1, chest2, chest3, chest4, goldCrown;
    private Image[] randomTreasure = new Image[3];
    private Image life, powerUp;
    private Image gateOpened, gateClosed;
    private String bgm;

    private int johnny_x, johnny_y, johnnyd_x, johnnyd_y;
    private int req_dx, req_dy, view_dx, view_dy;

    /*
    1 = EndPoint
    2 = Gate Closed;
    4 = Gate Opened; 
    8 = enemy
    16 = block
    32 = randomTreasure1
    64 = randomTreasure2
    128 = randomTreasure3
    256 = life
    512 = powerUp
    1024 = SHOW_DISGRACE
    power to kill tribe
    losing a life cannot kill the tribe 
     */
    private final short[] levelData = {
        16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16,
        16, 0, 0, 0, 16, 16, 16, 0, 0, 8, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 16, 0, 0, 16,
        16, 0, 16, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 16, 0, 16, 0, 0, 512, 0, 0, 0, 0, 16, 0, 0, 16, 0, 0, 16,
        16, 0, 16, 0, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 0, 8, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 16, 0, 0, 16,
        16, 16, 16, 0, 0, 0, 0, 0, 0, 16, 0, 512, 0, 16, 0, 16, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 16,
        16, 0, 0, 0, 256, 0, 0, 0, 0, 16, 0, 0, 0, 16, 0, 16, 16, 16, 16, 16, 16, 16, 16, 16, 0, 0, 16, 0, 0, 16,
        16, 0, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 16, 0, 16, 0, 0, 0, 16, 512, 256, 0, 0, 0, 0, 16, 0, 0, 16,
        16, 16, 16, 16, 16, 16, 16, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 16, 0, 0, 16,
        16, 0, 0, 0, 0, 0, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 16, 0, 0, 16,
        16, 16, 16, 16, 0, 16, 16, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 16, 0, 0, 16,
        16, 0, 16, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 16, 0, 16, 0, 0, 0, 16, 16, 16, 16, 16, 16, 16, 16, 0, 16, 16,
        16, 0, 16, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 16, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16,
        16, 0, 16, 0, 0, 0, 0, 8, 0, 0, 0, 16, 0, 16, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 512, 0, 16,
        16, 0, 16, 0, 16, 16, 16, 16, 16, 0, 16, 16, 16, 16, 0, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16,
        16, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16,
        16, 0, 16, 16, 16, 16, 16, 16, 16, 16, 0, 16, 16, 16, 0, 16, 16, 16, 0, 16, 0, 16, 0, 16, 16, 16, 0, 16, 0, 16,
        16, 0, 16, 0, 0, 0, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16,
        16, 0, 16, 0, 0, 0, 0, 16, 0, 16, 0, 16, 0, 16, 16, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16,
        16, 8, 16, 0, 16, 16, 0, 16, 0, 16, 0, 0, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16,
        16, 0, 16, 0, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 0, 0, 16, 0, 16,
        16, 0, 16, 0, 0, 16, 0, 16, 0, 8, 0, 16, 16, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 16, 16, 16, 16, 0, 16,
        16, 0, 16, 16, 16, 16, 0, 16, 0, 0, 0, 0, 0, 16, 0, 16, 8, 16, 16, 16, 0, 16, 0, 16, 16, 16, 16, 16, 0, 16,
        16, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 16, 0, 16,
        16, 0, 16, 0, 16, 512, 16, 16, 0, 0, 0, 0, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 0, 0, 0, 0, 16, 0, 16,
        16, 0, 16, 0, 16, 0, 0, 16, 0, 0, 0, 0, 0, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 0, 0, 0, 0, 16, 0, 16,
        16, 0, 16, 0, 16, 256, 0, 16, 0, 0, 0, 0, 0, 16, 0, 16, 512, 16, 0, 16, 0, 16, 0, 0, 0, 0, 0, 16, 4, 16,
        16, 0, 16, 16, 16, 16, 16, 16, 0, 16, 16, 16, 16, 16, 0, 16, 0, 16, 0, 16, 0, 16, 0, 0, 0, 16, 16, 16, 1024, 16,
        16, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 16, 16, 16, 16, 16, 16, 16, 0, 16, 16, 16, 16, 17, 0, 16, 1, 16,
        16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 8, 0, 16, 0, 16,
        16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 0, 16,};

    private short[] screenData;
    private Timer timer;

    public Board() {

        loadImages();
        initVariables();
        initBoard();
        playSound();

    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);
    }

    private void initVariables() {

        screenData = new short[N_BLOCKS * N_BLOCKS];
        d = new Dimension(400, 400);
        timer = new Timer(40, this);
        timer.start();
    }

    //the number of .....AnimCount will change so the animation of the character will change
    public void doAnim() {

        johnnyAnimCount--;
        tribeAnimCount--;
        enterAnimCount--;

        if (johnnyAnimCount <= 0) {
            johnnyAnimCount = JOHNNY_ANIM_DELAY;
            johnnyAnimPos = johnnyAnimPos + johnnyAnimDir;

            if (johnnyAnimPos == (JOHNNY_ANIM_COUNT) || johnnyAnimPos == 1) {
                johnnyAnimDir = -johnnyAnimDir;
            }
        }
        if (tribeAnimCount <= 0) {
            tribeAnimCount = TRIBE_ANIM_DELAY;
            tribeAnimPos = tribeAnimPos + tribeAnimDir;

            if (tribeAnimPos == (TRIBE_ANIM_COUNT) || tribeAnimPos == 1) {
                tribeAnimDir = -tribeAnimDir;
            }
        }
        if (enterAnimCount <= 0) {
            enterAnimCount = ENTER_ANIM_DELAY;
            enterAnimPos = enterAnimPos + enterAnimDir;

            if (enterAnimPos == (ENTER_ANIM_COUNT) || enterAnimPos == 1) {
                enterAnimDir = -enterAnimDir;
            }
        }
    }

    public void playGame(Graphics2D g2d) {

        if (dying) {

            death();

        } else {
            moveJohnny();
            drawJohnny(g2d);
            checkMaze();
        }
    }

    public void showIntroScreen(Graphics2D g2d) {

        g2d.setColor(new Color(53, 234, 121));
        g2d.drawImage(bg1, 0, 0, this);

        String s = "Start Game";
        String s1 = "(Press ENTER to start)";
        String title = "Maze Runner";
        String name1 = "BRANDON TAN ZHIRONG ";
        String name2 = "GOY SHUH XIAN ";
        String name3 = "CHENG LEY HANG ";
        String name4 = "CHEW SWEE KEAT ";
        String noMatric1 = "WID180007";
        String noMatric2 = "WID180018";
        String noMatric3 = "WID180009";
        String noMatric4 = "WID180010";
        String groupName = "Bring Me Fly";
        Font small = new Font("Times New Roman", Font.BOLD, 20);
        Font small2 = new Font("Times New Roman", Font.BOLD, 40);
        Font small1 = new Font("Manaspace Regular", Font.BOLD, 50);
        Font big = new Font("Manaspace Regular", Font.BOLD, 100);
        FontMetrics metr = this.getFontMetrics(small1);

        g2d.setColor(Color.black);
        g2d.setFont(small1);
        g2d.drawString(s, (1200 - metr.stringWidth(s)) / 2, SCREEN_SIZE / 2 + 240);
        switch (enterAnimPos) {
            case 1:
                g2d.drawString(s1, (1200 - metr.stringWidth(s1)) / 2, SCREEN_SIZE / 2 + 310);
                break;
            case 2:
                break;
        }
        g2d.setFont(small2);
        g2d.drawString(groupName, SCREEN_SIZE / 4 * 3, SCREEN_SIZE / 6 * 5 + BLOCK_SIZE);
        g2d.setFont(small);
        g2d.drawString(name1, SCREEN_SIZE / 3 * 2, SCREEN_SIZE / 6 * 5 + 2 * BLOCK_SIZE);
        g2d.drawString(name2, SCREEN_SIZE / 3 * 2, SCREEN_SIZE / 6 * 5 + 3 * BLOCK_SIZE);
        g2d.drawString(name3, SCREEN_SIZE / 3 * 2, SCREEN_SIZE / 6 * 5 + 4 * BLOCK_SIZE);
        g2d.drawString(name4, SCREEN_SIZE / 3 * 2, SCREEN_SIZE / 6 * 5 + 5 * BLOCK_SIZE);
        g2d.drawString(noMatric1, SCREEN_SIZE, SCREEN_SIZE / 6 * 5 + 2 * BLOCK_SIZE);
        g2d.drawString(noMatric2, SCREEN_SIZE, SCREEN_SIZE / 6 * 5 + 3 * BLOCK_SIZE);
        g2d.drawString(noMatric3, SCREEN_SIZE, SCREEN_SIZE / 6 * 5 + 4 * BLOCK_SIZE);
        g2d.drawString(noMatric4, SCREEN_SIZE, SCREEN_SIZE / 6 * 5 + 5 * BLOCK_SIZE);
        g2d.setFont(big);
        FontMetrics metr1 = this.getFontMetrics(big);
        g2d.drawString(title, (1200 - metr1.stringWidth(title)) / 2, SCREEN_SIZE / 4 + 100);
    }

    public void showGameOverScreen(Graphics2D g2d) {
        g2d.drawImage(gameOver1, 0, 0, this);
    }

    public void showCongratulationScreen(Graphics2D g2d) {
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, 1200, 1050);
        String gratz = "CONGRATULATION!!";
        String won = "YOU HAVE WON THE GAME!!";
        Font small = new Font("Manaspace Regular", Font.BOLD, 60);
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString(gratz, (1200 - metr.stringWidth(gratz)) / 2, SCREEN_SIZE / 2);
        g2d.drawString(won, (1200 - metr.stringWidth(won)) / 2, SCREEN_SIZE / 2 + 100);
    }

    public void showDisgrace(Graphics2D g2d) {
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, 1200, 1050);
        String gratz = "Game Over";
        String won = "YOU ARE A DISGRACE!!";
        Font small = new Font("Manaspace Regular", Font.BOLD, 60);
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString(gratz, (1200 - metr.stringWidth(gratz)) / 2, SCREEN_SIZE / 2);
        g2d.drawString(won, (1200 - metr.stringWidth(won)) / 2, SCREEN_SIZE / 2 + 100);
    }

    public void showIntroStory(Graphics2D g2d) {
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, 1200, 1050);
        String intro1 = "Johnny, a renowned Maze Runner is experienced in hunting for valuables.";
        String intro2 = "However, during his previous expedition, he was attacked by the scary Some ";
        String intro3 = "Tribe in Some Island. The moment when he woke up, it is in the middle ";
        String intro4 = "of a dark scary night. He has no idea where he is.";
        String intro5 = "Judging from his intuition, Johnny believes that he is being trapped in the ";
        String intro6 = "famous GG Maze of Some Island. Johnny needs to escape Some Island as soon ";
        String intro7 = "as possible but he needs to collect all the valuables that he has lost from GG Maze.";
        String intro8 = "He needs to collect all the valuables that he has lost from GG Maze.";
        String intro9 = "Can you help him?";
        String intro10 = "(Press ENTER to help him)";
        Font small = new Font("pix PixelFJVerdana12pt Regular", Font.PLAIN, 30);
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString(intro1, 20, 100);
        g2d.drawString(intro2, 20, 130);
        g2d.drawString(intro3, 20, 160);
        g2d.drawString(intro4, 20, 190);
        g2d.drawString(intro5, 20, 220);
        g2d.drawString(intro6, 20, 250);
        g2d.drawString(intro7, 20, 280);
        g2d.drawString(intro8, 20, 310);
        g2d.drawString(intro9, 20, 340);
        Font big = new Font("pix PixelFJVerdana12pt Regular", Font.PLAIN, 100);
        FontMetrics metr1 = this.getFontMetrics(big);
        g2d.drawString(intro10, (1200 - metr.stringWidth(intro10)) / 2, 600);

    }

    public void drawGameStatus(Graphics2D g) {

        int i;
        String s, life1, tL, pwr, tribe, xtraLife, power1, gate;

        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        life1 = "Life : ";
        tL = "Treasure Left : " + treasureLeft;
        pwr = "Power : " + power;
        tribe = "Some Tribe = ";
        xtraLife = "Extra life = ";
        power1 = "Power(to kill tribe) = ";
        gate = "GATE = ";
        g.drawString(life1, SCREEN_SIZE + 1, 270);
        g.drawString(tL, SCREEN_SIZE + 1, 130);
        g.drawString(pwr, SCREEN_SIZE + 1, 200);
        g.drawString(tribe, SCREEN_SIZE + 1, 400);
        g.drawImage(tribe1, SCREEN_SIZE + 3 * BLOCK_SIZE + 10, 380, this);
        g.drawString(xtraLife, SCREEN_SIZE + 1, 450);
        g.drawImage(life, SCREEN_SIZE + 3 * BLOCK_SIZE + 5, 430, this);
        g.drawString(power1, SCREEN_SIZE + 1, 500);
        g.drawImage(powerUp, SCREEN_SIZE + 5 * BLOCK_SIZE, 470, this);
        g.drawString(gate, SCREEN_SIZE + 1, 550);
        g.drawImage(gateClosed, SCREEN_SIZE + 2 * BLOCK_SIZE, 530, this);

        for (i = 0; i < johnnyLeft; i++) {
            g.drawImage(johnny4left, SCREEN_SIZE + i * BLOCK_SIZE, 300, this);
        }
        for (i = 0; i < power; i++) {
            g.drawImage(powerUp, SCREEN_SIZE + i * BLOCK_SIZE, 220, this);
        }
    }

    public void checkMaze() {
        int count = 0;
        //to change the treasures left
        for (int j = 0; j < N_BLOCKS * N_BLOCKS; j++) {
            if ((screenData[j] & 32) != 0 || (screenData[j] & 64) != 0 || (screenData[j] & 128) != 0) {
                count++;
            }
        }
        treasureLeft = count;

        // to change the gate from closed to opened
        if (treasureLeft == 0) {
            screenData[808] = 0;
        }
    }

    public void death() {

        johnnyLeft--;
        dying = false;
        if (johnnyLeft == 0) {
            inGame = 2;
        }
    }

    public void moveJohnny() {

        int pos;
        short ch;

        //when Johnny walk to a whole block , it will sense its postition and position array number.
        // then when johnny comes in contact with certain number , certain number actions will be performed as below
        if (johnny_x % BLOCK_SIZE == 0 && johnny_y % BLOCK_SIZE == 0) {
            pos = johnny_x / BLOCK_SIZE + N_BLOCKS * (int) (johnny_y / BLOCK_SIZE);
            ch = screenData[pos];

            //after comes in contact with 1000, the number will revert to 0000 by (ch & 7)
            if ((ch & 8) != 0) {
                if (power >= 1) {
                    power--;
                    screenData[pos] = (short) (ch & 7);
                } else {
                    dying = true;
                }
            }
            if ((ch & 32) != 0) {
                screenData[pos] = (short) (ch & 31);
            }
            if ((ch & 64) != 0) {
                screenData[pos] = (short) (ch & 63);
            }
            if ((ch & 128) != 0) {
                screenData[pos] = (short) (ch & 127);
            }
            if ((ch & 256) != 0) {
                screenData[pos] = (short) (ch & 255);
                johnnyLeft++;
            }
            if ((ch & 512) != 0) {
                screenData[pos] = (short) (ch & 511);
                power++;
            }
            if ((ch & 1) != 0) {
                inGame = 3;
            }
            if ((ch & 1024) != 0) {
                inGame = 5;
            }
            //to prevent it from passing through the obstacles when the arrow key is pressed
            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (screenData[pos - 1] & 18) != 0)
                        || (req_dx == 1 && req_dy == 0 && (screenData[pos + 1] & 18) != 0)
                        || (req_dx == 0 && req_dy == -1 && (screenData[pos - N_BLOCKS] & 18) != 0)
                        || (req_dx == 0 && req_dy == 1 && (screenData[pos + N_BLOCKS] & 18) != 0))) {

                    johnnyd_x = req_dx;
                    johnnyd_y = req_dy;
                    view_dx = johnnyd_x;
                    view_dy = johnnyd_y;
                }
            }

            // to prevent it from passing through the obstacles
            if ((johnnyd_x == -1 && johnnyd_y == 0 && (screenData[pos - 1] & 18) != 0)
                    || (johnnyd_x == 1 && johnnyd_y == 0 && (screenData[pos + 1] & 18) != 0)
                    || (johnnyd_x == 0 && johnnyd_y == -1 && (screenData[pos - N_BLOCKS] & 18) != 0)
                    || (johnnyd_x == 0 && johnnyd_y == 1 && (screenData[pos + N_BLOCKS] & 18) != 0)) {
                johnnyd_x = 0;
                johnnyd_y = 0;
            }
        }
        //Johnny walk block by block , that is why he appears to walk fast
        johnny_x = johnny_x + BLOCK_SIZE * johnnyd_x;
        johnny_y = johnny_y + BLOCK_SIZE * johnnyd_y;
    }

//Ex : when left is pressed , view_dx = -1
// drawJohnnyLeft(g2d) is initiated
    public void drawJohnny(Graphics2D g2d) {

        if (view_dx == -1) {
            drawJohnnyLeft(g2d);
        } else if (view_dx == 1) {
            drawJohnnyRight(g2d);
        } else if (view_dy == -1) {
            drawJohnnyUp(g2d);
        } else {
            drawJohnnyDown(g2d);
        }
    }

    //Johnny is animated by 3 pictures
    //the johnnyAnimPos will keep changing number so the tribe picture will change according to it 
    public void drawJohnnyUp(Graphics2D g2d) {

        switch (johnnyAnimPos) {
            case 1:
                g2d.drawImage(johnny2up, johnny_x - BLOCK_SIZE / 2, johnny_y - BLOCK_SIZE / 2, this);
                break;
            case 2:
                g2d.drawImage(johnny3up, johnny_x - BLOCK_SIZE / 2, johnny_y - BLOCK_SIZE / 2, this);
                break;
            case 3:
                g2d.drawImage(johnny4up, johnny_x - BLOCK_SIZE / 2, johnny_y - BLOCK_SIZE / 2, this);
                break;
        }
    }

    public void drawJohnnyDown(Graphics2D g2d) {

        switch (johnnyAnimPos) {
            case 1:
                g2d.drawImage(johnny2down, johnny_x - BLOCK_SIZE / 2, johnny_y - BLOCK_SIZE / 2, this);
                break;
            case 2:
                g2d.drawImage(johnny3down, johnny_x - BLOCK_SIZE / 2, johnny_y - BLOCK_SIZE / 2, this);
                break;
            case 3:
                g2d.drawImage(johnny4down, johnny_x - BLOCK_SIZE / 2, johnny_y - BLOCK_SIZE / 2, this);
                break;
        }
    }

    public void drawJohnnyLeft(Graphics2D g2d) {

        switch (johnnyAnimPos) {
            case 1:
                g2d.drawImage(johnny2left, johnny_x - BLOCK_SIZE / 2, johnny_y - BLOCK_SIZE / 2, this);
                break;
            case 2:
                g2d.drawImage(johnny3left, johnny_x - BLOCK_SIZE / 2, johnny_y - BLOCK_SIZE / 2, this);
                break;
            case 3:
                g2d.drawImage(johnny4left, johnny_x - BLOCK_SIZE / 2, johnny_y - BLOCK_SIZE / 2, this);
                break;
        }
    }

    public void drawJohnnyRight(Graphics2D g2d) {

        switch (johnnyAnimPos) {
            case 1:
                g2d.drawImage(johnny2right, johnny_x - BLOCK_SIZE / 2, johnny_y - BLOCK_SIZE / 2, this);
                break;
            case 2:
                g2d.drawImage(johnny3right, johnny_x - BLOCK_SIZE / 2, johnny_y - BLOCK_SIZE / 2, this);
                break;
            case 3:
                g2d.drawImage(johnny4right, johnny_x - BLOCK_SIZE / 2, johnny_y - BLOCK_SIZE / 2, this);
                break;
        }
    }

    //enemy is animated by 3 pictures
    //the tribeAnimPos will keep changing number so the tribe picture will change according to it 
    public Image drawEnemy() {

        switch (tribeAnimPos) {
            case 1:
                return tribe1;
            case 2:
                return tribe2;
            case 3:
                return tribe3;
            default:
                return tribe1;
        }
    }

    public void drawMaze(Graphics2D g2d) {

        int i = 0;
        int x = 1, y = 1;
        int[] blocked = {0, 0, 0, 0};
        g2d.setBackground(Color.black);

        if (visibility) {
            for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
                for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {
                    g2d.drawImage(ground1, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                    if ((screenData[i] & 2) != 0) {
                        g2d.drawImage(gateClosed, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                    } else if ((screenData[i] & 4) != 0) {
                        g2d.drawImage(gateOpened, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                    } else if ((screenData[i] & 8) != 0) {
                        g2d.drawImage(drawEnemy(), x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                    } else if ((screenData[i] & 16) != 0) {
                        g2d.drawImage(block1, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                    } else if ((screenData[i] & 32) != 0) {
                        g2d.drawImage(randomTreasure[0], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                    } else if ((screenData[i] & 64) != 0) {
                        g2d.drawImage(randomTreasure[1], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                    } else if ((screenData[i] & 128) != 0) {
                        g2d.drawImage(randomTreasure[2], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                    } else if ((screenData[i] & 256) != 0) {
                        g2d.drawImage(life, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                    } else if ((screenData[i] & 512) != 0) {
                        g2d.drawImage(powerUp, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                    }
                    i++;
                }

            }
            //the maze is drawn when Johnny is in a block (line 546) 
        } else {
            if (johnny_x % BLOCK_SIZE == 0 && johnny_y % BLOCK_SIZE == 0) {
                for (y = johnny_y - BLOCK_SIZE; y <= johnny_y + BLOCK_SIZE; y += BLOCK_SIZE) {
                    for (x = johnny_x - BLOCK_SIZE; x <= johnny_x + BLOCK_SIZE; x += BLOCK_SIZE) {
                        i = y / BLOCK_SIZE * N_BLOCKS + x / BLOCK_SIZE;
                        g2d.drawImage(ground1, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                        if ((screenData[i] & 18) == 0) {
                            if ((screenData[i] & 4) != 0) {
                                g2d.drawImage(gateOpened, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                            } else if ((screenData[i] & 8) != 0) {
                                g2d.drawImage(drawEnemy(), x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                            } else if ((screenData[i] & 32) != 0) {
                                g2d.drawImage(randomTreasure[0], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                            } else if ((screenData[i] & 64) != 0) {
                                g2d.drawImage(randomTreasure[1], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                            } else if ((screenData[i] & 128) != 0) {
                                g2d.drawImage(randomTreasure[2], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                            } else if ((screenData[i] & 256) != 0) {
                                g2d.drawImage(life, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                            } else if ((screenData[i] & 512) != 0) {
                                g2d.drawImage(powerUp, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                            }

                            //if block appeared around Johnny , blocked[i] = 1 so it wont draw further block            
                        } else {
                            if ((screenData[i] & 2) != 0) {
                                g2d.drawImage(gateClosed, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                            } else if ((screenData[i] & 16) != 0) {
                                g2d.drawImage(block1, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                            }
                            if (y == johnny_y && x == johnny_x - BLOCK_SIZE) {
                                blocked[0] = 1;
                            } else if (y == johnny_y - BLOCK_SIZE && x == johnny_x) {
                                blocked[1] = 1;
                            } else if (y == johnny_y && x == johnny_x + BLOCK_SIZE) {
                                blocked[2] = 1;
                            } else if (y == johnny_y + BLOCK_SIZE && x == johnny_x) {
                                blocked[3] = 1;
                            }
                        }
                    }
                }
                for (int j = 0; j < 4; j++) {
                    if (blocked[j] != 1) {
                        switch (j) {
                            case 0:
                                y = johnny_y;
                                x = johnny_x - 2 * BLOCK_SIZE;
                                i = y / BLOCK_SIZE * N_BLOCKS + x / BLOCK_SIZE;
                                g2d.drawImage(ground1, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                if ((screenData[i] & 18) == 0) {
                                    if ((screenData[i] & 4) != 0) {
                                        g2d.drawImage(gateOpened, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 8) != 0) {
                                        g2d.drawImage(drawEnemy(), x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 32) != 0) {
                                        g2d.drawImage(randomTreasure[0], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 64) != 0) {
                                        g2d.drawImage(randomTreasure[1], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 128) != 0) {
                                        g2d.drawImage(randomTreasure[2], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 256) != 0) {
                                        g2d.drawImage(life, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 512) != 0) {
                                        g2d.drawImage(powerUp, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    }
                                } else if ((screenData[i] & 2) != 0) {
                                    g2d.drawImage(gateClosed, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                } else if ((screenData[i] & 16) != 0) {
                                    g2d.drawImage(block1, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                }
                                break;
                            case 1:
                                y = johnny_y - 2 * BLOCK_SIZE;
                                x = johnny_x;
                                i = y / BLOCK_SIZE * N_BLOCKS + x / BLOCK_SIZE;
                                g2d.drawImage(ground1, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                if ((screenData[i] & 18) == 0) {
                                    if ((screenData[i] & 4) != 0) {
                                        g2d.drawImage(gateOpened, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 8) != 0) {
                                        g2d.drawImage(drawEnemy(), x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 32) != 0) {
                                        g2d.drawImage(randomTreasure[0], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 64) != 0) {
                                        g2d.drawImage(randomTreasure[1], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 128) != 0) {
                                        g2d.drawImage(randomTreasure[2], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 256) != 0) {
                                        g2d.drawImage(life, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 512) != 0) {
                                        g2d.drawImage(powerUp, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    }

                                } else if ((screenData[i] & 2) != 0) {
                                    g2d.drawImage(gateClosed, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                } else if ((screenData[i] & 16) != 0) {
                                    g2d.drawImage(block1, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                }
                                break;
                            case 2:
                                y = johnny_y;
                                x = johnny_x + 2 * BLOCK_SIZE;
                                i = y / BLOCK_SIZE * N_BLOCKS + x / BLOCK_SIZE;
                                g2d.drawImage(ground1, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                if ((screenData[i] & 18) == 0) {
                                    if ((screenData[i] & 4) != 0) {
                                        g2d.drawImage(gateOpened, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 8) != 0) {
                                        g2d.drawImage(drawEnemy(), x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 32) != 0) {
                                        g2d.drawImage(randomTreasure[0], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 64) != 0) {
                                        g2d.drawImage(randomTreasure[1], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 128) != 0) {
                                        g2d.drawImage(randomTreasure[2], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 256) != 0) {
                                        g2d.drawImage(life, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 512) != 0) {
                                        g2d.drawImage(powerUp, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    }
                                } else if ((screenData[i] & 2) != 0) {
                                    g2d.drawImage(gateClosed, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                } else if ((screenData[i] & 16) != 0) {
                                    g2d.drawImage(block1, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                }
                                break;
                            case 3:
                                y = johnny_y + 2 * BLOCK_SIZE;
                                x = johnny_x;
                                i = y / BLOCK_SIZE * N_BLOCKS + x / BLOCK_SIZE;
                                g2d.drawImage(ground1, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                if ((screenData[i] & 18) == 0) {
                                    if ((screenData[i] & 4) != 0) {
                                        g2d.drawImage(gateOpened, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 8) != 0) {
                                        g2d.drawImage(drawEnemy(), x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 32) != 0) {
                                        g2d.drawImage(randomTreasure[0], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 64) != 0) {
                                        g2d.drawImage(randomTreasure[1], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 128) != 0) {
                                        g2d.drawImage(randomTreasure[2], x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 256) != 0) {
                                        g2d.drawImage(life, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    } else if ((screenData[i] & 512) != 0) {
                                        g2d.drawImage(powerUp, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                    }
                                } else if ((screenData[i] & 2) != 0) {
                                    g2d.drawImage(gateClosed, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                } else if ((screenData[i] & 16) != 0) {
                                    g2d.drawImage(block1, x - BLOCK_SIZE / 2, y - BLOCK_SIZE / 2, this);
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    //when the game starts, all the variables back to original.
    public void initGame() {
        johnnyLeft = 3;
        power = 0;
        for (int i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }
        for (int j = 0; j < NUM_OF_TREASURES; j++) {
            randomTreasureLocation();
        }
        johnny_x = 14 * BLOCK_SIZE;
        johnny_y = 14 * BLOCK_SIZE;
        johnnyd_x = 0;
        johnnyd_y = 0;
        req_dx = 0;
        req_dy = 0;
        view_dx = 0;
        view_dy = 0;
        dying = false;
    }

    //images of treasures randomly assigned to each randomTreasure[] array
    public void randomTreasure(int i) {
        int random = (int) (Math.random() * 7);
        switch (random) {
            case 0:
                randomTreasure[i] = goldBar;
                break;
            case 1:
                randomTreasure[i] = diamond1;
                break;
            case 2:
                randomTreasure[i] = chest1;
                break;
            case 3:
                randomTreasure[i] = chest2;
                break;
            case 4:
                randomTreasure[i] = chest3;
                break;
            case 5:
                randomTreasure[i] = chest4;
                break;
            case 6:
                randomTreasure[i] = goldCrown;
                break;
        }
    }

    //treasures randomly scattered around the maze
    public void randomTreasureLocation() {
        int random = (int) (Math.random() * 898);
        int count = (int) (Math.random() * 3);
        // 1 + 2 + 4 + 8 + 16 + 32 + 64 + 128 + 256 + 512
        while ((screenData[random] & 1023) != 0 && random != 868 && random != 808 && random != 434) {
            random = (int) (Math.random() * 898);
        }
        switch (count) {
            case 0:
                screenData[random] = 32;
                break;
            case 1:
                screenData[random] = 64;
                break;
            case 2:
                screenData[random] = 128;
                break;
        }
    }

    public synchronized void playSound() {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream;
                inputStream
                        = AudioSystem.getAudioInputStream(
                                Board.class
                                        .getResourceAsStream("/BGM.wav"));
                clip.open(inputStream);
                FloatControl gainControl
                        = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                // System.out.println(gainControl.getValue());
                gainControl.setValue(0);
                clip.start();
                clip.loop(clip.LOOP_CONTINUOUSLY);
            } catch (IOException e) {
                System.err.println(e.getMessage());

            } catch (UnsupportedAudioFileException | LineUnavailableException ex) {
                Logger.getLogger(Board.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    private void loadImages() {

        tribe1 = new ImageIcon("./res/tribe_08.png").getImage();
        tribe2 = new ImageIcon("./res/tribe_09.png").getImage();
        tribe3 = new ImageIcon("./res/tribe_10.png").getImage();
        johnny2up = new ImageIcon("./res/professor_walk_cycle_no_hat_03.png").getImage();
        johnny3up = new ImageIcon("./res/professor_walk_cycle_no_hat_02.png").getImage();
        johnny4up = new ImageIcon("./res/professor_walk_cycle_no_hat_07.png").getImage();
        johnny2down = new ImageIcon("./res/professor_walk_cycle_no_hat_21.png").getImage();
        johnny3down = new ImageIcon("./res/professor_walk_cycle_no_hat_19.png").getImage();
        johnny4down = new ImageIcon("./res/professor_walk_cycle_no_hat_27.png").getImage();
        johnny2left = new ImageIcon("./res/professor_walk_cycle_no_hat_11.png").getImage();
        johnny3left = new ImageIcon("./res/professor_walk_cycle_no_hat_10.png").getImage();
        johnny4left = new ImageIcon("./res/professor_walk_cycle_no_hat_15.png").getImage();
        johnny2right = new ImageIcon("./res/professor_walk_cycle_no_hat_29.png").getImage();
        johnny3right = new ImageIcon("./res/professor_walk_cycle_no_hat_28.png").getImage();
        johnny4right = new ImageIcon("./res/professor_walk_cycle_no_hat_33.png").getImage();
        block1 = new ImageIcon("./res/forest_tile_08.png").getImage();
        ground1 = new ImageIcon("./res/plains.png").getImage();
        gateOpened = new ImageIcon("./res/gateOpened.png").getImage();
        gateClosed = new ImageIcon("./res/gateClosed.png").getImage();
        bg1 = new ImageIcon("./res/pirates-harbor-cave-skull.jpg").getImage();
        gameOver1 = new ImageIcon("./res/GameOver.png").getImage();
        goldBar = new ImageIcon("./res/7.png").getImage();
        diamond1 = new ImageIcon("./res/Gem.png").getImage();
        chest1 = new ImageIcon("./res/ChestBlue.png").getImage();
        chest2 = new ImageIcon("./res/ChestGreen.png").getImage();
        chest3 = new ImageIcon("./res/ChestYellow.png").getImage();
        chest4 = new ImageIcon("./res/ChestRed.png").getImage();
        goldCrown = new ImageIcon("./res/9.png").getImage();
        life = new ImageIcon("./res/heart.png").getImage();
        powerUp = new ImageIcon("./res/cursorSword_gold.png").getImage();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    public void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        doAnim();

        /* inGame
        0 = show intro screen
        1 = in game
        2 = game over (DIE)
        3 = Win game
        4 = intro Story
         */
        switch (inGame) {
            case 1:
                drawMaze(g2d);
                playGame(g2d);
                drawGameStatus(g2d);
                break;
            case 0:
                showIntroScreen(g2d);
                for (int i = 0; i < 3; i++) {
                    randomTreasure(i);
                }
                visibility = false;
                break;
            case 2:
                showGameOverScreen(g2d);
                break;
            case 3:
                showCongratulationScreen(g2d);
                break;
            case 4:
                showIntroStory(g2d);
                break;
            case 5:
                showDisgrace(g2d);
                break;
            default:
                break;
        }
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();

    }

    class TAdapter extends KeyAdapter {

        /*
        arrow keys = character movement
        P = pause
        E = exit when in game
        V = visibility
         */
        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (inGame == 1) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_P) {
                    if (timer.isRunning()) {
                        timer.stop();
                    } else {
                        timer.start();
                    }
                } else if (key == KeyEvent.VK_V) {
                    visibility = !visibility;
                } else if (key == KeyEvent.VK_E) {
                    inGame = 0;
                }
            } else if (inGame == 0) {
                if (key == KeyEvent.VK_ENTER) {
                    inGame = 4;
                }
            } else if (inGame == 2) {
                if (key == KeyEvent.VK_ENTER) {
                    inGame = 0;
                }
            } else if (inGame == 3) {
                if (key == KeyEvent.VK_ENTER) {
                    inGame = 0;
                }
            } else if (inGame == 4) {
                if (key == KeyEvent.VK_ENTER) {
                    inGame = 1;
                    initGame();
                }
            } else if (inGame == 5) {
                if (key == KeyEvent.VK_ENTER) {
                    inGame = 0;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT
                    || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
                req_dx = 0;
                req_dy = 0;
                johnnyd_x = 0;
                johnnyd_y = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        repaint();
    }

}
