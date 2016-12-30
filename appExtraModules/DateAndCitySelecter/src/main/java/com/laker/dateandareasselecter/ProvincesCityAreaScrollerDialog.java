package com.laker.dateandareasselecter;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.laker.dateandareasselecter.config.DefaultConfig;
import com.laker.dateandareasselecter.config.ProvinceCityAreaScrollerConfig;
import com.laker.dateandareasselecter.data.Type;
import com.laker.dateandareasselecter.listener.OnProvinceCityAreaSetListener;

/**
 * 省市区选择框, 设置若干参数
 */
public class ProvincesCityAreaScrollerDialog extends DialogFragment implements View.OnClickListener {
    private ProvinceCityAreaScrollerConfig mScrollerConfig;
    private ProvincesCityAreaWheel mProvincesCityAreaWheel;

    // 实例化参数, 传入数据
    private static ProvincesCityAreaScrollerDialog newInstance(ProvinceCityAreaScrollerConfig scrollerConfig) {
        ProvincesCityAreaScrollerDialog dateScrollerDialog = new ProvincesCityAreaScrollerDialog();
        dateScrollerDialog.initialize(scrollerConfig);
        return dateScrollerDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getActivity().getWindow();
        // 隐藏软键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Dialog的位置置底
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (mScrollerConfig.mPostion == DefaultConfig.POSTION_BOTTOM) {
                window.setGravity(Gravity.BOTTOM);
            } else if (mScrollerConfig.mPostion == DefaultConfig.POSTION_CENTER) {
                window.setGravity(Gravity.CENTER);
            }
        }
    }

    /**
     * 初始化参数, 来源{@link #newInstance(ProvinceCityAreaScrollerConfig)}
     *
     * @param scrollerConfig 滚动参数
     */
    private void initialize(ProvinceCityAreaScrollerConfig scrollerConfig) {
        mScrollerConfig = scrollerConfig;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Dialog_NoTitle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true); // 后退键取消
        dialog.setCanceledOnTouchOutside(true); // 点击外面被取消
        dialog.setContentView(initView()); // 设置View
        return dialog;
    }

    /**
     * 初始化视图
     *
     * @return 当前视图
     */
    private View initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
//        final ViewGroup nullParent = null;
//        View view = null;
//        if (mScrollerConfig.mPostion == DefaultConfig.POSTION_CENTER){
//            view = inflater.inflate(R.layout.provinces_layout_center, null);
//        }else if (mScrollerConfig.mPostion == DefaultConfig.POSTION_BOTTOM){
//            view = inflater.inflate(R.layout.provinces_layout_bottom, null);
//        }
        View view = inflater.inflate(R.layout.provinces_layout, null);
//        ViewStub stub;
        if (mScrollerConfig.mPostion == DefaultConfig.POSTION_CENTER) {
//
//             stub = (ViewStub) view.findViewById(R.id.vs_bottom);
//            stub.inflate();
            ((ViewStub) view.findViewById(R.id.vs_bottom)).inflate();
        } else if (mScrollerConfig.mPostion == DefaultConfig.POSTION_BOTTOM) {
           // view = inflater.inflate(R.layout.provinces_layout_bottom, null);
//            stub = (ViewStub) view.findViewById(R.id.vs_top);
//            stub.inflate();
            ((ViewStub) view.findViewById(R.id.vs_top)).inflate();
        }

        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(this); // 设置取消按钮
        TextView sure = (TextView) view.findViewById(R.id.tv_sure);
        sure.setOnClickListener(this); // 设置确认按钮
        TextView title = (TextView) view.findViewById(R.id.tv_title);

        // 设置顶部栏
        View toolbar = view.findViewById(R.id.toolbar); // 头部视图
        toolbar.setBackgroundResource(mScrollerConfig.mToolbarBkgColor);
        title.setText(mScrollerConfig.mTitleString); // 设置文字
        cancel.setText(mScrollerConfig.mCancelString);
        sure.setText(mScrollerConfig.mSureString);

        mProvincesCityAreaWheel = new ProvincesCityAreaWheel(view, mScrollerConfig); // 设置滚动参数
        return view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            dismiss(); // 取消
        } else if (i == R.id.tv_sure) {
            onSureClicked();
        }
    }


    /**
     * 确认按钮, 回调秒数
     */
    private void onSureClicked() {
        if (mScrollerConfig.mProvinceCallback != null) {
            mScrollerConfig.mProvinceCallback.onSelectSet(this, mProvincesCityAreaWheel.getSelectResult().toString());
        }
        dismiss();
    }

    @SuppressWarnings("unused")
    public static class Builder {
        ProvinceCityAreaScrollerConfig mScrollerConfig;

        public Builder() {
            mScrollerConfig = new ProvinceCityAreaScrollerConfig();
        }

        public Builder setType(Type type) {
            mScrollerConfig.mProvinceType = type;
            return this;
        }

        public Builder setThemeColor(@ColorRes int color) {
            mScrollerConfig.mToolbarBkgColor = color;
            return this;
        }

        public Builder setCancelStringId(String left) {
            mScrollerConfig.mCancelString = left;
            return this;
        }

        public Builder setSureStringId(String right) {
            mScrollerConfig.mSureString = right;
            return this;
        }

        public Builder setTitleStringId(String title) {
            mScrollerConfig.mTitleString = title;
            return this;
        }

        public Builder setToolBarTextColor(int color) {
            mScrollerConfig.mToolBarTVColor = color;
            return this;
        }

        public Builder setWheelItemTextNormalColor(int color) {
            mScrollerConfig.mWheelTVNormalColor = color;
            return this;
        }

        public Builder setWheelItemTextSelectorColor(int color) {
            mScrollerConfig.mWheelTVSelectorColor = color;
            return this;
        }

        public Builder setWheelItemTextSize(int size) {
            mScrollerConfig.mWheelTVSize = size;
            return this;
        }

        public Builder setCyclic(boolean cyclic) {
            mScrollerConfig.cyclic = cyclic;
            return this;
        }

        public Builder setProvince(String province) {
            mScrollerConfig.mProvince = province;
            return this;
        }

        public Builder setCity(String city) {
            mScrollerConfig.mCity = city;
            return this;
        }

        public Builder setArea(String area) {
            mScrollerConfig.mArea = area;
            return this;
        }

        public Builder setPostion(int postion) {
            mScrollerConfig.mPostion = postion;
            return this;
        }

        public Builder setCallback(OnProvinceCityAreaSetListener listener) {
            mScrollerConfig.mProvinceCallback = listener;
            return this;
        }

        public ProvincesCityAreaScrollerDialog build() {
            return newInstance(mScrollerConfig);
        }
    }
}
