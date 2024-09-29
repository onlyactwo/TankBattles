package enemyTank;

import Ammo.Ammo;
import tank.Tank;
import tankData.TankData;

import java.util.Iterator;

public class EnemyTank extends Tank implements Runnable {
    //每间隔 shootTime 就发射一颗子弹
    private int shootTime = 200;
    private long lastDirectionChangeTime = System.currentTimeMillis(); // 记录上次换方向的时间
    private final int changeDirctionTime = 3000;
    //初始化敌方坦克的数量
    public static int enemyTankSize = 4;

    @Override
    public void run() {
        while (isLive()) {
            shoot();
            move();
            if (System.currentTimeMillis() - lastDirectionChangeTime >= changeDirctionTime) {
                this.changeDirection();
            }
            try {
                Thread.sleep(shootTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //构造器
    public EnemyTank(int x, int y, int direction, int type) {
        super(x, y, direction, type);
    }

    //重写isLive(),用我方坦克子弹进行判断敌方坦克是否被攻击了
    public Boolean isAttacked(Tank tank) {
        if (tank.getAmmos().isEmpty()) return false;
        if (!isLive()) return false;
        Iterator<Ammo> ammoIterator = tank.getAmmos().values().iterator();
        switch (this.getDirection()) {
            case 1:
            case 2:
                while (ammoIterator.hasNext()) {
                    Ammo ammo = ammoIterator.next();
                    if (ammo.getX() >= this.getX() && ammo.getX() <= this.getX() + TankData.TANK_WHEEL_WIDTH * 2 + TankData.TANK_CIRCLE_DIA) {
                        if (ammo.getY() >= this.getY() && ammo.getY() <= this.getY() + TankData.TANK_WHEEL_HEIGHT) {
                            ammo.setLive(false);
                            return true;
                        }
                    }
                }
                break;
            case 3:
            case 4:
                while (ammoIterator.hasNext()) {
                    Ammo ammo = ammoIterator.next();
                    if (ammo.getX() >= this.getX() && ammo.getX() <= this.getX() + TankData.TANK_WHEEL_HEIGHT) {
                        if (ammo.getY() >= this.getY() && ammo.getY() <= this.getY() + TankData.TANK_WHEEL_WIDTH * 2 + TankData.TANK_CIRCLE_DIA) {
                            ammo.setLive(false);
                            return true;
                        }
                    }
                }
                break;
        }
        return false;
    }

    public void move() {
        switch (getDirection()) {
            case 1:
                if (!isEnemyTankReachBoundary(this, 1)) {
                    this.setY(this.getY() - this.getSpeed());
                }else{
                    changeDirection();
                }
                break;
            case 2:
                if (!isEnemyTankReachBoundary(this, 2)) {
                    this.setY(this.getY() + this.getSpeed());
                }else {
                    changeDirection();
                }
                break;
            case 3:
                if (!isEnemyTankReachBoundary(this, 3)) {
                    this.setX(this.getX() - this.getSpeed());
                }else {
                    changeDirection();
                }
                break;
            case 4:
                if (!isEnemyTankReachBoundary(this, 4)) {
                    this.setX(this.getX() + this.getSpeed());
                }else {
                    changeDirection();
                }
                break;
        }
    }

    //敌人坦克随机移动  0-25 上 25-50 下 50-75 左 75-100 右
    public void changeDirection() {
        lastDirectionChangeTime = System.currentTimeMillis();
        double direction = Math.random() * 100;
        if (direction < 25) {
            this.setDirection(1);
        } else if (direction < 50) {
            this.setDirection(2);
        } else if (direction < 75) {
            this.setDirection(3);
        } else {
            this.setDirection(4);
        }
    }

    public int getShootTime() {
        return shootTime;
    }

    public void setShootTime(int shootTime) {
        this.shootTime = shootTime;
    }
}
