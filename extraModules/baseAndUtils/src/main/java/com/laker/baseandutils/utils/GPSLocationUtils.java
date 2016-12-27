package com.laker.baseandutils.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * =============================================================================
 * [YTF] (C)2015-2099 Yuantuan Inc.
 * Link        http://www.ytframework.cn
 * =============================================================================
 *
 * @author laker<lakerandroiddev@gmail.com>
 * @created 2016/12/26
 * @description description
 * =============================================================================
 */
public class GPSLocationUtils {
    public interface ILocationCallBack {
        void onLocationChanged(Location location);
        void onStatusChanged(String provider, int status, Bundle extras);
        void onProviderEnabled(String provider);
        void onProviderDisabled(String provider);
    }

    public static Context mContext;
    private static long MIN_TIME_BW_UPDATES = 2000;
    private static float MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static LocationManager locationManager;
    private static Location location;
    private static ILocationCallBack iLocationCallBack;
    private static Criteria criteria;

    /**
     * 初始化位置信息
     *
     * @param context
     */
    public static void getLocation(Context context, ILocationCallBack locationCallBack) {
        mContext = context;
        iLocationCallBack = locationCallBack;
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
        criteria.setAltitudeRequired(true);//包含高度信息
        criteria.setBearingRequired(true);//包含方位信息
        criteria.setSpeedRequired(true);//包含速度信息
        criteria.setPowerRequirement(Criteria.POWER_LOW);//高耗电
        getLocationInfo();
    }

    public static long getMinTimeBwUpdates() {
        return MIN_TIME_BW_UPDATES;
    }

    public static void setMinTimeBwUpdates(long minTimeBwUpdates) {
        MIN_TIME_BW_UPDATES = minTimeBwUpdates;
    }

    public static float getMinDistanceChangeForUpdates() {
        return MIN_DISTANCE_CHANGE_FOR_UPDATES;
    }

    public static void setMinDistanceChangeForUpdates(float minDistanceChangeForUpdates) {
        MIN_DISTANCE_CHANGE_FOR_UPDATES = minDistanceChangeForUpdates;
    }

    /**
     * 获取位置信息
     */
    private static void getLocationInfo() {
        try {
            if (locationManager == null) {
                locationManager = (LocationManager) mContext
                        .getSystemService(mContext.LOCATION_SERVICE);
                locationManager.getBestProvider(criteria, false);
            }


            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            iLocationCallBack.onLocationChanged(location);
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                        if (locationManager != null) {
                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            if (iLocationCallBack == null) {
                throw new NullPointerException("iLocationCallBack need getLocationInfo!");
            }
            iLocationCallBack.onLocationChanged(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (iLocationCallBack == null) {
                throw new NullPointerException("iLocationCallBack need getLocationInfo!");
            }
            iLocationCallBack.onStatusChanged(provider, status, extras);
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (iLocationCallBack == null) {
                throw new NullPointerException("iLocationCallBack need getLocationInfo!");
            }
            iLocationCallBack.onProviderEnabled(provider);

        }

        @Override
        public void onProviderDisabled(String provider) {
            if (iLocationCallBack == null) {
                throw new NullPointerException("iLocationCallBack need getLocationInfo!");
            }
            iLocationCallBack.onProviderDisabled(provider);
        }
    };

    public void removeLocationListener() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);
    }


}
