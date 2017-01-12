package com.laker.dateandareasselecter;

import android.content.Context;
import android.view.View;

import com.laker.dateandareasselecter.adapters.ArrayWheelAdapter;
import com.laker.dateandareasselecter.config.ProvinceCityAreaScrollerConfig;
import com.laker.dateandareasselecter.data.CityIDBean;
import com.laker.dateandareasselecter.utils.Utils;
import com.laker.dateandareasselecter.wheel.OnWheelChangedListener;
import com.laker.dateandareasselecter.wheel.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 省市区滚轮
 */
class ProvincesCityAreaWheel {
    private Context mContext;
    private WheelView mProvince, mCity, mArea; // 滚动视图
    /**
     * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
     */
    private JSONArray mJsonArray;
    /**
     * 所有省
     */
    private ArrayList<CityIDBean> mProvinceDatas;
    /**
     * key - 省 value - 市s
     */
    private Map<String, ArrayList<CityIDBean>> mCitisDatasMap = new HashMap<String, ArrayList<CityIDBean>>();
    /**
     * key - 市ID values - 区s
     */
    private Map<String, ArrayList<CityIDBean>> mAreaDatasMap = new HashMap<String, ArrayList<CityIDBean>>();

    /**
     * 当前省的名称
     */
    private CityIDBean mCurrentProvice ;
    /**
     * 当前市的名称
     */
    private CityIDBean mCurrentCity ;
    /**
     * 当前区的名称
     */
    private CityIDBean mCurrentArea ;
    private ProvinceCityAreaScrollerConfig mScrollerConfig;

    private OnWheelChangedListener mProvinceListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int pCurrent = mProvince.getCurrentItem();
            mCurrentProvice = mProvinceDatas.get(pCurrent);
            updateCities(mCurrentProvice.getID());
        }
    };
    private OnWheelChangedListener mCityListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int pCurrent = mCity.getCurrentItem();
            mCurrentCity = mCitisDatasMap.get(mCurrentProvice.getID()).get(pCurrent);
            updateAreas(mCurrentCity.getID());

        }
    };
    private OnWheelChangedListener mAreaListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            mCurrentArea = mAreaDatasMap.get(mCurrentCity.getID()).get(newValue);
        }
    };
    private OnWheelChangedListener mSingleListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            mCurrentArea = mAreaDatasMap.get(mCurrentCity.getID()).get(newValue);

        }
    };

    /**
     * 设置视图与参数
     *
     * @param view           视图
     * @param scrollerConfig 滚动参数
     */
    ProvincesCityAreaWheel(View view, ProvinceCityAreaScrollerConfig scrollerConfig) {
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
            mCurrentProvice = new CityIDBean(mScrollerConfig.mProvince,null);

        }
        initCityView(mCurrentProvice.getID());

        if (mScrollerConfig.mCity != null && mScrollerConfig.mCity.length() > 0) {
            mCurrentCity = new CityIDBean(mScrollerConfig.mCity,null);
        }

        if (mCurrentCity != null)
        initAreaView(mCurrentCity.getID());


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

        String[] mProvinceName = new String[mProvinceDatas.size()];
        for (int i = 0; i < mProvinceDatas.size(); i++) {
            mProvinceName[i] = mProvinceDatas.get(i).getName();
        }
        mProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mProvinceName));
        mCurrentProvice = mProvinceDatas.get(mProvince.getCurrentItem());

    }

    /**
     * 初始化市
     */
    private void initCityView(String provinceID) {
        updateCities(provinceID);
    }

    /**
     * 初始化日视图
     */
    private void initAreaView(String mCurrentCityiD) {
        updateAreas(mCurrentCityiD);
    }

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
//            InputStream is = mContext.getAssets().open("ytf_city.json");
            InputStream is = mContext.getAssets().open("city_id.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();
//            mJsonObj = new JSONObject(sb.toString());
            mJsonArray = new JSONArray(sb.toString());
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
            mProvinceDatas = new ArrayList<>();
            for (int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonP = mJsonArray.getJSONObject(i);// 每个省的json对象
                String province = jsonP.getString("name");// 省名字
                String province_ID = jsonP.getString("id");// 省名字

                mProvinceDatas.add(new CityIDBean(province,province_ID));

                JSONArray jsonCs = null;
                try {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("city");
                } catch (Exception e1) {
                    continue;
                }
                ArrayList<CityIDBean> mCitiesDatas = new ArrayList<>();
                CityIDBean cityIDBean;
                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("name");// 市名字
                    String city_id = jsonCity.getString("id");// 市名字
                    cityIDBean = new CityIDBean(city,city_id);
                    mCitiesDatas.add(cityIDBean);
                    JSONArray jsonAreas = null;
                    try {
                        /**
                         * Throws JSONException if the mapping doesn't exist or
                         * is not a JSONArray.
                         */
                        jsonAreas = jsonCity.getJSONArray("city");
                    } catch (Exception e) {
                        continue;
                    }

                    ArrayList<CityIDBean> mAreasDatas = new ArrayList<>();// 当前市的所有区
                    for (int k = 0; k < jsonAreas.length(); k++) {
                        String area = jsonAreas.getJSONObject(k).getString("name");// 区域的名称
                        String area_id = jsonAreas.getJSONObject(k).getString("id");// 区域的名称
                        cityIDBean = new CityIDBean(area,area_id);
                        mAreasDatas.add(cityIDBean);
                    }
                    mAreaDatasMap.put(city_id, mAreasDatas);
                }

                mCitisDatasMap.put(province_ID, mCitiesDatas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonArray = null;
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas(String mCurrentCityID) {
        if (mArea.getVisibility() == View.VISIBLE) {
            ArrayList<CityIDBean> areas = mAreaDatasMap.get(mCurrentCityID);
            String[] areasName = new String[areas.size()];
            for (int i = 0; i < areas.size(); i++) {
                areasName[i] = areas.get(i).getName();
            }
            mArea.setViewAdapter(new ArrayWheelAdapter<String>(mContext, areasName));
            if (areas.size() > 0) {
                mArea.setCurrentItem(0);
                mCurrentArea = areas.get(0);
            }
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities(String mCurrentProviceID) {
        if (mCity.getVisibility() == View.VISIBLE) {
            ArrayList<CityIDBean> cities = mCitisDatasMap.get(mCurrentProviceID);
            String[] citiesName = new String[cities.size()];
            for (int i = 0; i < cities.size(); i++) {
                citiesName[i] = cities.get(i).getName();
            }
            mCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext, citiesName));
            if (cities.size() > 0) {
                mCity.setCurrentItem(0);
                mCurrentCity = cities.get(0);
            }
            updateAreas(mCurrentCity.getID());
        }

    }

    public JSONObject getSelectResult() {
        JSONObject selectResult = new JSONObject();

        try {
            if (mProvince.getVisibility() == View.VISIBLE && mCurrentProvice.getName().length() != 0) {
                selectResult.put("province", mCurrentProvice.getName());
                selectResult.put("province_id", mCurrentProvice.getID());
            }
            if (mCity.getVisibility() == View.VISIBLE && mCurrentCity.getName().length() != 0) {
                selectResult.put("city", mCurrentCity.getName());
                selectResult.put("city_id", mCurrentCity.getID());
            }
            if (mArea.getVisibility() == View.VISIBLE && mCurrentArea.getName().length() != 0) {
                selectResult.put("area", mCurrentArea.getName());
                selectResult.put("area_id", mCurrentArea.getID());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return selectResult;
    }

}