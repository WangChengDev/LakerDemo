package com.laker.dagger.di.modules;

import com.laker.dagger.bean.Person;
import com.laker.dagger.di.qualifiers.User;

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
public class UserModule {
//    @Provides
//    @User
//    Login provideXiaoMingUser() {
//        Login login = new Login();
//        login.setPassword("******");
//        login.setName("小明");
//        return login;
//    }
//    @Provides
//    Login provideXiaoGuanUser() {
//        Login login = new Login();
//        login.setPassword("######");
//        login.setName("小关");
//        return login;
//    }

    @Provides
    @User
    Person providePersonXiaoMing() {
        Person mPerson = new Person();
        mPerson.setAge(23);
        mPerson.setName("小明");
        return mPerson;
    }

    @Provides
    @User
    Person providePersonXiaoGuan() {
        Person mPerson = new Person();
        mPerson.setAge(25);
        mPerson.setName("小关");
        return mPerson;
    }
}
