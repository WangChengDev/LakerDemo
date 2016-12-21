package com.laker.frame.temp;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laker.frame.util.DensityUtils;
import com.laker.frame.R;

/**
 * Created by lyy on 2016/1/11.
 * 错误填充View
 */
public class TempView extends AbsTempView {
    private ImageView mImg;
    private Button    mBt;
    private TextView  mText;
    private int       mErrorDrawable;
    private int       mTempDrawable;
    private CharSequence mErrorStr     = "重新加载";
    private CharSequence mEmptyStr     = "别处看看";
    private CharSequence mErrorHintStr = "网络错误";
    private CharSequence mEmptyHintStr = "什么都没找到";
    private LinearLayout mTemp;
    private LinearLayout mErrorTemp;
    private ImageView    mLoadingTemp;
    private AnimationDrawable mAd;

    public TempView(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        mErrorDrawable = R.mipmap.icon_error;
        mTempDrawable = R.mipmap.icon_empty;
        mImg = (ImageView) findViewById(R.id.img);
        mBt = (Button) findViewById(R.id.bt);
        mText = (TextView) findViewById(R.id.text);
        mTemp = (LinearLayout) findViewById(R.id.temp);
        mErrorTemp = (LinearLayout) findViewById(R.id.error_temp);
        mLoadingTemp = (ImageView) findViewById(R.id.loading);
        mBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTempBtClick(v, mType);
            }
        });
        setType(mType);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_error_temp;
    }

    /**
     * 设置距离顶部的高度
     */
    public void setMarginTop(int dp) {
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, DensityUtils.dp2px(dp), 0, 0);
        mTemp.setLayoutParams(lp);
        requestLayout();
    }

    /**
     * 关闭加载填充
     */
    public void closeLoading() {
        if (mLoadingTemp != null) {
            mLoadingTemp.setVisibility(GONE);
        }
    }

    /**
     * 设置填充类型
     *
     * @param type {@link #ERROR}, {@link #DATA_NULL}, {@link #LOADING}
     */
    @Override
    public void setType(int type) {
        super.setType(type);
        mLoadingTemp.setVisibility(type == ITempView.LOADING ? VISIBLE : GONE);
        mErrorTemp.setVisibility(type == ITempView.LOADING ? GONE : VISIBLE);
        requestLayout();
    }

    @Override
    public void onError() {
        mImg.setImageResource(mErrorDrawable);
        mText.setText(mErrorHintStr);
        mBt.setText(mErrorStr);
    }

    @Override
    public void onNull() {
        mImg.setImageResource(mTempDrawable);
        mText.setText(mEmptyHintStr);
        mBt.setText(mEmptyStr);
    }

    @Override
    public void onLoading() {
        if (mAd == null){
            mAd = createLoadingAnim(getContext());
        }
        mLoadingTemp.setImageDrawable(mAd);
        mAd.start();
        requestLayout();
    }

    public void setLoadingAnimation(AnimationDrawable animation){
        mAd = animation;
    }

    public void setLoadingAnimation(@DrawableRes int animation){
        mAd = (AnimationDrawable) getResources().getDrawable(animation);
    }

    public AnimationDrawable createLoadingAnim(Context context) {
        AnimationDrawable ad = new AnimationDrawable();
        ad.addFrame(context.getResources().getDrawable(R.mipmap.icon_refresh_left), 200);
        ad.addFrame(context.getResources().getDrawable(R.mipmap.icon_refresh_center), 200);
        ad.addFrame(context.getResources().getDrawable(R.mipmap.icon_refresh_right), 200);
        ad.setOneShot(false);
        return ad;
    }

}
