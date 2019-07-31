package com.lpky.taopr.soool.Dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lpky.taopr.soool.R;

import java.util.ArrayList;

public class BottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener{

    public static BottomSheetDialog getInstance() { return new BottomSheetDialog(); }

    LinearLayout ll_tagBottlesLayout, ll_tagDrinkPersLayout, ll_tagHangoverLayout,ll_tagRecipeLayout,ll_tagHoneytipLayout,ll_tagHeronLayout,ll_tagEtcLayout;
    TextView tv_tagBottles, tv_tagDrinkPers, tv_tagHangover,tv_cancel, tv_complete,tv_tagRecipe,tv_tagHoneytip,tv_tagHeron,tv_tagEtc;

    public ArrayList<String> tagArray = new ArrayList<>();
    private BottomSheetDialoggListener bottomSheetDialoggListener;

    int flagTag1 = 0, flagTag2 = 0, flagTag3 = 0, flagTag4 = 0, flagTag5 = 0;

    //인터페이스 설정
    public interface BottomSheetDialoggListener{
        void onPositiveClicked(ArrayList<String> arrayList);
        void onNegativeClicked();
    }

    //호출할 리스너 초기화
    public void setDialogListener(BottomSheetDialoggListener bottomSheetDialoggListener){
        this.bottomSheetDialoggListener = bottomSheetDialoggListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container,false);
        ll_tagBottlesLayout = view.findViewById(R.id.tagBottlesLayout);
        ll_tagDrinkPersLayout = view.findViewById(R.id.tagDrinkPersLayout);
        ll_tagHangoverLayout = view.findViewById(R.id.tagHangoverLayout);

        ll_tagRecipeLayout = view.findViewById(R.id.tagRecipeLayout);
        ll_tagHoneytipLayout = view.findViewById(R.id.tagHoneytipLayout);
        ll_tagHeronLayout = view.findViewById(R.id.tagHeronLayout);
        ll_tagEtcLayout = view.findViewById(R.id.tagEtcLayout);


        tv_tagBottles = view.findViewById(R.id.tagBottles);
        tv_tagDrinkPers = view.findViewById(R.id.tagDrinkPers);
        tv_tagHangover = view.findViewById(R.id.tagHangover);

        tv_tagRecipe = view.findViewById(R.id.tagRecipe);
        tv_tagHoneytip = view.findViewById(R.id.tagHoneytip);
        tv_tagHeron = view.findViewById(R.id.tagHeron);
        tv_tagEtc = view.findViewById(R.id.tagEtc);


        tv_cancel = view.findViewById(R.id.cancel);
        tv_complete = view.findViewById(R.id.complete);


        ll_tagBottlesLayout.setOnClickListener(this);
        ll_tagDrinkPersLayout.setOnClickListener(this);
        ll_tagHangoverLayout.setOnClickListener(this);

        ll_tagRecipeLayout.setOnClickListener(this);
        ll_tagHoneytipLayout.setOnClickListener(this);
        ll_tagHeronLayout.setOnClickListener(this);
        ll_tagEtcLayout.setOnClickListener(this);


        tv_cancel.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tagBottlesLayout:
                flagTag1++;

                if (flagTag1 % 2 == 1) {
                    tagArray.add(tv_tagBottles.getText().toString());
                    tv_tagBottles.setTextColor(Color.parseColor("#08883e"));
                    ll_tagBottlesLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tag_frame_green));
                } else {
                    tagArray.remove(tv_tagBottles.getText().toString());
                    tv_tagBottles.setTextColor(Color.parseColor("#9d9d97"));
                    ll_tagBottlesLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tag_frame_gray));
                }
                break;
            case R.id.tagDrinkPersLayout:
                flagTag2++;

                if (flagTag2 % 2 == 1) {
                    tagArray.add(tv_tagDrinkPers.getText().toString());
                    tv_tagDrinkPers.setTextColor(Color.parseColor("#08883e"));
                    ll_tagDrinkPersLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tag_frame_green));
                } else {
                    tagArray.remove(tv_tagDrinkPers.getText().toString());
                    tv_tagDrinkPers.setTextColor(Color.parseColor("#9d9d97"));
                    ll_tagDrinkPersLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tag_frame_gray));
                }
                break;
            case R.id.tagHangoverLayout:
                flagTag3++;

                if (flagTag3 % 2 == 1) {
                    tagArray.add(tv_tagHangover.getText().toString());
                    tv_tagHangover.setTextColor(Color.parseColor("#08883e"));
                    ll_tagHangoverLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tag_frame_green));
                } else {
                    tagArray.remove(tv_tagHangover.getText().toString());
                    tv_tagHangover.setTextColor(Color.parseColor("#9d9d97"));
                    ll_tagHangoverLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tag_frame_gray));
                }
                break;
            case R.id.tagRecipeLayout:
                flagTag4++;

                if (flagTag4 % 2 == 1) {
                    tagArray.add(tv_tagRecipe.getText().toString());
                    tv_tagRecipe.setTextColor(Color.parseColor("#08883e"));
                    ll_tagRecipeLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tag_frame_green));
                } else {
                    tagArray.remove(tv_tagRecipe.getText().toString());
                    tv_tagRecipe.setTextColor(Color.parseColor("#9d9d97"));
                    ll_tagRecipeLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tag_frame_gray));
                }
                break;
            case R.id.tagHoneytipLayout:
                flagTag5++;

                if (flagTag5 % 2 == 1) {
                    tagArray.add(tv_tagHoneytip.getText().toString());
                    tv_tagHoneytip.setTextColor(Color.parseColor("#08883e"));
                    ll_tagHoneytipLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tag_frame_green));
                } else {
                    tagArray.remove(tv_tagHoneytip.getText().toString());
                    tv_tagHoneytip.setTextColor(Color.parseColor("#9d9d97"));
                    ll_tagHoneytipLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tag_frame_gray));
                }
                break;
            case R.id.tagHeronLayout:
                flagTag5++;

                if (flagTag5 % 2 == 1) {
                    tagArray.add(tv_tagHeron.getText().toString());
                    tv_tagHeron.setTextColor(Color.parseColor("#08883e"));
                    ll_tagHeronLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tag_frame_green));
                } else {
                    tagArray.remove(tv_tagHeron.getText().toString());
                    tv_tagHeron.setTextColor(Color.parseColor("#9d9d97"));
                    ll_tagHeronLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tag_frame_gray));
                }
                break;
            case R.id.tagEtcLayout:
                flagTag5++;

                if (flagTag5 % 2 == 1) {
                    tagArray.add(tv_tagEtc.getText().toString());
                    tv_tagEtc.setTextColor(Color.parseColor("#08883e"));
                    ll_tagEtcLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tag_frame_green));
                } else {
                    tagArray.remove(tv_tagEtc.getText().toString());
                    tv_tagEtc.setTextColor(Color.parseColor("#9d9d97"));
                    ll_tagEtcLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.tag_frame_gray));
                }
                break;
            case R.id.complete:
                bottomSheetDialoggListener.onPositiveClicked(tagArray);
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        flagTag1 = 0;
        flagTag2 = 0;
        flagTag3 = 0;
        flagTag4 = 0;
        flagTag5 = 0;
    }
}
