package com.laker.base;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import timber.log.Timber;

/**
 * @author wangcheng
 * @date 创建时间：2017/7/7
 * @Description
 * @Version
 * @EditHistory
 */
public class Logger {
    private static String TAG = "Laker";
    private static Context context;

    public static void init(boolean isDebug, Context _context) {
        context = _context;
        init(isDebug, TAG, context);
    }

    public static void init(boolean isDebug, String tag, Context _context) {
        if (isDebug) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new FileLoggingTree());
        }
        context = _context;
        Timber.tag(tag + getTag());
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void v(String tag, String msg) {
        Timber.tag(tag + getTag()).v(getInfo(msg));
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        Timber.tag(tag + getTag()).d(getInfo(msg));
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        Timber.tag(tag + getTag()).i(getInfo(msg));
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        Timber.tag(tag + getTag()).w(getInfo(msg));
    }

    public static void e(String msg) {
        Timber.tag(TAG + getTag()).e(getInfo(msg));
    }

    public static void e(String tag, String msg) {
        Timber.tag(tag + getTag()).e(getInfo(msg));
    }

    private static String getInfo(String msg) {
        Throwable stack = new Throwable().fillInStackTrace();//获得栈
        StackTraceElement[] trace = stack.getStackTrace();
        return  "╔═════════════════════════════════════\n" +
                "║***ThreadName:" + Thread.currentThread().getName() +"\n" +
                "║***MethodName:" + trace[2].getMethodName() + "\n" +
                "║***LineAt:" + trace[2].getLineNumber() + "\n" +
                "║***Message:" + msg +"\n" +
                "╚═════════════════════════════════════";//获得想要的信息
    }

    private static String getTag() {
        Throwable stack = new Throwable().fillInStackTrace();
        StackTraceElement[] trace = stack.getStackTrace();
        return trace[2].getClassName();
    }


    private static class FileLoggingTree extends Timber.Tree {
        String CacheDiaPath = context.getCacheDir().toString();

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (TextUtils.isEmpty(CacheDiaPath)) {
                return;
            }
            File file = new File(CacheDiaPath + "/log.txt");
            Logger.v("file.path:" + file.getAbsolutePath() + ",message:" + message);
            FileWriter writer = null;
            BufferedWriter bufferedWriter = null;
            try {
                writer = new FileWriter(file);
                bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(message);
                bufferedWriter.flush();

            } catch (IOException e) {
                Logger.v("存储文件失败");
                e.printStackTrace();
            } finally {
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
