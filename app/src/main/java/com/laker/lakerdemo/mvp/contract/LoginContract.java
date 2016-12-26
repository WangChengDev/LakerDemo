package com.laker.lakerdemo.mvp.contract;

import com.laker.lakerdemo.mvp.bean.User;
import com.laker.lakerdemo.mvp.interfaces.ILoginListener;

/**
 * =============================================================================
 * [YTF] (C)2015-2099 Yuantuan Inc.
 * Link        http://www.ytframework.cn
 * =============================================================================
 *
 * @author laker<lakerandroiddev@gmail.com>
 * @created 2016/12/22
 * @description description
 * =============================================================================
 */

public class LoginContract {

    public interface View {
        String getUserName();

        String getPassword();

        void clearUserName();

        void clearPassword();

        void showLoading();

        void hideLoading();

        void toNextActivity(User user);

        void showFailedError();
    }

    public interface Presenter {

    }

    public interface Model {
        public void login(String username, String password, ILoginListener loginListener);
    }


}