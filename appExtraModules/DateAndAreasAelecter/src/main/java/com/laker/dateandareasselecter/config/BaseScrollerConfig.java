package com.laker.dateandareasselecter.config;


import android.support.annotation.ColorRes;

/**
 * 滚动配置
 */
public class BaseScrollerConfig {

    @ColorRes public int mToolbarBkgColor = DefaultConfig.TOOLBAR_BKG_COLOR; // 背景颜色
    @ColorRes public int mItemSelectorLine = DefaultConfig.ITEM_SELECTOR_LINE; // 选中线颜色
    @ColorRes public int mItemSelectorRect = DefaultConfig.ITEM_SELECTOR_RECT; // 选中框颜色

    public String mCancelString = DefaultConfig.CANCEL; // 取消
    public String mSureString = DefaultConfig.SURE; // 确认
    public String mTitleString = DefaultConfig.TITLE; // 标题
    public int mToolBarTVColor = DefaultConfig.TOOLBAR_TV_COLOR; // ToolBar的颜色

    public int mWheelTVNormalColor = DefaultConfig.TV_NORMAL_COLOR; // 滚轮默认颜色
    public int mWheelTVSelectorColor = DefaultConfig.TV_SELECTOR_COLOR; // 滚轮选中颜色
    public int mWheelTVSize = DefaultConfig.TV_SIZE; // 文字默认大小
    public boolean cyclic = DefaultConfig.CYCLIC; // 是否循环

    public int mMaxLines = DefaultConfig.MAX_LINE; // 最大行数, 依据控件样式
    public int mPostion = DefaultConfig.POSTION_BOTTOM; // 居中还是底部
}
