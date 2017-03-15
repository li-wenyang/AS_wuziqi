package com.example.administrator.wuziqi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private ArrayList<Point> fpsum = new ArrayList<>();                //储存复盘棋子坐标

    //建表的语句
    public static final String createtable = "create table QPXX ("
            + "step integer primary key,"
            + "qizix integer,"
            + "qiziy integer,"
            + "whochess integer)";

    private Context mycontext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name,factory,version);
        mycontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("MyDatabaseHelper", "onCreate");
        //执行createtable语句
        db.execSQL(createtable);
    }

    //升级数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onCreate(db);
    }


    //存数据到表QPXX
    public void existtable(SQLiteDatabase db , ArrayList<Point> whitesum ,ArrayList<Point> blacksum) {
        //保存上把棋局情况之前，把上上把的棋局情况删除
        db.execSQL("delete from QPXX");

        //把黑白棋保存在同一张表中
        for (int i = 0; i<(whitesum.size()+blacksum.size());i++) {
            //判断保存的是白棋还是黑棋
            if ( i%2 == 0) {
                //保存白棋
                db.execSQL("insert into QPXX (step,qizix, qiziy, whochess) values(?,?,?,?)", new Integer[]{i+1,whitesum.get(i/2).x, whitesum.get(i/2).y, 0});
                Log.d("Open", "createtable");
            }
            else
                //保存黑棋
                db.execSQL("insert into QPXX (step,qizix, qiziy, whochess) values(?,?,?,?)", new Integer[]{i+1,blacksum.get((i-1)/2).x, blacksum.get((i-1)/2).y,1 });
        }
    }

    //从QPXX表中取出数据
    public ArrayList<Point> read_table(SQLiteDatabase db) {
        //查询QPXX表中所有的数据，保存到cursor中
        fpsum.clear();
        Cursor cursor = db.query("QPXX",null,null,null,null,null,null);
        Log.d("Open", "cursor");
        //遍历cursor中所有数据，用cursor.moveToFirst()索引到第一行数据
        if (cursor.moveToFirst()) {
            do {
                Log.d("Open", "moveToFirst");
                //遍历Cursor对象，取出数据并打印
                int step = cursor.getInt(cursor.getColumnIndex("step"));
                int qizix = cursor.getInt(cursor.getColumnIndex("qizix"));
                int qiziy = cursor.getInt(cursor.getColumnIndex("qiziy"));
                int whochess = cursor.getInt(cursor.getColumnIndex("whochess"));
                //调用fupan方法，把取到的值赋给fpsum
                Log.d("Open", "readtable"+qizix);
                fupan(step,qizix,qiziy,whochess);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return fpsum;
    }

    public void fupan(int step,int qizix,int qiziy,int whochess) {
        Point FPpoint = new Point();
        FPpoint.x = qizix;
        FPpoint.y = qiziy;
        Log.d("Open", "fupan"+qizix);
        //这里加个条件，防止多按了按钮，fpsum反复添加数据
        if (fpsum.size()<step)
            //把保存的数据调出来赋给fpsum
            fpsum.add(FPpoint);
    }

}
