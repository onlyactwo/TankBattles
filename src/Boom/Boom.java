package Boom;

import myPanel.MyPanel;

import java.awt.*;

public class Boom implements Runnable {
    MyPanel myPanel;
    private int x;
    private int y;
    private int life = 10;
    private boolean live = true;

    private final Image image1 = Toolkit.getDefaultToolkit().getImage("Booms/f1e31201f4431748f347a923cab4e17c.png");
    private final Image image2 = Toolkit.getDefaultToolkit().getImage("Booms/c447ae50229c6b62edcdee9635cc6df7.png");
    private final Image image3 = Toolkit.getDefaultToolkit().getImage("Booms/565ec244a1197020eea0806315b129c9.png");

    public Boom(int x, int y ,MyPanel myPanel) {
        this.x = x;
        this.y = y;
        this.myPanel = myPanel;
    }

    @Override
    public void run() {
        while (isLive()) {
            boom();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        myPanel.getBooms().remove(Thread.currentThread().getName());
    }


    public void boom() {
        if(life > 0){
            life--;
        }else {
            live = false;
        }
    }


    public int getLive() {
        return life;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Image getImage1() {
        return image1;
    }

    public Image getImage2() {
        return image2;
    }

    public Image getImage3() {
        return image3;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }



}
