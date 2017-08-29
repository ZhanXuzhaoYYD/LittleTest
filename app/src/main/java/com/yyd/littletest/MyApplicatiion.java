package com.yyd.littletest;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.Utils;

/**
 * <pre>
 *     author : ZhanXuzhao
 *     e-mail : zhanxuzhao@yydrobot.com
 *     time   : 2017/08/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MyApplicatiion extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
