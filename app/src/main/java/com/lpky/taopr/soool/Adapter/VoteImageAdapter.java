package com.lpky.taopr.soool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lpky.taopr.soool.Object.GridVoteItem;
import com.lpky.taopr.soool.R;

import java.util.ArrayList;

public class VoteImageAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private ArrayList<GridVoteItem> item = new ArrayList<>();
    private GridviewItemClickListner gridviewItemClickListner;

    public interface GridviewItemClickListner {
        void onListImageBtnClick(int position) ;
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

            TextView vote_text = convertView.findViewById(R.id.vote_text);
            ImageView vote_image = convertView.findViewById(R.id.vote_image);
            ImageView vote_delete_btn = convertView.findViewById(R.id.vote_delete_btn);

            viewHolder = new ViewHolder(vote_text, vote_image, vote_delete_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.vote_delete_btn.setTag(position);
        viewHolder.vote_text.setTag(position);
        viewHolder.vote_image.setTag(R.id.imageView, position);

        viewHolder.vote_text.setText(item.get(position).getStatus());

        if (item.get(position).isStringOrUri()){
            Glide.with(convertView.getContext())
                    .load(item.get(position).getStrImage())
                    .override(103, 103)
                    .into(viewHolder.vote_image);
        }
        else
            viewHolder.vote_image.setImageURI(item.get(position).getImage());

        viewHolder.vote_delete_btn.setOnClickListener(this);
        viewHolder.vote_text.setOnClickListener(this);

        if (item.get(position).getStatus().equals("항목추가")) {
            viewHolder.vote_delete_btn.setVisibility(View.GONE);
        }
        else if (item.get(position).getStatus().equals(""))
            viewHolder.vote_delete_btn.setVisibility(View.VISIBLE);

        return convertView;
    }

    private class ViewHolder {
        private TextView vote_text;
        private ImageView vote_image;
        private ImageView vote_delete_btn;

        public ViewHolder(TextView vote_text, ImageView vote_image, ImageView vote_delete_btn) {
            this.vote_text = vote_text;
            this.vote_image = vote_image;
            this.vote_delete_btn = vote_delete_btn;
        }
    }

    public void onClick(View v) {
        // ListBtnClickListener(MainActivity)의 onListBtnClick() 함수 호출.
        switch (v.getId()) {
            case R.id.vote_delete_btn:
                if (this.gridviewItemClickListner != null) {
                    this.gridviewItemClickListner.onListImageBtnClick((int)v.getTag()) ;
                }
                break;
        }
    }
}