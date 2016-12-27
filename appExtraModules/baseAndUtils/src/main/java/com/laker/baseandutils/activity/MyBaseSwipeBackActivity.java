package com.laker.baseandutils.activity;

import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

/**
 * =============================================================================
 * [YTF] (C)2015-2099 Yuantuan Inc.
 * Link        http://www.ytframework.cn
 * =============================================================================
 *
 * @author laker<lakerandroiddev@gmail.com>
 * @created 2016/12/21
 * @description description
 * =============================================================================
 */

public class MyBaseSwipeBackActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        //MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        //MobclickAgent.onPause(this);
    }
}
