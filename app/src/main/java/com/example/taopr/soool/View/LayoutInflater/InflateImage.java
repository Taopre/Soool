package com.example.taopr.soool.View.LayoutInflater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.taopr.soool.R;

public class InflateImage extends LinearLayout{

    public InflateImage(Context context) {
        super(context);
        inflateImg(context);
    }

    public InflateImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateImg(context);
    }

    private void inflateImg(Context context) {
        LayoutInflater imageInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageInflater.inflate(R.layout.inflate_image, this, true);
    }
}
