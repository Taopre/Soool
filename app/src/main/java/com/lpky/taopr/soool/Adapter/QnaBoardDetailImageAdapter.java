package com.lpky.taopr.soool.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lpky.taopr.soool.Object.GridVoteItem;
import com.lpky.taopr.soool.R;
import com.lpky.taopr.soool.View.VoteImageActivity;
import com.lpky.taopr.soool.Util.Whatisthis;

import java.util.ArrayList;

public class QnaBoardDetailImageAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    public ArrayList<GridVoteItem> item = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private GridviewItemClickListner gridviewItemClickListner;
    private RadioButton selected = null;
    public boolean voteFlag = false, alreadyVote = false;
    public int voteTotalNum = 0, totalImageNum = 0, userSelectPos = 999;
    String TAG = "디테일 이미지투표 어댑터";

    public interface GridviewItemClickListner {
        void onListImageClick(int position) ;
        void onListRadioClick(int position, View view) ;
    }

    public QnaBoardDetailImageAdapter() {}


    public QnaBoardDetailImageAdapter(Context c, ArrayList<GridVoteItem> item, GridviewItemClickListner gridviewItemClickListner) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.qnadetail_image_vote_item, null);

            viewHolder = new ViewHolder();
            viewHolder.vote_image_text = convertView.findViewById(R.id.vote_image_text);
            viewHolder.vote_image = convertView.findViewById(R.id.vote_image);
            viewHolder.vote_image_show_num = convertView.findViewById(R.id.vote_image_show_num);
            viewHolder.vote_image_select = convertView.findViewById(R.id.vote_image_select);
            viewHolder.vote_image_num = convertView.findViewById(R.id.vote_image_num);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.vote_image_show_num.setTag(position);
        viewHolder.vote_image_select.setTag(position);
        viewHolder.vote_image_text.setTag(position);
        viewHolder.vote_image_num.setTag(position);
        viewHolder.vote_image_num.setVisibility(View.INVISIBLE);
        viewHolder.vote_image.setTag(R.id.vote_image, position);

        viewHolder.vote_image_num.setText(item.get(position).getVote()+"");
        viewHolder.vote_image_text.setText(item.get(position).getStatus());
        if (item.get(position).isStringOrUri() == true) {
            Glide.with(convertView.getContext())
                    .load(Whatisthis.serverIp+item.get(position).getStrImage())
                    .centerCrop()
                    .override(309,309)
                    .centerCrop()
                    .into(viewHolder.vote_image);

            for (int i=0; i<getCount(); i++) {
                images.add(item.get(i).getStrImage());
            }

        } else {
            viewHolder.vote_image.setImageURI(item.get(position).getImage());
        }
        viewHolder.vote_image_show_num.post(new Runnable() {
            @Override
            public void run() {
                viewHolder.vote_image_show_num.setProgress(item.get(position).getVote());
            }
        });
        viewHolder.vote_image_show_num.setMax(voteTotalNum);

        if (voteFlag == true) {
            viewHolder.vote_image_select.setVisibility(View.GONE);
            viewHolder.vote_image_num.setVisibility(View.VISIBLE);
            viewHolder.vote_image_show_num.setVisibility(View.VISIBLE);
            viewHolder.vote_image.setAlpha(50);

            if (position != (userSelectPos - 1)) {
                viewHolder.vote_image_num.setTextColor(Color.parseColor("#9d9d97"));
            } else {
                viewHolder.vote_image_num.setTextColor(Color.parseColor("#08883e"));
            }

        }

        if (alreadyVote == true) {
            viewHolder.vote_image_show_num.setClickable(true);
            viewHolder.vote_image.setClickable(true);
        }

        viewHolder.vote_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VoteImageActivity.class);
                intent.putExtra("totalImageNum", item.size());
                intent.putExtra("imageList", images);
                intent.putExtra("nowPosition", position);
                v.getContext().startActivity(intent);
            }
        });
        viewHolder.vote_image_select.setOnClickListener(this);

        return convertView;
    }

    private class ViewHolder {
        private TextView vote_image_text;
        private TextView vote_image_num;
        private ImageView vote_image;
        private ProgressBar vote_image_show_num;
        private RadioButton vote_image_select;

    }

    public void onClick(View v) {
        // ListBtnClickListener(MainActivity)의 onListBtnClick() 함수 호출.
        switch (v.getId()) {

            case R.id.vote_image_select:
                if (this.gridviewItemClickListner != null) {
                    this.gridviewItemClickListner.onListRadioClick((int)v.getTag(), v) ;
                }
                break;
        }
    }
}
