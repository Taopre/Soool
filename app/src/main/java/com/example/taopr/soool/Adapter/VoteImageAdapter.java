package com.example.taopr.soool.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taopr.soool.Object.GridVoteItem;
import com.example.taopr.soool.R;

import java.util.ArrayList;

public class VoteImageAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private ArrayList<GridVoteItem> item = new ArrayList<>();
    private GridviewItemClickListner gridviewItemClickListner;

    public interface GridviewItemClickListner {
        void onListTextClick(int position) ;
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

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.qna_vote_image, null);

            TextView textView = convertView.findViewById(R.id.grid_text);
            ImageView imageView = convertView.findViewById(R.id.grid_image);
            Button button = convertView.findViewById(R.id.grid_btn);

            final ViewHolder viewHolder = new ViewHolder(textView, imageView, button);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        viewHolder.textView.setText(item.get(position).getStatus());
        viewHolder.imageView.setImageURI(item.get(position).getImage());

        viewHolder.button.setTag(position);
        viewHolder.textView.setTag(position);
        viewHolder.imageView.setTag(position);

        viewHolder.button.setOnClickListener(this);
        viewHolder.textView.setOnClickListener(this);

        if (item.get(position).getStatus().equals("항목추가")) {
            viewHolder.button.setVisibility(View.GONE);
        }

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
        private Button button;

        public ViewHolder(TextView textView, ImageView imageView, Button button) {
            this.textView = textView;
            this.imageView = imageView;
            this.button = button;
        }
    }

    public void onClick(View v) {
        // ListBtnClickListener(MainActivity)의 onListBtnClick() 함수 호출.
        switch (v.getId()) {
            case R.id.grid_btn:
                if (this.gridviewItemClickListner != null) {
                    this.gridviewItemClickListner.onListTextClick((int)v.getTag()) ;
                }
                break;
        }
    }
}