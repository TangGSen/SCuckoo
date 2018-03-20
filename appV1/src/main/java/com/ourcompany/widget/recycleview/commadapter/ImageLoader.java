package com.ourcompany.widget.recycleview.commadapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ourcompany.R;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/8/30 22:22
 * Des    :
 */

public class ImageLoader extends SViewHolder.HolderImageLoader {

    private volatile static ImageLoader imageLoader;

    private ImageLoader() {
    }

    public static ImageLoader getImageLoader(){
        if(imageLoader == null){
            synchronized (ImageLoader.class){
                if(imageLoader==null){
                    imageLoader = new ImageLoader();
                }
            }
        }
        return imageLoader;
    }

    public ImageLoader setPath (String path){
       this.mPath = path;
       return this;
    }

    @Override
    public void loadImage(ImageView imageView, String path) {

            Glide.with(imageView.getContext()).load((String)imageView.getTag(R.id.loading_image_url)).error(R.mipmap.photo)           //设置错误图片
                    .placeholder(R.mipmap.photo)     //设置占位图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
}
