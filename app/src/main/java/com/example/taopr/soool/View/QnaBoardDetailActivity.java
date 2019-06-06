package com.example.taopr.soool.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.taopr.soool.Adapter.QnaBoardDetailImageAdapter;
import com.example.taopr.soool.Adapter.QnaBoardDetailVoteAdapter;
import com.example.taopr.soool.Adapter.QnaBoardTagAdapter;
import com.example.taopr.soool.Adapter.QnaBoardVoteAdapter;
import com.example.taopr.soool.Adapter.RecyclerItemClickListener;
import com.example.taopr.soool.Adapter.VoteImageAdapter;
import com.example.taopr.soool.Object.BoardRecommend;
import com.example.taopr.soool.Object.GridVoteItem;
import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaBoardVoteItem;
import com.example.taopr.soool.Object.QnaItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Presenter.QnaDetailPresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.example.taopr.soool.Whatisthis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QnaBoardDetailActivity extends AppCompatActivity implements View.OnClickListener,
        QnaBoardDetailImageAdapter.GridviewItemClickListner, QnaDetailPresenter.View {

    String TAG = "QnaBoardDetailActivity", accountNick;
    String[] tagData = new String[0];
    int whatVoteSelect = 9999, btnOnOff = 9999, flagLike = 0, flagUnLike = 0, vote, voteStatus = 4, fromActivity, isSelectedPosition = 9999, isSelectedPositionImage = 9999, accountNo, postNo, voteTotalResult = 0, mySelectVoteNum = 0, actionKind, qnaListPosition;
    boolean isMyBoard = false;

    TextView tv_drawupReport, tv_drawupModify, tv_qnaboardLikeText, tv_qnaboardUnLikeText, tv_qnaboardTitle, tv_qnaboardWriter, tv_qnaboardContent, tv_qnaboardDate, tv_qnaboardCommentCount, tv_qnaboardViewCount, tv_qnaboardTagOne, tv_voteResultShow, tv_qnaboardLike, tv_qnaboardUnLike;
    EditText et_commentWrite;
    ImageView iv_qnaboardImage, iv_drawupBack;
    Button btn_commentEnroll;
    LinearLayout ll_voteLayout;
    RecyclerView rc_recycler, rc_qnaboardTagMany;
    GridView gv_gridview;
    HorizontalScrollView tagView;
    RelativeLayout rl_qnadetailLayout, rl_qnaboardUnLikeLayout, rl_qnaboardLikeLayout, rl_voteFinishLayout;
    private TextView tv_inActSelected = null;
    private ImageView iv_inActSelected = null;
    private ProgressBar pb_inActSelected = null;
    private RadioButton rb_inActSelected = null;
    InputMethodManager inputMethodManager;

    QnaItem qnaItem;
    QnaBoardItem qnaBoardItem;
    QnaBoardDetailVoteAdapter qnaBoardDetailVoteAdapter;
    QnaBoardTagAdapter qnaBoardTagAdapter;
    QnaBoardDetailImageAdapter qnaBoardDetailImageAdapter;
    GridVoteItem gridVoteItem;
    QnaDetailPresenter qnaDetailPresenter;

    ArrayList<QnaBoardVoteItem> editModelArrayList = new ArrayList<>();
    ArrayList<String> tagArray = new ArrayList<>();
    ArrayList<GridVoteItem> gridVoteItemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qnaboard_detail);

        DoBinding(); // ui 선언 및 presenter 선언, presenter에서 넘어올 응답에 대한 변화 view? 선언까지

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        String data = LoginSharedPreferences.LoginUserLoad(this, "LoginAccount");
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);
        accountNick = loginSessionItem.getAccountNick();
        Log.i(TAG, "onCreate: 닉네임" + accountNick);
        accountNo = loginSessionItem.getAccountNo();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);

        vote = getIntent().getIntExtra("vote", 2);
        fromActivity = getIntent().getIntExtra("fromActivity", 9999);
        actionKind = getIntent().getIntExtra("actionKind", 9999);
        qnaListPosition = getIntent().getIntExtra("qnaListPosition", 9999);
        voteStatus = getIntent().getIntExtra("voteStatus", 3);
        qnaItem = (QnaItem) getIntent().getSerializableExtra("qnaItem");
        qnaBoardItem = getIntent().getParcelableExtra("qnaBoardItem");

        if (qnaItem != null) {
            if (qnaItem.getTag().contains("@##@")) {
                tagData = qnaItem.getTag().split("@##@");
                for (int i = 0; i < tagData.length; i++) {
                    tagArray.add(tagData[i]);
                }
            }
            postNo = qnaItem.getPostNo();
        } else if (qnaBoardItem != null) {
            if (qnaBoardItem.getTag().contains("@##@")) {
                tagData = qnaBoardItem.getTag().split("@##@");
                for (int i = 0; i < tagData.length; i++) {
                    tagArray.add(tagData[i]);
                }
            }
            postNo = qnaBoardItem.getPostNo();
        }

        /*
        fromActivity 식별자

        0 -> QnaActivity, MypageActivity, MainActivity
        1 -> QnaBoardActivity
         */

        switch (fromActivity) {
            case 0:
                if (qnaBoardItem != null) {
                    if (accountNo == qnaBoardItem.getAccountNo()) {
                        isMyBoard = true;
                        if (isMyBoard == true) {
                            tv_drawupReport.setVisibility(View.GONE);
                            tv_drawupModify.setVisibility(View.VISIBLE);
                        }
                        else {
                            tv_drawupModify.setVisibility(View.GONE);
                            tv_drawupReport.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        isMyBoard = false;
                        if (isMyBoard == true) {
                            tv_drawupReport.setVisibility(View.GONE);
                            tv_drawupModify.setVisibility(View.VISIBLE);
                        }
                        else {
                            tv_drawupModify.setVisibility(View.GONE);
                            tv_drawupReport.setVisibility(View.VISIBLE);
                        }
                    }

                    if (qnaBoardItem.getQnaCate() == 0) {
                        // 투표 O.
                        ll_voteLayout.setVisibility(View.VISIBLE);
                        tv_voteResultShow.setVisibility(View.VISIBLE);
                        // 투표 항목 뿌려줘야함.
                        qnaDetailPresenter.downloadVoteData(accountNo, qnaBoardItem.getPostNo());

                        if (tagData.length == 0) {
                            tagView.setVisibility(View.GONE);
                            tv_qnaboardTagOne.setVisibility(View.VISIBLE);
                            tv_qnaboardTagOne.setText(qnaBoardItem.getTag());
                        } else if (tagData.length > 0) {
                            qnaBoardTagAdapter = new QnaBoardTagAdapter(this, tagArray, 1);
                            rc_qnaboardTagMany.setAdapter(qnaBoardTagAdapter);
                            rc_qnaboardTagMany.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                        }

                        tv_qnaboardTitle.setText(qnaBoardItem.getTitle());
                        tv_qnaboardWriter.setText(qnaBoardItem.getWriter());
                        tv_qnaboardContent.setText(qnaBoardItem.getContent());
                        tv_qnaboardDate.setText(qnaBoardItem.getDate());
                        tv_qnaboardCommentCount.setText(qnaBoardItem.getComments()+"");
                        tv_qnaboardViewCount.setText(qnaBoardItem.getViews()+"");
                        tv_qnaboardLike.setText(qnaBoardItem.getGoods()+"");
                        tv_qnaboardUnLike.setText(qnaBoardItem.getBads()+"");

                        if (qnaBoardItem.getImage() == null) {
                            iv_qnaboardImage.setVisibility(View.GONE);
                        } else {
                            Log.d(TAG, "onCreate: 이미지?? "+Whatisthis.serverIp+qnaBoardItem.getImage());
                                
                                
                            Glide.with(this)
                                    .load(Whatisthis.serverIp+qnaBoardItem.getImage())
                                    .override(100, 100)
                                    .centerCrop()
                                    .into(iv_qnaboardImage);
                        }
                    } else if (qnaBoardItem.getQnaCate() == 1) {
                        // 투표 X.

                        if (tagData.length == 0) {
                            tagView.setVisibility(View.GONE);
                            tv_qnaboardTagOne.setVisibility(View.VISIBLE);
                            tv_qnaboardTagOne.setText(qnaBoardItem.getTag());
                        } else if (tagData.length > 0) {
                            qnaBoardTagAdapter = new QnaBoardTagAdapter(this, tagArray, 1);
                            rc_qnaboardTagMany.setAdapter(qnaBoardTagAdapter);
                            rc_qnaboardTagMany.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                        }

                        tv_qnaboardTitle.setText(qnaBoardItem.getTitle());
                        tv_qnaboardWriter.setText(qnaBoardItem.getWriter());
                        tv_qnaboardContent.setText(qnaBoardItem.getContent());
                        tv_qnaboardDate.setText(qnaBoardItem.getDate());
                        tv_qnaboardCommentCount.setText(qnaBoardItem.getComments()+"");
                        tv_qnaboardViewCount.setText(qnaBoardItem.getViews()+"");
                        tv_qnaboardLike.setText(qnaBoardItem.getGoods()+"");
                        tv_qnaboardUnLike.setText(qnaBoardItem.getBads()+"");

                        if (qnaBoardItem.getImage() == null) {
                            iv_qnaboardImage.setVisibility(View.GONE);
                        } else {
                            Log.d(TAG, "onCreate: 이미지?? "+Whatisthis.serverIp+qnaBoardItem.getImage());
                            Glide.with(this)
                                    .load(Whatisthis.serverIp+qnaBoardItem.getImage())
                                    .override(100, 100)
                                    .centerCrop()
                                    .into(iv_qnaboardImage);
                        }

                    }
                }
                break;

            case 1:
//                if (qnaItem != null) {
//
//                    if (accountNo == qnaItem.getAccountNo())
//                        isMyBoard = true;
//                    else
//                        isMyBoard = false;
//
//                    if (vote == 0) {
//                        // 투표 O.
//                        ll_voteLayout.setVisibility(View.VISIBLE);
//                        tv_voteResultShow.setVisibility(View.VISIBLE);
//                        if (voteStatus == 0) {
//                            // 텍스트 투표
//                            rc_recycler.setVisibility(View.VISIBLE);
//
//                            if (tagData.length == 0) {
//                                tagView.setVisibility(View.GONE);
//                                tv_qnaboardTagOne.setVisibility(View.VISIBLE);
//                                tv_qnaboardTagOne.setText(qnaItem.getTag());
//                            } else if (tagData.length > 0) {
//                                qnaBoardTagAdapter = new QnaBoardTagAdapter(this, tagArray, 1);
//                                rc_qnaboardTagMany.setAdapter(qnaBoardTagAdapter);
//                                rc_qnaboardTagMany.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
//                            }
//
//                            tv_qnaboardTitle.setText(qnaItem.getTitle());
////        tv_qnaboardWriter.setText(qnaBoardItem.getWriter());
//                            tv_qnaboardWriter.setText(accountNick);
//                            tv_qnaboardContent.setText(qnaItem.getContent());
////        tv_qnaboardDate.setText(qnaBoardItem.getDate());
//                            tv_qnaboardDate.setText(getTime);
////        tv_qnaboardCommentCount.setText(qnaBoardItem.getComments());
//                            tv_qnaboardCommentCount.setText("0");
////        tv_qnaboardViewCount.setText(qnaBoardItem.getViews());
//                            tv_qnaboardViewCount.setText("0");
//
//                            if (qnaItem.getImage() == null) {
//                                iv_qnaboardImage.setVisibility(View.GONE);
//                            } else {
//                                Log.d(TAG, "onCreate: 이미지?? "+Whatisthis.serverIp+qnaItem.getImage());
//                                Glide.with(this)
//                                        .load(Whatisthis.serverIp+qnaItem.getImage())
//                                        .override(100, 100)
//                                        .centerCrop()
//                                        .into(iv_qnaboardImage);
//                            }
//
//                            for (int i = 0; i < qnaItem.getVoteText().size(); i++) {
//                                Log.d(TAG, "디테일 텍스트 어레이: " + qnaItem.getVoteText().get(i));
//                            }
//
//                            tv_voteResultShow.setText(voteTotalResult+"");
//
//                            for (int i = 0; i < qnaItem.getVoteText().size(); i++) {
//                                QnaBoardVoteItem editModel = new QnaBoardVoteItem();
//                                editModel.setEditTextValue(qnaItem.getVoteText().get(i));
//                                editModel.setFlag(false);
//                                editModelArrayList.add(editModel);
//                            }
////
//                            qnaBoardDetailVoteAdapter = new QnaBoardDetailVoteAdapter(this, editModelArrayList);
//                            rc_recycler.setAdapter(qnaBoardDetailVoteAdapter);
//                            rc_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//
//                        } else if (voteStatus == 1) {
//                            // 이미지 투표
//                            gv_gridview.setVisibility(View.VISIBLE);
//
//                            if (tagData.length == 0) {
//                                tagView.setVisibility(View.GONE);
//                                tv_qnaboardTagOne.setVisibility(View.VISIBLE);
//                                tv_qnaboardTagOne.setText(qnaItem.getTag());
//                            } else if (tagData.length > 0) {
//                                qnaBoardTagAdapter = new QnaBoardTagAdapter(this, tagArray, 1);
//                                rc_qnaboardTagMany.setAdapter(qnaBoardTagAdapter);
//                                rc_qnaboardTagMany.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
//                            }
//
//                            tv_qnaboardTitle.setText(qnaItem.getTitle());
////        tv_qnaboardWriter.setText(qnaBoardItem.getWriter());
//                            tv_qnaboardWriter.setText(accountNick);
//                            tv_qnaboardContent.setText(qnaItem.getContent());
////        tv_qnaboardDate.setText(qnaBoardItem.getDate());
//                            tv_qnaboardDate.setText(getTime);
////        tv_qnaboardCommentCount.setText(qnaBoardItem.getComments());
//                            tv_qnaboardCommentCount.setText("0");
////        tv_qnaboardViewCount.setText(qnaBoardItem.getViews());
//                            tv_qnaboardViewCount.setText("0");
//
//                            if (qnaItem.getImage() == null) {
//                                iv_qnaboardImage.setVisibility(View.GONE);
//                            } else {
//                                Log.d(TAG, "onCreate: 이미지?? "+Whatisthis.serverIp+qnaItem.getImage());
//                                Glide.with(this)
//                                        .load(Whatisthis.serverIp+qnaItem.getImage())
//                                        .override(100, 100)
//                                        .centerCrop()
//                                        .into(iv_qnaboardImage);
//                            }
//
//                            Log.d(TAG, "이미지 투표 항목 수: " + qnaItem.getVoteImage().size());
//
//                            tv_voteResultShow.setText(voteTotalResult+"");
//
//                            for (int i = 0; i < qnaItem.getVoteImage().size(); i++) {
//                                File imageFile = new File(qnaItem.getVoteImage().get(i));
//                                Uri uriImage = Uri.fromFile(imageFile);
//                                gridVoteItem = new GridVoteItem("", uriImage, false, false);
//                                gridVoteItemArrayList.add(gridVoteItem);
//                            }
//
//                            qnaBoardDetailImageAdapter = new QnaBoardDetailImageAdapter(this, gridVoteItemArrayList, this);
//                            gv_gridview.setAdapter(qnaBoardDetailImageAdapter);
//
//                        }
//                    } else if (vote == 1) {
//                        // 투표 X.
//                        if (tagData.length == 0) {
//                            tagView.setVisibility(View.GONE);
//                            tv_qnaboardTagOne.setVisibility(View.VISIBLE);
//                            tv_qnaboardTagOne.setText(qnaItem.getTag());
//                        } else if (tagData.length > 0) {
//                            qnaBoardTagAdapter = new QnaBoardTagAdapter(this, tagArray, 1);
//                            rc_qnaboardTagMany.setAdapter(qnaBoardTagAdapter);
//                            rc_qnaboardTagMany.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
//                        }
//
//                        tv_qnaboardTitle.setText(qnaItem.getTitle());
////        tv_qnaboardWriter.setText(qnaBoardItem.getWriter());
//                        tv_qnaboardWriter.setText(accountNick);
//                        tv_qnaboardContent.setText(qnaItem.getContent());
////        tv_qnaboardDate.setText(qnaBoardItem.getDate());
//                        tv_qnaboardDate.setText(getTime);
////        tv_qnaboardCommentCount.setText(qnaBoardItem.getComments());
//                        tv_qnaboardCommentCount.setText("0");
////        tv_qnaboardViewCount.setText(qnaBoardItem.getViews());
//                        tv_qnaboardViewCount.setText("0");
//
//                        if (qnaItem.getImage() == null) {
//                            iv_qnaboardImage.setVisibility(View.GONE);
//                        } else {
//                            Log.d(TAG, "onCreate: 이미지?? "+Whatisthis.serverIp+qnaItem.getImage());
//                            Glide.with(this)
//                                    .load(Whatisthis.serverIp+qnaItem.getImage())
//                                    .override(100, 100)
//                                    .centerCrop()
//                                    .into(iv_qnaboardImage);
//                        }
//
//                    }
//                }
                break;


            case 9999:
                break;
        }

        // 객체 넘어왔을 경우
        // 1. 게시물의 이미지가 존재하는지 구별
        // 2. 투표가 존재하는지 안하는지 구별.
        // 3. 투표 존재의 경우 이미지 or 텍스트 투표인지 구별.
        // 4. 이 주석 위에 것들을 아래 조건문에 잘 맞게 넣어줘서 보여질수 있게 하면 끝날 것 같다.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flagLike = 0;
        flagUnLike = 0;
    }

    private void DoBinding() {
        qnaDetailPresenter = new QnaDetailPresenter(this);
        qnaDetailPresenter.setView(this);

        // 뷰들 선언하는 부분입니다.
        tv_qnaboardTitle = findViewById(R.id.qnaboardTitle);
        tv_qnaboardWriter = findViewById(R.id.qnaboardWriter);
        tv_qnaboardContent = findViewById(R.id.qnaboardContent);
        tv_qnaboardDate = findViewById(R.id.qnaboardDate);
        tv_qnaboardCommentCount = findViewById(R.id.qnaboardCommentCount);
        tv_qnaboardViewCount = findViewById(R.id.qnaboardViewCount);
        iv_qnaboardImage = findViewById(R.id.qnaboardImage);
        tv_qnaboardLike = findViewById(R.id.qnaboardLike);
        tv_qnaboardUnLike = findViewById(R.id.qnaboardUnLike);
        ll_voteLayout = findViewById(R.id.voteLayout);
        rc_recycler = findViewById(R.id.recycler);
        gv_gridview = findViewById(R.id.gridview);
        rc_qnaboardTagMany = findViewById(R.id.qnaboardTagMany);
        tv_qnaboardTagOne = findViewById(R.id.qnaboardTagOne);
        tagView = findViewById(R.id.tagView);
        tv_voteResultShow = findViewById(R.id.voteResultShow);
        et_commentWrite = findViewById(R.id.commentWrite);
        btn_commentEnroll = findViewById(R.id.commentEnroll);
        rl_qnadetailLayout = findViewById(R.id.qnadetailLayout);
        rl_qnaboardUnLikeLayout = findViewById(R.id.qnaboardUnLikeLayout);
        rl_qnaboardLikeLayout = findViewById(R.id.qnaboardLikeLayout);
        tv_qnaboardLikeText = findViewById(R.id.qnaboardLikeText);
        tv_qnaboardUnLikeText = findViewById(R.id.qnaboardUnLikeText);
        rl_voteFinishLayout = findViewById(R.id.voteFinishLayout);
        iv_drawupBack = findViewById(R.id.drawupBack);
        tv_drawupReport = findViewById(R.id.drawupReport);
        tv_drawupModify = findViewById(R.id.drawupModify);

        ll_voteLayout.setVisibility(View.GONE);

        // 뷰의 리스너 선언 부분입니다.
        btn_commentEnroll.setOnClickListener(this);
        rl_qnadetailLayout.setOnClickListener(this);
        rl_qnaboardUnLikeLayout.setOnClickListener(this);
        rl_qnaboardLikeLayout.setOnClickListener(this);
        rl_voteFinishLayout.setOnClickListener(this);
        iv_drawupBack.setOnClickListener(this);
        tv_drawupReport.setOnClickListener(this);
        tv_drawupModify.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qnaboardLikeLayout:
                flagLike++;

                // 추천 클릭시 색깔 변함 준 부분
                if (flagLike % 2 == 1) {
                    btnOnOff = 1;
                    tv_qnaboardLike.setTextColor(Color.parseColor("#08883e"));
                    tv_qnaboardLikeText.setTextColor(Color.parseColor("#08883e"));
//                    rl_qnaboardUnLikeLayout.setClickable(false);
                    tv_qnaboardLike.setText((qnaBoardItem.getGoods()+1)+"");
                    tv_qnaboardUnLike.setText(qnaBoardItem.getBads()+"");
                } else {
                    btnOnOff = 0;
                    tv_qnaboardLike.setTextColor(Color.parseColor("#9d9d97"));
                    tv_qnaboardLikeText.setTextColor(Color.parseColor("#9d9d97"));
//                    rl_qnaboardUnLikeLayout.setClickable(true);
                    tv_qnaboardLike.setText(qnaBoardItem.getGoods()+"");
                    tv_qnaboardUnLike.setText(qnaBoardItem.getBads()+"");
                }

                Toast.makeText(view.getContext(), "추천 상태 : "+btnOnOff+"", Toast.LENGTH_SHORT).show();

                // 이 게시물의 추천 수를 올리기 위한 통신을 구현해야함. 보내야 할 값 아마도 게시물 번호, 회원 번호 정도?

                qnaDetailPresenter.recommendOnOffReq(accountNo, postNo, 0, btnOnOff);
                break;
            case R.id.qnaboardUnLikeLayout:
                flagUnLike++;
                // 비추천 클릭시 색깔 변함 준 부분
                if (flagUnLike % 2 == 1) {
                    btnOnOff = 1;
                    tv_qnaboardUnLike.setTextColor(Color.parseColor("#08883e"));
                    tv_qnaboardUnLikeText.setTextColor(Color.parseColor("#08883e"));
//                    rl_qnaboardLikeLayout.setClickable(false);
                    tv_qnaboardLike.setText(qnaBoardItem.getGoods()+"");
                    tv_qnaboardUnLike.setText((qnaBoardItem.getBads()+1)+"");
                } else {
                    btnOnOff = 0;
                    tv_qnaboardUnLike.setTextColor(Color.parseColor("#9d9d97"));
                    tv_qnaboardUnLikeText.setTextColor(Color.parseColor("#9d9d97"));
//                    rl_qnaboardLikeLayout.setClickable(true);
                    tv_qnaboardLike.setText(qnaBoardItem.getGoods()+"");
                    tv_qnaboardUnLike.setText(qnaBoardItem.getBads()+"");
                }

                Toast.makeText(view.getContext(), "비추천 상태 : "+btnOnOff+"", Toast.LENGTH_SHORT).show();

                // 이 게시물의 비추천 수를 올리기 위한 통신을 구현해야함. 보내야 할 값 아마도 게시물 번호, 회원 번호 정도?

                qnaDetailPresenter.recommendOnOffReq(accountNo, postNo, 1, btnOnOff);
                break;
            case R.id.qnadetailLayout:
                hideKeyboard();
                break;
            case R.id.drawupBack:
                finish();
                break;
            case R.id.drawupEnroll:
                break;
            case R.id.voteFinishLayout:
                // 투표가 뭐냐 에 따라 달라져야한다. 0 -> 텍스트  1 -> 이미지
                switch (whatVoteSelect) {
                    case 0:
                        qnaBoardDetailVoteAdapter.voteFlag = true;
                        qnaBoardDetailVoteAdapter.notifyDataSetChanged();

                        for (int i=0; i<editModelArrayList.size(); i++) {
                            Log.d(TAG, "플레그: "+editModelArrayList.get(i).isFlag());
                            if (editModelArrayList.get(i).isFlag() == true) {
                                editModelArrayList.get(i).setVoteboard(editModelArrayList.get(i).getVoteboard()+1);
                                editModelArrayList.get(i).setFlag(false);

                                Log.d(TAG, "몇번째 :"+i);
                                mySelectVoteNum = i+1;
                                voteTotalResult += 1;
                            }
                        }
                        qnaBoardDetailVoteAdapter.userSelectPos = mySelectVoteNum;
                        qnaBoardDetailVoteAdapter.voteTotalNums = voteTotalResult;
                        tv_voteResultShow.setText(voteTotalResult+"");
                        qnaBoardDetailVoteAdapter.notifyDataSetChanged();

                        if (fromActivity == 0) {
                            qnaDetailPresenter.updateVoteResult(accountNo, qnaBoardItem.getPostNo(), mySelectVoteNum);
                        } else if (fromActivity == 1) {
                            qnaDetailPresenter.updateVoteResult(accountNo, qnaItem.getPostNo(), mySelectVoteNum);
                        }

                        rl_voteFinishLayout.setVisibility(View.GONE);
                        whatVoteSelect = 9999;
                        qnaBoardDetailVoteAdapter.alreadyVote = true;
                        qnaBoardDetailVoteAdapter.notifyDataSetChanged();

                        break;
                    case 1:
                        qnaBoardDetailImageAdapter.voteFlag = true;
                        qnaBoardDetailImageAdapter.notifyDataSetChanged();
                        for (int i=0; i<gridVoteItemArrayList.size(); i++) {
                            if (gridVoteItemArrayList.get(i).isSelected() == true) {
                                gridVoteItemArrayList.get(i).setVote(gridVoteItemArrayList.get(i).getVote()+1);
                                gridVoteItemArrayList.get(i).setSelected(false);

                                Log.d(TAG, "몇번째 :"+i);
                                mySelectVoteNum = i+1;
                                voteTotalResult += 1;
                            }
                        }
                        qnaBoardDetailImageAdapter.voteTotalNum = voteTotalResult;
                        tv_voteResultShow.setText(voteTotalResult+"");
                        qnaBoardDetailImageAdapter.notifyDataSetChanged();

                        if (fromActivity == 0) {
                            qnaDetailPresenter.updateVoteResult(accountNo, qnaBoardItem.getPostNo(), mySelectVoteNum);
                        } else if (fromActivity == 1) {
                            qnaDetailPresenter.updateVoteResult(accountNo, qnaItem.getPostNo(), mySelectVoteNum);
                        }

                        rl_voteFinishLayout.setVisibility(View.GONE);
                        whatVoteSelect = 9999;

                        qnaBoardDetailImageAdapter.alreadyVote = true;
                        qnaBoardDetailImageAdapter.notifyDataSetChanged();
                        break;
                }


                break;
            case R.id.drawupModify:
                Intent intent = new Intent(this, QnaBoardActivity.class);
                intent.putExtra("qnaBoardItem", qnaBoardItem);
                intent.putExtra("actionKind", actionKind);
                intent.putExtra("qnaListPosition", qnaListPosition);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
                finish();
                break;
            case R.id.commentEnroll:
                break;
        }
    }

    private void hideKeyboard()
    {
        inputMethodManager.hideSoftInputFromWindow(et_commentWrite.getWindowToken(), 0);
    }

    @Override
    public void onListImageClick(int position) {
        Toast.makeText(this, position+"", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onListRadioClick(int position, View view) {
        Toast.makeText(view.getContext(), position+"", Toast.LENGTH_LONG).show();

        RadioButton radioButton = view.findViewById(R.id.radio);

        if (position != isSelectedPositionImage) {
            if(isSelectedPositionImage != 9999){
                gridVoteItemArrayList.get(isSelectedPositionImage).setSelected(false);
                rb_inActSelected.setChecked(false);
            }
            isSelectedPositionImage = position;
            radioButton.setChecked(true);
            gridVoteItemArrayList.get(isSelectedPositionImage).setSelected(true);

            rb_inActSelected = radioButton;
        }

        rl_voteFinishLayout.setVisibility(View.VISIBLE);
        whatVoteSelect = 1;

        qnaBoardDetailImageAdapter.notifyDataSetChanged();

        for (int i=0; i<gridVoteItemArrayList.size(); i++) {
            Log.d(TAG, "투표 현황: "+i+" "+gridVoteItemArrayList.get(i).isSelected());
        }


    }

    @Override
    public void getDataSuccess(QnaVoteItem getQnaVoteItem) {
        if (getQnaVoteItem.getQnaVoteStatus() == 0) {
            rc_recycler.setVisibility(View.VISIBLE);

            Log.d(TAG, "getDataSuccess: 투표했니? "+getQnaVoteItem.getMemberIsVoted());

            if (getQnaVoteItem.getMemberIsVoted() == 0) {
                for (int i = 0; i < getQnaVoteItem.getVoteText().size(); i++) {
                    QnaBoardVoteItem editModel = new QnaBoardVoteItem();
                    editModel.setEditTextValue(getQnaVoteItem.getVoteText().get(i));
                    editModel.setFlag(false);
                    editModelArrayList.add(editModel);
                }

                qnaBoardDetailVoteAdapter = new QnaBoardDetailVoteAdapter(this, editModelArrayList, new QnaBoardDetailVoteAdapter.ClickListener() {
                    @Override
                    public void onListVoteListClick(int position, View view) {
                        Toast.makeText(view.getContext(), position + "", Toast.LENGTH_SHORT).show();
                        TextView textView = view.findViewById(R.id.textViewssss);
                        ImageView imageView = view.findViewById(R.id.textSelect);

                        if (position != isSelectedPosition) {
                            if (isSelectedPosition != 9999) {
                                editModelArrayList.get(isSelectedPosition).setFlag(false);
                                tv_inActSelected.setTextColor(Color.parseColor("#9d9d97"));
                                iv_inActSelected.setVisibility(View.INVISIBLE);
                            }
                            isSelectedPosition = position;
                            textView.setTextColor(Color.parseColor("#08883e"));
                            imageView.setVisibility(View.VISIBLE);
                            editModelArrayList.get(isSelectedPosition).setFlag(true);

                            tv_inActSelected = textView;
                            iv_inActSelected = imageView;
                        }

                        rl_voteFinishLayout.setVisibility(View.VISIBLE);
                        whatVoteSelect = 0;

                        for (int i = 0; i < editModelArrayList.size(); i++) {
                            Log.d("어뎁터", "플레그: " + i + "  " + editModelArrayList.get(i).isFlag());
                        }
                    }
                });
                rc_recycler.setAdapter(qnaBoardDetailVoteAdapter);
                rc_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                Log.d(TAG, "getDataSuccess: text total : "+getQnaVoteItem.getTotalVoteCount());
                voteTotalResult = getQnaVoteItem.getTotalVoteCount();
                qnaBoardDetailVoteAdapter.voteTotalNums = voteTotalResult;
                qnaBoardDetailVoteAdapter.notifyDataSetChanged();

                tv_voteResultShow.setText(voteTotalResult + "");
            } else {
                for (int i = 0; i < getQnaVoteItem.getVoteText().size(); i++) {
                    QnaBoardVoteItem editModel = new QnaBoardVoteItem();
                    editModel.setEditTextValue(getQnaVoteItem.getVoteText().get(i));
//                    editModel.setFlag(true);
                    editModel.setVoteboard(getQnaVoteItem.getVoteResult().get(i));
                    editModelArrayList.add(editModel);
                }

                qnaBoardDetailVoteAdapter = new QnaBoardDetailVoteAdapter(this, editModelArrayList, new QnaBoardDetailVoteAdapter.ClickListener() {
                    @Override
                    public void onListVoteListClick(int position, View view) {
                        Toast.makeText(view.getContext(), position + "", Toast.LENGTH_SHORT).show();
                        TextView textView = view.findViewById(R.id.textViewssss);
                        ImageView imageView = view.findViewById(R.id.textSelect);

                        if (position != isSelectedPosition) {
                            if (isSelectedPosition != 9999) {
                                editModelArrayList.get(isSelectedPosition).setFlag(false);
                                tv_inActSelected.setTextColor(Color.parseColor("#9d9d97"));
                                iv_inActSelected.setVisibility(View.INVISIBLE);
                            }
                            isSelectedPosition = position;
                            textView.setTextColor(Color.parseColor("#08883e"));
                            imageView.setVisibility(View.VISIBLE);
                            editModelArrayList.get(isSelectedPosition).setFlag(true);

                            tv_inActSelected = textView;
                            iv_inActSelected = imageView;
                        }

                        rl_voteFinishLayout.setVisibility(View.VISIBLE);
                        whatVoteSelect = 0;

                        for (int i = 0; i < editModelArrayList.size(); i++) {
                            Log.d("어뎁터", "플레그: " + i + "  " + editModelArrayList.get(i).isFlag());
                        }
                    }
                });
                rc_recycler.setAdapter(qnaBoardDetailVoteAdapter);
                rc_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                qnaBoardDetailVoteAdapter.userSelectPos = getQnaVoteItem.getMemberIsVoted();
                qnaBoardDetailVoteAdapter.voteFlag = true;
                qnaBoardDetailVoteAdapter.notifyDataSetChanged();

                qnaBoardDetailVoteAdapter.alreadyVote = true;
                qnaBoardDetailVoteAdapter.notifyDataSetChanged();
                Log.d(TAG, "getDataSuccess: 투표했지?"+ qnaBoardDetailVoteAdapter.alreadyVote);

                Log.d(TAG, "getDataSuccess: text total : "+getQnaVoteItem.getTotalVoteCount());
                voteTotalResult = getQnaVoteItem.getTotalVoteCount();
                qnaBoardDetailVoteAdapter.voteTotalNums = voteTotalResult;
                qnaBoardDetailVoteAdapter.notifyDataSetChanged();

                tv_voteResultShow.setText(voteTotalResult + "");
            }
        } else {
            gv_gridview.setVisibility(View.VISIBLE);

            if (getQnaVoteItem.getMemberIsVoted() == 0) {
                for (int i = 0; i < getQnaVoteItem.getVoteImage().size(); i++) {
                    gridVoteItem = new GridVoteItem(
                            "", getQnaVoteItem.getVoteImage().get(i), false, true);
                    gridVoteItemArrayList.add(gridVoteItem);
                }

                qnaBoardDetailImageAdapter = new QnaBoardDetailImageAdapter(this, gridVoteItemArrayList, this);
                gv_gridview.setAdapter(qnaBoardDetailImageAdapter);

                qnaBoardDetailImageAdapter.totalImageNum = getQnaVoteItem.getVoteImage().size();
                voteTotalResult = getQnaVoteItem.getTotalVoteCount();
                Log.d(TAG, "getDataSuccess: total in act "+voteTotalResult+"");
                qnaBoardDetailImageAdapter.voteTotalNum = voteTotalResult;
                qnaBoardDetailImageAdapter.notifyDataSetChanged();

                tv_voteResultShow.setText(voteTotalResult+"");
            } else {
                for (int i = 0; i < getQnaVoteItem.getVoteImage().size(); i++) {
                    gridVoteItem = new GridVoteItem(
                            "", getQnaVoteItem.getVoteImage().get(i), getQnaVoteItem.getVoteResult().get(i), true);
                    gridVoteItemArrayList.add(gridVoteItem);
                }

                qnaBoardDetailImageAdapter = new QnaBoardDetailImageAdapter(this, gridVoteItemArrayList, this);
                gv_gridview.setAdapter(qnaBoardDetailImageAdapter);

                qnaBoardDetailImageAdapter.totalImageNum = getQnaVoteItem.getVoteImage().size();

                qnaBoardDetailImageAdapter.userSelectPos = getQnaVoteItem.getMemberIsVoted();

                qnaBoardDetailImageAdapter.voteFlag = true;
                qnaBoardDetailImageAdapter.notifyDataSetChanged();

                qnaBoardDetailImageAdapter.alreadyVote = true;
                qnaBoardDetailImageAdapter.notifyDataSetChanged();

                voteTotalResult = getQnaVoteItem.getTotalVoteCount();
                Log.d(TAG, "getDataSuccess: total in act "+voteTotalResult+"");
                qnaBoardDetailImageAdapter.voteTotalNum = voteTotalResult;
                qnaBoardDetailImageAdapter.notifyDataSetChanged();

                tv_voteResultShow.setText(voteTotalResult+"");

            }
        }
    }

    @Override
    public void getDataFail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void recommendComplete(boolean response, BoardRecommend boardRecommend) {
        if (response) {
            qnaBoardItem.goods = boardRecommend.getLikeCount();
            qnaBoardItem.bads = boardRecommend.getBadCount();
            Log.d(TAG, "recommendComplete: 추천 결과 제대로왔다. "+qnaBoardItem.getGoods() +"//"+ qnaBoardItem.getBads());
        } else {

        }
    }

    @Override
    public void updateVoteResultComplete(boolean flag, QnaVoteItem updateQnaVoteItem) {
        if (flag) {

        } else {

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
