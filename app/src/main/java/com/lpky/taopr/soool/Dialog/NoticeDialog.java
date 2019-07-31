package com.lpky.taopr.soool.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lpky.taopr.soool.R;

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


    private View.OnClickListener mPositiveListener=null;
    private View.OnClickListener mNegativeListener=null;
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

        // 다이얼로그에 제목이 있는 경우가 있고 없는 경우가 있기 때문에
        // null 값인지 아닌지로 구분하여 Title 에 text 값을 부여한다

        if (noticeDialogTitle != null) {
            noticeDialogTitle.setText(dialogTitle);
        }
        else noticeDialogTitle.setVisibility(View.GONE);

        noticeDialogContent.setText(dialogContent);

        // 확인버튼
        if (mPositiveListener != null) noticeDialogPositive.setText(positiveText);
        else noticeDialogPositive.setVisibility(View.GONE);

        // 취소버튼
        if (mNegativeListener != null)  noticeDialogNegative.setText(negativeText);
        else noticeDialogNegative.setVisibility(View.GONE);


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
    // 제목 없음 , 체크 박스 없음
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

    // 내용만 있음
    public NoticeDialog(@NonNull Context context, String dialogContent,String positiveText,View.OnClickListener positiveListener){
        super(context);
        this.dialogContent = dialogContent;
        this.positiveText = positiveText;
        this.mPositiveListener = positiveListener;
    }

}
