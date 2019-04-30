package com.example.taopr.soool.View;

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
    }

    @OnClick({R.id.btnOk,R.id.btnCancle,R.id.textView1,R.id.textView2,R.id.textView3,R.id.textView4})
    void clickMethod(View view){
        switch (view.getId()) {
            case R.id.btnOk:

                break;

            case R.id.tabBookmark:

                break;

            case R.id.tabCalendar:

                break;

            case R.id.myPageDrawerButton:

                break;
        }
    }
}
