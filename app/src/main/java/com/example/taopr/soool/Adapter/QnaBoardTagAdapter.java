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
    public static ArrayList<String> data;
    private static ClickListeners clickListeners;
    private int whatActivity = 9998;
    private Context context;
    public QnaBoardTagAdapter() {}

    public QnaBoardTagAdapter(Context ctx, ArrayList<String> data){
        inflater = LayoutInflater.from(ctx);
        this.data = data;
    }


    public QnaBoardTagAdapter(Context ctx, ArrayList<String> data, int whatActivity){
        inflater = LayoutInflater.from(ctx);
        this.data = data;
        this.whatActivity = whatActivity;
    }
    public QnaBoardTagAdapter(Context ctx, ArrayList<String> data, int whatActivity,Context context){
        inflater = LayoutInflater.from(ctx);
        this.data = data;
        this.whatActivity = whatActivity;
        this.context = context;
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
    public void onBindViewHolder(final QnaBoardTagAdapter.MyViewHolder holder, final int position) {

        holder.tagTitle.setText(data.get(position));
        //holder.deleteTag.setText(data.get(position));
        Log.d("print","yes");

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setClickListeners(ClickListeners clickListeners) {
        QnaBoardTagAdapter.clickListeners = clickListeners;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView tagTitle;
        protected ImageView deleteTag;
        protected ViewGroup tagLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            tagTitle = itemView.findViewById(R.id.tagTitle);
            deleteTag = itemView.findViewById(R.id.deleteTag);
            tagLayout = itemView.findViewById(R.id.tagLayout);

            if (whatActivity == 1) {
                deleteTag.setVisibility(View.GONE);
                tagTitle.setPadding(36,0,36,0);

            }
            else {
                deleteTag.setVisibility(View.VISIBLE);
                tagLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.tag_frame_green));
                tagTitle.setTextColor(ContextCompat.getColor(context,R.color.greenMain));

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
