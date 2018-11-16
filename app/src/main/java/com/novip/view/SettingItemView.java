package com.novip.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.novip.R;

public class SettingItemView extends FrameLayout {

    private TextView title,sub;


    public SettingItemView(@NonNull Context context) {
        this(context,null);
    }

    public SettingItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public SettingItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        View view = LayoutInflater.from(context).inflate(R.layout.setting_item_view,this,false);
        title = view.findViewById(R.id.title);
        sub = view.findViewById(R.id.sub);
        addView(view);
    }

    public void setText(CharSequence title,CharSequence sub){
        this.title.setText(title);
        this.sub.setText(sub);
    }
}
