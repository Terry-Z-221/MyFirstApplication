package com.jnu.student.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.jnu.student.R;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    boolean isRunning;
    private int viewWidth;
    private int viewHeight;
    private GameLoopThread gameLoopThread;
    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint paint;
    protected int score;
    private float touchX, touchY;
    public gameListener gameListener;
    protected CountDownTimer countDownTimer;
    protected int timeLeft;

    public int getScore() {
        return score;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    // 三个构造函数
    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 初始化GameView
    private void init() {
        holder = getHolder();
        holder.addCallback(this);
        canvas = new Canvas();
        paint = new Paint();
        score = 0;
        countDownTimer = new CountDownTimer(31000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = (int) millisUntilFinished / 1000;
            }

            @Override
            public void onFinish() {
                AlertDialog.Builder gameOverBuilder = new AlertDialog.Builder(getContext());
                gameOverBuilder.setTitle("游戏结束");
                gameOverBuilder.setMessage("你的得分是：" + score);
                gameOverBuilder.setCancelable(false);
                countDownTimer.cancel();
                gameLoopThread.end();
                gameOverBuilder.setPositiveButton("我知道了", (dialogInterface, i) -> {
                    AlertDialog.Builder continueBuilder = new AlertDialog.Builder(getContext());
                    continueBuilder.setTitle("游戏已结束");
                    continueBuilder.setMessage("是否选择继续游戏？");
                    continueBuilder.setCancelable(false);
                    continueBuilder.setPositiveButton("再玩一次", (dialogInterface_, i_) -> {
                        gameLoopThread = new GameLoopThread();
                        gameLoopThread.start();
                        countDownTimer.start();
                    });
                    continueBuilder.setNegativeButton("取消", (dialogInterface_, i_) -> gameListener.end());
                    continueBuilder.create().show();
                });
                gameOverBuilder.create().show();
            }
        };
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        // 开始游戏
        viewWidth = getWidth();
        viewHeight = getHeight();
        gameListener.update();
        gameLoopThread = new GameLoopThread();
        gameLoopThread.start();
        countDownTimer.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        // 停止游戏
        countDownTimer.cancel();
        gameLoopThread.end();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 处理触摸事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchX = event.getX();
            touchY = event.getY();
        }
        return true;
    }

    // 一个接口，用于监听游戏过程
    public interface gameListener {
        void update();
        void end();
    }

    public void setGameListener(gameListener listener) {
        gameListener = listener;
    }

    /** @noinspection BusyWait*/
    private class GameLoopThread extends Thread {
        @Override
        public void run() {
            super.run();
            // 让游戏持续运行
            isRunning = true;
            // 每次游戏开始时，得分都重置为0
            score = 0;
            // 存活精灵库和死亡精灵库
            ArrayList<GameSprite> gameSpritesAlive = new ArrayList<>();
            ArrayList<GameSprite> gameSpritesDead = new ArrayList<>();
            // 初始化存活精灵库，添加一些精灵
            for (int loop = 0; loop < 2; loop++) {
                gameSpritesAlive.add(new GameSprite(Math.random()*(viewWidth - 100), Math.random()*(viewHeight - 100), R.drawable.book_no_name));
                gameSpritesAlive.add(new GameSprite(Math.random()*(viewWidth - 100), Math.random()*(viewHeight - 100), R.drawable.book_1));
                gameSpritesAlive.add(new GameSprite(Math.random()*(viewWidth - 100), Math.random()*(viewHeight - 100), R.drawable.book_2));
            }
            // 游戏循环
            while (isRunning) {
                try {
                    // 画之前，锁定一个画布
                    canvas = holder.lockCanvas();
                    // 把画布画成背景色
                    canvas.drawColor(Color.GRAY);
                    // 画出所有存活的精灵
                    for (GameSprite gameSprite : gameSpritesAlive) {
                        gameSprite.draw(canvas);
                    }
                    // 对每个存活的精灵都检测一下是否被点击到，把被点击到的精灵从存活库移到死亡库
                    for (GameSprite gameSprite : gameSpritesAlive) {
                        if(gameSprite.detectCollision(touchX, touchY)) {
                            gameSpritesAlive.remove(gameSprite);
                            gameSpritesDead.add(gameSprite);
                            score++;
                            break;
                        }
                    }
                    // 刷新画布（用背景色刷一遍）
                    canvas.drawColor(Color.GRAY);
                    //把游戏得分和倒计时显示到TextView上
                    gameListener.update();
                    // 把存活的精灵绘画出来
                    for (GameSprite gameSprite : gameSpritesAlive) {
                        gameSprite.draw(canvas);
                    }
                    // 复活每个死亡的精灵，在它移动后，把它从死亡库移到存活库
                    for (GameSprite gameSprite : gameSpritesDead) {
                        gameSprite.move();
                        gameSpritesDead.remove(gameSprite);
                        gameSpritesAlive.add(gameSprite);
                    }
                } finally {
                    if (canvas != null) {
                        // 画之后，解锁画布
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void end() {
            isRunning = false;
        }
    }

    private class GameSprite {
        // 即图书对象
        private double locationX;
        private double locationY;
        private double movingDirection;
        private double movingDistance;
        private Bitmap bitmap;
        private final float bitmapWidth;
        private final float bitmapHeight;

        public GameSprite(double X, double Y, int imageResourceId) {
            this.locationX = X;
            this.locationY = Y;
            this.movingDirection = Math.random() * 2 * Math.PI;
            this.movingDistance = 100;
            bitmap = BitmapFactory.decodeResource(getResources(), imageResourceId);
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            bitmapWidth = bitmap.getWidth();
            bitmapHeight = bitmap.getHeight();
        }

        public void draw(Canvas canvas) {
            canvas.drawBitmap(bitmap, (float) locationX, (float) locationY, paint);
        }

        private boolean detectCollision(float x, float y) {
            return locationX <= x && x <= locationX + bitmapWidth && locationY <= y && y <= locationY + bitmapHeight;
        }

        public void move() {
            // 移动方向和移动距离都有可能改变，这里可能性都设为0.5
            if(Math.random() < 0.5) this.movingDirection = Math.random() * 2 * Math.PI;
            if(Math.random() < 0.5) this.movingDistance = Math.random() * 500;
            this.locationX += Math.cos(this.movingDirection) * this.movingDistance;
            this.locationY += Math.sin(this.movingDirection) * this.movingDistance;
            // 如果移动时超出了边界，则随机出现在一个地方
            if (this.locationX < 0 || this.locationX > viewWidth - bitmapWidth) this.locationX = Math.random() * (viewWidth - bitmapWidth);
            if (this.locationY < 0 || this.locationY > viewHeight - bitmapHeight) this.locationY = Math.random() * (viewHeight - bitmapHeight);
        }
    }
}