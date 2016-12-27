package com.laker.baseandutils.fragment;

import com.umeng.analytics.MobclickAgent;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

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

public class MyBaseSwipeBackFragment extends SwipeBackFragment {
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
