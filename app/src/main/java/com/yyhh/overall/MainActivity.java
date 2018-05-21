package com.yyhh.overall;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yyhh.overall.activity.SettingActivity;
import com.yyhh.overall.utils.ExampleUtil;
import com.yyhh.overall.utils.LocalBroadcastManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static boolean isForeground = false;
    private SharedPreferences mSettings;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    public static Context mContext;

    @BindView(R.id.relativeLayoutFragment)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.toolbarTopImageView)
    ImageView mTooBarTopImageView;

    private ImageView mIconImageView;
    private TextView mNameTextView;
    private String[] mIconSrc;
    private String mNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        if (getIntent() != null) {
            if (!getIntent().getStringExtra("nickname").isEmpty()) {
                mNickname = getIntent().getStringExtra("nickname");
            }
            String[] iconSrcs1 = getIntent().getStringArrayExtra("iconSrc");
            if (iconSrcs1.length!=0) {
                mIconSrc=iconSrcs1;
            }
        }

        mContext = this;
        ButterKnife.bind(this);
//        ButterKnife.bind( LayoutInflater.from(this).inflate(R.layout.nav_header_main,null));
        JPushInterface.init(getApplicationContext());

        registerMessageReceiver();  // used for receive msg

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        mIconImageView = headerView.findViewById(R.id.icon_imageView);
        mNameTextView=headerView.findViewById(R.id.nameTextView);

        init();
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = true;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    private void init() {
        initView();
        initData();
        initPermission();
        initOnClick();
        initJpush();
    }

    private void initOnClick() {

    }

    private void initView() {
        mCollapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
    }

    private void initData() {
        mSettings = getSharedPreferences(ExampleUtil.PREFS_NAME, MODE_PRIVATE);
        Glide.with(this).load(mIconSrc[1]).into(mIconImageView);
        Glide.with(this).load(mIconSrc[0]).into(mTooBarTopImageView);
        mNameTextView.setText(mNickname);

    }



    private void initJpush() {

        //调用JPush api设置Push时间
//        setPushTime();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Toast.makeText(this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {
            //设置
            startActivity(new Intent(MainActivity.this, SettingActivity.class));

        } else if (id == R.id.nav_share) {
            Toast.makeText(this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            Toast.makeText(this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                }
            } catch (Exception e) {
            }
        }
    }

}
