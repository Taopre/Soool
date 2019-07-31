package com.lpky.taopr.soool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lpky.taopr.soool.Object.QnaBoardVoteItem;
import com.lpky.taopr.soool.R;

import java.util.ArrayList;

public class QnaBoardDetailVoteAdapter extends RecyclerView.Adapter<QnaBoardDetailVoteAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    public ArrayList<QnaBoardVoteItem> editModelArrayList = new ArrayList<>();
    public int voteTotalNums, userSelectPos = 9999;
    public boolean voteFlag = false, alreadyVote = false;
    private Context ctx;

    String TAG = "텍스트 투표 어댑터";

    private ClickListener clickListener;
    public TextView tv_selected = null;
    public ImageView iv_selected = null;

    public interface ClickListener {
        void onListVoteListClick(int position, View view);
    }

    public QnaBoardDetailVoteAdapter(Context ctx, ArrayList<QnaBoardVoteItem> editModelArrayList) {
        this.editModelArrayList = editModelArrayList;
        this.ctx = ctx;
    }

    public QnaBoardDetailVoteAdapter(Context ctx, ArrayList<QnaBoardVoteItem> editModelArrayList, ClickListener clickListener){
        inflater = LayoutInflater.from(ctx);
        this.editModelArrayList = editModelArrayList;
        this.ctx = ctx;
        this.clickListener = clickListener;
    }

    @Override
    public QnaBoardDetailVoteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.qnadetail_text_vote_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final QnaBoardDetailVoteAdapter.MyViewHolder holder, final int position) {

        holder.textView.setText(editModelArrayList.get(position).getEditTextValue());
        holder.voteContentValue.setVisibility(View.INVISIBLE);
        holder.voteContentValue.setText((editModelArrayList.get(position).getVoteboard())+"");
        holder.itemView.setTag(position);
        holder.textSelect.setVisibility(View.INVISIBLE);
        holder.progressBar.post(new Runnable() {
            @Override
            public void run() {
                holder.progressBar.setProgress((editModelArrayList.get(position).getVoteboard()));
            }
        });
        holder.progressBar.setMax(voteTotalNums);
        holder.bind(clickListener);

        if (position == getItemCount() - 1) {
            if (tv_selected == null) {
                holder.textView.setTextColor(Color.parseColor("#9d9d97"));
                tv_selected = holder.textView;
                editModelArrayList.get(position).setFlag(false);
            }
        }

        if (voteFlag == true) {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.voteContentValue.setVisibility(View.VISIBLE);
            if (position != (userSelectPos - 1)) {
                holder.textSelect.setVisibility(View.INVISIBLE);
                holder.voteContentValue.setTextColor(Color.parseColor("#9d9d97"));
            } else {
                holder.textSelect.setVisibility(View.VISIBLE);
                holder.textView.setTextColor(Color.parseColor("#08883e"));
                holder.voteContentValue.setTextColor(Color.parseColor("#08883e"));
            }
        }

        if (alreadyVote == true) {
            clickListener = null;
        }
    }

    @Override
    public int getItemCount() {
        return editModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView textView;
        protected TextView voteContentValue;
        protected ProgressBar progressBar;
        protected ImageView textSelect;
        // 리스트 아이템 클릭 -> 클릭된 아이템 글자 색 변경 및 체크표시 이미지 출력 -> 클릭되지않은 아이템 글자 검정 및 널 이미지 출력

        public MyViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textViewssss);
            voteContentValue = itemView.findViewById(R.id.voteContentValue);
            progressBar = itemView.findViewById(R.id.progressbar);
            textSelect = itemView.findViewById(R.id.textSelect);
        }

        public void bind (ClickListener clickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onListVoteListClick(getAdapterPosition(), v);
                    } else {
                    }
                }
            });
        }
    }
}

