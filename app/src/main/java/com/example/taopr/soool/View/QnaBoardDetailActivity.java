package com.example.taopr.soool.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taopr.soool.Adapter.QnaBoardTagAdapter;
import com.example.taopr.soool.Adapter.QnaBoardVoteAdapter;
import com.example.taopr.soool.Adapter.VoteImageAdapter;
import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaBoardVoteItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.R;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QnaBoardDetailActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "QnaBoardDetailActivity", accountNick;
    int vote;

    TextView tv_qnaboardTag, tv_qnaboardTitle, tv_qnaboardWriter, tv_qnaboardContent, tv_qnaboardDate, tv_qnaboardCommentCount, tv_qnaboardViewCount;
    ImageView iv_qnaboardImage;
    Button btn_qnaboardLike, btn_qnaboardUnLike;
    LinearLayout ll_voteLayout;
    RecyclerView rc_recycler, rc_qnaboardTag;
    GridView gv_gridview;

    QnaBoardItem qnaBoardItem;
    QnaVoteItem qnaVoteItem;
    QnaBoardVoteAdapter qnaBoardVoteAdapter;
    QnaBoardTagAdapter qnaBoardTagAdapter;
    VoteImageAdapter voteImageAdapter;

    ArrayList<QnaBoardVoteItem> editModelArrayList;
    ArrayList<Uri> realPath = new ArrayList<>();
    ArrayList<Integer> number = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qnaboard_detail);

        DoBinding(); // ui 선언 및 presenter 선언, presenter에서 넘어올 응답에 대한 변화 view? 선언까지

        String data = LoginSharedPreferences.LoginUserLoad(this, "LoginAccount");
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);
        accountNick = loginSessionItem.getAccountNick();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);

        qnaBoardItem = (QnaBoardItem) getIntent().getSerializableExtra("QnaBoardItem");
        qnaVoteItem = (QnaVoteItem) getIntent().getSerializableExtra("QnaVoteItem");

        Log.d(TAG, "onCreate: "+qnaBoardItem.getTitle()+qnaBoardItem.getContent()+qnaBoardItem.getTag() +qnaBoardItem.getImage());
        // 값이 넘어왔으므로 이제 뷰들 만들어서 넣어주는 작업하면 될거같다.
        tv_qnaboardTag.setText(qnaBoardItem.getTag());

        /*
        만약 객체에서 tag값을 어레이리스트로 변경한다면
        어댑터 생성시 객체의 tag 어레이값을 넣어줘서 보여지게 작업하면됩니다.
        그 후 이 주석위의 tv_qnaboardTag는 삭제해주면 됩니다.

        qnaBoardTagAdapter = new QnaBoardTagAdapter(this, );
        rc_qnaboardTag.setAdapter(qnaBoardTagAdapter);
        rc_qnaboardTag.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
         */

        tv_qnaboardTitle.setText(qnaBoardItem.getTitle());
//        tv_qnaboardWriter.setText(qnaBoardItem.getWriter());
        tv_qnaboardWriter.setText(accountNick);
        tv_qnaboardContent.setText(qnaBoardItem.getContent());
//        tv_qnaboardDate.setText(qnaBoardItem.getDate());
        tv_qnaboardDate.setText(getTime);
//        tv_qnaboardCommentCount.setText(qnaBoardItem.getComments());
        tv_qnaboardCommentCount.setText("0");
//        tv_qnaboardViewCount.setText(qnaBoardItem.getViews());
        tv_qnaboardViewCount.setText("0");
        File file = new File(qnaBoardItem.getImage());
        Uri test = Uri.fromFile(file);

        Glide.with(this)
                .load(test)
                .override(100,100)
                .centerCrop()
                .into(iv_qnaboardImage);

        // 객체 넘어왔을 경우
        // 1. 게시물의 이미지가 존재하는지 구별
        // 2. 투표가 존재하는지 안하는지 구별.
        // 3. 투표 존재의 경우 이미지 or 텍스트 투표인지 구별.
        // 4. 이 주석 위에 것들을 아래 조건문에 잘 맞게 넣어줘서 보여질수 있게 하면 끝날 것 같다.

        if (!qnaBoardItem.getImage().equals(null)) {
            if (qnaBoardItem.getQnaCate().equals("yes vote")) {
                // 투표 레이아웃 보이도록
                ll_voteLayout.setVisibility(View.VISIBLE);

                if (qnaVoteItem.qnaVoteStatus.equals("text")) {
                    // 투표 텍스트이므로 리싸이클러뷰 보이도록
                    rc_recycler.setVisibility(View.VISIBLE);

                    // 투표가 텍스트 투표일 경우 리싸이클러뷰에 값 넣어주는 부분
                    /*
                    for (int i=0; i<qnaBoardItem.getVoteText().size(); i++) {
                        QnaBoardVoteItem editModel = new QnaBoardVoteItem();
                        editModel.setEditTextValue(qnaBoardItem.getVoteText().get(i));
                        editModelArrayList.add(editModel);
                    }

                    qnaBoardVoteAdapter = new QnaBoardVoteAdapter(this,editModelArrayList);
                    rc_recycler.setAdapter(qnaBoardVoteAdapter);
                    rc_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    */

                }else if (qnaVoteItem.qnaVoteStatus.equals("image")) {
                    // 투표 이미지이므로 그리드뷰 보이도록
                    gv_gridview.setVisibility(View.VISIBLE);

                    // 투표가 이미지 투표일 경우 리싸이클러뷰에 값 넣어주는 부분
                    /*
                    for (int i=0; i<qnaBoardItem.getVoteImage().size(); i++) {
                        File imageFile = new File(qnaBoardItem.getVoteImage().get(i));
                        Uri imageUri = Uri.fromFile(imageFile);

                        realPath.add(imageUri);
                        number.add(i+1);
                    }

                    voteImageAdapter = new VoteImageAdapter(this, realPath, number);
                    gv_gridview.setAdapter(voteImageAdapter);
                    */
                }
            }else if(qnaBoardItem.getQnaCate().equals("no vote")) {

            }
        }else {
            if (qnaBoardItem.getQnaCate().equals("yes vote")) {
                // 투표 레이아웃 보이도록
                ll_voteLayout.setVisibility(View.VISIBLE);

                if (qnaVoteItem.qnaVoteStatus.equals("text")) {
                    // 투표 텍스트이므로 리싸이클러뷰 보이도록
                    rc_recycler.setVisibility(View.VISIBLE);

                    // 투표가 텍스트 투표일 경우 리싸이클러뷰에 값 넣어주는 부분
                    /*
                    for (int i=0; i<qnaBoardItem.getVoteText().size(); i++) {
                        QnaBoardVoteItem editModel = new QnaBoardVoteItem();
                        editModel.setEditTextValue(qnaBoardItem.getVoteText().get(i));
                        editModelArrayList.add(editModel);
                    }

                    qnaBoardVoteAdapter = new QnaBoardVoteAdapter(this,editModelArrayList);
                    rc_recycler.setAdapter(qnaBoardVoteAdapter);
                    rc_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    */

                }else if (qnaVoteItem.qnaVoteStatus.equals("image")) {
                    // 투표 이미지이므로 그리드뷰 보이도록
                    gv_gridview.setVisibility(View.VISIBLE);

                    // 투표가 이미지 투표일 경우 리싸이클러뷰에 값 넣어주는 부분
                    /*
                    for (int i=0; i<qnaBoardItem.getVoteImage().size(); i++) {
                        File imageFile = new File(qnaBoardItem.getVoteImage().get(i));
                        Uri imageUri = Uri.fromFile(imageFile);

                        realPath.add(imageUri);
                        number.add(i+1);
                    }

                    voteImageAdapter = new VoteImageAdapter(this, realPath, number);
                    gv_gridview.setAdapter(voteImageAdapter);
                    */
                }
            }else if(qnaBoardItem.getQnaCate().equals("no vote")) {

            }
        }
    }

    private void DoBinding() {
//        qnaBoardPresenter = new QnaBoardPresenter(this, this);
//        qnaBoardPresenter.setView(this);

        // 뷰들 선언하는 부분입니다.
        tv_qnaboardTag = findViewById(R.id.qnaboardTags);
        tv_qnaboardTitle = findViewById(R.id.qnaboardTitle);
        tv_qnaboardWriter = findViewById(R.id.qnaboardWriter);
        tv_qnaboardContent = findViewById(R.id.qnaboardContent);
        tv_qnaboardDate = findViewById(R.id.qnaboardDate);
        tv_qnaboardCommentCount = findViewById(R.id.qnaboardCommentCount);
        tv_qnaboardViewCount = findViewById(R.id.qnaboardViewCount);
        iv_qnaboardImage = findViewById(R.id.qnaboardImage);
        btn_qnaboardLike = findViewById(R.id.qnaboardLike);
        btn_qnaboardUnLike = findViewById(R.id.qnaboardUnLike);
        ll_voteLayout = findViewById(R.id.voteLayout);
        rc_recycler = findViewById(R.id.recycler);
        gv_gridview = findViewById(R.id.gridview);

        ll_voteLayout.setVisibility(View.GONE);

        // 뷰의 리스너 선언 부분입니다.
        btn_qnaboardLike.setOnClickListener(this);
        btn_qnaboardUnLike.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.qna_detail_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        ImageButton ib_drawupBack = findViewById(R.id.drawupBack);
        TextView tv_drawupTitle = findViewById(R.id.drawupTitle);
        Button btn_drawupReport = findViewById(R.id.drawupReport);

        tv_drawupTitle.setText("Board");
        ib_drawupBack.setOnClickListener(this);
        btn_drawupReport.setOnClickListener(this);

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawupBack:
                break;
            case R.id.drawupEnroll:
                break;
//            case R.id.qnaboardCommentShow:
//                break;
        }
    }
}
