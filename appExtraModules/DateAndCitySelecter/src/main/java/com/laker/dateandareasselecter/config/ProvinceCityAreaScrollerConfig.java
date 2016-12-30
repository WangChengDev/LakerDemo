package com.laker.dateandareasselecter.config;


import com.laker.dateandareasselecter.data.Type;
import com.laker.dateandareasselecter.listener.OnProvinceCityAreaSetListener;

/**
 * 滚动配置
 */
public class ProvinceCityAreaScrollerConfig extends BaseScrollerConfig {
    public Type mProvinceType = DefaultConfig.PROVINCE_TYPE;

    public String mProvince = ""; // 省
    public String mCity = ""; // 市
    public String mArea = ""; // 区

    public OnProvinceCityAreaSetListener mProvinceCallback; // 回调

}
