package com.laker.dagger.di.components;

import com.laker.dagger.ui.SecondActivity;

import dagger.Subcomponent;

/**
 * =============================================================================
 * [YTF] (C)2015-2099 Yuantuan Inc.
 * Link        http://www.ytframework.cn
 * =============================================================================
 *
 * @author laker<lakerandroiddev@gmail.com>
 * @created 2017/2/16
 * @description description
 * =============================================================================
 */
@Subcomponent(modules = {
        UserComponent.class
})
public interface UserComponent {
    void inject(SecondActivity mSecondActivity);
}
