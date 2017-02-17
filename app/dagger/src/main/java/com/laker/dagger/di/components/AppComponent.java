package com.laker.dagger.di.components;

import android.content.Context;

import com.laker.dagger.di.modules.UserModule;

import javax.inject.Singleton;

import dagger.Component;

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
@Singleton
@Component(modules = {
        AppComponent.class})
public interface AppComponent {
    Context getAppContext();
    UserComponent createUserComponent(UserModule userModule);
}
