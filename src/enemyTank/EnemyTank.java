package enemyTank;

import tank.Tank;

public class EnemyTank extends Tank {
    //初始化敌方坦克的数量
    public static final int enemyTankSize = 1;

    public EnemyTank(int x, int y, int direction, int type) {
        super(x, y, direction, type);
    }
}
