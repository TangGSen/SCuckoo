package com.ourcompany.widget.recycleview.commadapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ourcompany.R;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/8/30 20:50
 * Des    :
 */

public class SViewHolder extends RecyclerView.ViewHolder {
    //减少viewfindbyid
    private SparseArray<View> viewCaches;

    public SViewHolder(View itemView) {
        super(itemView);
        viewCaches = new SparseArray<>();
    }

    public <V extends View> V getView(int viewId) {
        View view = viewCaches.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            viewCaches.put(viewId, view);
        }
        return (V) view;
    }

    /**
     * 设置TextView
     *
     * @param viewId
     * @param text
     * @return
     */
    public SViewHolder setText(int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    /**
     * 设置图片资源
     *
     * @param viewId
     * @param
     * @return
     */
    public SViewHolder setImage(int viewId, String url) {
        ImageView imageView = getView(viewId);
        imageView.setTag(R.id.loading_image_url, url);
        ImageLoader.getImageLoader().loadImage(imageView, url);
        return this;
    }

    public SViewHolder setViewEnable(int viewId, boolean able) {
        View view = getView(viewId);
        view.setEnabled(able);
        return this;
    }
    //加载图片，定制流程，但是使用图片框架，看使用人

    public abstract static class HolderImageLoader{
        public String mPath;

        public HolderImageLoader() {
        }

        public abstract void loadImage(ImageView imageView, String path);


        public String getPath() {
            return mPath;
        }
    }


    /**
     * 设置item 监听
     * @param viewId
     * @param listener
     * @return
     */
    public SViewHolder setItemListener(int viewId, View.OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
        return this;
    }
}