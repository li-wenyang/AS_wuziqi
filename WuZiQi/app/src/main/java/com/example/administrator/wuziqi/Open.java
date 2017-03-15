package com.example.administrator.wuziqi;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;
import android.graphics.Point;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.ArrayList;

/*
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import android.content.res.Configuration;
import android.database.Cursor;
import android.app.Activity;
 */

public class Open extends View {

    private int qipansize;                                       //棋盘边长
    private int qipangeshu = 8;                                  //棋盘格数
    private Paint mpaint = new Paint();                           //创建画笔
    private int gezisize;                                         //每格边长
    private int screenWidth;                                     //手机屏幕宽
    private int screenHeight;                                    //手机屏幕高
    private boolean whochess = true;                             //判断谁下的参数，true是白棋下，false是黑棋下
    private ArrayList<Point> whitesum = new ArrayList<>();        //储存白棋坐标
    private ArrayList<Point> blacksum = new ArrayList<>();        //储存黑棋坐标
    private Bitmap whitepieces;                                   //存放白棋图片资源
    private Bitmap blackpieces;                                   //存放黑棋图片资源
    private boolean gameover = false;                            //判断游戏是否结束,true表示结束
    private boolean isfupan = false;                             //判断是复盘局还是正常棋盘
    private int fpstep;                                           //记录复盘时所画的棋子数
    private ArrayList<Point> fpsum = new ArrayList<>();           //储存复盘棋子坐标
    private MyDatabaseHelper dbhelper;
    private Win win;

    /*
    private int high;
    private int change = (qipangeshu+3)*(qipangeshu+2);       //转屏时棋盘改变的大小
    private int[] whitex = new int[qipangeshu*qipangeshu];    //记录白棋x坐标
    private int[] whitey = new int[qipangeshu*qipangeshu];    //记录白棋y坐标
    private int[] blackx = new int[qipangeshu*qipangeshu];    //记录白棋x坐标
    private int[] blacky = new int[qipangeshu*qipangeshu];    //记录白棋y坐标
    private int white = 0;
    private int black = 0;
    */

    public Open(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        // 获取手机屏幕宽
        screenWidth = dm.widthPixels;
        // 获取手机屏幕高
        screenHeight = dm.heightPixels;

        //设置棋盘背景色为红色
        setBackgroundColor(0x44ff0000);
        Log.d("Open", "Open");
    }

    //重写onMeasure获取大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /*
        获取width尺寸(因为知道设的是match_parent，所以就不判断mode了)
        int qipansizewidth = MeasureSpec.getSize(widthMeasureSpec);
        获取height尺寸
        int qipansizeheight = MeasureSpec.getSize(heightMeasureSpec);
        */

        //因为棋盘是正方形，所以取小的为边
        int lowsize = Math.min(screenWidth, screenHeight);
        qipansize = lowsize;
        //重新设置View的宽高
        setMeasuredDimension(qipansize, qipansize);
        gezisize = qipansize / (qipangeshu+2);
        Log.d("Open", "onMeasure");
    }

    //重写onDraw绘图
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //去锯齿
        mpaint.setAntiAlias(true);
        //防抖动
        mpaint.setDither(true);
        //调用drawBoard方法画线
        drawBoard(canvas);
        //获取图片资源
        whitepieces = BitmapFactory.decodeResource(getResources(), R.drawable.b);
        blackpieces = BitmapFactory.decodeResource(getResources(), R.drawable.h);
        //把棋子变得跟棋格一样大
        whitepieces = Bitmap.createScaledBitmap(whitepieces, gezisize, gezisize, true);
        blackpieces = Bitmap.createScaledBitmap(blackpieces, gezisize, gezisize, true);

        //判断是复盘局还是游戏局
        if (isfupan)
            fpdrawChess(canvas);
        else {
            //调用drawChess方法画棋子
            drawChess(canvas);
            gameover();
        }
        Log.d("Open", "onDraw");
    }

    //构造画线的方法
    public void drawBoard(Canvas canvas) {
        for (int i = 0; i < qipangeshu + 1; i++) {
            //画横线
            canvas.drawLine(gezisize, gezisize * (i + 1), gezisize * (qipangeshu+1), gezisize * (i + 1), mpaint);
            //画纵线
            canvas.drawLine(gezisize * (i + 1), gezisize, gezisize * (i + 1), gezisize * (qipangeshu+1), mpaint);
        }
        Log.d("Open", "drawBoard");
    }

    //构造画棋子方法
    public void drawChess(Canvas canvas) {
        for (int i = 0; i < whitesum.size(); i++) {
            //把whitesum中的白棋位置赋给point
            Point point = whitesum.get(i);

            /*
            画棋方法二
            获取白棋图片资源
            Drawable drawable = getResources().getDrawable(R.drawable.b );
            改变白棋大小，使白棋显示在固定区域
            drawable.setBounds(point.x-gezisize/2, point.y-gezisize/2,point.x+gezisize/2 , point.y+gezisize/2);
            画白棋
            drawable.draw(canvas);
            */

            canvas.drawBitmap(whitepieces, point.x - gezisize / 2, point.y - gezisize / 2, null);
        }

        for (int i = 0; i < blacksum.size(); i++) {
            //把blacksum中的黑棋位置赋给point
            Point point = blacksum.get(i);

            /*
            画棋方法二
            获取黑棋图片资源
            Drawable drawable = getResources().getDrawable(R.drawable.h );
            改变黑棋大小，使黑棋显示在固定区域
            drawable.setBounds(point.x-gezisize/2, point.y-gezisize/2,point.x+gezisize/2 , point.y+gezisize/2);
            画黑棋
            drawable.draw(canvas);
            */

            canvas.drawBitmap(blackpieces, point.x - gezisize / 2, point.y - gezisize / 2, null);
        }
        Log.d("Open", "drawChess");
    }

    //点击
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //判断游戏是否结束
        if (gameover)
            return false;
        //当手指离开屏幕时再画
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (isfupan) {
                    fpstep++;
                    invalidate();
                }
                else {
                    //获取点击处的坐标
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    //如果点击的点在棋盘外就不算（其实就是去除屏幕边界那些点）
                    if (x > gezisize / 2 && x < qipansize - gezisize / 2 && y > gezisize / 2 && y < qipansize - gezisize / 2) {
                        //调用zuanhuanPoint（）方法，把点击点的坐标转换成棋盘上离最近的棋盘的点的坐标
                        Point point = zuanhuanPoint(x, y);

                        //如果黑棋的集合或者白棋的集合包含这个坐标，那么返回false,不往集合里添加这个坐标，以及不刷新界面
                        if (whitesum.contains(point) || blacksum.contains(point)) {
                            return false;
                        }

                        //如果whochess == 0，把该坐标添加到whitesum（白棋集合）集合中去
                        if (whochess) {

                            whitesum.add(point);
                            whochess = false;

                            /*
                            whitex[white] = point.x;
                            whitey[white] = point.y;
                            white++;
                            Log.d("Open",""+white);
                            */
                        }

                        //如果whochess == 1，把该坐标添加到blacksum（黑棋集合）集合中去
                        else {

                            blacksum.add(point);
                            whochess = true;

                            /*
                            blackx[black] = point.x;
                            blacky[black] = point.y;
                            black++;
                            */
                        }
                        //刷新View
                        invalidate();
                    }
                    Log.d("Open", "onTouchEvent");
                }
            }
        return true;
    }

    //根据点击的坐标，转换成棋盘上离最近的点的坐标
    public Point zuanhuanPoint(int x, int y) {
        int Xfact;
        int Yfact;
        int Xyushu = x % gezisize;
        int Yyushu = y % gezisize;
        if (Xyushu > gezisize / 2)
            Xfact = x - Xyushu + gezisize;
        else
            Xfact = x - Xyushu;

        if (Yyushu > gezisize / 2)
            Yfact = y - Yyushu + gezisize;
        else
            Yfact = y - Yyushu;
        //把转换好的坐标返回给point
        Log.d("Open", "zuanhuanPoint");
        return new Point(Xfact, Yfact);
    }

    //游戏结束时的处理
    public void gameover() {
        win = new Win();
        //调用win方法，判断是否有玩家获胜，如果返回true，说明玩家获胜
        if (win.Win(whitesum,gezisize)) {
            gameover = true;
            //设置消息框
            new AlertDialog.Builder(getContext()).setTitle(getResources().getText(R.string.whitewin)).setPositiveButton(getResources().getText(R.string.sure), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    //如果点击确定，那么调用initialize()方法清除上把的数据，然后调用invalidate()刷新界面
                    initialize();
                    invalidate();

                }
            }).setNegativeButton(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
        }

        if (win.Win(blacksum,gezisize)) {
            gameover = true;
            new AlertDialog.Builder(getContext()).setTitle(getResources().getText(R.string.blackwin)).setPositiveButton(getResources().getText(R.string.sure), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    initialize();
                    invalidate();
                }
            }).setNegativeButton(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
        }

        if (gameover) {
            //建数据库
            dbhelper = new MyDatabaseHelper(getContext(), "QPXXStory.db", null, 3);
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            //存数据
            dbhelper.existtable(db, whitesum, blacksum);
        }
        Log.d("Open", "gameover");
    }

    //初始化一些参数的方法
    public void initialize() {
        whochess = true;
        gameover = false;
        whitesum.clear();
        blacksum.clear();
        //white = 0;
        //black = 0;
    }

    //悔棋的方法
    public void backmove() {
        //如果whochess == 0并且黑棋集合不为空，说明上一轮是黑棋下，那黑棋悔棋，重新下
        if (whochess && blacksum.size()>0) {
            //清除黑棋最后一步的数据
            blacksum.remove(blacksum.size() - 1);
            whochess = false;
        }

        //如果whochess == 1并且白棋集合不为空，说明上一轮是白棋下，那白棋悔棋，重新下
        else if (!whochess && whitesum.size()>0) {
            //清除白棋最后一步的数据
            whitesum.remove(whitesum.size() - 1);
            whochess = true;
        }
        //如果已经赢了，按了悔棋，还能继续玩，不按悔棋不能继续玩
        gameover = false;
        invalidate();
    }

    //从QPXX表中取出数据
    public void readtable() {
        fpsum.clear();
        gameover = false;
        isfupan = true;
        Log.d("Open", "ccccccccccc");
        dbhelper = new MyDatabaseHelper(getContext(),"QPXXStory.db",null,3);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Log.d("Open", "readtable");
        fpsum.addAll(dbhelper.read_table(db));
        invalidate();
    }

    //构造画棋子方法
    public void fpdrawChess(Canvas canvas) {
        Log.d("Open", "fpdrawChess"+fpsum.size());
        //i小于fpstep和fpsum.size()中的最小值，防止反复画图，fpstep的作用是让棋子一颗一颗的画出来
        for (int i = 0; i < ((fpstep<fpsum.size())?fpstep:fpsum.size()); i++) {
            //把fpsum中的棋子位置赋给point
            Point point = fpsum.get(i);
            //判断画白棋还是黑棋
            if (i%2==0)
                canvas.drawBitmap(whitepieces, point.x - gezisize / 2, point.y - gezisize / 2, null);
            else
                canvas.drawBitmap(blackpieces, point.x - gezisize / 2, point.y - gezisize / 2, null);
        }
        Log.d("Open", "fpdrawChess");
    }

    //返回游戏，使得玩家从复盘局恢复到游戏局
    public void fhyx() {
        isfupan = false;
        fpsum.clear();
        fpstep = 0;
        invalidate();
    }

    //保存数据的一些参数
    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAMEOVER = "instance_gameover";
    private static final String INSTANCE_WHITEARRAY = "instance_whitearray";
    private static final String INSTANCE_ISFUPAN = "instance_isfupan";
    private static final String INSTANCE_FPSUM = "instance_fpsum";
    private static final String INSTANCE_BLACKARRAY = "instance_blackarray";
    private static final String INSTANCE_FPSTEP = "instance_fpstep";


     //横屏时，保存状态
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        //保存系统默认状态
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        //保存是否游戏结束的值
        bundle.putBoolean(INSTANCE_GAMEOVER, gameover);
        //保存白棋
        bundle.putParcelableArrayList(INSTANCE_WHITEARRAY, whitesum);
        //保存黑棋
        bundle.putParcelableArrayList(INSTANCE_BLACKARRAY, blacksum);

        //保存复盘时的棋子
        bundle.putParcelableArrayList(INSTANCE_FPSUM, fpsum);
        //保存是否复盘
        bundle.putBoolean(INSTANCE_ISFUPAN, isfupan);
        //保存复盘步数
        bundle.putInt(INSTANCE_FPSTEP, fpstep);
        return bundle;
    }


     //取出保存的数据
    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            gameover = bundle.getBoolean(INSTANCE_GAMEOVER);
            whitesum = bundle.getParcelableArrayList(INSTANCE_WHITEARRAY);
            blacksum = bundle.getParcelableArrayList(INSTANCE_BLACKARRAY);
            fpsum = bundle.getParcelableArrayList(INSTANCE_FPSUM);
            isfupan = bundle.getBoolean(INSTANCE_ISFUPAN);
            fpstep = bundle.getInt(INSTANCE_FPSTEP);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    /*
    public void gethigh(int h){
        high = h;
    }

    public void sql()
    {
        db = SQLiteDatabase.openOrCreateDatabase(YouxiJiemian.getFilesDir().toString()+"/my.db3",null);
    }

    获取gameover来判断游戏是否结束
    public boolean getgameover()
    {
        Log.d("Open", "getgameover");
        return gameover;
    }

    获取白棋的x坐标
    public int[] getwhitex()
    {
        Log.d("Open", "getwhitex");
        return whitex;
    }

    获取白棋的y坐标
    public int[] getwhitey()
    {
        Log.d("Open", "getwhitey");
        return whitey;
    }

    获取黑棋的x坐标
    public int[] getblackx()
    {
        Log.d("Open", "getblackx");
        return blackx;
    }

    获取黑棋的y坐标
    public int[] getblacky()
    {
        Log.d("Open", "getblacky");
        return blacky;
    }

    public void setwhitesum(ArrayList<Point> sum)
    {
        whitesum.clear();
        whitesum.addAll(sum);
    }

    public void setblacksum(ArrayList<Point> sum)
    {
        blacksum.clear();
        blacksum.addAll(sum);
    }

    public ArrayList<Point> getwhitesum() {
        return whitesum;
    }

    public ArrayList<Point> getblacksum() {
        return blacksum;
    }

    public int getchange() {
        return change;
    }
    */
}
