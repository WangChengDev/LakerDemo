package com.laker.dateandareasselecter.utils;

import android.content.Context;
import android.view.View;

import com.laker.dateandareasselecter.data.WheelCalendar;

import java.util.Calendar;


/**
 * 工具类
 *
 */
public class Utils {

    /**
     * 判断时间是否相等
     *
     * @param calendar 日期
     * @param params   参数
     * @return 时间是否相等
     */
    public static boolean isTimeEquals(WheelCalendar calendar, int... params) {
        switch (params.length) {
            case 1:
                return calendar.year == params[0];
            case 2:
                return calendar.year == params[0] &&
                        calendar.month == params[1];
            case 3:
                return calendar.year == params[0] &&
                        calendar.month == params[1] &&
                        calendar.day == params[2];
            case 4:
                return calendar.year == params[0] &&
                        calendar.month == params[1] &&
                        calendar.day == params[2] &&
                        calendar.hour == params[3];
        }
        return false;
    }

    /**
     * 全部隐藏视图
     *
     * @param views 视图
     */
    public static void showViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static int px2dp(Context context, float pxValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / m + 0.5f);
    }

    public static int dp2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }



    public static int getYear(long milliseconds) {
        if (milliseconds <= 0) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(milliseconds);

        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(long milliseconds) {
        if (milliseconds <= 0) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(milliseconds);

        return calendar.get(Calendar.MONTH)+1;
    }

    public static int getDay(long milliseconds) {
        if (milliseconds <= 0) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(milliseconds);

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour24(long milliseconds) {
        if (milliseconds <= 0) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(milliseconds);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getHour12(long milliseconds) {
        if (milliseconds <= 0) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(milliseconds);

        return calendar.get(Calendar.HOUR);
    }

    public static int getMinute(long milliseconds) {
        if (milliseconds <= 0) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(milliseconds);

        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecond(long milliseconds) {
        if (milliseconds <= 0) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(milliseconds);

        return calendar.get(Calendar.SECOND);
    }
}
