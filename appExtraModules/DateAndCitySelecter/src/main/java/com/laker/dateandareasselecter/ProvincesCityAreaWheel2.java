package com.laker.dateandareasselecter;

import android.content.Context;
import android.view.View;

import com.laker.dateandareasselecter.adapters.ArrayWheelAdapter;
import com.laker.dateandareasselecter.config.ProvinceCityAreaScrollerConfig;
import com.laker.dateandareasselecter.utils.Utils;
import com.laker.dateandareasselecter.wheel.OnWheelChangedListener;
import com.laker.dateandareasselecter.wheel.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 省市区滚轮
 */
class ProvincesCityAreaWheel2 {
    private Context mContext;
    private WheelView mProvince, mCity, mArea; // 滚动视图
    /**
     * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
     */
    private JSONObject mJsonObj;
    /**
     * 所有省
     */
    private String[] mProvinceDatas;
    /**
     * key - 省 value - 市s
     */
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区s
     */
    private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();

    /**
     * 当前省的名称
     */
    private String mCurrentProviceName = "";
    /**
     * 当前市的名称
     */
    private String mCurrentCityName = "";
    /**
     * 当前区的名称
     */
    private String mCurrentAreaName = "";
    private ProvinceCityAreaScrollerConfig mScrollerConfig;

    private OnWheelChangedListener mProvinceListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int pCurrent = mProvince.getCurrentItem();
            mCurrentProviceName = mProvinceDatas[pCurrent];
            updateCities(mCurrentProviceName);
        }
    };
    private OnWheelChangedListener mCityListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int pCurrent = mCity.getCurrentItem();
            mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
            updateAreas(mCurrentCityName);

        }
    };
    private OnWheelChangedListener mAreaListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[newValue];
        }
    };
    private OnWheelChangedListener mSingleListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[newValue];

        }
    };

    /**
     * 设置视图与参数
     *
     * @param view           视图
     * @param scrollerConfig 滚动参数
     */
    ProvincesCityAreaWheel2(View view, ProvinceCityAreaScrollerConfig scrollerConfig) {
        mScrollerConfig = scrollerConfig;
        mContext = view.getContext();
        initJsonData();
        initDatas();
        initialize(view);
    }

    /**
     * 初始化与设置视图
     *
     * @param view 视图
     */
    private void initialize(View view) {
        initView(view); // 初始化视图
        initProvinceView();
        if (mScrollerConfig.mProvince != null && mScrollerConfig.mProvince.length() > 0) {
            mCurrentProviceName = mScrollerConfig.mProvince;
        }
        initCityView(mCurrentProviceName);

        if (mScrollerConfig.mCity != null && mScrollerConfig.mCity.length() > 0) {
            mCurrentCityName = mScrollerConfig.mCity;
        }
        initAreaView(mCurrentCityName);


    }

    /**
     * 初始化视图
     *
     * @param view 视图
     */
    private void initView(View view) {
        mProvince = (WheelView) view.findViewById(R.id.province);
        mCity = (WheelView) view.findViewById(R.id.city);
        mArea = (WheelView) view.findViewById(R.id.area);
        mProvince.addChangingListener(mProvinceListener);
        mCity.addChangingListener(mCityListener);
        mArea.addChangingListener(mAreaListener);

        switch (mScrollerConfig.mProvinceType) {
            case PROVINCE:
                Utils.showViews(mProvince);
                break;
            case PROVINCE_CITY:
                Utils.showViews(mProvince, mCity);
                break;
            case PROVINCE_CITY_AREA:
                Utils.showViews(mProvince, mCity, mArea);
                break;
            case SINGLE_PROVINCE:
                Utils.showViews(mProvince);
                break;
            case SINGLE_CITY:
                Utils.showViews(mCity);
                break;
            case SINGLE_AREA:
                Utils.showViews(mArea);
                break;
        }

    }

    /**
     * 初始化省
     */
    private void initProvinceView() {
        mProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mProvinceDatas));
        mCurrentProviceName = mProvinceDatas[mProvince.getCurrentItem()];
    }

    /**
     * 初始化市
     */
    private void initCityView(String provinceName) {
        updateCities(provinceName);
    }

    /**
     * 初始化日视图
     */
    private void initAreaView(String mCurrentCityName) {
        updateAreas(mCurrentCityName);
    }

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = mContext.getAssets().open("ytf_city.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     */
    private void initDatas() {
        try {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvinceDatas = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
                String province = jsonP.getString("p");// 省名字

                mProvinceDatas[i] = province;

                JSONArray jsonCs = null;
                try {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1) {
                    continue;
                }
                String[] mCitiesDatas = new String[jsonCs.length()];
                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("n");// 市名字
                    mCitiesDatas[j] = city;
                    JSONArray jsonAreas = null;
                    try {
                        /**
                         * Throws JSONException if the mapping doesn't exist or
                         * is not a JSONArray.
                         */
                        jsonAreas = jsonCity.getJSONArray("a");
                    } catch (Exception e) {
                        continue;
                    }

                    String[] mAreasDatas = new String[jsonAreas.length()];// 当前市的所有区
                    for (int k = 0; k < jsonAreas.length(); k++) {
                        String area = jsonAreas.getJSONObject(k).getString("s");// 区域的名称
                        mAreasDatas[k] = area;
                    }
                    mAreaDatasMap.put(city, mAreasDatas);
                }

                mCitisDatasMap.put(province, mCitiesDatas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas(String mCurrentCityName) {
        if (mArea.getVisibility() == View.VISIBLE) {
            String[] areas = mAreaDatasMap.get(mCurrentCityName);

            if (areas == null) {
                areas = new String[]{""};
            }
            mArea.setViewAdapter(new ArrayWheelAdapter<String>(mContext, areas));
            if (areas.length > 0) {
                mArea.setCurrentItem(0);
                mCurrentAreaName = areas[0];
            }
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities(String mCurrentProviceName) {
        if (mCity.getVisibility() == View.VISIBLE) {
            String[] cities = mCitisDatasMap.get(mCurrentProviceName);
            if (cities == null) {
                cities = new String[]{""};
            }
            mCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext, cities));
            if (cities.length > 0) {
                mCity.setCurrentItem(0);
                mCurrentCityName = cities[0];
            }
            updateAreas(mCurrentCityName);
        }

    }

    public JSONObject getSelectResult() {
        JSONObject selectResult = new JSONObject();

        try {
            if (mProvince.getVisibility() == View.VISIBLE && mCurrentProviceName.length() != 0) {
                selectResult.put("province", mCurrentProviceName);
            }
            if (mCity.getVisibility() == View.VISIBLE && mCurrentCityName.length() != 0) {
                selectResult.put("city", mCurrentCityName);
            }
            if (mArea.getVisibility() == View.VISIBLE && mCurrentAreaName.length() != 0) {
                selectResult.put("area", mCurrentAreaName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return selectResult;
    }

}
