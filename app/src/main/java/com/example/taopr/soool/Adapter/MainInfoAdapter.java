package com.example.taopr.soool.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taopr.soool.R;

public class MainInfoAdapter extends PagerAdapter{

    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    private Context context = null ;
    private OnItemClick mListener;

    public interface OnItemClick {
        void onItemClick(int position);
    }

    // Context를 전달받아 mContext에 저장하는 생성자 추가.
    public MainInfoAdapter(Context context,OnItemClick mListener) {
        this.context = context ;
        this.mListener = mListener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null ;

        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_main_info, container, false);
            /*view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                }
            });*/
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position);
                }
            });

            TextView textView = view.findViewById(R.id.mainInfoTitle) ;

            textView.setText("TEXT " + position) ;
        }

        // 뷰페이저에 추가.
        container.addView(view) ;

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
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }

}
