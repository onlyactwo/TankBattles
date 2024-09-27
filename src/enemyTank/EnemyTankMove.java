package enemyTank;

import tank.Tank;
import tankData.TankData;

public class EnemyTankMove implements Runnable {
    EnemyTank enemyTank;

    @Override
    public void run() {
        while (true) {
            //用于决定敌方坦克运动的方向
            double setDirection = (Math.random() * 100);
            //向上并且没有运动到边界
            if (setDirection < 25 && !Tank.isEnemyTankReachBoundary(enemyTank, 1)) {

                //写到了这里。。。待解决问题：敌方=坦克满足运动条件以后如何进行重新设置参数并重绘。。也就是子线程如何在父线程的panel上面绘制

                //向下并且没有运动到边界
            } else if (setDirection > 25 && setDirection < 50 && !Tank.isEnemyTankReachBoundary(enemyTank, 2)) {


                //向左并且没有运动到边界
            } else if (setDirection > 50 && setDirection < 75 && !Tank.isEnemyTankReachBoundary(enemyTank, 3)) {


                //向右并且没有运动到边界
            } else if (setDirection > 75 && !Tank.isEnemyTankReachBoundary(enemyTank, 4)) {

            }
        }
    }

    public EnemyTankMove(EnemyTank enemyTank) {
        this.enemyTank = enemyTank;
    }
}
