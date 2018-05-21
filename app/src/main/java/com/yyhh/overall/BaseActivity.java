package com.yyhh.overall;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pgyersdk.crash.PgyCrashManager;

/**
 * Created by yh on 2017/11/7.
 */

public class BaseActivity extends AppCompatActivity {

    private App application;
    public BaseActivity oContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (application == null) {
            // 得到Application对象
            application = (App) getApplication();
        }
        oContext = this;
        PgyCrashManager.register(this);
        addActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyCrashManager.unregister();
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    // 添加Activity方法
    public void addActivity() {
        application.addActivity_(oContext);// 调用myApplication的添加Activity方法
    }
    //销毁当个Activity方法
    public void removeActivity(Activity activity) {
        application.removeActivity_(activity);// 调用myApplication的销毁单个Activity方法
    }
    //销毁所有Activity方法
    public void removeALLActivity() {
        application.removeALLActivity_();// 调用myApplication的销毁所有Activity方法
    }

}
