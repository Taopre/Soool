package com.example.taopr.soool.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.taopr.soool.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeDialog extends Dialog{

    @BindView(R.id.noticeDialogNegative)
    TextView noticeDialogNegative;
    @BindView(R.id.noticeDialogCheck)
    RadioButton noticeDialogCheck;
    @BindView(R.id.noticeDialogContent)
    TextView noticeDialogContent;
    @BindView(R.id.noticeDialogPositive)
    TextView noticeDialogPositive;
    @BindView(R.id.noticeDialogTitle)
    TextView noticeDialogTitle;


    private View.OnClickListener mPositiveListener;
    private View.OnClickListener mNegativeListener;
    private String dialogTitle,dialogContent;
    private String negativeText,positiveText; // 네거티브 버튼 라벨, 포지티브 버튼 라벨
    private boolean isExistCheck = false;  // '다시 보지 않기' 체크박스 유무  false = 무
    private final String TAG = "notice 다이얼로그";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_all_notice);

        ButterKnife.bind(this);
    /*    noticeDialogCheck = findViewById(R.id.noticeDialogCheck);
        noticeDialogNegative = findViewById(R.id.noticeDialogNegative);
        noticeDialogPositive = findViewById(R.id.noticeDialogPositive);
        noticeDialogContent = findViewById(R.id.noticeDialogContent);
        noticeDialogTitle = findViewById(R.id.noticeDialogTitle);*/

        noticeDialogNegative.setOnClickListener(mNegativeListener);
        noticeDialogPositive.setOnClickListener(mPositiveListener);

        // title
        try{
            noticeDialogTitle.setVisibility(View.GONE);
            noticeDialogTitle.setText(dialogTitle);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }

        noticeDialogContent.setText(dialogContent);

        noticeDialogPositive.setText(positiveText);
        noticeDialogNegative.setText(negativeText);

        if (isExistCheck) noticeDialogCheck.setVisibility(View.VISIBLE);
        else noticeDialogCheck.setVisibility(View.GONE);

    }

    public NoticeDialog(@NonNull Context context, String dialogTitle, String dialogContent, boolean isExistCheck, String positiveText,
                        String negativeText, View.OnClickListener positiveListener, View.OnClickListener negativeListener) {
        super(context);
        Log.i(TAG, "NoticeDialog: ");
        this.mPositiveListener = positiveListener;
        this.mNegativeListener = negativeListener;
        this.dialogTitle = dialogTitle;
        this.dialogContent = dialogContent;
        this.isExistCheck = isExistCheck;
        this.positiveText =positiveText;
        this.negativeText = negativeText;
    }
    public NoticeDialog(@NonNull Context context,  String dialogContent, boolean isExistCheck, String positiveText,
                        String negativeText, View.OnClickListener positiveListener, View.OnClickListener negativeListener) {
        super(context);
        Log.i(TAG, "NoticeDialog: ");
        this.mPositiveListener = positiveListener;
        this.mNegativeListener = negativeListener;
        this.dialogContent = dialogContent;
        this.isExistCheck = isExistCheck;
        this.positiveText =positiveText;
        this.negativeText = negativeText;
    }


}
