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
            viewHolder.textView = convertView.findViewById(R.id.grid_text);
            viewHolder.imageView = convertView.findViewById(R.id.grid_image);
            viewHolder.progressBar = convertView.findViewById(R.id.progressbar);
            viewHolder.radioButton = convertView.findViewById(R.id.radio);
            viewHolder.num = convertView.findViewById(R.id.num);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.progressBar.setTag(position);
        viewHolder.radioButton.setTag(position);
        viewHolder.textView.setTag(position);
        viewHolder.num.setTag(position);
        viewHolder.num.setVisibility(View.INVISIBLE);
        viewHolder.imageView.setTag(R.id.grid_image, position);

        viewHolder.num.setText(item.get(position).getVote()+"");
        viewHolder.textView.setText(item.get(position).getStatus());
        if (item.get(position).isStringOrUri() == true) {
            Glide.with(convertView.getContext())
                    .load(Whatisthis.serverIp+item.get(position).getStrImage())
                    .centerCrop()
                    .override(309,309)
                    .into(viewHolder.imageView);

            for (int i=0; i<getCount(); i++) {
                images.add(item.get(i).getStrImage());
            }

        } else {
            viewHolder.imageView.setImageURI(item.get(position).getImage());
        }
        viewHolder.progressBar.post(new Runnable() {
            @Override
            public void run() {
                viewHolder.progressBar.setProgress(item.get(position).getVote());
            }
        });
        viewHolder.progressBar.setMax(voteTotalNum);

        if (voteFlag == true) {
            viewHolder.radioButton.setVisibility(View.GONE);
            viewHolder.num.setVisibility(View.VISIBLE);
            viewHolder.progressBar.setVisibility(View.VISIBLE);
            viewHolder.imageView.setAlpha(50);

            if (position != (userSelectPos - 1)) {
                viewHolder.num.setTextColor(Color.parseColor("#9d9d97"));
            } else {
                viewHolder.num.setTextColor(Color.parseColor("#08883e"));
            }

        }

        if (alreadyVote == true) {
            viewHolder.progressBar.setClickable(true);
            viewHolder.imageView.setClickable(true);
        }

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VoteImageActivity.class);
                intent.putExtra("totalImageNum", item.size());
                intent.putExtra("imageList", images);
                intent.putExtra("nowPosition", position);
                v.getContext().startActivity(intent);
            }
        });
        viewHolder.radioButton.setOnClickListener(this);

        return convertView;
    }

    private class ViewHolder {
        private TextView textView;
        private TextView num;
        private ImageView imageView;
        private ProgressBar progressBar;
        private RadioButton radioButton;

    }

    public void onClick(View v) {
        // ListBtnClickListener(MainActivity)의 onListBtnClick() 함수 호출.
        switch (v.getId()) {

            case R.id.radio:
                if (this.gridviewItemClickListner != null) {
                    this.gridviewItemClickListner.onListRadioClick((int)v.getTag(), v) ;
                }
                break;
        }
    }
}