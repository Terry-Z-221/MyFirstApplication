package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        RelativeLayout relativeLayout = new RelativeLayout(this);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        //addRule参数对应RelativeLayout的XML布局属性
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);                    //设置居中显示
//
//        //TextView textView = new TextView(this);                     //创建TextView控件
//        TextView textView = findViewById(R.id.text_view_hello_world);       //通过id找到view中的TextView并获得对应的对象
//
//        textView.setText(getResources().getString(R.string.JavaForActivity));  //设置TextView控件的文字内容
//        textView.setTextColor(Color.RED);                                   //设置TextView控件的文字颜色
//        textView.setTextSize(18);                                           //设置TextView控件的文字大小
//        //添加TextView对象和TextView的布局属性
//        relativeLayout.addView(textView, params);
//        setContentView(relativeLayout);
    }
}