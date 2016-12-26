package com.laker.lakerdemo.mvp.model;
import com.laker.lakerdemo.mvp.bean.User;
import com.laker.lakerdemo.mvp.contract.LoginContract;
import com.laker.lakerdemo.mvp.interfaces.ILoginListener;

/**
* Created by app on 2016/12/22
*/

public class LoginModelImpl implements LoginContract.Model{

    @Override
    public void login(final String username, final String password, final ILoginListener loginListener) {
        //模拟子线程耗时操作
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                //模拟登录成功
                if ("laker".equals(username) && "123".equals(password))
                {
                    User user = new User();
                    user.setName(username);
                    user.setPwd(password);
                    loginListener.onLoginSuccess(user);
                } else
                {
                    loginListener.onLoginFail();
                }
            }
        }.start();
    }
}