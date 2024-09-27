
package myPanel;

import Ammo.Ammo;
import enemyTank.EnemyTank;
import tank.Tank;
import tankData.TankData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Vector;

@SuppressWarnings({"all"})
public class MyPanel extends JPanel implements KeyListener, Runnable {
    public MyPanel() {
        setDoubleBuffered(true);//双缓冲
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            repaint();
        }
    }

    //我方坦克对象
    private Tank tank = null;

    //敌方坦克集合
    private Vector<EnemyTank> enemyTanks = new Vector<>();

    //初始化我方坦克对象
    public void tankInitialize(Tank tank) {
        this.tank = tank;
    }

    //初始化敌方坦克对象集合
    public void enemyTankInitialize() {
        for (int i = 0; i < EnemyTank.enemyTankSize; i++) {
            //初始化敌方坦克的坐标，添加到enemyTanks中
            EnemyTank enemyTank = new EnemyTank(100 * (i + 1), 0, 2, 1);
            enemyTanks.add(enemyTank);
        }
    }

    //我方坦克参数发生变化，重新设置坦克参数，重新绘制
    public void setAndRepaintTanks(Tank tank) {
        System.out.println("坦克参数重置，重新绘制");
        this.tank = tank;
        this.repaint();
    }

    //初始化窗口,以后重绘的时候还要再调用
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //System.out.println("paint被调用");
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, TankData.WINDOW_WIDTH, TankData.WINDOW_HEIGHT);//初始化界面背景为灰色

        //绘制我方坦克
        if (tank != null) {
            drawTank(tank.getX(), tank.getY(), tank.getDirection(), tank.getType(), g);
            //System.out.println("正在绘制坦克");
        }

        //绘制敌方坦克(一次性把容器里面的全部绘制完)
        if (!enemyTanks.isEmpty()) {
            for (int i = 0; i < EnemyTank.enemyTankSize; i++) {
                EnemyTank e = enemyTanks.get(i);
                drawTank(e.getX(), e.getY(), e.getDirection(), e.getType(), g);
            }
        }

        //绘制子弹（把tank对象的Hashtable<String, Ammo> ammos子弹容器全部绘制完）
        if (!tank.getAmmos().isEmpty()) {
            Iterator<Ammo> ammoIterator = tank.getAmmos().values().iterator();
            while (ammoIterator.hasNext()) {
                Ammo next = (Ammo) ammoIterator.next();
                drawAmmo(next, g);
            }
        }

    }

    /**
     * 画坦克
     *
     * @param x         坦克的初始位置（左上）
     * @param y         坦克的初始位置（左上）
     * @param direction 坦克的朝向 (1,2,3,4  分别代表 上下左右)
     * @param type      敌我坦克状态 ,0 代表自己 ，1代表敌人
     * @param g         画笔
     */

    //画坦克
    public void drawTank(int x, int y, int direction, int type, Graphics g) {
        //根据状态绘制不同颜色
        switch (type) {
            case 0://己方坦克
                g.setColor(Color.GREEN);//己方坦克设置为绿色
                break;
            case 1://敌方坦克
                g.setColor(Color.RED);//敌方坦克设置会红色
                break;
        }
        //绘制不同方向的坦克
        switch (direction) {
            case 1:
                drawTank_direction_1(x, y, g);
                break;
            case 2:
                drawTank_direction_2(x, y, g);
                break;
            case 3:
                drawTank_direction_3(x, y, g);
                break;
            case 4:
                drawTank_direction_4(x, y, g);
                break;
        }
    }

    //绘制子弹
    public void drawAmmo(Ammo ammo, Graphics g) {
        switch (ammo.getType()) {
            case 0:
                g.setColor(Color.GREEN);
                break;
            case 1:
                g.setColor(Color.RED);
                break;
        }
        g.fill3DRect(ammo.getX(), ammo.getY(), TankData.TANK_AMMO_WIDTH, TankData.TANK_AMMO_HIGHT, false);
    }

    //画出四个方向的坦克
    //上
    private void drawTank_direction_1(int x, int y, Graphics g) {
        //轮子
        g.fillRect(x, y, TankData.TANK_WHEEL_WIDTH, TankData.TANK_WHEEL_HEIGHT);
        //轮子
        g.fillRect(x + TankData.TANK_WHEEL_WIDTH + TankData.TANK_CIRCLE_DIA, y, TankData.TANK_WHEEL_WIDTH, TankData.TANK_WHEEL_HEIGHT);
        //圆
        g.fillOval(x + TankData.TANK_WHEEL_WIDTH, y + ((TankData.TANK_WHEEL_HEIGHT / 2) - (TankData.TANK_CIRCLE_DIA / 2)), TankData.TANK_CIRCLE_DIA, TankData.TANK_CIRCLE_DIA);
        //炮
        g.fillRect(x + TankData.TANK_WHEEL_WIDTH + (TankData.TANK_CIRCLE_DIA / 3), y + (TankData.TANK_WHEEL_HEIGHT / 2) - TankData.TANK_GUN_HEIGHT, TankData.TANK_GUN_WIDTH, TankData.TANK_GUN_HEIGHT);
    }

    //下
    private void drawTank_direction_2(int x, int y, Graphics g) {
        //轮子
        g.fillRect(x, y, TankData.TANK_WHEEL_WIDTH, TankData.TANK_WHEEL_HEIGHT);
        //轮子
        g.fillRect(x + TankData.TANK_WHEEL_WIDTH + TankData.TANK_CIRCLE_DIA, y, TankData.TANK_WHEEL_WIDTH, TankData.TANK_WHEEL_HEIGHT);
        //圆
        g.fillOval(x + TankData.TANK_WHEEL_WIDTH, y + ((TankData.TANK_WHEEL_HEIGHT / 2) - (TankData.TANK_CIRCLE_DIA / 2)), TankData.TANK_CIRCLE_DIA, TankData.TANK_CIRCLE_DIA);
        //炮
        g.fillRect(x + TankData.TANK_WHEEL_WIDTH + (TankData.TANK_CIRCLE_DIA / 3), y + TankData.TANK_WHEEL_HEIGHT / 2, TankData.TANK_GUN_WIDTH, TankData.TANK_GUN_HEIGHT);
    }

    //左
    private void drawTank_direction_3(int x, int y, Graphics g) {
        g.fillRect(x, y, TankData.TANK_WHEEL_HEIGHT, TankData.TANK_WHEEL_WIDTH);
        g.fillRect(x, y + TankData.TANK_WHEEL_WIDTH + TankData.TANK_CIRCLE_DIA, TankData.TANK_WHEEL_HEIGHT, TankData.TANK_WHEEL_WIDTH);
        g.fillOval(x + (TankData.TANK_WHEEL_HEIGHT / 2) - (TankData.TANK_CIRCLE_DIA / 2), y + TankData.TANK_WHEEL_WIDTH, TankData.TANK_CIRCLE_DIA, TankData.TANK_CIRCLE_DIA);
        g.fillRect(x + (TankData.TANK_WHEEL_HEIGHT / 2) - TankData.TANK_GUN_HEIGHT, y + TankData.TANK_WHEEL_WIDTH + (TankData.TANK_CIRCLE_DIA / 3), TankData.TANK_GUN_HEIGHT, TankData.TANK_GUN_WIDTH);
    }

    //右
    private void drawTank_direction_4(int x, int y, Graphics g) {
        g.fillRect(x, y, TankData.TANK_WHEEL_HEIGHT, TankData.TANK_WHEEL_WIDTH);
        g.fillRect(x, y + TankData.TANK_WHEEL_WIDTH + TankData.TANK_CIRCLE_DIA, TankData.TANK_WHEEL_HEIGHT, TankData.TANK_WHEEL_WIDTH);
        g.fillOval(x + (TankData.TANK_WHEEL_HEIGHT / 2) - (TankData.TANK_CIRCLE_DIA / 2), y + TankData.TANK_WHEEL_WIDTH, TankData.TANK_CIRCLE_DIA, TankData.TANK_CIRCLE_DIA);
        g.fillRect(x + (TankData.TANK_WHEEL_HEIGHT / 2), y + TankData.TANK_WHEEL_WIDTH + (TankData.TANK_CIRCLE_DIA / 3), TankData.TANK_GUN_HEIGHT, TankData.TANK_GUN_WIDTH);
    }


    //键盘监听机制
    @Override
    public void keyTyped(KeyEvent e) {
        //不需要
    }

    //响应键盘按动
    @Override
    public void keyPressed(KeyEvent e) {

        //按键去发射子弹
        if ((char) e.getKeyCode() == 'J') {
            tank.shoot();
        } else {
            //按键去移动坦克
            Tank newTank = tank.move(e);//坦克移动并接收返回一个新的坦克
            setAndRepaintTanks(newTank);

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //不需要
    }

    //修复闪烁问题
    public void update(Graphics g) {
        paint(g);
    }
}

