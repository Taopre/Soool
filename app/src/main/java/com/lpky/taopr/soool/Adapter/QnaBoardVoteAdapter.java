package com.lpky.taopr.soool.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.lpky.taopr.soool.Object.QnaBoardVoteItem;
import com.lpky.taopr.soool.R;

import java.util.ArrayList;

public class QnaBoardVoteAdapter extends RecyclerView.Adapter<QnaBoardVoteAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    public ArrayList<QnaBoardVoteItem> editModelArrayList;
    private QnaBoardVoteListener qnaBoardVoteListener;
    private Context context;

    public interface QnaBoardVoteListener {
        void voteContentClickListner(int position, View view) ;
    }

    public QnaBoardVoteAdapter(Context context, QnaBoardVoteListener qnaBoardVoteListener) {
        this.context = context;
        this.qnaBoardVoteListener = qnaBoardVoteListener;
    }

    public QnaBoardVoteAdapter(Context ctx, ArrayList<QnaBoardVoteItem> editModelArrayList, QnaBoardVoteListener qnaBoardVoteListener){

        inflater = LayoutInflater.from(ctx);
        this.editModelArrayList = editModelArrayList;
        this.context = ctx;
        this.qnaBoardVoteListener = qnaBoardVoteListener;
    }

    @Override
    public QnaBoardVoteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.qna_text_vote_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final QnaBoardVoteAdapter.MyViewHolder holder, final int position) {

        holder.editText.setText(editModelArrayList.get(position).getEditTextValue());
        holder.bind(qnaBoardVoteListener);
    }

    @Override
    public int getItemCount() {
        return editModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        protected EditText editText;
        protected ImageView deleteText;

        public MyViewHolder(View itemView) {
            super(itemView);

            editText = itemView.findViewById(R.id.editid);
            deleteText = itemView.findViewById(R.id.deleteText);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    editModelArrayList.get(getAdapterPosition()).setEditTextValue(editText.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

        public void bind(QnaBoardVoteListener qnaBoardVoteListener) {
            deleteText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    qnaBoardVoteListener.voteContentClickListner(getAdapterPosition(), v);
                }
            });
        }

    }
}
