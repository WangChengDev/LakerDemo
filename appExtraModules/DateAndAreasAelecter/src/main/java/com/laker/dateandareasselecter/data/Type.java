package com.laker.dateandareasselecter.data;

public enum Type {
    // 选择模式，年月日时分，年月日，时分，月日时分，年月,省，省市，省市区，单个选择( 省市区依次选下来，传入上次选择的结果)
    ALL,
    YEAR_MONTH_DAY,
    HOURS_MINS,
    MONTH_DAY_HOUR_MIN,
    YEAR_MONTH,
    YEAR,
    PROVINCE,
    PROVINCE_CITY,
    PROVINCE_CITY_AREA,
    SINGLE_PROVINCE,
    SINGLE_CITY,
    SINGLE_AREA
}
