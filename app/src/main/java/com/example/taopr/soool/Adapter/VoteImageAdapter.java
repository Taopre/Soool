package com.example.taopr.soool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taopr.soool.Object.GridVoteItem;
import com.example.taopr.soool.R;
import com.example.taopr.soool.Util.Whatisthis;

import java.util.ArrayList;

public class VoteImageAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private ArrayList<GridVoteItem> item = new ArrayList<>();
    private GridviewItemClickListner gridviewItemClickListner;

    public interface GridviewItemClickListner {
        void onListImageBtnClick(int position) ;
        void onListLayoutClick(int position) ;
    }


    public VoteImageAdapter(Context c, ArrayList<GridVoteItem> item, GridviewItemClickListner gridviewItemClickListner) {
        mContext = c;
        this.item = item;
        this.gridviewItemClickListner = gridviewItemClickListner;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return item.get(position);
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

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.qna_vote_image, null);

            TextView textView = convertView.findViewById(R.id.grid_text);
            ImageView imageView = convertView.findViewById(R.id.grid_image);
            ImageView button = convertView.findViewById(R.id.grid_btn);
            FrameLayout layoutView = convertView.findViewById(R.id.layoutView);

            viewHolder = new ViewHolder(textView, imageView, button, layoutView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.button.setTag(position);
        viewHolder.textView.setTag(position);
        viewHolder.imageView.setTag(R.id.imageView, position);
        viewHolder.layoutView.setTag(position);

        viewHolder.textView.setText(item.get(position).getStatus());

        if (item.get(position).isStringOrUri()){
            Glide.with(convertView.getContext())
                    .load(item.get(position).getStrImage())
                    .override(103, 103)
                    .into(viewHolder.imageView);
        }
        else
            viewHolder.imageView.setImageURI(item.get(position).getImage());

        viewHolder.button.setOnClickListener(this);
        viewHolder.textView.setOnClickListener(this);
        viewHolder.layoutView.setOnClickListener(this);

        if (item.get(position).getStatus().equals("항목추가")) {
            viewHolder.button.setVisibility(View.GONE);
        }
        else if (item.get(position).getStatus().equals(""))
            viewHolder.button.setVisibility(View.VISIBLE);

        return convertView;
    }

    private class ViewHolder {
        private TextView textView;
        private ImageView imageView;
        private ImageView button;
        private FrameLayout layoutView;

        public ViewHolder(TextView textView, ImageView imageView, ImageView button, FrameLayout layoutView) {
            this.textView = textView;
            this.imageView = imageView;
            this.button = button;
            this.layoutView = layoutView;
        }
    }

    public void onClick(View v) {
        // ListBtnClickListener(MainActivity)의 onListBtnClick() 함수 호출.
        switch (v.getId()) {
            case R.id.grid_btn:
                if (this.gridviewItemClickListner != null) {
                    this.gridviewItemClickListner.onListImageBtnClick((int)v.getTag()) ;
                }
                break;
            case R.id.layoutView:
                if (this.gridviewItemClickListner != null) {
                    this.gridviewItemClickListner.onListLayoutClick((int)v.getTag()) ;
                }
                break;
        }
    }
}