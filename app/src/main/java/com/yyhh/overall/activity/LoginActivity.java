package com.yyhh.overall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.mob.ums.OperationCallback;
import com.mob.ums.User;
import com.mob.ums.gui.UMSGUI;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.yyhh.overall.BaseActivity;
import com.yyhh.overall.MainActivity;
import com.yyhh.overall.R;
import com.yyhh.overall.utils.UpdateAppManager;
import com.yyhh.overall.wight.wowsplash.WowSplashView;
import com.yyhh.overall.wight.wowsplash.WowView;

import butterknife.ButterKnife;

/**
 * Created by yh on 2017/11/17.
 */

public class LoginActivity extends BaseActivity {
    private WowSplashView mWowSplashView;
    private WowView mWowView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 将activity设置为全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_activity);
        PgyCrashManager.register(this);
        ButterKnife.bind(this);
        startView();
        initUpData();

    }

    private void startView() {
        mWowSplashView = (WowSplashView) findViewById(R.id.wowSplash);
        mWowView = (WowView) findViewById(R.id.wowView);
        mWowSplashView.startAnimate();
        mWowSplashView.setOnEndListener(new WowSplashView.OnEndListener() {
            @Override
            public void onEnd(WowSplashView wowSplashView) {
//                mWowSplashView.setVisibility(View.GONE);
//                mWowView.setVisibility(View.VISIBLE);
//                mWowView.startAnimate(wowSplashView.getDrawingCache());
                showLogin();

            }
        });

    }

    private void showLogin() {
        UMSGUI.showLogin(new OperationCallback<User>(){
            @Override
            public void onSuccess(User user) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("nickname",user.nickname.get());
                intent.putExtra("iconSrc",new String[]{user.avatar.get()[0],user.avatar.get()[1],user.avatar.get()[2]});
                LoginActivity.this.startActivity(intent);
                finish();
                super.onSuccess(user);
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
            }

            @Override
            public void onCancel() {
                removeALLActivity();
                super.onCancel();
            }
        });
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
        PgyUpdateManager.unregister();
        ButterKnife.bind(this).unbind();
    }


    private void initUpData() {
        PgyUpdateManager.setIsForced(true); //设置是否强制更新。true为强制更新；false为不强制更新（默认值）。
        PgyUpdateManager.register(this, "com.yyhh.pp",
                new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {
                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);


                        UpdateAppManager updateAppManager=new UpdateAppManager(LoginActivity.this);
                        updateAppManager.setNAME("Overall");
                        updateAppManager.setApk_path(appBean.getDownloadURL());
                        updateAppManager.setResult(result);
                        updateAppManager.getUpdateMsg();


                       /* new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("更新")
                                .setMessage("")
                                .setNegativeButton(
                                        "确定",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                startDownloadTask(
                                                        LoginActivity.this,
                                                        appBean.getDownloadURL());
                                            }
                                        }).show();*/
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                    }
                });


    }
}
