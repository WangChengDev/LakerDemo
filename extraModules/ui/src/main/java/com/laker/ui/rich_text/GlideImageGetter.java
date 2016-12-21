package com.laker.ui.rich_text;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.laker.ui.util.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
final public class GlideImageGetter implements Html.ImageGetter, Drawable.Callback {

    private final Context mContext;

    private final RichText mTextView;
    private final Set<BaseViewTarget> mTargets;
    private Drawable mPlaceHolder, mErrorImage;//占位图，错误图

    private int id;
    private boolean isShowImg = true;
    private Rect mPlaceR;
    private OnFixUrlCallback mCallback;

    public interface OnFixUrlCallback {
        public String fixUrl(String url);
    }

    public GlideImageGetter(Context context, RichText textView, Drawable placeHolder, Drawable errorImage, boolean isShowImg) {
        this.mContext = context;
        this.mTextView = textView;
        this.isShowImg = isShowImg;
        this.mPlaceHolder = placeHolder;
        this.mErrorImage = errorImage;
        id = mTextView.getId();
        clear();
        mTargets = new HashSet<>();
        mTextView.setTag(id, this);
        final int w = Util.getScreenWidth(context) - textView.getPaddingLeft() - textView.getPaddingRight();
        final int h = w * placeHolder.getIntrinsicHeight() / placeHolder.getIntrinsicWidth();
        mPlaceR = new Rect(0, 0, w, h);
    }

    public GlideImageGetter get(View view) {
        return (GlideImageGetter) view.getTag(id);
    }

    public void clear() {
        GlideImageGetter prev = get(mTextView);
        if (prev == null) return;

        for (BaseViewTarget target : prev.mTargets) {
            Glide.clear(target);
        }
        prev.mTargets.clear();
    }

    public void setFixUrlCallback(OnFixUrlCallback fixUrlCallback) {
        mCallback = fixUrlCallback;
    }

    int mGifNum = 0;

    @Override
    public Drawable getDrawable(String url) {
        if (isShowImg) {
            Glide.get(mContext).clearMemory();
            final UrlDrawable urlDrawable = new UrlDrawable();
            int endP = url.lastIndexOf(".");
            String name = endP > -1 ? url.substring(endP + 1, url.length()) : "";
            boolean isGif = name.equals("gif");
            if (isGif) {
                mGifNum++;
                if (mGifNum > 3 && mCallback != null) {
                    url = mCallback.fixUrl(url);
                }
                Glide.with(mContext)
                        .load(url)
                        .crossFade()
                        .thumbnail(0.6f)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(new ImageGetterViewTarget(mTextView, urlDrawable, url));
            } else {
                if (mCallback != null) {
                    url = mCallback.fixUrl(url);
                }
                Glide.with(mContext)
                        .load(url)
                        .asBitmap()
                        .thumbnail(0.6f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new BitmapViewTarget(mTextView, urlDrawable, url));
            }
            return urlDrawable;
        } else {
            mPlaceHolder.setBounds(mPlaceR);
            return mPlaceHolder;
        }
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        mTextView.invalidate();
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {

    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {

    }

    private abstract class BaseViewTarget<T extends View, Z> extends ViewTarget<T, Z> {
        protected final UrlDrawable mDrawable;
        String imgUrl;
        protected GlideBitmapDrawable mPlaceHolder;

        public BaseViewTarget(T view, UrlDrawable drawable, String imgUrl) {
            super(view);
            this.mDrawable = drawable;
            this.imgUrl = imgUrl;
            mPlaceHolder = new GlideBitmapDrawable(mContext.getResources(), ImageFactory.drawableToBitmap(GlideImageGetter.this.mPlaceHolder));
            mPlaceHolder.setBounds(mPlaceR);
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            super.onLoadStarted(placeholder);
            mDrawable.setDrawable(mPlaceHolder);
            mDrawable.setBounds(mPlaceHolder.getBounds());
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            super.onLoadFailed(e, errorDrawable);
            mDrawable.setDrawable(mPlaceHolder);
            mDrawable.setBounds(mPlaceHolder.getBounds());
        }
    }

    private class BitmapViewTarget extends BaseViewTarget<TextView, Bitmap> {

        public BitmapViewTarget(TextView view, UrlDrawable drawable, String imgUrl) {
            super(view, drawable, imgUrl);
            mTargets.add(this);
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            final int w = getView().getWidth() - getView().getPaddingLeft() - getView().getPaddingRight();
            final int h = w * resource.getHeight() / resource.getWidth();
            if (mTextView.getImages().size() > 4) {
                compress(resource, w, h);
            } else {
                final Rect rect = new Rect(0, 0, w, h);
                final GlideBitmapDrawable drawable = new GlideBitmapDrawable(mContext.getResources(), resource);
                drawable.setBounds(rect);
                mDrawable.setBounds(rect);
                mDrawable.setDrawable(drawable);
            }
            getView().setText(getView().getText());
            getView().invalidate();
        }

        private void compress(Bitmap bm, int w, int h) {
            Rect rect = new Rect(0, 0, w, h);
            if (bm.isRecycled()) {
                mDrawable.setBounds(rect);
                mDrawable.setDrawable(mPlaceHolder);
                return;
            }
            Bitmap resource = compression(bm, w, h);
            if (resource == null) {
                mDrawable.setBounds(rect);
                mDrawable.setDrawable(mPlaceHolder);
                return;
            }
            GlideBitmapDrawable drawable = new GlideBitmapDrawable(mContext.getResources(), resource);
            drawable.setBounds(rect);
            mDrawable.setBounds(rect);
            mDrawable.setDrawable(drawable);
//            mCache.put(imgUrl, resource);
        }

        /**
         * compression pic
         * @param image
         * @param pixelW
         * @param pixelH
         * @return
         */
        public Bitmap compression(Bitmap image, float pixelW, float pixelH) {
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, os);
                if (os.toByteArray().length / 1024 > 1024) {
                    os.reset();
                    image.compress(Bitmap.CompressFormat.JPEG, 50, os);
                }
                image.recycle();
                ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
                BitmapFactory.Options newOpts = new BitmapFactory.Options();
                newOpts.inJustDecodeBounds = true;
                newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
                newOpts.inJustDecodeBounds = false;
                int w = newOpts.outWidth;
                int h = newOpts.outHeight;
                float hh = pixelH;
                float ww = pixelW;
                int be = 1;
                if (w > h && w > ww) {
                    be = (int) (newOpts.outWidth / ww);
                } else if (w < h && h > hh) {
                    be = (int) (newOpts.outHeight / hh);
                }
                if (be <= 0) be = 1;
                newOpts.inSampleSize = be;
                is = new ByteArrayInputStream(os.toByteArray());
                bitmap = BitmapFactory.decodeStream(is, null, newOpts);
                return bitmap;
            }catch (Exception e){
                return null;
            }
        }

        private Request request;

        @Override
        public Request getRequest() {
            return request;
        }

        @Override
        public void setRequest(Request request) {
            this.request = request;
        }
    }

    private class ImageGetterViewTarget extends BaseViewTarget<TextView, GlideDrawable> {

        private ImageGetterViewTarget(TextView view, UrlDrawable drawable, String imgUrl) {
            super(view, drawable, imgUrl);
            mTargets.add(this);
        }

        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            Rect rect;
            if (resource.getIntrinsicHeight() > 4096) {
                mDrawable.setBounds(mPlaceR);
                mDrawable.setDrawable(mPlaceHolder);
            } else {
                int w = getView().getWidth() - getView().getPaddingLeft() - getView().getPaddingRight();
                int h = w * resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
                rect = new Rect(0, 0, w, h);
                resource.setBounds(rect);
                mDrawable.setBounds(rect);
                mDrawable.setDrawable(resource);
                if (resource.isAnimated()) {
                    mDrawable.setCallback(get(getView()));
                    resource.setLoopCount(GlideDrawable.LOOP_FOREVER);
                    resource.start();
                }
            }
            getView().setText(getView().getText());
            getView().invalidate();
        }

        private Request request;

        @Override
        public Request getRequest() {
            return request;
        }

        @Override
        public void setRequest(Request request) {
            this.request = request;
        }
    }
}