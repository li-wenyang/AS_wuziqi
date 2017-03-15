package com.example.administrator.wuziqi;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

public class Win {

    private int connectsum;                               //记录相连的棋子个数

    //判断是否胜利
    public boolean Win(ArrayList<Point> sum , int gezisize) {
        //只要满足五子连珠的其中一种情况，则表示胜利
        if (horizontal(sum,gezisize) || vertical(sum,gezisize) || upslant(sum,gezisize) || downslant(sum,gezisize)) {
            Log.d("Open", "win");
            connectsum = 0;
            return true;
        }
        return false;
    }

    /*
    判断五子连珠时脑子里就蹦出两种方法，选用的方法一
    方法一：
    判断方法五子连珠方法：首先我们要知道，如果有五子连珠，那肯定都是以中间那个棋子为中心的，
    那我们只要判断离中间那个棋子的距离小于两格的棋子的数量是否达到五就好了，达到了那就表示有五子连珠，
    这个方法适用五子连珠的四种情况。
    方法二：
    写个有参（一颗棋子的坐标，point）有返回值（一颗棋子的坐标，point）的方法，遍历每颗棋子，
    把该棋子调入判断是否有两边相邻的棋子，如果有，把相邻的棋子当做参数调入该方法，相连棋子个数（connectsum）+1。
    */

    //判断水平是否五子相连
    public boolean horizontal(ArrayList<Point> sum, int gezisize) {
        //两个棋子的距离，用来判断是否有五子连珠的可能性
        int wanting;
        //循环遍历每个棋子，这个循环遍历出来的棋子被当做五子连珠中的中间棋子，判断是否有五子连珠
        for (int i = 0; i < sum.size(); i++) {
            //相连棋子数
            connectsum = 0;
            Point mpoint = sum.get(i);
            int xx = mpoint.x;
            int yy = mpoint.y;
            for (int j = 0; j < sum.size(); j++) {
                Point mmpoint = sum.get(j);
                int xxx = mmpoint.x;
                int yyy = mmpoint.y;
                if (yyy == yy) {
                    //求两数之差的绝对值
                    wanting = xxx > xx ? xxx - xx : xx - xxx;
                    //判断距离小于两格的棋子数
                    if (wanting <= gezisize * 2)
                        connectsum++;
                }
            }
            //如果距离小于两格的棋子数有五个，表示有五子连珠
            if (connectsum == 5)
                return true;
        }
        return false;
    }

    //判断垂直是否五子相连
    public boolean vertical(ArrayList<Point> sum, int gezisize) {
        //两个棋子的距离
        int wanting;
        for (int i = 0; i < sum.size(); i++) {
            connectsum = 0;
            Point mpoint = sum.get(i);
            int xx = mpoint.x;
            int yy = mpoint.y;
            for (int j = 0; j < sum.size(); j++) {
                Point mmpoint = sum.get(j);
                int xxx = mmpoint.x;
                int yyy = mmpoint.y;
                if (xxx == xx) {
                    //求两数之差的绝对值
                    wanting = yyy > yy ? yyy - yy : yy - yyy;
                    if (wanting <= gezisize * 2)
                        connectsum++;
                }
            }
            if (connectsum == 5)
                return true;
        }
        return false;
    }

    //判断上升斜线是否五子相连
    public boolean upslant(ArrayList<Point> sum, int gezisize) {
        int wanting;
        for (int i = 0; i < sum.size(); i++) {
            connectsum = 0;
            Point mpoint = sum.get(i);
            int xx = mpoint.x;
            int yy = mpoint.y;
            for (int j = 0; j < sum.size(); j++) {
                Point mmpoint = sum.get(j);
                int xxx = mmpoint.x;
                int yyy = mmpoint.y;
                if (xxx - xx == yy - yyy) {
                    //求两数之差的绝对值
                    wanting = yyy > yy ? yyy - yy : yy - yyy;
                    if (wanting <= gezisize * 2)
                        connectsum++;
                }
            }
            if (connectsum == 5)
                return true;
        }
        return false;
    }

    //判断下降斜线是否五子相连
    public boolean downslant(ArrayList<Point> sum, int gezisize) {
        int wanting;
        for (int i = 0; i < sum.size(); i++) {
            connectsum = 0;
            Point mpoint = sum.get(i);
            int xx = mpoint.x;
            int yy = mpoint.y;
            for (int j = 0; j < sum.size(); j++) {
                Point mmpoint = sum.get(j);
                int xxx = mmpoint.x;
                int yyy = mmpoint.y;
                if (xxx - xx == yyy - yy) {
                    //求两数之差的绝对值
                    wanting = yyy > yy ? yyy - yy : yy - yyy;
                    if (wanting <= gezisize * 2)
                        connectsum++;
                }
            }
            if (connectsum == 5)
                return true;
        }
        return false;
    }
}
