package com.laker.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

import com.laker.ui.R;
import com.laker.ui.util.Util;

/**
 * Created by lyy on 2016/6/3.
 */
public class IconText extends TextView {

    private int width, height;

    public IconText(Context context) {
        this(context, null);
    }

    public IconText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.style.TextAppearance);
    }

    public IconText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconText);
        width = (int) a.getDimension(R.styleable.IconText_icon_w, Util.dp2px(16));
        height = (int) a.getDimension(R.styleable.IconText_icon_h, Util.dp2px(16));
        a.recycle();
        handlerDrawable();
    }

    public void setLeftDrawable(@NonNull Drawable drawable) {
        drawable.setBounds(0, 0, width, height);
        setCompoundDrawables(drawable, null, null, null);
    }

    public void setLeftDrawable(@DrawableRes int drawable) {
        setLeftDrawable(getResources().getDrawable(drawable));
    }

    private void handlerDrawable() {
        Drawable[] ds = getCompoundDrawables();
        for (Drawable d : ds) {
            if (d != null) {
                d.setBounds(0, 0, width, height);
            }
        }
        setCompoundDrawables(ds[0], ds[1], ds[2], ds[3]);
    }

}
