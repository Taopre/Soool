package com.example.taopr.soool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taopr.soool.Object.QnaBoardVoteItem;
import com.example.taopr.soool.R;

import java.util.ArrayList;

public class QnaBoardDetailVoteAdapter extends RecyclerView.Adapter<QnaBoardDetailVoteAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    public ArrayList<QnaBoardVoteItem> editModelArrayList = new ArrayList<>();
    public int voteTotalNums;
    public boolean voteFlag = false, textSelectFlag = false, alreadyVote = false;

    private static ClickListener clickListener;
    public TextView selected = null;

    public interface ClickListener {
        void onListVoteListClick(int position, View view) ;
    }


    public QnaBoardDetailVoteAdapter() {}

    public QnaBoardDetailVoteAdapter(Context ctx, ArrayList<QnaBoardVoteItem> editModelArrayList){
        inflater = LayoutInflater.from(ctx);
        this.editModelArrayList = editModelArrayList;
    }

    @Override
    public QnaBoardDetailVoteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_items, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final QnaBoardDetailVoteAdapter.MyViewHolder holder, final int position) {
        Log.d("어댑터", "onBindViewHolder: 하위" + " " + editModelArrayList.get(position).getVoteboard());
        //holder.textView.setText(editModelArrayList.get(position).getEditTextValue());
        holder.textView.setText(String.valueOf(editModelArrayList.get(position).getVoteboard()));
        holder.progressBar.setProgress((editModelArrayList.get(position).getVoteboard()));
        holder.progressBar.setMax(voteTotalNums);
        holder.progressBar.setScaleY(5f);


        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    clickListener.onListVoteListClick(pos, v);
                } catch (NullPointerException e) {
                    Log.d("adapter", "onClick: 끝나라!!!");
                }
            }
        });


        if (position == getItemCount() - 1) {
            if (selected == null) {
                holder.textView.setTextColor(Color.parseColor("#000000"));
                selected = holder.textView;
                editModelArrayList.get(position).setFlag(false);
            }
        }

        if (voteFlag == true) {
            holder.progressBar.setVisibility(View.VISIBLE);
        }

        if (alreadyVote == true) {
            Log.d("adapter", "onBindViewHolder: 투표끝 작동되지마");
            clickListener = null;
        }

        if (textSelectFlag == true) {
//            for (int i=0; i<editModelArrayList.size(); i++) {
//                Log.d("어뎁터", "플레그: "+editModelArrayList.get(i).isFlag());
//            }
        }

    }

    @Override
    public int getItemCount() {
        return editModelArrayList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        QnaBoardDetailVoteAdapter.clickListener = clickListener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView textView;
        protected ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textViewssss);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
}

