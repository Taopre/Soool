package com.lpky.taopr.soool.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lpky.taopr.soool.R;

public class BottomSheetDialogVoteSelect extends BottomSheetDialogFragment implements View.OnClickListener{

    public static BottomSheetDialogVoteSelect getInstance() { return new BottomSheetDialogVoteSelect(); }

    private ImageView iv_text, iv_image, iv_ExitBtn;
    private TextView tv_text, tv_image;

    public boolean flagVote = false;
    public int voteSelectInDialog = 9999;
    private BottomSheetDialogVoteSelectDialoggListener bottomSheetDialogVoteSelectDialoggListener;

    //인터페이스 설정
    public interface BottomSheetDialogVoteSelectDialoggListener{
        void onSelectVoteStatus(int voteSelectInDialog);
        void noVotesoReturn(boolean flag);
    }

    //호출할 리스너 초기화
    public void setDialogListener(BottomSheetDialogVoteSelectDialoggListener bottomSheetDialogVoteSelectDialoggListener){
        this.bottomSheetDialogVoteSelectDialoggListener = bottomSheetDialogVoteSelectDialoggListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog_vote, container,false);

        iv_text = view.findViewById(R.id.iv_textVote);
        iv_image = view.findViewById(R.id.iv_imageVote);
        tv_text = view.findViewById(R.id.tv_textVote);
        tv_image = view.findViewById(R.id.tv_imageVote);
        iv_ExitBtn = view.findViewById(R.id.iv_Exit);

        iv_text.setOnClickListener(this);
        iv_image.setOnClickListener(this);
        tv_text.setOnClickListener(this);
        tv_image.setOnClickListener(this);
        iv_ExitBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_textVote:
                voteSelectInDialog = 0;
                bottomSheetDialogVoteSelectDialoggListener.onSelectVoteStatus(voteSelectInDialog);
                bottomSheetDialogVoteSelectDialoggListener.noVotesoReturn(false);
                dismiss();
                break;
            case R.id.iv_imageVote:
                voteSelectInDialog = 1;
                bottomSheetDialogVoteSelectDialoggListener.onSelectVoteStatus(voteSelectInDialog);
                bottomSheetDialogVoteSelectDialoggListener.noVotesoReturn(false);
                dismiss();
                break;
            case R.id.tv_textVote:
                voteSelectInDialog = 0;
                bottomSheetDialogVoteSelectDialoggListener.onSelectVoteStatus(voteSelectInDialog);
                bottomSheetDialogVoteSelectDialoggListener.noVotesoReturn(false);
                dismiss();
                break;
            case R.id.tv_imageVote:
                voteSelectInDialog = 1;
                bottomSheetDialogVoteSelectDialoggListener.onSelectVoteStatus(voteSelectInDialog);
                bottomSheetDialogVoteSelectDialoggListener.noVotesoReturn(false);
                dismiss();
                break;
            case R.id.iv_Exit:
                bottomSheetDialogVoteSelectDialoggListener.noVotesoReturn(true);
                dismiss();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

