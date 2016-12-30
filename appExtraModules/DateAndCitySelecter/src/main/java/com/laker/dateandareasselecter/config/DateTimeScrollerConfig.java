package com.laker.dateandareasselecter.config;


import com.laker.dateandareasselecter.data.Type;
import com.laker.dateandareasselecter.data.WheelCalendar;
import com.laker.dateandareasselecter.listener.OnDateTimeSetListener;

/**
 * 滚动配置
 */
public class DateTimeScrollerConfig extends BaseScrollerConfig {
    public Type mDateType = DefaultConfig.DATE_TYPE;

    public String mYearUnit = DefaultConfig.YEAR; // 年单位
    public String mMonthUnit = DefaultConfig.MONTH; // 月单位
    public String mDayUnit = DefaultConfig.DAY; // 日单位
    public String mHourUnit = DefaultConfig.HOUR; // 小时单位
    public String mMinuteUnit = DefaultConfig.MINUTE; // 分钟单位

    public WheelCalendar mMinCalendar = new WheelCalendar(0); // 最小日期
    public WheelCalendar mMaxCalendar = new WheelCalendar(0); // 最大日期
    public WheelCalendar mCurCalendar = new WheelCalendar(System.currentTimeMillis()); // 当前日期

    public OnDateTimeSetListener mCallback; // 回调
    public static int mHourMode = DefaultConfig.HOUR_24; // 24小时制还是12小时制，默认24
    public String[] HOUR_12_STRINGS = {"am","pm"};

}
