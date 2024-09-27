package Ammo;

import myPanel.MyPanel;
import tankData.TankData;

public class Ammo implements Runnable {
    private int x;
    private int y;
    private int direction;
    private int type;
    private int speed = 20;

    public Ammo(int x, int y, int direction, int type) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.type = type;
    }

    /*
        绘制子弹的思路：
            有了初始坐标，朝向，绘制 -》休眠s秒-》更改子弹坐标（加上速度）-》重新绘制
        问题：怎么解决画笔？
     */
    @Override
    public void run() {
        System.out.println("启动了一个绘制子弹的线程");
        MyPanel.setAndRepaintAmmo(Thread.currentThread().getName(),this);//把参数传到MyPanel
    }

    public static Boolean isAmmoReachBoundary(Ammo ammo){
        if(ammo.getX()<0||ammo.getX()> TankData.WINDOW_WIDTH)return true;
        if(ammo.getY()<0||ammo.getY()>TankData.WINDOW_HEIGHT)return true;
        return false;
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
