package com.yyd.littletest.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yyd.littletest.R;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("thread main: " + Thread.currentThread());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("TimerTask: " + Thread.currentThread());
                Toast.makeText(MainActivity.this, "123132", Toast.LENGTH_LONG);
            }
        }, 0);

        // resolve bug 01
    }
}