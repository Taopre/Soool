package com.example.taopr.soool.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.taopr.soool.R;

import java.util.ArrayList;


public class TagDialog extends Dialog implements View.OnClickListener{

    private Button positiveButton;
    private Button negativeButton;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private Context context;

    private ArrayList<String> arrayList = new ArrayList<>();

    TagDialogListener tagDialogListener;

    public TagDialog(Context context) {
        super(context);
        this.context = context;
    }


    //인터페이스 설정
    public interface TagDialogListener{
        void onPositiveClicked(ArrayList<String> arrayList);
        void onNegativeClicked();
    }

    //호출할 리스너 초기화
    public void setDialogListener(TagDialogListener tagDialogListener){
        this.tagDialogListener = tagDialogListener;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tag_dialog);

        //init
        positiveButton = findViewById(R.id.btnPositive);
        negativeButton = findViewById(R.id.btnNegative);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);


        //버튼 클릭 리스너 등록
        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPositive: //확인 버튼을 눌렀을 때
                //각각의 변수에 EidtText에서 가져온 값을 저장


                //인터페이스의 함수를 호출하여 변수에 저장된 값들을 Activity로 전달
                tagDialogListener.onPositiveClicked(arrayList);
                dismiss();
                break;
            case R.id.btnNegative: //취소 버튼을 눌렀을 때
                cancel();
                break;
            case R.id.textView1:
                arrayList.add(textView1.getText().toString());
                break;
            case R.id.textView2:
                arrayList.add(textView2.getText().toString());
                break;
            case R.id.textView3:
                arrayList.add(textView3.getText().toString());
                break;
            case R.id.textView4:
                arrayList.add(textView4.getText().toString());
                break;
        }
    }
}

