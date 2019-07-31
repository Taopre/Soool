package com.lpky.taopr.soool.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lpky.taopr.soool.R;

import java.util.ArrayList;


public class QnaTagAdapter extends RecyclerView.Adapter<QnaTagAdapter.ViewHolder>{

    private LayoutInflater inflater;
    public static ArrayList<String> data;
    private final String TAG = "큐앤에이 태그 어댑터 ";
    private Context context;

    public QnaTagAdapter(Context ctx, ArrayList<String> data, int whatActivity){
        inflater = LayoutInflater.from(ctx);
        this.data = data;
        this.context = ctx;
    }

    @NonNull
    @Override
    public QnaTagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull QnaTagAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
         TextView tagTitle;
         ImageView deleteTag;
         ViewGroup itemTag;

        public ViewHolder(View v) {
            super(v);
            tagTitle = itemView.findViewById(R.id.tagTitle);
            deleteTag = itemView.findViewById(R.id.deleteTag);
            itemTag = itemView.findViewById(R.id.itemTag);
            deleteTag.setVisibility(View.GONE);
            tagTitle.setPadding(36,0,36,0);

        }
    }
}
