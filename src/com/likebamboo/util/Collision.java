
package com.likebamboo.util;

import android.graphics.Rect;

import com.likebamboo.sprite.Pos;

/**
 * 游戏碰撞检测类
 * 
 * @author likebamboo
 */
public class Collision {
    /**
     * 矩形碰撞检测 参数为x,y,width,height
     * 
     * @param x1 第一个矩形的x
     * @param y1 第一个矩形的y
     * @param w1 第一个矩形的w
     * @param h1 第一个矩形的h
     * @param x2 第二个矩形的x
     * @param y2 第二个矩形的y
     * @param w2 第二个矩形的w
     * @param h2 第二个矩形的h
     * @return 是否碰撞
     */
    public static boolean isRectCls(int x1, int y1, int w1, int h1, int x2, int y2, int w2,
            int h2) {
        if (x2 > x1 && x2 > x1 + w1) {
            return false;
        } else if (x2 < x1 && x2 < x1 - w2) {
            return false;
        } else if (y2 > y1 && y2 > y1 + h1) {
            return false;
        } else if (y2 < y1 && y2 < y1 - h2) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 矩形碰撞检测 参数为Rect对象
     * 
     * @param r1 第一个Rect对象
     * @param r2 第二个Rect对象
     * @return 是否碰撞
     */
    public static boolean isRectCls(Rect r1, Rect r2) {
        return isRectCls(r1.left, r1.top, r1.right - r1.left, r1.bottom - r1.top, r2.left,
                r2.top, r2.right - r2.left, r2.bottom - r2.top);
    }

    /**
     * 圆形碰撞检测
     * 
     * @param x1 第一个圆的圆心x
     * @param y1 第一个圆的圆心y
     * @param r1 第一个圆的半径
     * @param x2 第二个圆的圆心x
     * @param y2 第二个圆的圆心y
     * @param r2 第二个圆的半径
     * @return 是否碰撞
     */
    public boolean isCircleCls(int x1, int y1, int r1, int x2, int y2, int r2) {
        // 两点距大于 2圆形半径距离
        if (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) > r1 + r2) {
            return false;
        }
        return true;
    }

    /**
     * 圆形与矩形碰撞检测
     * 
     * @param x1 第一个矩形的x
     * @param y1 第一个矩形的y
     * @param w1 第一个矩形的宽
     * @param h1 第一个矩形的高
     * @param x2 圆的圆心x
     * @param y2 圆的圆心y
     * @param r2 圆的半径r
     * @return 是否碰撞
     */
    public static boolean isC2RCls(int x1, int y1, int w1, int h1, int x2, int y2, int r2) {
        if ((Math.abs(x2 - (x1 + w1 / 2)) > w1 / 2 + r2)
                || Math.abs(y2 - (y1 + h1 / 2)) > h1 / 2 + r2) {
            return false;
        }
        return true;
    }

    /**
     * 多矩形碰撞
     * 
     * @param rArray1
     * @param rArray2
     * @return 是否碰撞
     */
    public static boolean isRectsCls(Rect[] rArray1, Rect[] rArray2) {
        for (Rect rt1 : rArray1) {
            for (Rect rt2 : rArray2) {
                if (isRectCls(rt1, rt2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * 判断线段是否在矩形内
     * <p>
     * 先看线段所在直线是否与矩形相交， 如果不相交则返回false， 如果相交，
     * 则看线段的两个点是否在矩形的同一边（即两点的x(y)坐标都比矩形的小x(y)坐标小，或者大）, 若在同一边则返回false， 否则就是相交的情况。
     * </p>
     * 
     * @param linePointX1 线段起始点x坐标
     * @param linePointY1 线段起始点y坐标
     * @param linePointX2 线段结束点x坐标
     * @param linePointY2 线段结束点y坐标
     * @param rectangleLeftTopX 矩形左上点x坐标
     * @param rectangleLeftTopY 矩形左上点y坐标
     * @param rectangleRightBottomX 矩形右下点x坐标
     * @param rectangleRightBottomY 矩形右下点y坐标
     * @return 是否相交
     */
    public static boolean isLineRectCls(int linePointX1, int linePointY1,
            int linePointX2, int linePointY2, int rectangleLeftTopX, int rectangleLeftTopY,
            int rectangleRightBottomX, int rectangleRightBottomY) {

        int lineHeight = linePointY1 - linePointY2;
        int lineWidth = linePointX2 - linePointX1;
        // 计算叉乘
        int c = linePointX1 * linePointY2 - linePointX2 * linePointY1;

        if ((lineHeight * rectangleLeftTopX + lineWidth * rectangleLeftTopY + c >= 0 && lineHeight
                * rectangleRightBottomX + lineWidth * rectangleRightBottomY + c <= 0)
                || (lineHeight * rectangleLeftTopX + lineWidth * rectangleLeftTopY + c <= 0 && lineHeight
                        * rectangleRightBottomX + lineWidth * rectangleRightBottomY + c >= 0)
                || (lineHeight * rectangleLeftTopX + lineWidth * rectangleRightBottomY + c >= 0 && lineHeight
                        * rectangleRightBottomX + lineWidth * rectangleLeftTopY + c <= 0)
                || (lineHeight * rectangleLeftTopX + lineWidth * rectangleRightBottomY + c <= 0 && lineHeight
                        * rectangleRightBottomX + lineWidth * rectangleLeftTopY + c >= 0)) {
            if (rectangleLeftTopX > rectangleRightBottomX) {
                int temp = rectangleLeftTopX;
                rectangleLeftTopX = rectangleRightBottomX;
                rectangleRightBottomX = temp;
            }
            if (rectangleLeftTopY < rectangleRightBottomY) {
                int temp = rectangleLeftTopY;
                rectangleLeftTopY = rectangleRightBottomY;
                rectangleRightBottomY = temp;
            }
            if ((linePointX1 < rectangleLeftTopX && linePointX2 < rectangleLeftTopX)
                    || (linePointX1 > rectangleRightBottomX && linePointX2 > rectangleRightBottomX)
                    || (linePointY1 > rectangleLeftTopY && linePointY2 > rectangleLeftTopY)
                    || (linePointY1 < rectangleRightBottomY && linePointY2 < rectangleRightBottomY)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 直线与矩形碰撞
     * 
     * @param linePos1 直线的第一个点
     * @param linePos2 直线的第二个点
     * @param rect 矩形
     * @return
     */
    public static boolean isLineIntersectRect(Pos linePos1, Pos linePos2, Rect rect) {
        return isLineRectCls((int)linePos1.x, (int)linePos1.y, (int)linePos2.x,
                (int)linePos2.y, rect.left, rect.top, rect.right, rect.bottom);
    }
}
