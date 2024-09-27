/**
 * MyPanel这个类包含：
 * 实现对键盘事件的监听
 * 一个坦克对象，敌人对象
 * 一个设置坦克的方法，初始化敌人坦克对象方法
 * paint方法 ，用于绘制背景，调用绘制坦克的方法 ---- 调用drawTank（）
 * drawTank（） ，用于判断敌我状态并分别使用不通过颜色的笔调用drawTank_direction_1_2_3_4（）
 * drawTank_direction_1_2_3_4（） 绘制出四个不同方向的坦克
 * 重写keyPresse（）监听方法，移动坦克
 * 重写updat（），解决刷新闪烁的问题
 */
package myPanel;

import Ammo.Ammo;
import enemyTank.EnemyTank;
import tank.Tank;
import tankData.TankData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

@SuppressWarnings({"all"})
public class MyPanel extends JPanel implements KeyListener {
    public MyPanel() {
        setDoubleBuffered(true);
    }

    //坦克对象
    private Tank tank = null;
    private int enemyTankSize = 1;//初始化坦克的数量
    private Vector<EnemyTank> enemyTanks = new Vector<>();
    //子弹对象(Hashtable具有线程安全)<线程名，子弹对象>
    private static Hashtable<String,Ammo> ammos = new Hashtable<>();

    //初始化坦克对象
    public void tankInitialize(Tank tank) {
        this.tank = tank;
    }

    public void enemyTankInitialize() {
        for (int i = 0; i < enemyTankSize; i++) {
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

    //子弹参数发生变化，并重新绘制,设置为静态方法,方便Ammo线程调用,这里参数设置两个，一个是线程的名字，一个是子弹对象)
    public static void setAndRepaintAmmo(String threadName,Ammo ammo){
        ammos.put(threadName,ammo);
        //this.repaint();
        /*
            问题：
            在Ammo线程中，修改子弹的参数，并传给MyPanel，希望更改MyPanel中子弹的参数 ---- 可以实现
            并进行重绘 ---- 无法实现（因为为了修改子弹的参数，并传给MyPanel，把setAndRepaintAmmo设置成了静态的方法，就无法再调用repaint方法了）


         */
    }
    //初始化窗口,以后重绘的时候还要再调用
    @Override
    public void paint(Graphics g) {
        //每一次绘制，敌方和我方都要绘制
        super.paint(g);
        System.out.println("paint被调用");
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, TankData.WINDOW_WIDTH, TankData.WINDOW_HEIGHT);//初始化界面背景为灰色

        //绘制我方坦克
        if (tank != null) {
            drawTank(tank.getX(), tank.getY(), tank.getDirection(), tank.getType(), g);
            System.out.println("正在绘制坦克");
        }
        //绘制敌方坦克(一次性把容器里面的全部绘制完)
        if (!enemyTanks.isEmpty()) {
            for (int i = 0; i < enemyTankSize; i++) {
                EnemyTank e = enemyTanks.get(i);
                drawTank(e.getX(), e.getY(), e.getDirection(), e.getType(), g);
            }
        }

        //绘制子弹 ---- 意味着MyPanel类必须含有一个Ammo对象，以此对其重绘 ---- 但是有多发子弹，说明要含有一个Ammo容器
        //线程不断对子弹容器里面的子弹数据进行修改，修改一次，就要全部重绘出来 ---- 怎么区别每一发子弹？编号！线程的名字是独一无二的！
        //线程安全，需要对容器进行线程同步！ ---- HashTable！

        //这里进行具体的子弹绘制
        if(!ammos.isEmpty()){
            Iterator<Ammo> ammoIterator = ammos.values().iterator();
            while (ammoIterator.hasNext()) {
                Ammo next = (Ammo) ammoIterator.next();
                drawAmmo(next,g);//还没具体实现
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
    public void drawAmmo(Ammo ammo ,Graphics g){

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


    //键盘监听
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
        }//按键去移动坦克
        else {
            Tank newTank = tank.move(e);//坦克移动并接收返回一个新的坦克
            setAndRepaintTanks(newTank);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //不需要
    }

    /*
        在动画的实现中，经常用到repaint()函数来重画屏幕，实现动画的加载，
        其实在Java中repaint()是通过两个步骤来实现刷新功能的，首先它调用public void update()来刷新屏幕，
        其次再调用paint(Graphcis g)来重画屏幕，这就容易造成闪烁，特别是一些需要重画背景的程序，
        如果下一桢图象可以完全覆盖上一桢图象的话，便可以重写update函数如下来消除闪烁：
        public void update(GraphiCS g){ paint(g) }
     */
    public void update(Graphics g) {
        paint(g);
    }
}

