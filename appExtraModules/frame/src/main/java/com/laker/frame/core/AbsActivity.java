package com.laker.frame.core;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.laker.frame.module.AbsModule;
import com.laker.frame.module.IOCProxy;
import com.laker.frame.temp.AbsTempView;
import com.laker.frame.temp.OnTempBtClickListener;
import com.laker.frame.temp.TempView;
import com.laker.frame.util.StringUtil;
import com.laker.frame.util.show.T;

import butterknife.ButterKnife;

/**
 * Created by lyy on 2015/11/3.
 */
public abstract class AbsActivity<VB extends ViewDataBinding> extends AppCompatActivity implements OnTempBtClickListener {
    protected String TAG = "";
    private VB       mBind;
    private IOCProxy mProxy;
    private long mFirstClickTime = 0;
    protected MVVMFrame     mAm;
    protected View          mRootView;
    private   ModuleFactory mModuleF;
    protected AbsTempView   mTempView;
    protected boolean useTempView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialization();
        init(savedInstanceState);
    }

    private void initialization() {
        mAm = MVVMFrame.getInstance();
        mAm.addActivity(this);
        mBind = DataBindingUtil.setContentView(this, setLayoutId());
        mProxy = IOCProxy.newInstance(this);
        TAG = StringUtil.getClassName(this);
        mModuleF = ModuleFactory.newInstance();
        ButterKnife.bind(this);
        mRootView = mBind.getRoot();
        if (useTempView) {
            mTempView = new TempView(this);
            mTempView.setBtListener(this);
        }
    }

    protected AbsTempView getTempView() {
        return mTempView;
    }

    /**
     *
     * @param useTempView
     */
    protected void setUseTempView(boolean useTempView) {
        this.useTempView = useTempView;
    }

    /**
     *
     * @param tempView
     */
    protected void setCustomTempView(AbsTempView tempView) {
        mTempView = tempView;
        mTempView.setBtListener(this);
    }

    protected void showTempView(int type) {
        if (mTempView == null || !useTempView) {
            return;
        }
        mTempView.setVisibility(View.VISIBLE);
        mTempView.setType(type);
        setContentView(mTempView);
    }

    protected void hintTempView() {
        hintTempView(0);
    }

    protected void hintTempView(int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTempView == null || !useTempView) {
                    return;
                }
                mTempView.clearFocus();
                mTempView.setVisibility(View.GONE);
                setContentView(mRootView);
            }
        }, delay);
    }

    @Override
    public void onBtTempClick(View view, int type) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected void init(Bundle savedInstanceState) {

    }

    @Override
    public void finish() {
        super.finish();
        mAm.removeActivity(this);
    }

    public View getRootView() {
        return mRootView;
    }

    /**
     * @return
     */
    protected abstract int setLayoutId();

    protected VB getBinding() {
        return mBind;
    }

    /**
     * @param clazz {@link AbsModule}
     */
    protected <M extends AbsModule> M getModule(Class<M> clazz) {
        M module = mModuleF.getModule(this, clazz);
        mProxy.changeModule(module);
        return module;
    }

    /**
     * @param result
     * @param data
     */
    protected abstract void dataCallback(int result, Object data);

    private boolean onDoubleClickExit(long timeSpace) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - mFirstClickTime > timeSpace) {
            T.showShort(this, "再按一次退出");
            mFirstClickTime = currentTimeMillis;
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return
     */
    public boolean onDoubleClickExit() {
        return onDoubleClickExit(2000);
    }

    public void exitApp(Boolean isBackground) {
        mAm.exitApp(isBackground);
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        mAm.exitApp(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelp.getInstance().handlePermissionCallback(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionHelp.getInstance().handleSpecialPermissionCallback(this, requestCode, resultCode, data);
    }
}
