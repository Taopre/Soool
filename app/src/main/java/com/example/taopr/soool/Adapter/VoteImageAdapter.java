package com.example.taopr.soool.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taopr.soool.R;

import java.util.ArrayList;

public class VoteImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Uri> image = new ArrayList<>();
    private ArrayList<Integer> imageNum = new ArrayList<>();

    public VoteImageAdapter(Context c, ArrayList<Uri> image, ArrayList<Integer> imageNum) {
        mContext = c;
        this.image = image;
        this.imageNum = imageNum;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return image.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.qna_vote_image, null);
            TextView textView =  convertView.findViewById(R.id.grid_text);
            ImageView imageView =  convertView.findViewById(R.id.grid_image);
            textView.setText(imageNum.get(position)+"");
            imageView.setImageURI(image.get(position));
        }

        return convertView;
    }
}