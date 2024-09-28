package enemyTank;

import Ammo.Ammo;
import tank.Tank;
import tankData.TankData;

import java.util.Iterator;

public class EnemyTank extends Tank implements Runnable {
    //每间隔shootTime就发射一颗子弹
    private int shootTime = 500;

    //初始化敌方坦克的数量
    public static  int enemyTankSize = 3;

    @Override
    public void run() {
        while (isLive()) {
            this.shoot();
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

    //重写isLive(),用我方坦克子弹进行判断是否被攻击了
    public Boolean isAttacked(Tank tank) {
        if (tank.getAmmos().isEmpty()) return true;
        Iterator<Ammo> ammoIterator = tank.getAmmos().values().iterator();
        switch (this.getDirection()) {
            case 1:
            case 2:
                while (ammoIterator.hasNext()) {
                    Ammo next = ammoIterator.next();
                    if (next.getX() >= this.getX() && next.getX() <= this.getX() + TankData.TANK_WHEEL_WIDTH * 2 + TankData.TANK_CIRCLE_DIA) {
                        if (next.getY() >= this.getY() && next.getY() <= this.getY() + TankData.TANK_WHEEL_HEIGHT) {
                            setLive(false);
                            return false;
                        }
                    }
                }
                break;
            case 3:
            case 4:
                while (ammoIterator.hasNext()) {
                    Ammo next = ammoIterator.next();
                    if (next.getX() >= this.getX() && next.getX() <= this.getX() + TankData.TANK_WHEEL_HEIGHT) {
                        if (next.getY() >= this.getY() && next.getY() <= this.getY() + TankData.TANK_WHEEL_WIDTH * 2 + TankData.TANK_CIRCLE_DIA) {
                            setLive(false);
                            return false;
                        }
                    }
                }
                break;
        }
        return true;
    }

    public int getShootTime() {
        return shootTime;
    }

    public void setShootTime(int shootTime) {
        this.shootTime = shootTime;
    }
}
