package com.lpky.taopr.soool.View.LayoutInflater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.lpky.taopr.soool.R;

public class InflateBody extends LinearLayout {

    public InflateBody(Context context) {
        super(context);
        inflateBody(context);
    }

    public InflateBody(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateBody(context);
    }

    private void inflateBody(Context context) {

        LayoutInflater bodyInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bodyInflater.inflate(R.layout.inflate_body, this, true);
    }
}
