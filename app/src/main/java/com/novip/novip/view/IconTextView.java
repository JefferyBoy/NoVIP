package com.novip.novip.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.novip.novip.R;

public class IconTextView extends FrameLayout {

    private ImageView imageView;
    private TextView textView;
    public IconTextView(@NonNull Context context) {
        this(context,null);
    }

    public IconTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IconTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public IconTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        View view = LayoutInflater.from(context).inflate(R.layout.icon_text_view,this,false);
        imageView = view.findViewById(R.id.image);
        textView = view.findViewById(R.id.text);
        addView(view);
    }

    public void setImage(@DrawableRes int id){

        RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//磁盘缓存
                .skipMemoryCache(true);
        Glide.with(this).load(id).apply(mRequestOptions).into(imageView);
        imageView.setImageResource(id);
    }

    public void setText(@StringRes int sid){
        textView.setText(sid);
    }

    public void setImageText(@DrawableRes int id, @StringRes int sid){
        setImage(id);
        setText(sid);
    }

}
