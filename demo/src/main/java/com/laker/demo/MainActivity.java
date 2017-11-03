package com.laker.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.laker.base.FileProvider7;
import com.laker.base.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final String TAG = "RxPermissionsSample";
    private RxPermissions rxPermissions;
    private ImageView mIvPhoto;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissions = new RxPermissions(MainActivity.this);
        rxPermissions.setLogging(true);



        rxPermissions.request(
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.RECEIVE_MMS,
//                Manifest.permission.READ_CALL_LOG,
//                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
        )
                .subscribe(granted -> {
                            Logger.i(TAG, " TRIGGER Received result " + granted);
                            if (granted) {
                                Toast.makeText(MainActivity.this,
                                        "Permission granted",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this,
                                        "Permission denied",
                                        Toast.LENGTH_SHORT).show();
                            }
                        },
                        t -> Logger.e(TAG, "onError"+t),
                        () -> Logger.i(TAG, "OnComplete"));


        setContentView(R.layout.activity_main);
        mIvPhoto = (ImageView) findViewById(R.id.mIvPhoto);
        findViewById(R.id.btn_check).setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,
                        "camera permission not granted",
                        Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this,
                        "camera permission is granted",
                        Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn).setOnClickListener(v -> rxPermissions.request(
                Manifest.permission.CAMERA)
                .subscribe(granted -> {
                            Logger.i(TAG, " TRIGGER Received result " + granted);
                            if (granted) {
                                takePhotoNoCompress(mIvPhoto);
                            } else {
                                Toast.makeText(MainActivity.this,
                                        "Permission denied",
                                        Toast.LENGTH_SHORT).show();
                            }
                        },
                        t -> Logger.e(TAG, "onError"+t),
                        () -> Logger.i(TAG, "OnComplete")));



    }


    private static final int REQUEST_CODE_TAKE_PHOTO = 0x110;
    private String mCurrentPhotoPath;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void takePhotoNoCompress(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                    .format(new Date()) + ".png";
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            mCurrentPhotoPath = file.getAbsolutePath();
            // 仅需改变这一行
            Uri fileUri = FileProvider7.getUriForFile(this, file);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_TAKE_PHOTO) {
            mIvPhoto.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
        }
        // else tip?

    }

}
