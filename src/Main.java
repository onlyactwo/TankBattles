import GameLogicProcessing.GameLogicProcessing;
import myPanel.MyPanel;
import tank.Tank;
import tankData.TankData;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        gameInitialize();
    }

    public static void gameInitialize() {
        //创建窗口，标题
        JFrame myFrame = new JFrame("坦克大战");
        //设置窗口大小
        myFrame.setSize(TankData.WINDOW_WIDTH, TankData.WINDOW_HEIGHT);
        //设置可见
        myFrame.setVisible(true);
        //设置关闭
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //创建画板，创建线程
        MyPanel myPanel_0 = new MyPanel();
        Thread myPanelRunnable = new Thread(myPanel_0);
        //添加画板
        myFrame.add(myPanel_0);
        //为myPanel添加监听器
        myFrame.addKeyListener(myPanel_0);
        //初始化一个坦克,敌人坦克
        myPanel_0.tankInitialize(new Tank(400, 100, 1, 0));
        myPanel_0.enemyTankInitialize();

        //创建游戏逻辑处理线程
        GameLogicProcessing gameLogicProcessing = new GameLogicProcessing(myPanel_0);
        Thread gameLogicProcessingThread = new Thread(gameLogicProcessing);

        //启动线程！必须在所有初始化完成之后
        myPanelRunnable.start();
        gameLogicProcessingThread.start();


    }
}

