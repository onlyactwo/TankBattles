package Ammo;

import tank.Tank;
import tankData.TankData;

import java.util.Iterator;

@SuppressWarnings({"all"})
public class Ammo implements Runnable {

    //让Ammo对象拥有tank实例，因为Ammo对象要去修改tank实例的子弹集合的数据
    //这个坦克对象可以接受我方坦克，也可以接受敌方坦克对象
    private final Tank tank;
    private int x;
    private int y;
    private int direction;
    private int type;
    private int speed = 50;
    private int changeAmmoPositionMiles = 50;

    public Ammo(int x, int y, int direction, int type, Tank tank) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.type = type;
        this.tank = tank;
    }

    /**
     * 子弹线程的任务：
     * 判断是否到了边界
     * 移动子弹（调整子弹的位置），并在
     */
    @Override
    public void run() {
        while (!isAmmoReachBoundary(this)) {
            ammoMove();
            //停顿
            try {
                Thread.sleep(changeAmmoPositionMiles);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //这颗子弹到达了边界，就要踢出子弹容器 ---- 防止并发修改异常，不能直接删掉
        tank.getAmmos().remove(Thread.currentThread().getName()); //---- BUG ----ConcurrentModificationException
    }

    public static Boolean isAmmoReachBoundary(Ammo ammo) {
        if (ammo.getX() < 0 || ammo.getX() > TankData.WINDOW_WIDTH) return true;
        if (ammo.getY() < 0 || ammo.getY() > TankData.WINDOW_HEIGHT) return true;
        return false;
    }

    public void ammoMove() {
        //System.out.println("子弹： " + Thread.currentThread().getName() + " 的横坐标 ： " + getX() + " 的纵坐标： " + getY());
        //每移动一次，就要对tank实例的子弹容器里面的参数进行更新
        tank.setAmmo(Thread.currentThread().getName(), this);
        //改变子弹的参数
        switch (direction) {
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

    }

    public int getChangeAmmoPositionMiles() {
        return changeAmmoPositionMiles;
    }

    public void setChangeAmmoPositionMiles(int changeAmmoPositionMiles) {
        this.changeAmmoPositionMiles = changeAmmoPositionMiles;
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
