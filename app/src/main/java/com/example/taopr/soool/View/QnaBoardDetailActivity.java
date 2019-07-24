package com.example.taopr.soool.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taopr.soool.Adapter.CommentAdapter;
import com.example.taopr.soool.Adapter.QnaBoardDetailImageAdapter;
import com.example.taopr.soool.Adapter.QnaBoardDetailVoteAdapter;
import com.example.taopr.soool.Adapter.QnaBoardTagAdapter;


import com.example.taopr.soool.Decorater.RecyclerDecoration;
import com.example.taopr.soool.Object.BoardRecommend;
import com.example.taopr.soool.Object.CommentItem;
import com.example.taopr.soool.Object.GridVoteItem;
import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaBoardVoteItem;
import com.example.taopr.soool.Object.QnaItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Object.RecommentItem;
import com.example.taopr.soool.Presenter.CommentPresenter;
import com.example.taopr.soool.Presenter.QnaDetailPresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.example.taopr.soool.Util.Keyboard;
import com.example.taopr.soool.Util.Whatisthis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.ArrayList;

public class QnaBoardDetailActivity extends AppCompatActivity implements View.OnClickListener,
        QnaBoardDetailImageAdapter.GridviewItemClickListner, QnaDetailPresenter.View, CommentPresenter.View{

    String TAG = "QnaBoardDetailActivity", accountNick;
    String[] tagData = new String[0];
    int whatVoteSelect = 9999, likeBtnOnOff = 9999, unLikeBtnOnOff = 9999, flagLike = 0, flagUnLike = 0, vote, voteStatus = 4, fromActivity, isSelectedPosition = 9999, isSelectedPositionImage = 9999, accountNo, postNo, voteTotalResult = 0, mySelectVoteNum = 0, actionKind, qnaListPosition;
    boolean isMyBoard = false;

    TextView tv_drawupReport, tv_drawupModify, tv_qnaboardLikeText, tv_qnaboardUnLikeText, tv_qnaboardTitle, tv_qnaboardWriter, tv_qnaboardContent, tv_qnaboardDate, tv_qnaboardCommentCount, tv_qnaboardViewCount, tv_qnaboardTagOne, tv_voteResultShow, tv_qnaboardLike, tv_qnaboardUnLike;
    EditText et_commentWrite;
    ImageView iv_qnaboardImage, iv_drawupBack;
    Button btn_commentEnroll;
    LinearLayout ll_voteLayout;
    RecyclerView rc_recycler, rc_qnaboardTagMany;
    GridView gv_gridview;
    HorizontalScrollView tagView;
    RelativeLayout rl_qnadetailLayout, rl_qnaboardUnLikeLayout, rl_qnaboardLikeLayout, rl_voteFinishLayout, rl_drawupBackLayout, rl_drawupTextLayout;
    ProgressBar pb_qnaBoardDetailProgress;
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
    Keyboard keyboard;

    ArrayList<QnaBoardVoteItem> editModelArrayList = new ArrayList<>();
    ArrayList<String> tagArray = new ArrayList<>();
    ArrayList<GridVoteItem> gridVoteItemArrayList = new ArrayList<>();


    //댓글 부분
    //댓글 입력 버튼 = btn_commentEnroll
    //댓글 내용 입력 에딧텍스트 = et_commentWrite
    //댓글 개수 텍스트뷰 = tv_qnaboardCommentCount
    //RecyclerView
    RecyclerView commentList;
    private LinearLayoutManager linearLayoutManager;
    private CommentAdapter commentAdapter;
    private ArrayList<CommentItem> commentitem = new ArrayList<>();
    private int Get_commentNo;
    CommentPresenter commentPresenter;
    String TextAddWriter;
    private boolean commentBoolean = false;
    private boolean  recommendResponse = false;
    int comment_position;
    //
    ScrollView qnaboard_detail_scrollView;
    LinearLayout comment_layout_top;
    boolean commentDeleteRecommentDelete = false;
    RelativeLayout noComment_notice;
    RelativeLayout infoCommentContainer;
    View divideFrame;
    TextView noComment_notice_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qnaboard_detail);

        DoBinding(); // ui 선언 및 presenter 선언, presenter에서 넘어올 응답에 대한 변화 view? 선언까지

        keyboard = new Keyboard(this);
        keyboard.hideKeyboard(et_commentWrite);

        String data = LoginSharedPreferences.LoginUserLoad(this, "LoginAccount");
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);
        accountNick = loginSessionItem.getAccountNick();
        accountNo = loginSessionItem.getAccountNo();

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

                    // isLike / isBad 값은 0 아니면 1.

                    flagLike = qnaBoardItem.getIsLike();
                    flagUnLike = qnaBoardItem.getIsBad();
                    likeBtnOnOff = qnaBoardItem.getIsLike();
                    unLikeBtnOnOff = qnaBoardItem.getIsBad();

                    // 추천 클릭시 색깔 변함 준 부분
                    if (flagLike % 2 == 1) {
                        tv_qnaboardLike.setTextColor(Color.parseColor("#08883e"));
                        tv_qnaboardLikeText.setTextColor(Color.parseColor("#08883e"));
                        tv_qnaboardLike.setText(qnaBoardItem.getGoods()+"");
                        tv_qnaboardUnLike.setText(qnaBoardItem.getBads()+"");
                    } else {
                        tv_qnaboardLike.setTextColor(Color.parseColor("#9d9d97"));
                        tv_qnaboardLikeText.setTextColor(Color.parseColor("#9d9d97"));
                        tv_qnaboardLike.setText(qnaBoardItem.getGoods()+"");
                        tv_qnaboardUnLike.setText(qnaBoardItem.getBads()+"");
                    }

                    // 비추천 클릭시 색깔 변함 준 부분
                    if (flagUnLike % 2 == 1) {
                        tv_qnaboardUnLike.setTextColor(Color.parseColor("#08883e"));
                        tv_qnaboardUnLikeText.setTextColor(Color.parseColor("#08883e"));
                        tv_qnaboardLike.setText(qnaBoardItem.getGoods()+"");
                        tv_qnaboardUnLike.setText(qnaBoardItem.getBads()+"");
                    } else {
                        tv_qnaboardUnLike.setTextColor(Color.parseColor("#9d9d97"));
                        tv_qnaboardUnLikeText.setTextColor(Color.parseColor("#9d9d97"));
                        tv_qnaboardLike.setText(qnaBoardItem.getGoods()+"");
                        tv_qnaboardUnLike.setText(qnaBoardItem.getBads()+"");
                    }

                    if (qnaBoardItem.getQnaCate() == 0) {
                        // 투표 O.
                        ll_voteLayout.setVisibility(View.VISIBLE);
                        tv_voteResultShow.setVisibility(View.VISIBLE);
                        // 투표 항목 뿌려줘야함.
                        qnaDetailPresenter.downloadVoteData(accountNo, qnaBoardItem.getPostNo());
                        showLoading();
                        // 로딩시 클릭 다 막자 -> 수정하기 뒤로가기 추천 비추천 등록 댓글입력공간

                        if (tagData.length == 0) {
                            tagView.setVisibility(View.GONE);
                            tv_qnaboardTagOne.setVisibility(View.VISIBLE);
                            tv_qnaboardTagOne.setText(qnaBoardItem.getTag());
                        } else if (tagData.length > 0) {
                            tv_qnaboardTagOne.setVisibility(View.GONE);
                            tagView.setVisibility(View.VISIBLE);
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

                                
                            Glide.with(this)
                                    .load(Whatisthis.serverIp+qnaBoardItem.getImage())
                                    .override(100, 100)
                                    .centerCrop()
                                    .into(iv_qnaboardImage);
                        }
                    } else if (qnaBoardItem.getQnaCate() == 1) {
                        // 투표 X.
                        hideLoading();
                        if (tagData.length == 0) {
                            tagView.setVisibility(View.GONE);
                            tv_qnaboardTagOne.setVisibility(View.VISIBLE);
                            tv_qnaboardTagOne.setText(qnaBoardItem.getTag());
                        } else if (tagData.length > 0) {
                            tv_qnaboardTagOne.setVisibility(View.GONE);
                            tagView.setVisibility(View.VISIBLE);
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
                            Glide.with(this)
                                    .load(Whatisthis.serverIp+qnaBoardItem.getImage())
                                    .override(100, 100)
                                    .centerCrop()
                                    .into(iv_qnaboardImage);
                        }

                    }
                }
                break;

            case 9999:
                break;
        }

        // 객체 넘어왔을 경우
        // 1. 게시물의 이미지가 존재하는지 구별
        // 2. 투표가 존재하는지 안하는지 구별.
        // 3. 투표 존재의 경우 이미지 or 텍스트 투표인지 구별.
        // 4. 이 주석 위에 것들을 아래 조건문에 잘 맞게 넣어줘서 보여질수 있게 하면 끝날 것 같다.



        commentList = (RecyclerView) findViewById(R.id.commentList);
        linearLayoutManager = new LinearLayoutManager(this)
        {
            @Override
            public  boolean canScrollVertically()
            {
                return false;
            }
        };

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        commentList.setLayoutManager(linearLayoutManager);


        commentPresenter = new CommentPresenter(this,this);
        commentPresenter.setView(this);
        commentPresenter.loadData(qnaBoardItem.getPostNo());
        commentList.setAdapter(commentAdapter);
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
        rc_qnaboardTagMany.addItemDecoration(new RecyclerDecoration(16));
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
        pb_qnaBoardDetailProgress = findViewById(R.id.qnaBoardDetailProgress);
        rl_drawupBackLayout = findViewById(R.id.drawupBackLayout);
        rl_drawupTextLayout = findViewById(R.id.drawupTextLayout);

        ll_voteLayout.setVisibility(View.GONE);

        //댓글관련
        commentList = findViewById(R.id.commentList);
        qnaboard_detail_scrollView = findViewById(R.id.qnaboard_detail_scrollView);
        comment_layout_top = findViewById(R.id.comment_layout_top);
        noComment_notice = findViewById(R.id.noComment_notice);
        infoCommentContainer = findViewById(R.id.infoCommentContainer);
        divideFrame = findViewById(R.id.divideFrame);
        noComment_notice_text = findViewById(R.id.noComment_notice_text);


        // 뷰의 리스너 선언 부분입니다.
        btn_commentEnroll.setOnClickListener(this);
        rl_qnadetailLayout.setOnClickListener(this);
        rl_qnaboardUnLikeLayout.setOnClickListener(this);
        rl_qnaboardLikeLayout.setOnClickListener(this);
        rl_voteFinishLayout.setOnClickListener(this);
        rl_drawupBackLayout.setOnClickListener(this);
        tv_drawupReport.setOnClickListener(this);
        rl_drawupTextLayout.setOnClickListener(this);
    }

    public void showLoading() {
        pb_qnaBoardDetailProgress.setVisibility(View.VISIBLE);
        rl_drawupBackLayout.setClickable(false);
        rl_drawupTextLayout.setClickable(false);
        rl_qnaboardLikeLayout.setClickable(false);
        rl_qnaboardUnLikeLayout.setClickable(false);
        btn_commentEnroll.setClickable(false);
        et_commentWrite.setClickable(false);
    }

    public void hideLoading() {
        pb_qnaBoardDetailProgress.setVisibility(View.GONE);
        rl_drawupBackLayout.setClickable(true);
        rl_drawupTextLayout.setClickable(true);
        rl_qnaboardLikeLayout.setClickable(true);
        rl_qnaboardUnLikeLayout.setClickable(true);
        btn_commentEnroll.setClickable(true);
        et_commentWrite.setClickable(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qnaboardLikeLayout:
                flagLike++;
                // 추천 클릭시 색깔 변함 준 부분
                if (flagLike % 2 == 1) {
                    if (flagUnLike % 2 == 1) {
                        flagUnLike = 0;
                        unLikeBtnOnOff = 0;
                        tv_qnaboardUnLike.setTextColor(Color.parseColor("#9d9d97"));
                        tv_qnaboardUnLikeText.setTextColor(Color.parseColor("#9d9d97"));
                        tv_qnaboardUnLike.setText((qnaBoardItem.getBads()-1)+"");
                    }
                    else {
                        tv_qnaboardUnLike.setText(qnaBoardItem.getBads()+"");
                    }
                    likeBtnOnOff = 1;
                    tv_qnaboardLike.setTextColor(Color.parseColor("#08883e"));
                    tv_qnaboardLikeText.setTextColor(Color.parseColor("#08883e"));
                    tv_qnaboardLike.setText((qnaBoardItem.getGoods()+1)+"");
                } else {
                    likeBtnOnOff = 0;
                    tv_qnaboardLike.setTextColor(Color.parseColor("#9d9d97"));
                    tv_qnaboardLikeText.setTextColor(Color.parseColor("#9d9d97"));
                    tv_qnaboardLike.setText((qnaBoardItem.getGoods()-1)+"");
                    tv_qnaboardUnLike.setText(qnaBoardItem.getBads()+"");
                }

                // 이 게시물의 추천 수를 올리기 위한 통신을 구현해야함. 보내야 할 값 아마도 게시물 번호, 회원 번호 정도?

                qnaDetailPresenter.recommendOnOffReq(accountNo, postNo, 0, likeBtnOnOff);
                showLoading();
                break;
            case R.id.qnaboardUnLikeLayout:
                flagUnLike++;
                // 비추천 클릭시 색깔 변함 준 부분
                if (flagUnLike % 2 == 1) {
                    if (flagLike % 2 == 1) {
                        flagLike = 0;
                        likeBtnOnOff = 0;
                        tv_qnaboardLike.setTextColor(Color.parseColor("#9d9d97"));
                        tv_qnaboardLikeText.setTextColor(Color.parseColor("#9d9d97"));
                        tv_qnaboardLike.setText((qnaBoardItem.getGoods()-1)+"");
                    }
                    else {
                        tv_qnaboardLike.setText(qnaBoardItem.getGoods()+"");
                    }
                    unLikeBtnOnOff = 1;
                    tv_qnaboardUnLike.setTextColor(Color.parseColor("#08883e"));
                    tv_qnaboardUnLikeText.setTextColor(Color.parseColor("#08883e"));
                    tv_qnaboardUnLike.setText((qnaBoardItem.getBads()+1)+"");
                } else {
                    unLikeBtnOnOff = 0;
                    tv_qnaboardUnLike.setTextColor(Color.parseColor("#9d9d97"));
                    tv_qnaboardUnLikeText.setTextColor(Color.parseColor("#9d9d97"));
                    tv_qnaboardLike.setText(qnaBoardItem.getGoods()+"");
                    tv_qnaboardUnLike.setText((qnaBoardItem.getBads()-1)+"");
                }

                // 이 게시물의 비추천 수를 올리기 위한 통신을 구현해야함. 보내야 할 값 아마도 게시물 번호, 회원 번호 정도?

                qnaDetailPresenter.recommendOnOffReq(accountNo, postNo, 1, unLikeBtnOnOff);
                showLoading();
                break;
            case R.id.qnadetailLayout:
                keyboard.hideKeyboard(et_commentWrite);
                break;
            case R.id.drawupBackLayout:
                Intent intentActionKind;

                if (commentBoolean)
                {
                    intentActionKind = new Intent();
                    intentActionKind.putExtra("qnaBoardItem", qnaBoardItem);
                    intentActionKind.putExtra("actionKind", 1);
                    intentActionKind.putExtra("qnaListPosition", qnaListPosition);
                    intentActionKind.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    setResult(RESULT_OK, intentActionKind);
                }

                if (recommendResponse) {
                    intentActionKind = new Intent();
                    intentActionKind.putExtra("qnaBoardItem", qnaBoardItem);
                    intentActionKind.putExtra("actionKind", 1);
                    intentActionKind.putExtra("qnaListPosition", qnaListPosition);
                    intentActionKind.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    setResult(RESULT_OK, intentActionKind);

                    recommendResponse = false;
                }
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
                            if (editModelArrayList.get(i).isFlag() == true) {
                                editModelArrayList.get(i).setVoteboard(editModelArrayList.get(i).getVoteboard()+1);
                                editModelArrayList.get(i).setFlag(false);

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
                            showLoading();
                        } else if (fromActivity == 1) {
                            qnaDetailPresenter.updateVoteResult(accountNo, qnaItem.getPostNo(), mySelectVoteNum);
                            showLoading();
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

                                mySelectVoteNum = i+1;
                                voteTotalResult += 1;
                            }
                        }
                        qnaBoardDetailImageAdapter.userSelectPos = mySelectVoteNum;
                        qnaBoardDetailImageAdapter.voteTotalNum = voteTotalResult;
                        tv_voteResultShow.setText(voteTotalResult+"");
                        qnaBoardDetailImageAdapter.notifyDataSetChanged();


                        if (fromActivity == 0) {
                            qnaDetailPresenter.updateVoteResult(accountNo, qnaBoardItem.getPostNo(), mySelectVoteNum);
                            showLoading();
                        } else if (fromActivity == 1) {
                            qnaDetailPresenter.updateVoteResult(accountNo, qnaItem.getPostNo(), mySelectVoteNum);
                            showLoading();
                        }

                        rl_voteFinishLayout.setVisibility(View.GONE);
                        whatVoteSelect = 9999;

                        qnaBoardDetailImageAdapter.alreadyVote = true;
                        qnaBoardDetailImageAdapter.notifyDataSetChanged();
                        break;
                }

                break;
            case R.id.drawupTextLayout:
                if (isMyBoard) {
                    Intent intent = new Intent(this, QnaBoardActivity.class);
                    intent.putExtra("qnaBoardItem", qnaBoardItem);
                    intent.putExtra("actionKind", actionKind);
                    intent.putExtra("qnaListPosition", qnaListPosition);
                    intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivity(intent);
                    finish();
                }
                else
                {

                }
                break;
            case R.id.commentEnroll:
                String commentContent = et_commentWrite.getText().toString();
                if(commentContent.length() == 0)
                {
                    Toast.makeText(view.getContext(),"댓글을 입력해주세요.",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (Get_commentNo == 0)
                    {
                        commentPresenter.commentRequest(postNo, accountNo, commentContent);
                        et_commentWrite.getText().clear();
                    }
                    else
                    {
                        commentPresenter.recommentRequest(postNo,Get_commentNo,accountNo,commentContent);
                        et_commentWrite.getText().clear();
                    }
                }

                keyboard.hideKeyboard(et_commentWrite);
                break;
        }
    }

    @Override
    public void onListImageClick(int position) {
    }

    @Override
    public void onListRadioClick(int position, View view) {

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
        }


    }

    @Override
    public void getDataSuccess(QnaVoteItem getQnaVoteItem) {
        hideLoading();
        if (getQnaVoteItem.getQnaVoteStatus() == 0) {
            rc_recycler.setVisibility(View.VISIBLE);


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
                        }
                    }
                });
                rc_recycler.setAdapter(qnaBoardDetailVoteAdapter);
                rc_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                voteTotalResult = getQnaVoteItem.getTotalVoteCount();
                qnaBoardDetailVoteAdapter.voteTotalNums = voteTotalResult;
                qnaBoardDetailVoteAdapter.notifyDataSetChanged();

                tv_voteResultShow.setText(voteTotalResult + "");
            } else {
                for (int i = 0; i < getQnaVoteItem.getVoteText().size(); i++) {
                    QnaBoardVoteItem editModel = new QnaBoardVoteItem();
                    editModel.setEditTextValue(getQnaVoteItem.getVoteText().get(i));
                    editModel.setVoteboard(getQnaVoteItem.getVoteResult().get(i));
                    editModelArrayList.add(editModel);
                }

                qnaBoardDetailVoteAdapter = new QnaBoardDetailVoteAdapter(this, editModelArrayList, new QnaBoardDetailVoteAdapter.ClickListener() {
                    @Override
                    public void onListVoteListClick(int position, View view) {
                    }
                });
                rc_recycler.setAdapter(qnaBoardDetailVoteAdapter);
                rc_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                qnaBoardDetailVoteAdapter.userSelectPos = getQnaVoteItem.getMemberIsVoted();
                qnaBoardDetailVoteAdapter.voteFlag = true;
                qnaBoardDetailVoteAdapter.notifyDataSetChanged();

                qnaBoardDetailVoteAdapter.alreadyVote = true;
                qnaBoardDetailVoteAdapter.notifyDataSetChanged();

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
                qnaBoardDetailImageAdapter.voteTotalNum = voteTotalResult;
                qnaBoardDetailImageAdapter.notifyDataSetChanged();

                tv_voteResultShow.setText(voteTotalResult+"");

            }
        }
    }

    @Override
    public void getDataFail(String message) {
        hideLoading();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void recommendComplete(boolean response, BoardRecommend boardRecommend) {
        hideLoading();
        if (response) {
            qnaBoardItem.setGoods(boardRecommend.getLikeCount());
            qnaBoardItem.setBads(boardRecommend.getBadCount());


            if (likeBtnOnOff > 0)
                qnaBoardItem.setIsLike(1);
            else
                qnaBoardItem.setIsLike(0);

            if (unLikeBtnOnOff > 0)
                qnaBoardItem.setIsBad(1);
            else
                qnaBoardItem.setIsBad(0);


            recommendResponse = true;
        } else {

        }
    }

    @Override
    public void updateVoteResultComplete(boolean flag, QnaVoteItem updateQnaVoteItem) {
        if (flag) {
            hideLoading();

            if (updateQnaVoteItem.getQnaVoteStatus() == 0) {
                for (int i = 0; i < editModelArrayList.size(); i++) {
                    editModelArrayList.get(i).setVoteboard(updateQnaVoteItem.getVoteResult().get(i));
                }
                qnaBoardDetailVoteAdapter.notifyDataSetChanged();
            }
            else {
                for (int i = 0; i < gridVoteItemArrayList.size(); i++) {
                    gridVoteItemArrayList.get(i).setVote(updateQnaVoteItem.getVoteResult().get(i));
                }
                qnaBoardDetailImageAdapter.notifyDataSetChanged();
            }
        } else {

        }
    }

    @Override
    public void getCommentDataSuccess(ArrayList<CommentItem> commentitem,int position)
    {
        this.commentitem = commentitem;
        //댓글리스트데이터 성공적으로 받아왔을때 댓글어댑터 생성
        commentAdapter = new CommentAdapter(QnaBoardDetailActivity.this,this.commentitem,this,postNo,accountNo,accountNick);
        commentPresenter.setView(this);
        commentList.setAdapter(commentAdapter);
        setCommentList();

        commentAdapter.toss_commentNo_Methods(new CommentAdapter.toss_commentNo_interface()
        {
            @Override
            public void toss_comment_position(int position)
            {
                commentAdapter.getCommentDataSuccess(commentitem,comment_position);
            }

            @Override
            public void toss_commentNo_atActivity(int commentNo,String commentWriter,int position) {
                comment_position = position;

                if (commentNo != 9999)
                {
                    Get_commentNo = commentNo;
                    commentWriter = commentWriter;
                    TextAddWriter = "@" + commentWriter +" ";
                    if (Get_commentNo != 0)
                    {
                        et_commentWrite.getText().clear();
                        et_commentWrite.setText(TextAddWriter);
                        et_commentWrite.setSelection(et_commentWrite.length());
                        EditText_commentWirte_tag();
                    }
                    else if(et_commentWrite.getHint().toString().equals(TextAddWriter))
                    {
                        et_commentWrite.getText().clear();
                        et_commentWrite.setHint("댓글을 입력해주세요");
                    }
                    else
                    {
                        et_commentWrite.getText().clear();
                        et_commentWrite.setHint("댓글을 입력해주세요");
                    }
                }

            }

            @Override
            public void toss_commentCount_actiivity(int commentCount,int commentDeleteOrRecommentDelete,int deletePosition)
            {
              comment_position = deletePosition;
              if (commentDeleteOrRecommentDelete == 0)
              {
                  commentDeleteRecommentDelete = true;
              }
                commentPresenter.commentDeleteRequest(postNo,commentCount,0);
            }

            @Override
            public void toss_likeRequest_activity(int postNo, int commentNo, int accountNo, int like_check, int commentORrecomment, int recommentNo)
            {
                commentPresenter.likeRequest(postNo,commentNo,accountNo,like_check,commentORrecomment,recommentNo);
            }
        });

    }

    public void setCommentList()
    {
        if(commentitem.size() == 0)
        {

            int topPadding =
                    (int) this.getResources().getDimension(R.dimen.qnaDetail_commentList_noComment_text_padding);
            noComment_notice_text.setPaddingRelative(0,topPadding,0,topPadding);
            commentList.setVisibility(View.INVISIBLE);
            noComment_notice.setVisibility(View.VISIBLE);
            divideFrame.setVisibility(View.VISIBLE);

        }
        else
        {
            commentList.setVisibility(View.VISIBLE);
            noComment_notice.setVisibility(View.INVISIBLE);
            divideFrame.setVisibility(View.INVISIBLE);
        }
    }


    public void EditText_commentWirte_tag()
    {

        et_commentWrite.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            //s = editText전체값
            //setText = 작성자 아이디 "@tag "
            //text전체값이 setText보다 길이가 짧아질경우 editText클리어
            //댓글번호 0
            //부착되있는 TextWatcher remove
            //1번 답글달기버튼을 누르고 2번답글달기 버튼을 눌렀을떄
            //1번 답글달기 버튼을 누른상태면 editText의 text값이 1번의 setText
            //ex) s.length() =  "@yu " = 4
            //이상태에서
            //2번 답글달기 버튼을 눌렀을때
            //---해결 처음 editText에 setText하기전에 clear로 해결 - 여러 계정으로 디버깅 필요
            if (s.length() < TextAddWriter.length())
            {
                et_commentWrite.getText().clear();
                Get_commentNo = 0;
                //첫번째 문제 해결책 removeTextChangeListener
                et_commentWrite.removeTextChangedListener(this);
            }
        }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    @Override
    public void getCommentDataFail(String message) {

    }

    @Override
    public void moveToPage(Intent intent, int requestCode) {

    }

    @Override
    public void commentInsertGoResponse(int response,int commentCount,CommentItem commentItems)
    {
        //댓글 작성
        if (response == 0)
        {

            commentitem.add(commentItems);
            commentAdapter.notifyItemChanged(0);
            scrollToView(comment_layout_top,qnaboard_detail_scrollView,0);

            setCommentList();
            qnaBoardItem.setComments(commentCount);
            tv_qnaboardCommentCount.setText(String.valueOf(commentCount));
            commentBoolean = true;
        }
    }
    @Override
    public void recommentInsertGoResponse(int response,RecommentItem recommentItem,int commentNo)
    {
        //대댓글 작성
        if (response == 0)
        {

            commentAdapter.recommentInsertGoResponse(response,recommentItem,comment_position);
            commentList.requestFocus(commentNo - 1);
        }

    }

    @Override
    public void commentDeleteGoResponse(int response,int commentCount,int commentNo)
    {
        //댓글 삭제
        if (response == 0)
        {
            qnaBoardItem.setComments(commentCount);
            tv_qnaboardCommentCount.setText(String.valueOf(commentCount));
            commentBoolean = true;
            if (commentDeleteRecommentDelete)
            {
                //댓글
                commentAdapter.commentDeleteGoResponse(0,commentCount,comment_position);
            }
            else
            {
                //대댓글
                commentAdapter.commentDeleteGoResponse(1,commentCount,comment_position);
            }
        }
    }

    @Override
    public void likeGoResponse(int response) {
    }

    @Override
    public void CommentOrRecommentActivity(int commentNo)
    {

    }

    public static void scrollToView(View view, final ScrollView scrollView, int count) {
        if (view != null && view != scrollView) {
            count += view.getTop();
            scrollToView((View) view.getParent(), scrollView, count);
        } else if (scrollView != null) {
            final int finalCount = count;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, finalCount);
                }
            }, 200);
        }
    }


    @Override
    public void onBackPressed()
    {
        Intent intent;
        if (commentBoolean)
        {
            intent = new Intent();
            intent.putExtra("qnaBoardItem", qnaBoardItem);
            intent.putExtra("actionKind", 1);
            intent.putExtra("qnaListPosition", qnaListPosition);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            setResult(RESULT_OK, intent);
        }

        if (recommendResponse)
        {
            intent = new Intent();
            intent.putExtra("qnaBoardItem", qnaBoardItem);
            intent.putExtra("actionKind", 1);
            intent.putExtra("qnaListPosition", qnaListPosition);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            setResult(RESULT_OK, intent);

            recommendResponse = false;
        }

        finish();
    }
}
