package com.yyhh.overall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.yyhh.overall.BaseActivity;
import com.yyhh.overall.MainActivity;
import com.yyhh.overall.R;
import com.yyhh.overall.utils.DB;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yh on 2017/11/16.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.exitLogin)
    Button mExitLoginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srtting);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initOnclick();
    }

    private void initOnclick() {
        mExitLoginButton.setOnClickListener(this);
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
        ButterKnife.bind(this).unbind();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exitLogin:
                DB.saveLOGIN(this,false);
                startActivity(new Intent(this,LoginActivity.class));
                removeActivity(((Activity) MainActivity.mContext));
                finish();

                break;
        }

    }
}
