package com.example.taopr.soool.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taopr.soool.Object.QnaBoardVoteItem;
import com.example.taopr.soool.R;

import java.util.ArrayList;

public class QnaBoardTagAdapter extends RecyclerView.Adapter<QnaBoardTagAdapter.MyViewHolder> {

    private LayoutInflater inflater;

    // ArrayList<String> data 와 ClickListeners 가 static 변수로 되어있었음.
    // 해당 부분을 static 를 해제해서 태그 동기화 문제를 해결할 수 있었는데, 정확한 이유는 모르겠음.

    private ArrayList<String> data;
    private ClickListeners clickListeners;
    private int whatActivity = 9998;
    private final String TAG = "큐앤에이 태그 어댑터 ";
    private Context context;
    public QnaBoardTagAdapter(Context ctx) {
        this.context = ctx;
    }

    public QnaBoardTagAdapter(Context ctx, ArrayList<String> data){
        inflater = LayoutInflater.from(ctx);
        this.data = data;
    }


    public QnaBoardTagAdapter(Context ctx, ArrayList<String> data, int whatActivity){
        inflater = LayoutInflater.from(ctx);
        this.data = data;
        this.whatActivity = whatActivity;
        this.context = ctx;
    }


    public interface ClickListeners {
        void onBtnClick(int position, View view) ;
    }

    @Override
    public QnaBoardTagAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.qnaboard_tag, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder( QnaBoardTagAdapter.MyViewHolder holder, final int position) {

        Log.i(TAG, "onBindViewHolder: 포지션 "+ position  + " 태그값 :" + data.get(position) + "태그리스트사이즈" + data.size());

        holder.tagTitle.setText(data.get(position));
        //holder.deleteTag.setText(data.get(position));
        Log.d("print","yes");

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setClickListeners(ClickListeners clickListeners) {
        this.clickListeners = clickListeners;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView tagTitle;
        protected ImageView deleteTag;
        protected ViewGroup itemTag;

        public MyViewHolder(View itemView) {
            super(itemView);

            tagTitle = itemView.findViewById(R.id.tagTitle);
            deleteTag = itemView.findViewById(R.id.deleteTag);
            itemTag = itemView.findViewById(R.id.itemTag);

            if (whatActivity == 1) {
                deleteTag.setVisibility(View.GONE);
                tagTitle.setPadding(36,0,36,0);
            }
            else {
                deleteTag.setVisibility(View.VISIBLE);
                itemTag.setBackground(ContextCompat.getDrawable(itemView.getContext(),R.drawable.tag_frame_green));
                tagTitle.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.greenMain));

            }
            deleteTag.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.deleteTag:
                    clickListeners.onBtnClick(getAdapterPosition(), v);
                    break;
            }
        }
    }
}
