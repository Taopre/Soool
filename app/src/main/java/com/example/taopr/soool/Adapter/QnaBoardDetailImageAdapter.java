package com.example.taopr.soool.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taopr.soool.Object.GridVoteItem;
import com.example.taopr.soool.R;

import java.util.ArrayList;

public class QnaBoardDetailImageAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private ArrayList<GridVoteItem> item = new ArrayList<>();
    private GridviewItemClickListner gridviewItemClickListner;
    private RadioButton selected = null;
    public boolean voteFlag = false;
    public int voteTotalNum;
    ProgressBar progressBars;

    public interface GridviewItemClickListner {
        void onListImageClick(int position) ;
        void onListRadioClick(int position, View view) ;
    }


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

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.qnadetail_item, null);

            TextView textView = convertView.findViewById(R.id.grid_text);
            ImageView imageView = convertView.findViewById(R.id.grid_image);
            ProgressBar progressBar = convertView.findViewById(R.id.progressbar);
            RadioButton radioButton = convertView.findViewById(R.id.radio);

            final ViewHolder viewHolder = new ViewHolder(textView, imageView, progressBar, radioButton);
            convertView.setTag(viewHolder);
        }

        Log.d("어댑터", "getView 투표 값 상태: "+position+" "+item.get(position).getVote());

        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        viewHolder.textView.setText(item.get(position).getStatus());
        viewHolder.imageView.setImageURI(item.get(position).getImage());
        viewHolder.progressBar.setProgress(item.get(position).getVote());
        viewHolder.progressBar.setScaleY(20f);

        viewHolder.progressBar.setTag(position);
        viewHolder.radioButton.setTag(position);
        viewHolder.textView.setTag(position);
        viewHolder.imageView.setTag(position);
        viewHolder.progressBar.setMax(voteTotalNum);

//        if (position == getCount() - 1) {
//            if (selected == null) {
//                viewHolder.radioButton.setChecked(true);
//                selected = viewHolder.radioButton;
//            }
//        }

        if (voteFlag == true) {
            viewHolder.radioButton.setVisibility(View.GONE);
            viewHolder.progressBar.setVisibility(View.VISIBLE);
            viewHolder.imageView.setAlpha(50);
        }

        viewHolder.imageView.setOnClickListener(this);
        viewHolder.radioButton.setOnClickListener(this);
//        viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selected != null) {
//                    selected.setChecked(false);
//                    item.get(position).setSelected(false);
//                }
//                viewHolder.radioButton.setChecked(true);
//                selected = viewHolder.radioButton;
//                item.get(position).setSelected(true);
//            }
//        });


//            if (image.size() < 7) {
//                textView.setVisibility(View.VISIBLE);
//            } else {
//                textView.setVisibility(View.GONE);
//            }

        return convertView;
    }

    private class ViewHolder {
        private TextView textView;
        private ImageView imageView;
        private ProgressBar progressBar;
        private RadioButton radioButton;

        public ViewHolder(TextView textView, ImageView imageView, ProgressBar progressBar, RadioButton radioButton) {
            this.textView = textView;
            this.imageView = imageView;
            this.progressBar = progressBar;
            this.radioButton = radioButton;
        }
    }

    public void onClick(View v) {
        // ListBtnClickListener(MainActivity)의 onListBtnClick() 함수 호출.
        switch (v.getId()) {
            case R.id.grid_image:
                if (this.gridviewItemClickListner != null) {
                    this.gridviewItemClickListner.onListImageClick((int)v.getTag()) ;
                }
                break;
            case R.id.radio:
                if (this.gridviewItemClickListner != null) {
                    this.gridviewItemClickListner.onListRadioClick((int)v.getTag(), v) ;
                }
                break;
        }
    }
}
