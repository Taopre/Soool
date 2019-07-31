package com.lpky.taopr.soool.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lpky.taopr.soool.R;


public class ProfileSpinnerAdapter<T> extends ArrayAdapter<T> {

    private Context context;
    private T[] items;
    private final String TAG ="마이 프로필 스피너";

    public ProfileSpinnerAdapter(Context context, T[] objects) {
      /*  super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;*/
        super(context, android.R.layout.simple_spinner_item, objects);
        this.context = context;
        this.items = objects;
    }

    // 스피너에서 내려보기 했을 때 보여지는 아이템들의 설정 값을 부여하는 부분
    // 글자색 grayMain 으로 변경하고 textSize를 14dp로 변경


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        Log.i(TAG, "getDropDownView: ");
        if (convertView == null) {
            convertView= super.getView(position, convertView, parent);
        }


        TextView text = convertView.findViewById(android.R.id.text1);
        text.setTextColor(ContextCompat.getColor(context, R.color.grayMain));
        text.setTextSize(14);

        // textView 값이 처음에 보여질때는 정상적으로 리스트의 값이 보여졌지만
        // 아이템들이 선택 후에는 스피너의 아이템들끼리 순서가 바껴서 보여지는
        // 현상이 생김. 그래서 뷰를 가져올때마다 text 에 위치에 맞는 항목 값을 보여주도록 함
        text.setText((String)items[position]);

        int bottomPadding = 0;
        int topPadding = 0;
        int leftPadding =  getContext().getResources().getDimensionPixelSize(R.dimen.all_space_between_16dp);


        if(position ==0){
            topPadding = getContext().getResources().getDimensionPixelSize(R.dimen.all_space_between_20dp);
            bottomPadding = getContext().getResources().getDimensionPixelSize(R.dimen.all_space_between_16dp);
        }
        else if (position == items.length-1) {
            topPadding = getContext().getResources().getDimensionPixelSize(R.dimen.all_space_between_16dp);
            bottomPadding = getContext().getResources().getDimensionPixelSize(R.dimen.all_space_between_20dp);
        }
        else{
            topPadding = getContext().getResources().getDimensionPixelSize(R.dimen.all_space_between_16dp);
            bottomPadding = getContext().getResources().getDimensionPixelSize(R.dimen.all_space_between_16dp);
        }

        text.setPadding(leftPadding,topPadding,0,bottomPadding);

        return convertView;

    }

    // 스피너의 리스트 항목 중 선택한 항목을 보여주는 뷰에 대한 설정 값을 부여하는 부분
    // 설정 값을 리스트 설정 값과 일치

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView= super.getView(position, convertView, parent);
        }

        TextView text = (TextView) convertView.findViewById(android.R.id.text1);

        if(position ==0) {
            text.setTextColor(ContextCompat.getColor(context, R.color.grayMain));
        }
        else{
            text.setTextColor(ContextCompat.getColor(context, R.color.greenDark));
        }
        text.setTextSize(14);
        text.setPadding(16,0,0,0);
        return convertView;

    }
}

