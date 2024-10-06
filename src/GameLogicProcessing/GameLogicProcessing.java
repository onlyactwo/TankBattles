package GameLogicProcessing;

import Boom.Boom;
import enemyTank.EnemyTank;
import myPanel.MyPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class GameLogicProcessing implements Runnable {
    private MyPanel myPanel;

    //构造器
    public GameLogicProcessing(MyPanel myPanel) {
        this.myPanel = myPanel;
    }

    @Override
    public void run() {
        //如果游戏没有结束，就重复检测
        while (!myPanel.isGameOver()) {

            //检测是否所有敌方坦克全部死亡
            if (!myPanel.getEnemyTanks().isEmpty()) {
                boolean isAllEnemyTankDead = true;
                for (EnemyTank enemyTank : myPanel.getEnemyTanks()) {
                    if (enemyTank.isLive()) {
                        isAllEnemyTankDead = false;
                        break;
                    }
                }
                if (isAllEnemyTankDead) gameOver();
            }

            //检查我方坦克是否被攻击,如果被攻击，启动炸弹，直接游戏结束
            if (myPanel.getTank().isAttacked(myPanel.getEnemyTanks()) && myPanel.getTank().isLive()) {
                System.out.println("检测到我方坦克被攻击");
                myPanel.getTank().setLive(false);
                Boom boom = new Boom(myPanel.getTank().getX(), myPanel.getTank().getY(), myPanel);
                Thread boomThread = new Thread(boom);
                boomThread.start();
                myPanel.getBooms().put(boomThread.getName(), boom);
                gameOver();
            }

            //检查敌方坦克是否被攻击,如果被攻击，启动炸弹
            if (!myPanel.getEnemyTanks().isEmpty()) {
                Iterator<EnemyTank> enemyTankIterator = myPanel.getEnemyTanks().iterator();
                while (enemyTankIterator.hasNext()) {
                    EnemyTank enemyTank = enemyTankIterator.next();
                    if (enemyTank.isAttacked(myPanel.getTank()) && enemyTank.isLive()) {
                        enemyTank.setLive(false);
                        Boom boom = new Boom(enemyTank.getX(), enemyTank.getY(), myPanel);
                        Thread boomThread = new Thread(boom);
                        boomThread.start();
                        System.out.println("炸弹线程启用！");
                        myPanel.getBooms().put(boomThread.getName(), boom);
                    }
                }
            }

        }

    }

    public void gameOver() {
        myPanel.setGameOver(true);
        String message = myPanel.getTank().isLive() ? "WE WIN!" : "WE LOSE!";
        JTextArea textArea = new JTextArea(message);
        textArea.setFont(new Font("Arial", Font.BOLD, 20)); // 设置字体
        textArea.setEditable(false);
        textArea.setBackground(null); // 背景透明

        JOptionPane.showMessageDialog(myPanel, textArea, "Game Over", JOptionPane.INFORMATION_MESSAGE);

    }

}
