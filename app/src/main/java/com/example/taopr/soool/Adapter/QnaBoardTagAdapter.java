package com.example.taopr.soool.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.taopr.soool.Object.QnaBoardVoteItem;
import com.example.taopr.soool.R;

import java.util.ArrayList;

public class QnaBoardTagAdapter extends RecyclerView.Adapter<QnaBoardTagAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    public static ArrayList<String> data;


    public QnaBoardTagAdapter(Context ctx, ArrayList<String> data){

        inflater = LayoutInflater.from(ctx);
        this.data = data;
    }

    @Override
    public QnaBoardTagAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.qnaboard_tag, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final QnaBoardTagAdapter.MyViewHolder holder, final int position) {


        holder.textView.setText(data.get(position));
        Log.d("print","yes");

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        protected TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.horizon_description);

        }

    }
}
