package com.laker.dateandareasselecter.listener;


import com.laker.dateandareasselecter.DateTimeScrollerDialog;

/**
 * 日期设置的监听器
 *
 * @author C.L. Wang
 */
public interface OnDateTimeSetListener {
    void onDateTimeSet(DateTimeScrollerDialog timePickerView, long milliseconds,int am_pm_postion);
}
