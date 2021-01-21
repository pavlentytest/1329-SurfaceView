package com.example.myapplication;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import java.util.Random;


public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    MyThread myThread;

    public MySurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    // вызывается когда surfaceview появляется на экране
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        myThread = new MyThread(getHolder());
        myThread.setRunning(true);
        myThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        
    }

    // onDestroy()
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        myThread.setRunning(false);
        while(retry) {
            try {
                myThread.join();
            } catch (InterruptedException e) {
                retry = false;
                e.printStackTrace();
            }
        }

    }
}
