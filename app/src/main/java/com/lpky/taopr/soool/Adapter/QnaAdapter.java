package com.lpky.taopr.soool.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lpky.taopr.soool.Calendar.SooolCalendar;
import com.lpky.taopr.soool.Decorater.RecyclerDecoration;
import com.lpky.taopr.soool.Object.QnaBoardItem;
import com.lpky.taopr.soool.R;
import com.lpky.taopr.soool.TimeCalculator;
import com.lpky.taopr.soool.Util.Whatisthis;

import java.util.ArrayList;

public class QnaAdapter extends RecyclerView.Adapter<QnaAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<QnaBoardItem> qnaBoardItems;
    private Context context;
    private TimeCalculator timeCalculator;
    private SooolCalendar sooolCalendar;
    private final String TAG ="큐앤에이 어댑터";
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
        timeCalculator = new TimeCalculator();
        sooolCalendar = new SooolCalendar();
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
            qnaboardTagView.addItemDecoration(new RecyclerDecoration(32));

        }
    }
    public ArrayList<QnaBoardItem> deleteItem(int position){
        qnaBoardItems.remove(position);
        notifyItemRemoved(position);
        return qnaBoardItems;
    }
    public ArrayList<QnaBoardItem> deleteItem(QnaBoardItem qnaBoardItem){
        for (int a=0; a<qnaBoardItems.size(); a++){
            if(qnaBoardItems.get(a).getPostNo() == qnaBoardItem.getPostNo()) {
                int position = a;
                qnaBoardItems.remove(position);
                notifyItemRemoved(position);
            }
        }
        /*qnaBoardItems.remove(position);
        notifyItemRemoved(position);*/
        return qnaBoardItems;
    }

    public ArrayList<QnaBoardItem> modifyItem(QnaBoardItem qnaBoardItem, int position){
        qnaBoardItems.set(position,qnaBoardItem);
        notifyItemChanged(position,qnaBoardItem);
        return qnaBoardItems;
    }

    // 내 게시물에 대한 리스트가 메모리에 올라가 있는 상황에서 커뮤니티나 홈 탭에서 내 게시물과 겹치는
    // 게시글에 대해서 수정이 일어났을 경우 QnaBoardItem 객체를 받아 객체의 postNo 값이 같은
    // 리스트의 아이템에 대해서 찾아서 수정
    public ArrayList<QnaBoardItem> modifyItem(QnaBoardItem modifiedQnaBoardItem){
        for (int a=0; a<qnaBoardItems.size(); a++) {
            if(qnaBoardItems.get(a).getPostNo() == modifiedQnaBoardItem.getPostNo()) {
                int position = a;
                qnaBoardItems.set(position, modifiedQnaBoardItem);
                notifyItemChanged(position, modifiedQnaBoardItem);
            }
        }
        return qnaBoardItems;
    }

    public ArrayList<QnaBoardItem> addItem(QnaBoardItem qnaBoardItem){
       // qnaBoardItems.add(qnaBoardItem); 에러코드 해당 소스코드로 실행 시 배열의 마지막 위치에 추가가 되면서 아이템 위치들이 꼬임
        qnaBoardItems.add(0,qnaBoardItem);
        notifyItemInserted(0);
        //notifyItemInserted(0);
        return qnaBoardItems;
    }

    public void addItemList(ArrayList<QnaBoardItem> addQnaBoardItems){
        this.qnaBoardItems = addQnaBoardItems;
        notifyDataSetChanged();
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
        tagArray = new ArrayList<String>();

        // 투표 태그 추가 부분
        // qnaCate 값이 0이 투표가 있는 경우이기 때문에
        // 투표 태그를 추가. 그리고 투표 태그만 다른 태그와 달리 태그의 색상을 초록색으로 설정
        if(qnaBoardItem.getQnaCate() == 0){
            tagArray.add("투표");
        }

        if(qnaBoardItem.getTag().length() > 0) {

            if (qnaBoardItem.getTag().contains("@##@")) {
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

            // onBindViewHolder 에서 아이템 간격을 조정할 경우 아이템간의 간격이 onBindViewHolder 로 들어올 때마다 간격이
            // 늘어나는 에러가 있었음. addItemDecoration 에서 데코 값을 보내줘서 보내준 width 값이 간격 값으로 바뀌는 것이 아닌
            // width 값이 간격 값에 추가되는 걸 알 수 있었음.
            // 그래서 해당 부분을 ViewHolder 로 이동해서 한번만 실행하게끔 해서 에러 수정
           // holder.qnaboardTagView.addItemDecoration(new RecyclerDecoration(32));

            holder.qnaboardTagView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }

        holder.qnaBoardTitle.setText(qnaBoardItem.getTitle());
        holder.qnaBoardComments.setText(String.valueOf(qnaBoardItem.getComments()));
        holder.qnaBoardWriter.setText(qnaBoardItem.getWriter());

        holder.qnaBoardViews.setText(String.valueOf(qnaBoardItem.getViews()));

        // 작성시간을 현재 시간 기준으로 시간이 얼마나 지났지를 표현
       holder.qnaBoardDate.setText(timeCalculator.getbeforeTime(qnaBoardItem.date));

        if(qnaBoardItem.getImage() != null){
            //holder.qnaBoardImage
            String qnaImageURI= Whatisthis.serverIp + qnaBoardItem.getImage();


            int imageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, context.getResources().getDimension(R.dimen.size_72dp)
            , context.getResources().getDisplayMetrics());

            Glide.with(context)
                    .load(qnaImageURI)
                    .centerCrop()
                    .override(216,216)
                    .centerCrop()
                    .into(holder.qnaBoardImage);

            holder.qnaBoardImage.setVisibility(View.VISIBLE);

        }
        else{
            holder.qnaBoardImage.setVisibility(View.INVISIBLE);
        }

    }

    public QnaBoardItem getQnaBoardItem(int position){
        return qnaBoardItems.get(position);
    }
}
