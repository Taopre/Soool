package com.example.taopr.soool.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.taopr.soool.R;
import com.example.taopr.soool.View.MyPageFragment.MyBoardFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TagActivity extends AppCompatActivity {

    @BindView(R.id.btnOk)
    Button btnOk;
    @BindView(R.id.btnCancle)
    Button btnCancle;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView4)
    TextView textView4;

    private ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnOk,R.id.btnCancle,R.id.textView1,R.id.textView2,R.id.textView3,R.id.textView4})
    void clickMethod(View view){
        switch (view.getId()) {
            case R.id.btnOk:
                Intent intent = new Intent(this, QnaBoardActivity.class);
                intent.putExtra("tagList", arrayList);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                // 이 엑티비티 선택후에 task에서 없애줘야하는데 현재는 살아있음.....
                startActivity(intent);
                break;

            case R.id.btnCancle:
                startActivity(new Intent(this, QnaBoardActivity.class));
//                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                finish();
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
