package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void changeText(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("交换文本"); // 设置弹出窗口标题
        builder.setMessage("确定要交换文本吗？"); // 设置弹出窗口消息内容
        builder.setPositiveButton("确定", (dialog, which) -> {
            // 点击确定按钮后的操作

            //更换文本
            TextView textViewL = findViewById(R.id.textViewLeft);
            TextView textViewR = findViewById(R.id.textViewRight);
            if (getResources().getString(R.string.text_Hello) == textViewL.getText()) {
                textViewL.setText(R.string.text_JNU);
                textViewR.setText(R.string.text_Hello);
            } else {
                textViewL.setText(R.string.text_Hello);
                textViewR.setText(R.string.text_JNU);
            }
            //弹出提醒消息
            Toast toast = Toast.makeText(getApplicationContext(), "交换成功！", Toast.LENGTH_SHORT);
            toast.show();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            // 点击取消按钮后的操作

            //弹出提醒消息
            Toast toast = Toast.makeText(getApplicationContext(), "操作取消成功!", Toast.LENGTH_SHORT);
            toast.show();
        });
        builder.show(); // 显示弹出窗口
    }
}