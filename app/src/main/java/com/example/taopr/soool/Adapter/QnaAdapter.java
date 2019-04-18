package com.example.taopr.soool.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.R;
import com.example.taopr.soool.View.QnaActivity;

import java.util.List;

public class QnaAdapter extends RecyclerView.Adapter<QnaAdapter.ViewHolder> {

    private Activity activity;
    private List<QnaBoardItem> qnaBoardItems;
    private QnaActivity ac;
    private Context context;
    private static String TAG ="큐앤에이_adapter";

    public QnaAdapter(Activity activity, List<QnaBoardItem> qnaBoardItems, Context context) {
        this.activity = activity;
        this.context = context;
        this.qnaBoardItems = qnaBoardItems;
    }

    @Override
    public int getItemCount() {
        return qnaBoardItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView qnaBoardTitle;

        ImageView qnaBoardImage;

        TextView qnaBoardDate;

        TextView qnaBoardViews;

        TextView qnaBoardWriter;

        TextView qnaBoardComments;

        TextView qnaBoardTag;

        public ViewHolder(View v) {
            super(v);

            qnaBoardTitle = v.findViewById(R.id.qnaBoardTitle);
            qnaBoardImage = v.findViewById(R.id.qnaBoardImage);
            qnaBoardDate = v.findViewById(R.id.qnaBoardDate);
            qnaBoardViews = v.findViewById(R.id.qnaBoardViews);
            qnaBoardWriter = v.findViewById(R.id.qnaBoardWriter);
            qnaBoardComments = v.findViewById(R.id.qnaBoardComments);
            qnaBoardTag = v.findViewById(R.id.qnaBoardTag);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qna_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        QnaBoardItem qnaBoardItem = qnaBoardItems.get(position);

        holder.qnaBoardTitle.setText(qnaBoardItem.getTitle());
        holder.qnaBoardComments.setText(String.valueOf(qnaBoardItem.getComments()));
        holder.qnaBoardWriter.setText(qnaBoardItem.getWriter());
        holder.qnaBoardViews.setText(String.valueOf(qnaBoardItem.getViews()));
        holder.qnaBoardViews.setText(qnaBoardItem.getDate());

    }
}
