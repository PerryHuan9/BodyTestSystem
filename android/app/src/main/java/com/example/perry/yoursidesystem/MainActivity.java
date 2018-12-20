package com.example.perry.yoursidesystem;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.perry.yoursidesystem.database.UserCommonInfo;
import com.example.perry.yoursidesystem.fragment.analysefragment.AnalyseFragment;
import com.example.perry.yoursidesystem.fragment.bodytestfragment.BodytestFragment;
import com.example.perry.yoursidesystem.fragment.heathfragment.HeathFragment;
import com.example.perry.yoursidesystem.test.LogUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout heathLayout, bodytestLayout, analyseLayout;
    private ImageView heathView, bodytestView, analyseView, bgView;
    private AnalyseFragment analyseFragment;
    private HeathFragment heathFragment;
    private BodytestFragment bodytestFragment;
    private TextView titleView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CircleImageView circleImageView;
    public static String userName;
    public static String userSex;
    public static String userWeight;
    public static String userHeight;
    public static final String USER_NAME_TAG = "username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        userName = intent.getStringExtra(LoginActivity.USER_NAME_ARG);
        initViews();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("是否退出?");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAll();
                }
            });
            builder.setNegativeButton("否", null);
            builder.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void finishAll() {
        finish();
        LoginActivity.loginActivity.finish();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getUserCommonInfo();
        bgView = (ImageView) findViewById(R.id.bg);
        Glide.with(this).load(R.drawable.bg5).into(bgView);
        titleView = (TextView) findViewById(R.id.fragment_title);
        heathLayout = (LinearLayout) findViewById(R.id.heathLayout);
        bodytestLayout = (LinearLayout) findViewById(R.id.bodytestLayout);
        analyseLayout = (LinearLayout) findViewById(R.id.analyseLayout);
        heathView = (ImageView) findViewById(R.id.heathView);
        bodytestView = (ImageView) findViewById(R.id.bodytestView);
        analyseView = (ImageView) findViewById(R.id.analyseView);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        circleImageView = (CircleImageView) findViewById(R.id.titleImageView);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_edit:
                        Intent intent = new Intent(MainActivity.this, InputActivity
                                .class);
                        intent.putExtra(USER_NAME_TAG, userName);
                        startActivityForResult(intent, 888);
                        break;
                    case R.id.nav_logout:
                        finish();
                        break;
                    case R.id.nav_exit:
                        finishAll();
                        break;
                    case R.id.nav_up:
                        if (BodytestFragment.connected) {
                            BodytestFragment.bluetoothTool.startWrite("COMMAND");
                        }
                        break;


                }
                return true;
            }
        });
        heathFragment = new HeathFragment();
        analyseFragment = new AnalyseFragment();
        FragmentManager manager = getFragmentManager();
        bodytestFragment = (BodytestFragment) manager.findFragmentById(R.id.fragment);
        if (bodytestFragment == null) {
            bodytestFragment = new BodytestFragment();
            manager.beginTransaction().add(R.id.fragment, bodytestFragment).commit();
        }
        changeSelectedImage(R.id.bodytestView);
        heathLayout.setOnClickListener(this);
        bodytestLayout.setOnClickListener(this);
        analyseLayout.setOnClickListener(this);
        circleImageView.setOnClickListener(this);

    }


    private void getUserCommonInfo() {
        List<UserCommonInfo> infoList = DataSupport.select("userName", "sex", "weight",
                "height").where("userName=?",
                userName).find(UserCommonInfo.class);
        if (infoList.size() > 0) {
            UserCommonInfo info = infoList.get(0);
            userHeight = info.getHeight();
            userWeight = info.getWeight();
            userSex = info.getSex();
            LogUtil.i("tag1", userSex + "");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heathLayout:
                changeSelectedImage(R.id.heathView);
                getFragmentManager().beginTransaction().replace(R.id.fragment,
                        heathFragment).commit();
                break;
            case R.id.bodytestLayout:
                changeSelectedImage(R.id.bodytestView);
                bodytestView.setImageDrawable(getResources().getDrawable(R.drawable
                        .lightbodytest));
                getFragmentManager().beginTransaction().replace(R.id.fragment,
                        bodytestFragment).commit();
                break;
            case R.id.analyseLayout:
                changeSelectedImage(R.id.analyseView);
                getFragmentManager().beginTransaction().replace(R.id.fragment,
                        analyseFragment).commit();
                break;
            case R.id.titleImageView:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

        }
    }

    void changeSelectedImage(int i) {
        switch (i) {
            case R.id.analyseView:
                titleView.setText(R.string.analyseText);
                analyseView.setImageResource(R.drawable.lightanalyse);
                heathView.setImageResource(R.drawable.heath);
                bodytestView.setImageResource(R.drawable.bodytest);
                break;
            case R.id.bodytestView:
                titleView.setText(R.string.bodytestText);
                bodytestView.setImageResource(R.drawable.lightbodytest);
                analyseView.setImageResource(R.drawable.analyse);
                heathView.setImageResource(R.drawable.heath);
                break;
            case R.id.heathView:
                titleView.setText(R.string.heathText);
                bodytestView.setImageResource(R.drawable.bodytest);
                analyseView.setImageResource(R.drawable.analyse);
                heathView.setImageResource(R.drawable.lightheath);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 888 && resultCode == 999) {
            userSex = data.getStringExtra(InputActivity.SEX_TAG);
            userWeight = data.getStringExtra(InputActivity.WEIGHT_TAG);
            userHeight = data.getStringExtra(InputActivity.HEIGHT_TAG);
            LogUtil.i("tag1", "" + userSex + userHeight + userWeight);
            List<UserCommonInfo> infoList = DataSupport.select("userName").where
                    ("userName=?", userName).find(UserCommonInfo.class);
            UserCommonInfo user;
            if (infoList.size() > 0) {
                user = infoList.get(0);
                user.setSex(userSex);
                user.setHeight(userHeight);
                user.setWeight(userWeight);
            } else {
                user = new UserCommonInfo(MainActivity.userName, userSex,
                        userWeight, userHeight);
            }
            user.save();
        }
    }


}
