package com.lpky.taopr.soool.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.lpky.taopr.soool.R;

import java.util.ArrayList;

public class MainInfoTagAdapter extends RecyclerView.Adapter<MainInfoTagAdapter.MyViewHolder> {

    private LayoutInflater inflater;

    public ArrayList<String> data;
    private final String TAG = "큐앤에이 태그 어댑터 ";


    public MainInfoTagAdapter(Context ctx, ArrayList<String> data){
        inflater = LayoutInflater.from(ctx);
        this.data = data;
    }

    @Override
    public MainInfoTagAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_main_info_tag, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder( MainInfoTagAdapter.MyViewHolder holder, final int position) {

        holder.tagTitle.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView tagTitle;

        protected ViewGroup itemTag;


        public MyViewHolder(View itemView) {
            super(itemView);

            tagTitle = itemView.findViewById(R.id.mainInfoItemTagTitle);
            itemTag = itemView.findViewById(R.id.mainInfoItemTag);

        }

    }
}
