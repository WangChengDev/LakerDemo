package com.laker.lakerdemo.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laker.lakerdemo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

//    @Bind(R.id.btn_click)
//    Button mBtnClick;
//    @Bind(R.id.activity_main)
//    RelativeLayout mActivityMain;
//    @Bind(R.id.tv)
//    TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(final String s) {

    }

//    @OnClick(R.id.btn_click)
//    public void onClick() {
//
//
//    }


}
