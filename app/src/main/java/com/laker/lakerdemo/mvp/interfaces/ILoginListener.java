package com.laker.lakerdemo.mvp.interfaces;

import com.laker.lakerdemo.mvp.bean.User;

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

public interface ILoginListener {
    void onLoginSuccess(User user);
    void onLoginFail();
}
