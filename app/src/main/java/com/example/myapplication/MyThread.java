package com.example.myapplication;

import android.animation.ArgbEvaluator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class MyThread extends Thread {

    // константа для интерполяции
    public static final int FRACTION_TIME = 1000;

    // переменная для интерполяции
    private ArgbEvaluator argbEvaluator;

    private Paint paint;

    // указатель на SurfaceView
    private SurfaceHolder surfaceHolder;

    private boolean flag;

    private long startTime;

    // timestamp момента перерисовки
    private long buffRedrawTime;

    MyThread(SurfaceHolder h) {
        flag = false;
        surfaceHolder = h;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        argbEvaluator = new ArgbEvaluator();
    }

    public void setRunning(boolean f) {
        this.flag = f;
    }

    @Override
    public void run() {
        Canvas canvas;
        startTime = getTime();
        while(flag) {
            long currTime = getTime();
            long elapsedTime = currTime - buffRedrawTime;
            if(elapsedTime < 500000) {
                continue;
            }
            // блок. canvas
            canvas = surfaceHolder.lockCanvas();
            // рисуем
            drawCircles(canvas);
            // показываем
            surfaceHolder.unlockCanvasAndPost(canvas);
            // обновление экрана
            buffRedrawTime = getTime();
        }
    }

    public long getTime() {
        return System.nanoTime()/1000;  // микросекунды
    }

    public void drawCircles(Canvas canvas) {
        long currentTime = getTime();
        int centerX = canvas.getWidth()/2;
        int centerY = canvas.getHeight()/2;
        canvas.drawColor(Color.BLACK); // Фон
        float maxRadius = Math.min(canvas.getHeight(),canvas.getWidth())/2;
        Log.d("RRRR maxRadius=",Float.toString(maxRadius));
        float fraction = (float)(currentTime%FRACTION_TIME)/FRACTION_TIME;
        Log.d("RRRR fraction=",Float.toString(fraction));
        int color = (int)argbEvaluator.evaluate(fraction, Color.RED, Color.BLACK);
        Log.d("RRRR color=",Integer.toString(color));
        paint.setColor(color);
        canvas.drawCircle(centerX,centerY,maxRadius*fraction,paint);

    }

}
