
package myPanel;

import Ammo.Ammo;
import Boom.Boom;
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
public class MyPanel extends JPanel implements KeyListener, Runnable {
    public MyPanel() {
        setDoubleBuffered(true);//双缓冲
    }

    //游戏状态
    private boolean isGameOver = false;

    //我方坦克对象
    private Tank tank;

    //敌方坦克集合
    private Vector<EnemyTank> enemyTanks = new Vector<>();

    //炸弹集合(boomThreadName,boom)
    private Hashtable<String ,Boom> booms = new Hashtable<>();

    //初始化我方坦克对象
    public void tankInitialize(Tank tank) {
        this.tank = tank;
    }

    //初始化敌方坦克对象集合，并启动敌人坦克线程
    public void enemyTankInitialize() {
        for (int i = 0; i < EnemyTank.enemyTankSize; i++) {
            //初始化敌方坦克的坐标，添加到enemyTanks中
            EnemyTank enemyTank = new EnemyTank(100 * (i + 1), 100 * (i + 1), 2, 1);
            enemyTanks.add(enemyTank);
            Thread enemyTankAction = new Thread(enemyTank);
            enemyTankAction.start();
        }
    }

    //我方坦克参数发生变化，重新设置坦克参数
    public void setTank(Tank tank) {
        this.tank = tank;
    }

    /**
     * paint方法的功能
     * 初始化画板
     * 绘制我方坦克
     * 绘制敌方所有坦克
     * 绘制我方坦克子弹
     * 绘制敌方所有坦克子弹
     */

    @Override
    public void run() {
        while (!isGameOver()) {
            //检测是否所有敌方坦克全部死亡
            if (!enemyTanks.isEmpty()) {
                boolean isAllEnemyTankDead = true;
                for (EnemyTank enemyTank : enemyTanks) {
                    if (enemyTank.isLive()) {
                        isAllEnemyTankDead = false;
                        break;
                    }
                }
                if (isAllEnemyTankDead) gameOver();
            }
            //检查我方坦克是否被攻击,如果被攻击，启动炸弹，直接游戏结束
            if (tank.isAttacked(enemyTanks)&&tank.isLive()) {
                tank.setLive(false);
                Boom boom = new Boom(tank.getX(), tank.getY(),this);
                Thread boomThread = new Thread(boom);
                boomThread.start();
                booms.put(boomThread.getName(),boom);
                gameOver();
            }
            //检查敌方坦克是否被攻击,如果被攻击，启动炸弹
            if (!enemyTanks.isEmpty()) {
                Iterator<EnemyTank> enemyTankIterator = enemyTanks.iterator();
                while (enemyTankIterator.hasNext()) {
                    EnemyTank enemyTank = enemyTankIterator.next();
                    if (enemyTank.isAttacked(tank)&&enemyTank.isLive()) {
                        enemyTank.setLive(false);
                        Boom boom = new Boom(enemyTank.getX(), enemyTank.getY(),this);
                        Thread boomThread = new Thread(boom);
                        boomThread.start();
                        booms.put(boomThread.getName(),boom);
                    }
                }
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //System.out.println("paint被调用");
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, TankData.WINDOW_WIDTH, TankData.WINDOW_HEIGHT);//初始化界面背景为灰色

        //绘制我方坦克(存活状态才进行绘制)
        if (tank != null && tank.isLive()) {
            drawTank(tank.getX(), tank.getY(), tank.getDirection(), tank.getType(), g);
        }

        //绘制敌方坦克(一次性把容器里面的全部绘制完，存活状态才进行绘制)
        if (!enemyTanks.isEmpty()) {
            for (int i = 0; i < EnemyTank.enemyTankSize; i++) {
                EnemyTank e = enemyTanks.get(i);
                if (e.isLive()) {
                    drawTank(e.getX(), e.getY(), e.getDirection(), e.getType(), g);
                }
            }
        }

        //绘制我方坦克子弹（把tank对象的Hashtable<String, Ammo> ammos子弹容器全部绘制完）
        if (!tank.getAmmos().isEmpty()) {
            //保证遍历和修改子弹容器只有一个线程在操作
            synchronized (tank.getAmmos()) {
                Iterator<Ammo> ammoIterator = tank.getAmmos().values().iterator();
                while (ammoIterator.hasNext()) {
                    Ammo next = null;
                    next = (Ammo) ammoIterator.next();
                    drawAmmo(next, g);
                }
            }
        }

        //绘制敌方坦克子弹
        if (!enemyTanks.isEmpty()) {
            for (EnemyTank enemyTank : enemyTanks) {
                synchronized (enemyTank.getAmmos()) {
                    if (!enemyTank.getAmmos().isEmpty()) {
                        Iterator<Ammo> ammoIterator = enemyTank.getAmmos().values().iterator();
                        while (ammoIterator.hasNext()) {
                            Ammo next = null;
                            next = (Ammo) ammoIterator.next();
                            drawAmmo(next, g);
                        }
                    }
                }
            }
        }

        //画炸弹
        if (!booms.isEmpty()) {
            Iterator<Boom> it = booms.values().iterator();
            synchronized (getBooms()) {
                while (it.hasNext()) {
                    Boom boom =  it.next();
                    drawBoom(boom,g);
                }
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
        if (ammo == null) return;
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

    //画炸弹
    public void drawBoom(Boom boom, Graphics g) {
        if (boom.getLive() >= 6) {
            g.drawImage(boom.getImage1(), boom.getX(), boom.getY(), 60, 60, this);
        } else if (boom.getLive() >= 3 && boom.getLive() <= 6) {
            g.drawImage(boom.getImage2(), boom.getX(), boom.getY(), 60, 60, this);
        } else if (boom.getLive() > 0 && boom.getLive() < 3) {
            g.drawImage(boom.getImage3(), boom.getX(), boom.getY(), 60, 60, this);
        }

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
        if (tank == null) return;
        //按键去发射子弹
        if ((char) e.getKeyCode() == 'J') {
            System.out.println("当前炸弹数量： " + booms.size());
            System.out.println("当前子弹数量 ： " + tank.getAmmos().size());
            tank.shoot();
        } else {
            //按键去移动坦克
            Tank newTank = tank.move(e);//坦克移动并接收返回一个新的坦克
            setTank(newTank);

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

    public void gameOver() {
        setGameOver(true);

        String message = tank.isLive() ? "WE WIN!" : "WE LOSE!";
        JTextArea textArea = new JTextArea(message);
        textArea.setFont(new Font("Arial", Font.BOLD, 20)); // 设置字体
        textArea.setEditable(false);
        textArea.setBackground(null); // 背景透明

        JOptionPane.showMessageDialog(this, textArea, "Game Over", JOptionPane.INFORMATION_MESSAGE);

    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public Hashtable<String, Boom> getBooms() {
        return booms;
    }
}

