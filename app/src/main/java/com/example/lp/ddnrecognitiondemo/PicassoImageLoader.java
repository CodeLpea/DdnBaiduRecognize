package com.example.lp.ddnrecognitiondemo;

import android.app.Activity;
import android.net.Uri;

import android.util.Log;
import android.widget.ImageView;

import com.lzy.imagepicker.loader.ImageLoader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;

public class PicassoImageLoader implements ImageLoader {
    private static final String TAG="PicassoImageLoader";

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

        File file = new File(path);
        // 仅需改变这一行
        Uri fileUri = FileProvider7.getUriForFile(activity, file);
        Picasso.with(activity)//
                .load(fileUri)//
                .placeholder(R.mipmap.ic_launcher)//
                .error(R.mipmap.ic_launcher)//
                .resize(width, height)//
                .centerInside()//
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {

        File file = new File(path);
        // 仅需改变这一行
        Uri fileUri = FileProvider7.getUriForFile(activity, file);
        Picasso.with(activity)//
                .load(fileUri)//
                .placeholder(R.mipmap.ic_launcher)//
                .error(R.mipmap.ic_launcher)//
                .resize(width, height)//
                .centerInside()//
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//
                .into(imageView);
    }


    @Override
    public void clearMemoryCache() {
        Log.i(TAG, "clearMemoryCache: ");
        //这里是清除缓存的方法,根据需要自己实现
    }
}