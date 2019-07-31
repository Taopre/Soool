package com.lpky.taopr.soool.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lpky.taopr.soool.Decorater.RecyclerDecoration;
import com.lpky.taopr.soool.Object.InfoItem;
import com.lpky.taopr.soool.R;
import com.lpky.taopr.soool.TimeCalculator;
import com.lpky.taopr.soool.Util.Whatisthis;

import java.util.ArrayList;

public class MainInfoAdapter extends PagerAdapter{

    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    private Context context = null ;
    private OnItemClick mListener;
    private ArrayList<InfoItem> infoItems;
    private TextView mainInfoTitle,mainInfoWriter,mainInfoDate,mainInfoViews,mainInfoComments;
    private TextView mainInfoTotalConnt,mainInfoCurrentPos;
    private ImageView mainInfoImage;
    private RecyclerView mainInfoTagView;
    private HorizontalScrollView mainInfoTagSV;
    private ArrayList<String> tagArray = new ArrayList<>();
    private  String[] tags = new String[0];
    private String TAG = "홈 메인 정보 어댑터";
    private TimeCalculator timeCalculator;
    SparseArray<View> views = new SparseArray<View>();



    public interface OnItemClick {
        void onItemClick(int position);
    }

    // Context를 전달받아 mContext에 저장하는 생성자 추가.
    public MainInfoAdapter(Context context,ArrayList<InfoItem> infoItems,OnItemClick mListener) {
        this.context = context ;
        this.infoItems = infoItems;
        this.mListener = mListener;
        timeCalculator = new TimeCalculator();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null ;

        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_main_info, container, false);

            Log.i(TAG, "instantiateItem: 포지션" +position);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position);
                }
            });

            mainInfoTitle = view.findViewById(R.id.mainInfoTitle) ;
            mainInfoWriter= view.findViewById(R.id.mainInfoWriter);
            mainInfoDate = view.findViewById(R.id.mainInfoDate);
            mainInfoComments = view.findViewById(R.id.mainInfoComments);
            mainInfoViews = view.findViewById(R.id.mainInfoViews);
            mainInfoComments = view.findViewById(R.id.mainInfoComments);
            mainInfoTagSV = view.findViewById(R.id.mainInfoTagSV);
            mainInfoTagView = view.findViewById(R.id.mainInfoTagView);
            mainInfoTagView.addItemDecoration(new RecyclerDecoration(32));
            mainInfoImage = view.findViewById(R.id.mainInfoImage);
            mainInfoTotalConnt = view.findViewById(R.id.mainInfoTotalCount);
            mainInfoCurrentPos = view.findViewById(R.id.mainInfoCurrentPos);

            InfoItem infoItem = infoItems.get(position);

            // 태그 받아서 배열에 담고 TagAdapter에 연결하기
            tagArray = new ArrayList<String>();
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
                MainInfoTagAdapter mainInfoTagAdapter = new MainInfoTagAdapter(context, tagArray);
                mainInfoTagView.setAdapter(mainInfoTagAdapter);
                mainInfoTagView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            }


            mainInfoTitle.setText(infoItem.getTitle());
            mainInfoComments.setText(String.valueOf(infoItem.getComments()));
            mainInfoWriter.setText(infoItem.getWriter());
            mainInfoViews.setText(String.valueOf(infoItem.getViews()));

            mainInfoTotalConnt.setText("3"); // 현재는 메인페이지에서 정보글은 3개로 픽스해서 보여주고 있음
            mainInfoCurrentPos.setText(String.valueOf(position+1));

            // 작성시간 '몇 분 전' 으로 표기하기
            mainInfoDate.setText(timeCalculator.getbeforeTime(infoItem.date));


            // 이미지 glide로 띄우기
            String infoCoverURI= Whatisthis.serverIp + infoItem.getCover();
            Glide.with(context)
                    .load(infoCoverURI)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // 이미지 로드에 실패 했을 경우
                            // reload 를 진행할 것인지, reload 를 진행한다면 회수를 정해야 함
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // 이미지 로드 완료 했을 때
                            // 투명도 설정, 투명도를 준 이유 이미지안의 글씨들이 좀 더 선명하게 보이게 하기 위해서
                            return false;
                        }
                    })
                    .thumbnail(0.1f)
                    .into(mainInfoImage);

        }

        // 뷰페이저에 추가.
        container.addView(view) ;
        views.put( position, view );
        return view ;
    }

    @Override
    public int getCount() {
        //전체 페이지 수는 3개로 고정
        return 3;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
        views.remove(position);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }

    // 메인 페이지에서 정보글을 클릭했을 경우 디테일페이지에 해당 정보글 내용을 전달
    public InfoItem getInfoItem(int position){
        Log.i(TAG, "getInfoItem: 포지션 " + position);
        return infoItems.get(position);
    }

    public void infoItemUpdate(InfoItem infoItem,int position){
        Log.i(TAG, "infoItemUpdate: 인포" + position);
        infoItems.set(position,infoItem);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        int key = 0;

        for(int i = 0; i < views.size(); i++) {

            key = views.keyAt(i);

            View view = views.get(key);

        }


        super.notifyDataSetChanged();
    }
    @Override
    public int getItemPosition(Object object) {
        Log.i(TAG, "getItemPosition: 인포");
        return POSITION_NONE;

    }


}
