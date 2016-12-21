package com.laker.lakerdemo.mvp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.laker.frame.util.show.L;
import com.laker.frame.util.show.T;
import com.laker.lakerdemo.R;
import com.laker.lakerdemo.base.activity.MyBaseCommonActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends MyBaseCommonActivity {

    @Bind(R.id.button)
    Button mButton;
    @Bind(R.id.button2)
    Button mButton2;
    @Bind(R.id.button3)
    Button mButton3;
    @Bind(R.id.activity_main2)
    RelativeLayout mActivityMain2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main2;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button, R.id.button2, R.id.button3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                T.showShort(this,"button");
                L.d("button");
                Log.d("11111111111","111111111111button");
                break;
            case R.id.button2:
                T.showShort(this,"button2");
                L.d("button2");
                Log.d("11111111111","111111111111button2");
                break;
            case R.id.button3:
                T.showShort(this,"button3");
                L.d("button3");
                Log.d("11111111111","111111111111button3");
                break;
        }
    }
}
