/**
 * 此类是坦克的一些基本属性：
 * 轮子的长宽
 * 圆形
 * 界面的大小
 * 炮管的长宽
 * isTankReachBoundary，判断我方坦克是否达到了边界，需要传入坦克，Keyevent
 * isEnemyTankReachBoundary，判断敌方坦克是否达到了边界，需要传入敌方坦克，和即将要运动的方向
 */
package tankData;

import tank.Tank;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public final class TankData {
    public static final int WINDOW_HEIGHT = 800;//窗口长度
    public static final int WINDOW_WIDTH = 800;//窗口宽度
    public static final int TANK_WHEEL_HEIGHT = 60;
    public static final int TANK_WHEEL_WIDTH = 20;
    public static final int TANK_CIRCLE_DIA = 30;
    public static final int TANK_GUN_HEIGHT = 50;
    public static final int TANK_GUN_WIDTH = 10;
    public static final int TANK_AMMO_HIGHT = 4;
    public static final int TANK_AMMO_WIDTH = 4;
    public static final int CENTRE_R = 40;


}
