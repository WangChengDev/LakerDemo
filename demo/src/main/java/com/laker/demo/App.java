package com.laker.demo;

import android.app.Application;

import com.jiongbull.jlog.Logger;
import com.jiongbull.jlog.LoggerGlobal;
import com.jiongbull.jlog.constant.LogLevel;
import com.jiongbull.jlog.constant.LogSegment;
import com.jiongbull.jlog.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangcheng
 * @date 创建时间：2017/6/19
 * @Description
 * @Version
 * @EditHistory
 */
public class App extends Application {
    private static App appInstance;

    public static Logger getsLogger() {
        return sLogger;
    }

    private static Logger sLogger;

    public static App getAppInstance() {
        return appInstance;
    }
    private boolean isDebug = true;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        List<String> logLevels = new ArrayList<>();
        logLevels.add(LogLevel.ERROR);
        logLevels.add(LogLevel.WARN);
        sLogger = Logger.Builder.newBuilder(getApplicationContext(), "jlog")
                /* 下面的属性都是默认值，你可以根据需求决定是否修改它们. */
                .setDebug(true)
                .setWriteToFile(false)
                .setLogDir("jLogFile")
                .setLogPrefix("")
                .setLogSegment(LogSegment.TWELVE_HOURS)
                .setLogLevelsForFile(logLevels)
                .setZoneOffset(TimeUtils.ZoneOffset.P0800)
                .setTimeFormat("yyyy-MM-dd HH:mm:ss")
                .setPackagedLevel(0)
                .setStorage(null)
                .build();

        com.laker.base.Logger.init(isDebug,this);


    }
}
