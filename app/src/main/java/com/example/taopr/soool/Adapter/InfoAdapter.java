package com.example.taopr.soool.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.taopr.soool.Object.InfoOfSoool;
import com.example.taopr.soool.R;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private Activity activity;
    private List<InfoOfSoool> infoOfSoools;
    private Context context;
    private final String TAG ="정보_adapter";

    public InfoAdapter(Activity activity, List<InfoOfSoool> info, Context context) {
        this.activity = activity;
        this.context = context;
        this.infoOfSoools = info;
    }

    @Override
    public int getItemCount() {
        return infoOfSoools.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView infoDate;

        ImageView infoImage;

        TextView infoScraps;

        TextView infoTitle;

        TextView infoViews;


        public ViewHolder(View v) {
            super(v);

            infoTitle = v.findViewById(R.id.infoTitle);
            infoImage = v.findViewById(R.id.infoImage);
            infoDate = v.findViewById(R.id.infoDate);
            infoViews = v.findViewById(R.id.infoViews);
            infoScraps = v.findViewById(R.id.infoScraps);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "click " +
                            infoOfSoools.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(activity, "remove " +
                           infoOfSoools.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                    removeItemView(getAdapterPosition());
                    return false;
                }
            });*/
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info, parent, false);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.info_of_soool,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // 재활용 되는 View가 호출, Adapter가 해당 position에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder: ");
        InfoOfSoool infoOfSoool = infoOfSoools.get(position);

        // 데이터 결합
       // holder.name.setText(data.getTitle());
       // holder.number.setText(data.getNumber());

        holder.infoTitle.setText(infoOfSoool.getTitle());
        holder.infoDate.setText(infoOfSoool.getDate());
        holder.infoViews.setText(String.valueOf(infoOfSoool.getViews()));
        holder.infoScraps.setText(String.valueOf(infoOfSoool.getScraps()));

    }

    private void removeItemView(int position) {
        infoOfSoools.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, infoOfSoools.size()); // 지워진 만큼 다시 채워넣기.
    }
}