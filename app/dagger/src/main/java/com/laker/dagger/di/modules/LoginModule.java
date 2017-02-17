package com.laker.dagger.di.modules;

import com.laker.dagger.bean.Person;
import com.laker.dagger.di.scopes.LoginScope;

import dagger.Module;
import dagger.Provides;

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
@Module
public class LoginModule {

    @Provides
    @LoginScope
    Person providePerson() {
        Person mPerson = new Person();
        mPerson.setAge(23);
        mPerson.setName("WeiLu");
        return mPerson;
    }
}
