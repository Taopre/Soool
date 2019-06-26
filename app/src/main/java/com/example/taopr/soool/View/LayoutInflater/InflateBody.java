package com.example.taopr.soool.View.LayoutInflater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taopr.soool.R;

public class InflateBody extends LinearLayout {
    public InflateBody(Context context) {
        super(context);
        //inflateBody(context);
    }

    public InflateBody(Context context, AttributeSet attrs) {
        super(context, attrs);
        //inflateBody(context);
        String attributes = String.valueOf(attrs);
        inflateBody(context, attributes);
    }

    private void inflateBody(Context context) {

        LayoutInflater bodyInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bodyInflater.inflate(R.layout.inflate_body, this, true);
        TextView body = (TextView) findViewById(R.id.body_tv);
        body.setText("bodyText");
    }

    private void inflateBody(Context context, String text) {

        LayoutInflater bodyInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bodyInflater.inflate(R.layout.inflate_body, this, true);
        TextView body = (TextView) findViewById(R.id.body_tv);
        body.setText(text);
    }
}
