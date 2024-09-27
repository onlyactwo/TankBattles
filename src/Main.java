import myPanel.MyPanel;
import tank.Tank;
import tankData.TankData;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        //创建窗口，标题，哈哈
        JFrame myFrame = new JFrame("坦克大战");
        //设置窗口大小
        myFrame.setSize(TankData.WINDOW_WIDTH,TankData.WINDOW_HEIGHT);
        //设置可见
        myFrame.setVisible(true);
        //设置关闭
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //创建画板
        MyPanel myPanel_0 = new MyPanel();
        //添加画板
        myFrame.add(myPanel_0);
        //为myPanel添加监听器
        myFrame.addKeyListener(myPanel_0);
        //初始化一个坦克,敌人坦克
        myPanel_0.tankInitialize(new Tank(100,100,1,0));
        myPanel_0.enemyTankInitialize();

    }
}

