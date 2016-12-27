package com.laker.fragment;

import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;

import com.laker.EventBusIndex;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;

/**
 * =============================================================================
 * [YTF] (C)2015-2099 Yuantuan Inc.
 * Link        http://www.ytframework.cn
 * =============================================================================
 *
 * @author laker<lakerandroiddev@gmail.com>
 * @created 2016/12/19
 * @description description
 * =============================================================================
 */

public class App extends MultiDexApplication {
    String UMAnalyticsAppKey;
    String UMAnalyticsChannelID;
    private static App appInstance;

    public static App getAppInstance() {
        return appInstance;
    }

    public static void setAppInstance(App appInstance) {
        App.appInstance = appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setAppInstance(this);
        EventBus.builder().addIndex(new EventBusIndex()).installDefaultEventBus();
//        List<LogLevel> logLevels = new ArrayList<>();
//        logLevels.add(LogLevel.ERROR);
//        logLevels.add(LogLevel.WARN);
//        JLog.getLocationInfo(this)
//                .writeToFile(true)
//                .setLogLevelsForFile(logLevels)
//                .setLogSegment(LogSegment.ONE_HOUR)
//                .setTimeFormat("yyyy年MM月dd日 HH时mm分ss秒")
//                .setLogDir(getString(R.string.app_name) + File.separator + "LogFiles")
//                .setLogPrefix("RxAndroid");
//        Log.d("11111 getLogDir",JLog.getSettings().getLogDir());
               /* Bugly SDK初始化
        * 参数1：上下文对象
        * 参数2：APPID，平台注册时得到,注意替换成你的appId
        * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
        */
//        CrashReport.initCrashReport(getApplicationContext(), "9cbae90ade", true);

        //String UMAnalyticsAppKey = getPackageManager().getApplicationInfo(getPackageName(),PackageManager.GET_META_DATA).ge
        try {
            UMAnalyticsAppKey = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.getString("UMENG_APPKEY");
            UMAnalyticsChannelID = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        Log.d("1111111111111", "UMAnalyticsChannelID = " + UMAnalyticsChannelID + ",UMAnalyticsAppKey = " + UMAnalyticsAppKey);
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, UMAnalyticsAppKey, UMAnalyticsChannelID, MobclickAgent.EScenarioType.E_UM_NORMAL, true));

        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }
}
