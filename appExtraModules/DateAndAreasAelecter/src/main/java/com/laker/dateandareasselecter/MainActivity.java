package com.laker.dateandareasselecter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.laker.dateandareasselecter.config.DefaultConfig;
import com.laker.dateandareasselecter.data.Type;
import com.laker.dateandareasselecter.listener.OnDateTimeSetListener;
import com.laker.dateandareasselecter.listener.OnProvinceCityAreaSetListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final long HUNDRED_YEARS = 100L * 365 * 1000 * 60 * 60 * 24L; // 100年
    private TextView mTvTime;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private long mLastTime = System.currentTimeMillis(); // 上次设置的时间

    // 数据的回调
    private OnDateTimeSetListener mOnDateTimeSetListener = new OnDateTimeSetListener() {
        @Override
        public void onDateTimeSet(DateTimeScrollerDialog timePickerView, long milliseconds) {
            mLastTime = milliseconds;
            String text = getDateToString(milliseconds);
            mTvTime.setText(text);
        }
    };

    // 数据的回调
    private OnProvinceCityAreaSetListener mOnProvinceCityAreaSetListener = new OnProvinceCityAreaSetListener() {
//        @Override public void onDateTimeSet(DateTimeScrollerDialog timePickerView, long milliseconds) {
//            mLastTime = milliseconds;
//            String text = getDateToString(milliseconds);
//            mTvTime.setText(text);
//        }

        @Override
        public void onSelectSet(ProvincesCityAreaScrollerDialog timePickerView, String s) {
            mTvTime.setText(s);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(); // 初始化View
    }

    /**
     * 显示日期
     *
     * @param view 视图
     */
    public void showDate(View view) {
        // 出生日期
        DateTimeScrollerDialog dialog = new DateTimeScrollerDialog.Builder()
                .setType(Type.ALL)
                .setTitleStringId("请选择出生日期")
                .setMinMilliseconds(System.currentTimeMillis() - HUNDRED_YEARS)
                .setMaxMilliseconds(System.currentTimeMillis()+HUNDRED_YEARS)
                .setYearUnit("2009")
                .setMonthUnit("10")
//                .setCurMilliseconds(mLastTime)
                .setCallback(mOnDateTimeSetListener)
                .setPostion(DefaultConfig.POSTION_CENTER)
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

    // 初始化视图
    private void initView() {
        mTvTime = (TextView) findViewById(R.id.tv_time);
    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }
}
