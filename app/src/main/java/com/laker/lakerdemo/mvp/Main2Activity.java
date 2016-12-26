package com.laker.lakerdemo.mvp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.laker.frame.util.show.T;
import com.laker.lakerdemo.R;
import com.laker.lakerdemo.base.activity.MyBaseCommonActivity;
import com.laker.lakerdemo.mvp.bean.User;
import com.laker.lakerdemo.mvp.contract.LoginContract;
import com.laker.lakerdemo.mvp.presenter.LoginPresenterImpl;
import com.laker.lakerdemo.utils.CoordinateTransformUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends MyBaseCommonActivity implements LoginContract.View {


    @Bind(R.id.et_name)
    EditText mEtName;
    @Bind(R.id.et_pwd)
    EditText mEtPwd;
    @Bind(R.id.btn_login)
    Button mBtnLogin;
    @Bind(R.id.btn_cancle)
    Button mBtnCancle;
    @Bind(R.id.label)
    TextView mLabel;

    //    private UserLoginPresenterImpl loginPresenter;
    private LoginPresenterImpl loginPresenter2;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        loginPresenter = new UserLoginPresenterImpl(this);
        loginPresenter2 = new LoginPresenterImpl(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }else {
            startSysLBS();
        }


    }

    public void startSysLBS() {
        String serviceString = Context.LOCATION_SERVICE;
        String provider = LocationManager.GPS_PROVIDER;
        LocationManager locationManager = (LocationManager) getSystemService(serviceString);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        getLocationInfo(location);
        locationManager.requestLocationUpdates(provider, 2000, 0, locationListener);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main2;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_login, R.id.btn_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                loginPresenter2.login();
                break;
            case R.id.btn_cancle:
                loginPresenter2.clear();
                break;
        }
    }

    @Override
    public String getUserName() {
        return mEtName.getText().toString();
    }

    @Override
    public String getPassword() {
        return mEtPwd.getText().toString();
    }

    @Override
    public void clearUserName() {

        mEtName.getText().clear();
    }

    @Override
    public void clearPassword() {
        mEtPwd.getText().clear();
    }

    @Override
    public void showLoading() {
        T.showShort(this, "showLoading");
    }

    @Override
    public void hideLoading() {
        T.showShort(this, "hideLoading");
    }

    @Override
    public void toNextActivity(User user) {
        T.showShort(this, "toNextActivity");
    }

    @Override
    public void showFailedError() {
        T.showShort(this, "showFailedError");
    }

    private void getLocationInfo(Location location) {
        String latLongInfo;
        if (location != null) {
            double latitude = location.getLatitude();//维度
            double longitude = location.getLongitude();//经度
            double[] doubles = CoordinateTransformUtil.wgs84togcj02(longitude,latitude);
            latLongInfo = "维度：" + latitude + "\n精度:" + longitude+"\n维度：" + doubles[0] + "\n精度:" + doubles[1];
        } else {
            latLongInfo = "No location found";
        }
        mLabel.setText("Your current position is:\n" + latLongInfo);
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            getLocationInfo(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            getLocationInfo(null);
        }

        @Override
        public void onProviderEnabled(String provider) {
            getLocationInfo(null);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        startSysLBS();
    }

}
