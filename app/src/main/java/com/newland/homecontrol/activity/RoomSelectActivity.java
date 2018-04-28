package com.newland.homecontrol.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.newland.homecontrol.adapter.GridViewAdapter;
import com.newland.homecontrol.bean.GridViewEntity;
import com.newland.homecontrol.broadcast.MyReceiver;
import com.newland.homecontrol.R;
import com.newland.homecontrol.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 房间选择界面
 * Created by yizhong.xu on 2017/7/27.
 */

public class RoomSelectActivity extends Activity implements MyReceiver.Message {

    private TextView tvTitle;

    private GridView gridView;
    private RadioButton rbAutomatic;
    private RadioButton rbManual;

    private Button btnSetTime;
    private int[] imgoff = {R.mipmap.live_bg_close, R.mipmap.bedroom_bg_close, R.mipmap.study_bg_close};//图片背景关灯 数组
    private int[] imgon = {R.mipmap.live_bg, R.mipmap.bedroom_bg, R.mipmap.study_bg};//图片背景开灯 数组
    private int[] imgclass = {R.mipmap.live_normal, R.mipmap.bedroom_normal, R.mipmap.study_normal};//类别
    private boolean[] check = new boolean[3];

    private List<GridViewEntity> list;
    private GridViewAdapter mAdapter;

    private SharedPreferencesUtils sharedPreferencesUtils;

    private MyReceiver myReceiver;//广播接收器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomselect);

        //初始化视图
        initView();
        //注册广播
        receiver();
        //gridView监听点击事件
        gridViewListener();
    }

    private void initView() {

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        gridView = (GridView) findViewById(R.id.gridView);
        btnSetTime = (Button) findViewById(R.id.btnSetTime);
        rbAutomatic = (RadioButton) findViewById(R.id.rbAutomatic);
        rbManual = (RadioButton) findViewById(R.id.rbManual);
        tvTitle.setText("灯光控制");

        sharedPreferencesUtils = SharedPreferencesUtils.getInstant(this);

        //如果操作模式是模拟操作，让自动按钮隐藏,并存取状态
        if (sharedPreferencesUtils.getIsSimulation()) {
            rbManual.performClick();//模拟人手去触摸“手动控制”按钮
            rbAutomatic.setVisibility(View.INVISIBLE);//自动按钮不可见
        } else {
            rbAutomatic.setVisibility(View.VISIBLE);//让自动按钮可见
            //当时自动模式时 自动按钮点击，否则点击手动按钮
            if (sharedPreferencesUtils.getISAutomatic()) {
                rbAutomatic.performClick();//自动按钮点击
            } else {
                rbManual.performClick();//手动按钮点击
            }
        }

        list = new ArrayList<>();
        mAdapter = new GridViewAdapter(this, list);
        initList();
        gridView.setAdapter(mAdapter);

    }

    /**
     * 动态注册广播接收器
     */
    private void receiver() {
        // TODO: 2017/10/19

    }

    /**
     * gridView点击事件监听
     */
    private void gridViewListener(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置点击项 监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {//当第一个位置时是客厅
                    startActivity(new Intent(RoomSelectActivity.this, LivingRoomActivity.class));
                } else if (position == 1) {//第二个位置时是卧室
                    startActivity(new Intent(RoomSelectActivity.this, BedroomActivity.class));
                } else {//第三个位置是书房
                    startActivity(new Intent(RoomSelectActivity.this, StudyActivity.class));

                }
            }
        });
    }
    /**
     * 获取消息
     *
     * @param str
     */
    @Override
    public void getMsg(String str) {
        //TODO:当接收到首页check消息时，获取各个房间灯光状态 设置适配器 进行界面展示图片改变

    }

    /**
     * 添加 开关灯背景图数据到适配器中
     */
    private void initList() {
        // TODO:更新list数据并刷新适配器

    }

    /**
     * 发送广播
     */
    private void setMess() {
        Intent intent = new Intent("com.nangch.broadcasereceiver.MYRECEIVER");
        intent.putExtra("check", "Automatic");  //发送Automatic消息给首页 通知当前状态是自动状态
        sendBroadcast(intent);      //发送广播
    }

    /**
     * 点击事件
     * @param view
     */
    public void MyClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack://返回
                finish();
                break;
            case R.id.rbAutomatic://自动
                btnSetTime.setVisibility(View.VISIBLE);//可见
                sharedPreferencesUtils.setISAutomatic(true);
                setMess();
                break;
            case R.id.rbManual://手动
                btnSetTime.setVisibility(View.INVISIBLE);//不可见
                sharedPreferencesUtils.setISAutomatic(false);
                setMess();
                break;
            case R.id.btnSetTime://设置时间
                startActivity(new Intent(RoomSelectActivity.this, TimeSetActivity.class));
                break;
        }
    }


}
