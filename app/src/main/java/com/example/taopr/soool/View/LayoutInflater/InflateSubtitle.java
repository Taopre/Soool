package com.example.taopr.soool.View.LayoutInflater;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.taopr.soool.R;

public class InflateSubtitle extends LinearLayout {

    public InflateSubtitle(Context context) {
        super(context);
        inflateSubtitle(context);
    }

    public InflateSubtitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateSubtitle(context);
    }

    private void inflateSubtitle(Context context) {
        LayoutInflater subtitleInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        subtitleInflater.inflate(R.layout.inflate_subtitle, this, true);
    }

}
