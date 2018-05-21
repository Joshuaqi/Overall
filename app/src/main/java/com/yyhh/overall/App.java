package com.yyhh.overall;

import android.app.Activity;
import android.app.Application;

import com.mob.MobSDK;
import com.pgyersdk.crash.PgyCrashManager;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by yh on 2017/11/7.
 */

public class App extends Application {
    private List<Activity> oList;//用于存放所有启动的Activity的集合

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.init(this);
        PgyCrashManager.register(this);
        MobSDK.init(this, null, null);
        oList=new ArrayList<>();
    }
    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
// 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
//判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }
}
