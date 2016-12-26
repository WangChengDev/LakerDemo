package com.laker.lakerdemo.mvp.presenter;
import android.os.Handler;

import com.laker.lakerdemo.mvp.bean.User;
import com.laker.lakerdemo.mvp.contract.LoginContract;
import com.laker.lakerdemo.mvp.interfaces.ILoginListener;
import com.laker.lakerdemo.mvp.model.LoginModelImpl;

/**
* Created by app on 2016/12/22
*/

public class LoginPresenterImpl implements LoginContract.Presenter{
    private LoginContract.Model loginModel;
    private LoginContract.View loginView;

    private Handler handler = new Handler();

    public LoginPresenterImpl(LoginContract.View loginView) {
        this.loginView = loginView;
        loginModel  = new LoginModelImpl();
    }

    public void login(){
        loginModel.login(loginView.getUserName(), loginView.getPassword(), new ILoginListener() {
            @Override
            public void onLoginSuccess(final User user) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        loginView.hideLoading();
                        loginView.toNextActivity(user);
                    }
                });
            }

            @Override
            public void onLoginFail() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        loginView.showFailedError();
                        loginView.hideLoading();
                    }
                });


            }
        });
    }

    public void clear(){
        loginView.clearUserName();
        loginView.clearPassword();
    }
}