package com.example.administrator.wuziqi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载画布zhujiemian.xml
        setContentView(R.layout.zhujiemian);
        Button button = (Button) findViewById(R.id.button);

        //添加button（开始游戏按钮）的点击事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到Activity：YouxiJiemian.class
                Intent intent = new Intent(MainActivity.this, YouxiJiemian.class);
                //开始执行
                startActivity(intent);
            }
        });
    }
}
