package com.example.administrator.wuziqi;

/*
import android.content.SharedPreferences;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import android.graphics.Rect;
import android.graphics.Point;
import java.util.ArrayList;
*/

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class YouxiJiemian extends Activity
{

    /*
    ArrayList<Point> sum = new ArrayList<>();
    private int whitey[];
    final String FILE_NAME = "qizixinxi";
    private int whitex[];
    private int blackx[];
    private int blacky[];
    private int getwhitex[];
    private int getwhitey[];
    private int getblackx[];
    private int getblacky[];
    private int sumwhite = 0;
    private int sumblack = 0;
    private MyDatabaseHelper dbhelper;
    private ArrayList<Point> getwhitesum = new ArrayList<>();
    private ArrayList<Point> getblacksum = new ArrayList<>();
    private int high;
    */

    private Open open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.landscape);                       // 横屏
        }

        else if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_youxi_jiemian);                        // 竖屏
        }
        Button button3 = (Button) findViewById(R.id.button3);
        Button fupan = (Button) findViewById(R.id.fupan);
        Button fhyx = (Button) findViewById(R.id.fhyx);
        open = (Open) findViewById(R.id.open);

        /*
        Rect outRect=new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        high = outRect.top;
        open.gethigh(high);

        dbhelper = new MyDatabaseHelper(this,"QPXXStory.db",null,1);
        baocun.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (open.getgameover())
                {
                    Log.d("Open", "aaaaaaaaa");
                    whitex = open.getwhitex();
                    whitey = open.getwhitey();
                    blackx = open.getblackx();
                    blacky = open.getblacky();
                    SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
                    for (int i = 0; i < whitex.length; i++)
                    {
                        editor.putInt("whitex" + i, whitex[i]);
                        editor.putInt("whitey" + i, whitey[i]);
                        sumwhite = i;
                    }

                    for (int j = 0; j < blackx.length; j++)
                    {
                        editor.putInt("blackx" + j, blackx[j]);
                        editor.putInt("blacky" + j, blacky[j]);
                        sumblack = j;
                    }
                    editor.commit();
                }
                Log.d("YouxiJiemian", "baocunanniu");
            }
        });
        */

        fupan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("YouxiJiemian", "fupan");
                open.readtable();

                /*
                SharedPreferences spf = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
                for (int i=0;i<sumwhite;i++)
                {
                    getwhitex[i] = spf.getInt("blackx"+i,0);
                    getwhitey[i] = spf.getInt("blacky"+i,0);
                    Log.d("YouxiJiemian",""+getwhitex[0] );
                    Log.d("YouxiJiemian",""+getwhitey[0] );
                    Log.d("YouxiJiemian",""+getwhitex[1] );
                    Log.d("YouxiJiemian",""+getwhitey[1] );
                }
                for (int j=0;j<sumblack;j++)
                {
                    getblackx[j] = spf.getInt("blackx"+j,0);
                    getblacky[j] = spf.getInt("blacky"+j,0);
                    Log.d("YouxiJiemian",""+getblackx[0] );
                    Log.d("YouxiJiemian",""+getblacky[0] );
                    Log.d("YouxiJiemian",""+getblackx[1] );
                    Log.d("YouxiJiemian",""+getblacky[1] );
                }
                */
            }
        });


        //添加button3（悔棋）的点击事件
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sum = open.getsum();
                open.backmove();
                //open.setsum(sum);
            }
        });

        //返回游戏按钮的点击事件
        fhyx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("YouxiJiemian","fhyx");
                open.fhyx();
            }
        });
    }

    /*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int getchange = open.getchange();
        getwhitesum.addAll(open.getwhitesum());
        getblacksum.addAll(open.getblacksum());
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            for (int i = 0;i<getwhitesum.size();i++) {
                getwhitesum.get(i).x = getwhitesum.get(i).x/getchange;
                getwhitesum.get(i).y = getwhitesum.get(i).y/getchange;
            }

            for (int i = 0;i<getblacksum.size();i++) {
                getblacksum.get(i).x = getblacksum.get(i).x/getchange;
                getblacksum.get(i).y = getblacksum.get(i).y/getchange;
            }
        }

        else if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
            // 竖屏
            for (int i = 0;i<getwhitesum.size();i++) {
                getwhitesum.get(i).x = getwhitesum.get(i).x*getchange;
                getwhitesum.get(i).y = getwhitesum.get(i).y*getchange;
            }

            for (int i = 0;i<getblacksum.size();i++) {
                getblacksum.get(i).x = getblacksum.get(i).x*getchange;
                getblacksum.get(i).y = getblacksum.get(i).y*getchange;
            }
        }

        open.setwhitesum(getwhitesum);
        open.setblacksum(getwhitesum);
    }

    private void save()
    {
        try
        {
            FileOutputStream fops = openFileOutput(FILE_NAME,MODE_APPEND);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fops));
            writer.write();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private int read()
    {
        int whitex;
        try
        {
            FileInputStream fips = openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fips));
            String line = "";
            while ((line = reader.readLine())!=null)
                whitex = Integer.parseInt(reader.readLine());
            fips.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return whitex;
    }

    悔棋的方法
    public void backmove(ArrayList<Point> sum)
    {
        sum.remove(sum.size() - 1);
    }
    */
}
