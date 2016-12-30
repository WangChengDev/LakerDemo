package com.laker.mvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.laker.dateandareasselecter.DateTimeScrollerDialog;
import com.laker.dateandareasselecter.config.DefaultConfig;
import com.laker.dateandareasselecter.data.Type;
import com.laker.dateandareasselecter.listener.OnDateTimeSetListener;
import com.laker.dateandareasselecter.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final long HUNDRED_YEARS = 100L * 365 * 1000 * 60 * 60 * 24L; // 100年
    private TextView mTvTime;

    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private long mLastTime = System.currentTimeMillis(); // 上次设置的时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    /**
     * 显示日期
     *
     * @param view 视图
     */
    public void showDate(View view) {
        // 出生日期
//        DateTimeScrollerDialog dialog = new DateTimeScrollerDialog.Builder()
//                .setType(Type.ALL)
//                .setTitleStringId("请选择出生日期")
//                .setMinMilliseconds(System.currentTimeMillis() - HUNDRED_YEARS)
//                .setMaxMilliseconds(System.currentTimeMillis()+HUNDRED_YEARS)
//                .setYearUnit("年")
//                .setMonthUnit("月")
//                .setDayUnit("日")
//                .setCurMilliseconds(mLastTime)
//                .setCallback(mOnDateTimeSetListener)
//                .setPostion(DefaultConfig.POSTION_CENTER)
//                .build();

        String[] HOUR_12_STRINGS = {"上午","下午"};
        DateTimeScrollerDialog dialog = new DateTimeScrollerDialog.Builder()
                .setType(Type.ALL)
                .setTitleStringId("请选择出生日期")
                .setMinMilliseconds(System.currentTimeMillis() - HUNDRED_YEARS)
                .setMaxMilliseconds(System.currentTimeMillis()+HUNDRED_YEARS)
                .setYearUnit("年")
                .setMonthUnit("月")
                .setDayUnit("日")
                .setHourMode(DefaultConfig.HOUR_12)
                .set12HourStrings(HOUR_12_STRINGS)
                .setCurMilliseconds(mLastTime)
                .setCallback(mOnDateTimeSetListener)
                .setMaxLines(3)
                .setPostion(DefaultConfig.POSTION_BOTTOM)
                .build();

//        ProvincesCityAreaScrollerDialog dialog = new ProvincesCityAreaScrollerDialog.Builder()
//                .setType(Type.PROVINCE_CITY_AREA)
//                .setTitleStringId("请选择城市")
////                .setProvince("四川")
////                .setMaxMilliseconds(System.currentTimeMillis()+HUNDRED_YEARS)
////                .setCurMilliseconds(mLastTime)
//                .setCallback(mOnProvinceCityAreaSetListener)
//                .build();

//        ProvincesCityAreaScrollerDialog dialog = new ProvincesCityAreaScrollerDialog.Builder()
//                .setType(Type.SINGLE_PROVINCE)
//                .setTitleStringId("请选择城市")
////                .setProvince("河北")
////                .setCity("沧州")
////                .setMaxMilliseconds(System.currentTimeMillis()+HUNDRED_YEARS)
////                .setCurMilliseconds(mLastTime)
//                .setCallback(mOnProvinceCityAreaSetListener)
//                .build();

//        ProvincesCityAreaScrollerDialog dialog = new ProvincesCityAreaScrollerDialog.Builder()
//                .setType(Type.SINGLE_CITY)
//                .setTitleStringId("请选择城市")
//                .setProvince("河北")
////                .setCity("沧州")
////                .setMaxMilliseconds(System.currentTimeMillis()+HUNDRED_YEARS)
////                .setCurMilliseconds(mLastTime)
//                .setCallback(mOnProvinceCityAreaSetListener)
//                .setPostion(DefaultConfig.POSTION_CENTER)
//                .build();
//
        if (dialog != null) {
            if (!dialog.isAdded() && dialog.getShowsDialog()) {
                dialog.show(getSupportFragmentManager(), "tag");
                dialog.setShowsDialog(false);
            }
        }
    }

    // 数据的回调
    private OnDateTimeSetListener mOnDateTimeSetListener = new OnDateTimeSetListener() {
        @Override
        public void onDateTimeSet(DateTimeScrollerDialog timePickerView, long milliseconds,int am_pm_postion) {
            mLastTime = milliseconds;
            String text =String.format("%s 年 %s 月 %s 日 %s 时 %s 分 ，24小时制模式 = %d  "
                    ,Utils.getYear(milliseconds)
                    ,Utils.getMonth(milliseconds)
                    ,Utils.getDay(milliseconds)
                    ,Utils.getHour12(milliseconds)
                    ,Utils.getMinute(milliseconds)
                    ,am_pm_postion

            ) ;
            mTvTime.setText(text);
        }
    };
    // 初始化视图
    private void initView() {
        mTvTime = (TextView) findViewById(R.id.tv_time);
    }


}
