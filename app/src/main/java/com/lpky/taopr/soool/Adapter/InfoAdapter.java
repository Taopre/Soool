package com.lpky.taopr.soool.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
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
import com.lpky.taopr.soool.Decorater.RecyclerDecoration;
import com.lpky.taopr.soool.Object.InfoItem;
import com.lpky.taopr.soool.R;
import com.lpky.taopr.soool.TimeCalculator;
import com.lpky.taopr.soool.Util.Whatisthis;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<InfoItem> infoItems;
    private TimeCalculator timeCalculator;
    private Context context;
    private final String TAG ="정보_adapter";
    private  String[] tags = new String[0];
    private ArrayList<String> tagArray = new ArrayList<>();

    public InfoAdapter(Activity activity, ArrayList<InfoItem> infoItems, Context context) {
        this.activity = activity;
        this.context = context;
        this.infoItems = infoItems;
    }

    public InfoAdapter(ArrayList<InfoItem> infoItems, Context context){
        this.context = context;
        this.infoItems = infoItems;
        timeCalculator = new TimeCalculator();
    }

    @Override
    public int getItemCount() {
        return infoItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView infoTitle;
        ImageView infoCover;
        TextView infoDate;
        TextView infoViews;
        TextView infoWriter;
        TextView infoComments;
        HorizontalScrollView infoScrollView; // tag 담아둔 scrollView
        RecyclerView infoTagView; // scrollView에 들어가는 tag-recyclerView



        public ViewHolder(View v) {
            super(v);

            infoTitle = v.findViewById(R.id.itemInfoTitle); // info_of_soool.xml 지우고 나면 R.id.itemInfo(*) -> item(*)로 변경
            infoCover = v.findViewById(R.id.itemInfoCover);
            infoDate = v.findViewById(R.id.itemInfoDate);
            infoViews = v.findViewById(R.id.itemInfoViews);
            infoWriter = v.findViewById(R.id.itemInfoWriter);
            infoComments = v.findViewById(R.id.itemInfoComments);
            infoScrollView = v.findViewById(R.id.itemInfoTagSV);
            infoTagView = v.findViewById(R.id.itemInfoTagList);
            infoTagView.addItemDecoration(new RecyclerDecoration(32));

        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info, parent, false);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_info,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // 재활용 되는 View가 호출, Adapter가 해당 position에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder: ");

        InfoItem infoItem = infoItems.get(position);

        // 태그 받아서 배열에 담고 TagAdapter에 연결하기
        tagArray = new ArrayList<String>();
        Log.e(TAG, "onBindViewHolder: infoItem.getPostTag().length"+tagArray);
        if(infoItem.getPostTag().length() > 0) {

            // 태그 여러개일 때 정규표현식으로 분리하기
            if (infoItem.getPostTag().contains("@##@")) {
                tags = infoItem.getPostTag().split("@##@");
                for (int i = 0; i < tags.length; i++) {
                    tagArray.add(tags[i]);
                }

            }
            else{
                tagArray.add(infoItem.getPostTag());
            }

            // 기존에 만들어둔 qnaBoardTagAdapter 재활용
            QnaBoardTagAdapter tagAdapter = new QnaBoardTagAdapter(context, tagArray, 1);
            holder.infoTagView.setAdapter(tagAdapter);
            holder.infoTagView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }


        holder.infoTitle.setText(infoItem.getTitle());
        holder.infoComments.setText(String.valueOf(infoItem.getComments()));
        holder.infoWriter.setText(infoItem.getWriter());
        holder.infoViews.setText(String.valueOf(infoItem.getViews()));


        // 작성시간 '몇 분 전' 으로 표기하기
        holder.infoDate.setText(timeCalculator.getbeforeTime(infoItem.date));


        // 이미지 glide로 띄우기
        String infoCoverURI= Whatisthis.serverIp + infoItem.getCover();
        //String infoCoverURI= Whatisthis.serverIp + "/infoImage/"+ infoItem.getCover();
        Glide.with(context)
                .load(infoCoverURI)
                .centerCrop()
                .thumbnail(0.3f)
                .into(holder.infoCover);


    }

    // 정보 fragment에서는 필요없지만 북마크 fragment에서는 추가, 삭제 필요
    public ArrayList<InfoItem> removeItem(int position){

        // removing a single item from the list
        infoItems.remove(position);
        notifyItemRemoved(position);
        return infoItems;

    }


    public ArrayList<InfoItem> addItem(InfoItem infoItem) {

        // adding a single item to the list // 정보글엔 굳이 필요 없음
        infoItems.add(0, infoItem);
        notifyItemInserted(0);
        return infoItems;

    }

    public void addList(ArrayList<InfoItem> addListOfItems){

        // adding a list of items into the list
        this.infoItems = addListOfItems;
        notifyDataSetChanged();

    }

    public ArrayList<InfoItem> updateItem(InfoItem infoItem, int position) {
        infoItems.set(position, infoItem);
        notifyItemChanged(position, infoItem);
        return infoItems;
    }

}