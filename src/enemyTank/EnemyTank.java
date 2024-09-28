package enemyTank;

import tank.Tank;

import java.util.concurrent.ThreadLocalRandom;

public class EnemyTank extends Tank implements Runnable {
    //每间隔shootTime就发射一颗子弹
    private int shootTime = 500;

    public int getShootTime() {
        return shootTime;
    }

    public void setShootTime(int shootTime) {
        this.shootTime = shootTime;
    }

    @Override
    public void run() {
        while (true) {
            this.shoot();
            try {
                Thread.sleep(shootTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //初始化敌方坦克的数量
    public static final int enemyTankSize = 1;

    public EnemyTank(int x, int y, int direction, int type) {
        super(x, y, direction, type);
    }
}
