package tank;

import Ammo.Ammo;
import tankData.TankData;

import java.awt.event.KeyEvent;
import java.util.Hashtable;

public class Tank {
    /**
     * x,坦克横坐标，看图
     * y,坦克纵坐标，看图
     * direction，坦克朝向 1234 -> 上下左右
     * type，敌我状态 01 -> 我敌
     * speed，坦贝速度
     */
    private int x;
    private int y;
    private int direction;
    private int type;
    private int speed = 20;

    public Tank(int x, int y, int direction, int type) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.type = type;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }



    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    public int getType() {
        return type;
    }

    //用于计算坦克在不同朝向的时候的边界坐标（判断边界）,返回一个数组，【0】第一个是x，【1】第二个是y
    public static int[] getTankCoordinate(Tank tank, int direction) {
        int[] coordinate = new int[2];
        switch (tank.getDirection()) {
            case 1:
                coordinate[0] = tank.getX() + TankData.TANK_WHEEL_WIDTH + (TankData.TANK_CIRCLE_DIA / 3);
                coordinate[1] = tank.getY() + (TankData.TANK_WHEEL_HEIGHT / 2) - TankData.TANK_GUN_HEIGHT;
                break;
            case 2:
                coordinate[0] = tank.getX() + TankData.TANK_WHEEL_WIDTH + (TankData.TANK_CIRCLE_DIA / 3);
                coordinate[1] = tank.getY() + (TankData.TANK_WHEEL_HEIGHT / 2) + TankData.TANK_GUN_HEIGHT;
                break;
            case 3:
                coordinate[0] = tank.getX() + (TankData.TANK_WHEEL_HEIGHT / 2) - TankData.TANK_GUN_HEIGHT;
                coordinate[1] = tank.getY() + TankData.TANK_WHEEL_WIDTH + (TankData.TANK_CIRCLE_DIA / 3);
                break;
            case 4:
                coordinate[0] = tank.getX() + (TankData.TANK_WHEEL_HEIGHT / 2) + TankData.TANK_GUN_HEIGHT;
                coordinate[1] = tank.getY() + TankData.TANK_WHEEL_WIDTH + (TankData.TANK_CIRCLE_DIA / 3);
                break;
        }
        return coordinate;
    }


    public static boolean isTankReachBoundary(Tank tank, KeyEvent e) {
        //利用x，y把炮管给计算出来
        int gunX = Tank.getTankCoordinate(tank, tank.getDirection())[0];
        int gunY = Tank.getTankCoordinate(tank, tank.getDirection())[1];
        if (gunX <= 0 && (char) e.getKeyCode() == 'A') return true;
        else if (gunY <= 0 && (char) e.getKeyCode() == 'W') return true;
        else if (gunX + TankData.TANK_WHEEL_HEIGHT >= TankData.WINDOW_HEIGHT && (char) e.getKeyCode() == 'D')
            return true;
        else if (gunY + TankData.TANK_WHEEL_HEIGHT >= TankData.WINDOW_WIDTH && (char) e.getKeyCode() == 'S')
            return true;
        else
            return false;
    }

    public static boolean isEnemyTankReachBoundary(Tank tank, int x) {
        //利用x，y把炮管给计算出来
        int gunX = Tank.getTankCoordinate(tank, tank.getDirection())[0];
        int gunY = Tank.getTankCoordinate(tank, tank.getDirection())[1];
        if (gunX <= 0 && x == 3) return true;
        else if (gunY <= 0 && x == 1) return true;
        else if (gunX + TankData.TANK_WHEEL_HEIGHT >= TankData.WINDOW_HEIGHT && x == 4)
            return true;
        else if (gunY + TankData.TANK_WHEEL_HEIGHT >= TankData.WINDOW_WIDTH && x == 2)
            return true;
        else
            return false;
    }

    //坦克发射子弹
    /*
        思路：
        坦克发射一颗子弹，调用一次这个函数，开启一个线程(该线程的具体任务是在Ammo类中实现)
        子弹的初始位置/朝向 = 坦克边界位置/朝向
     */
    public void shoot() {
       // System.out.println("子弹发射。。。功能还没写");
        //初始化一颗子弹，并返回
        Ammo ammo =  new Ammo(getTankCoordinate(this,this.getDirection())[0],getTankCoordinate(this,this.getDirection())[1],getDirection(),getType());
        //开启一个子弹线程
        Thread shoot = new Thread(ammo);
        shoot.start();
    }

    //坦克移动，并返回一个移动后的坦克
    public Tank move(KeyEvent e) {
        //判断是否是到达了边界
        if (Tank.isTankReachBoundary(this, e)) return this;
        switch ((char) e.getKeyCode()) {
            case 'W':
                if (this.getDirection() == 1) {
                    return (new Tank(this.getX(), this.getY() - this.getSpeed(), this.getDirection(), this.getType()));
                } else {
                    return (new Tank(this.getX(), this.getY(), 1, this.getType()));
                }

            case 'S':
                if (this.getDirection() == 2) {
                    return (new Tank(this.getX(), this.getY() + this.getSpeed(), this.getDirection(), this.getType()));
                } else {
                    return (new Tank(this.getX(), this.getY(), 2, this.getType()));
                }

            case 'A':
                if (this.getDirection() == 3) {
                    return (new Tank(this.getX() - this.getSpeed(), this.getY(), this.getDirection(), this.getType()));
                } else {
                    return (new Tank(this.getX(), this.getY(), 3, this.getType()));
                }

            case 'D':
                if (this.getDirection() == 4) {
                    return (new Tank(this.getX() + this.getSpeed(), this.getY(), this.getDirection(), this.getType()));
                } else {
                    return (new Tank(this.getX(), this.getY(), 4, this.getType()));
                }

        }
        return this;
    }
}