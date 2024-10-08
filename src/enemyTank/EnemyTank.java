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
    public static int enemyTankSize = 5;
    public static boolean isGameOver = false;
    @Override
    public void run() {
        while (isLive()&&!isGameOver) {
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

    //判断是否敌方坦克到达了边界
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

    //敌方坦克移动
    public void move() {
        //由于这里敌方坦克move不是返回一个新的坦克，就需要手动的去更改中心坐标的值
        switch (getDirection()) {
            case 1:
                if (!isEnemyTankReachBoundary(this, 1)) {
                    this.setY(this.getY() - this.getSpeed());
                    this.setCentreX(this.getX() + (TankData.TANK_WHEEL_HEIGHT / 2));
                    this.setCentreY(this.getY() + (TankData.TANK_WHEEL_HEIGHT / 2));
                }else{
                    changeDirection();
                }
                break;
            case 2:
                if (!isEnemyTankReachBoundary(this, 2)) {
                    this.setY(this.getY() + this.getSpeed());
                    this.setCentreX(this.getX() + (TankData.TANK_WHEEL_HEIGHT / 2));
                    this.setCentreY(this.getY() + (TankData.TANK_WHEEL_HEIGHT / 2));
                }else {
                    changeDirection();
                }
                break;
            case 3:
                if (!isEnemyTankReachBoundary(this, 3)) {
                    this.setX(this.getX() - this.getSpeed());
                    this.setCentreX(this.getX() + (TankData.TANK_WHEEL_HEIGHT / 2));
                    this.setCentreY(this.getY() + (TankData.TANK_WHEEL_HEIGHT / 2));
                }else {
                    changeDirection();
                }
                break;
            case 4:
                if (!isEnemyTankReachBoundary(this, 4)) {
                    this.setX(this.getX() + this.getSpeed());
                    this.setCentreX(this.getX() + (TankData.TANK_WHEEL_HEIGHT / 2));
                    this.setCentreY(this.getY() + (TankData.TANK_WHEEL_HEIGHT / 2));
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
