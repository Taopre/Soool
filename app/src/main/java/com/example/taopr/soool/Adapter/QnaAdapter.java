package com.example.taopr.soool.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taopr.soool.Decorater.RecyclerDecoration;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.R;
import com.example.taopr.soool.Whatisthis;

import java.util.ArrayList;
import java.util.List;

public class QnaAdapter extends RecyclerView.Adapter<QnaAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<QnaBoardItem> qnaBoardItems;
    private Context context;
    private static String TAG ="큐앤에이_adapter";
    private  String[] tagData = new String[0];
    private ArrayList<String> tagArray = new ArrayList<>();

    public QnaAdapter(Activity activity, ArrayList<QnaBoardItem> qnaBoardItems, Context context) {
        this.activity = activity;
        this.context = context;
        this.qnaBoardItems = qnaBoardItems;
    }

    public QnaAdapter(ArrayList<QnaBoardItem> qnaBoardItems, Context context){
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

        //TextView qnaBoardTag;

        HorizontalScrollView h_scrollView;

        RecyclerView qnaboardTagView;

        public ViewHolder(View v) {
            super(v);

            qnaBoardTitle = v.findViewById(R.id.qnaBoardTitle);
            qnaBoardImage = v.findViewById(R.id.qnaBoardImage);
            qnaBoardDate = v.findViewById(R.id.qnaBoardDate);
            qnaBoardViews = v.findViewById(R.id.qnaBoardViews);
            qnaBoardWriter = v.findViewById(R.id.qnaBoardWriter);
            qnaBoardComments = v.findViewById(R.id.qnaBoardComments);
          //  qnaBoardTag = v.findViewById(R.id.qnaBoardTag);
            h_scrollView = v.findViewById(R.id.h_scrollView);
            qnaboardTagView = v.findViewById(R.id.qnaboardTagView);

        }
    }
    public ArrayList<QnaBoardItem> deleteItem(int position){
        qnaBoardItems.remove(position);
        notifyItemRemoved(position);
        return qnaBoardItems;
    }

    public ArrayList<QnaBoardItem> modifyItem(QnaBoardItem qnaBoardItem, int position){
        qnaBoardItems.set(position,qnaBoardItem);
        notifyItemChanged(position,qnaBoardItem);
        return qnaBoardItems;
    }

    public ArrayList<QnaBoardItem> addItem(QnaBoardItem qnaBoardItem){
        qnaBoardItems.add(qnaBoardItem);

        notifyItemInserted(0);
        return qnaBoardItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qna,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        QnaBoardItem qnaBoardItem = qnaBoardItems.get(position);

        if(qnaBoardItem.getTag().length() > 0) {
            tagArray = new ArrayList<String>();

            if (qnaBoardItem.getTag().contains("@##@")) {

                //tagArray = new ArrayList<String>();
                tagData = qnaBoardItem.getTag().split("@##@");
                for (int i = 0; i < tagData.length; i++) {
                    tagArray.add(tagData[i]);
                }

            }
            else{
                tagArray.add(qnaBoardItem.getTag());
            }
            QnaBoardTagAdapter qnaBoardTagAdapter = new QnaBoardTagAdapter(context, tagArray, 1);
            holder.qnaboardTagView.setAdapter(qnaBoardTagAdapter);
            holder.qnaboardTagView.addItemDecoration(new RecyclerDecoration(32));
            holder.qnaboardTagView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }



        holder.qnaBoardTitle.setText(qnaBoardItem.getTitle());
        holder.qnaBoardComments.setText(String.valueOf(qnaBoardItem.getComments()));
        holder.qnaBoardWriter.setText(qnaBoardItem.getWriter());
        holder.qnaBoardViews.setText(String.valueOf(qnaBoardItem.getViews()));
        holder.qnaBoardDate.setText(qnaBoardItem.getDate());


        if(qnaBoardItem.getImage() != null){
            //holder.qnaBoardImage
            String qnaImageURI= Whatisthis.serverIp + qnaBoardItem.getImage();

            Glide.with(context)
                    .load(qnaImageURI)
                    .centerCrop()
                    .into(holder.qnaBoardImage);
        }
        else{
            holder.qnaBoardImage.setVisibility(View.INVISIBLE);
        }

    }
}
