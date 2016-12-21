package com.laker.lakerdemo.base.fragment;

import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

import me.yokeyword.fragmentation.SupportFragment;

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

public class MyBaseCommonFragment extends Fragment {
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }
}
