package com.jnu.student.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.jnu.student.R;

import java.util.Calendar;

public class CustomClockView extends View {
    private Bitmap dialBitmap;
    private Bitmap hourHandBitmap;
    private Bitmap minuteHandBitmap;
    private Bitmap secondHandBitmap;
    private int centerX;
    private int centerY;

    public CustomClockView(Context context) {
        super(context);
        init();
    }

    public CustomClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 初始化画笔
        float dial_scale = 0.5f;
        float hand_scale = 0.8f;
        dialBitmap = getResizedBitmap(R.drawable.clock_dial, dial_scale);
//        hourHandBitmap = getResizedBitmap(R.drawable.hour, hand_scale);
//        minuteHandBitmap = getResizedBitmap(R.drawable.minute, hand_scale);
//        secondHandBitmap = getResizedBitmap(R.drawable.second, hand_scale) ;
        hourHandBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hour);
        int width = (int) (hourHandBitmap.getWidth() * hand_scale);
        int height = (int) (hourHandBitmap.getHeight() * hand_scale);
        hourHandBitmap = Bitmap.createScaledBitmap(hourHandBitmap, width, height, false);

        minuteHandBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.minute);
        width = (int) (minuteHandBitmap.getWidth() * hand_scale);
        height = (int) (minuteHandBitmap.getHeight() * hand_scale);
        minuteHandBitmap = Bitmap.createScaledBitmap(minuteHandBitmap, width, height, false);

        secondHandBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.second);
        width = (int) (secondHandBitmap.getWidth() * hand_scale);
        height = (int) (secondHandBitmap.getHeight() * hand_scale);
        secondHandBitmap = Bitmap.createScaledBitmap(secondHandBitmap, width, height, false);
    }

    private Bitmap getResizedBitmap(int resourceId, float scale) {
        // 将Bitmap按比例缩放
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        int width = (int) (bitmap.getWidth() * scale);
        int height = (int) (bitmap.getWidth() * scale);
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 控件大小发生改变时，重新计算中心点坐标
        centerX = w / 2;
        centerY = h / 2;
    }

    private void drawDial(Canvas canvas, Paint paint) {
        canvas.save();  // 相当于新建画布
        float dialTopLeftX = centerX - dialBitmap.getWidth() / 2.0f;
        float dialTopLeftY = centerY - dialBitmap.getHeight() / 2.0f;
        canvas.drawBitmap(dialBitmap, dialTopLeftX, dialTopLeftY, paint);
        canvas.restore();   // 回到以前的画布
    }

    private void drawHand(Canvas canvas, Bitmap handBitmap, float angle, Paint paint) {
        canvas.save();
        // angle必须是度数
        canvas.rotate(angle, centerX, centerY);
        float handTopLeftX = centerX - handBitmap.getWidth() / 2.0f;
        float handTopLeftY = centerY - handBitmap.getHeight() / 2.0f;
        canvas.drawBitmap(handBitmap, handTopLeftX, handTopLeftY, paint);
        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        @SuppressLint("DrawAllocation") Paint paint = new Paint();

        // 绘制表盘
        drawDial(canvas, paint);

        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // 绘制时针
        float hourAngle = (hour + minute / 60.0f + second / 3600.0f) % 12 * 360 / 12;
        drawHand(canvas, hourHandBitmap, hourAngle, paint);

        // 绘制分针
        float minuteAngle = (minute + second / 60.0f) * 360 / 60;
        drawHand(canvas, minuteHandBitmap, minuteAngle, paint);

        // 绘制秒针
        float secondAngle = second * 360 / 60.0f;
        drawHand(canvas, secondHandBitmap, secondAngle, paint);

        invalidate();
    }
}