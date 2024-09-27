package Ammo;

import myPanel.MyPanel;
import tankData.TankData;

public class Ammo implements Runnable {

    private int x;
    private int y;
    private int direction;
    private int type;
    private int speed = 50;

    public Ammo(int x, int y, int direction, int type) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.type = type;
    }

    @Override
    public void run() {
        System.out.println("启动了一个绘制子弹的线程");
        while (!isAmmoReachBoundary(this)) {
            ammoMove();
        }
    }

    public static Boolean isAmmoReachBoundary(Ammo ammo){
        if(ammo.getX()<0||ammo.getX()> TankData.WINDOW_WIDTH)return true;
        if(ammo.getY()<0||ammo.getY()>TankData.WINDOW_HEIGHT)return true;
        return false;
    }

    public void ammoMove(){
        System.out.println("子弹： " + Thread.currentThread().getName() + " 的横坐标 ： " + getX() + " 的纵坐标： " + getY());
        //每移动一次，就要对MyPanel类中的子弹容器里面的参数进行更新
        MyPanel.setAndRepaintAmmo(Thread.currentThread().getName(), this);//把参数传到MyPanel
        //改变子弹的参数
        switch (direction){
            case 1:
                y -= speed;
                break;
            case 2:
                y += speed;
                break;
            case 3:
                x -= speed;
                break;
            case 4:
                x += speed;
                break;
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
