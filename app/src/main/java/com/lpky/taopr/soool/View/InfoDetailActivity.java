package com.lpky.taopr.soool.View;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lpky.taopr.soool.Adapter.CommentAdapter;
import com.lpky.taopr.soool.Adapter.QnaBoardTagAdapter;
import com.lpky.taopr.soool.Decorater.RecyclerDecoration;
import com.lpky.taopr.soool.Dialog.NoticeDialog;
import com.lpky.taopr.soool.Object.CommentItem;
import com.lpky.taopr.soool.Object.InfoContentText;
import com.lpky.taopr.soool.Object.InfoItem;
import com.lpky.taopr.soool.Object.LoginSessionItem;
import com.lpky.taopr.soool.Object.RecommentItem;
import com.lpky.taopr.soool.Presenter.CommentPresenter;
import com.lpky.taopr.soool.Presenter.InfoDetailPresenter;
import com.lpky.taopr.soool.R;
import com.lpky.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.lpky.taopr.soool.Util.Keyboard;
import com.lpky.taopr.soool.View.LayoutInflater.InflateBody;
import com.lpky.taopr.soool.View.LayoutInflater.InflateImage;
import com.lpky.taopr.soool.View.LayoutInflater.InflateSubtitle;
import com.lpky.taopr.soool.Util.Whatisthis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoDetailActivity extends AppCompatActivity implements View.OnClickListener, InfoDetailPresenter.View, CommentPresenter.View{

    String TAG = "InfoDetailActivity";

    // Variables
    String [] tags = new String[0];
    String accountNick, formattedTime;
    int accountNo, postNo;
    int infoPosition, fromActivity, actionKind;

    // Bookmark related Variables
    boolean hasBookmarked = false;
    boolean bookmarkChanged = false;
    int bookmarkNo;

    // Views
    @BindView(R.id.tagScroll)
    HorizontalScrollView tagScroll; // 태그 표시 부분
    @BindView(R.id.tagRecycler)
    RecyclerView tagRecycler;
    @BindView(R.id.infoDetailTitle)
    TextView infoDetailTitle;
    @BindView(R.id.infoDetailWriter)
    TextView infoDetailWriter;
    @BindView(R.id.infoDetailViewLabel)
    TextView infoDetailViewLabel;
    @BindView(R.id.infoDetailView)
    TextView infoDetailView;
    @BindView(R.id.infoDetailDate)
    TextView infoDetailDate;
    @BindView(R.id.infoDetailContent)
    LinearLayout infoDetailContent; // 이미지, 부제목, 내용 추가될 부분
    @BindView(R.id.infoDetailBookmarkLabel)
    TextView infoDetailBookmarkLabel;
    @BindView(R.id.infoDetailBookmarkCount)
    TextView infoDetailBookmarkCount;
    @BindView(R.id.infoDetailBookmark)
    RelativeLayout infoDetailBookmark;
    @BindView(R.id.infoDetailShareLabel)
    TextView infoDetailShareLabel;
    @BindView(R.id.infoDetailShare)
    RelativeLayout infoDetailShare;

    @BindView(R.id.body)
    ScrollView body;
    @BindView(R.id.comment_layout_top)
    LinearLayout comment_layout_top;



    @BindView(R.id.bodyRelative)
    RelativeLayout bodyRelative;

    //(ACTION BAR)
    TextView subActionBarRight, subActionBarLeft, subActionBarTitle;
    ImageView subActionBarLeftImage;

    // Objects
    InfoItem infoItem;

    // Arrays
    ArrayList<String> tagArray = new ArrayList<>();
    ArrayList<InfoContentText> infoText = new ArrayList<>();

    // Others
    InfoDetailPresenter infoDetailPresenter;
    InputMethodManager inputMethodManager;
    Activity mActivity;
    private ProgressBar infoProgress;


    //댓글 부분
    //댓글 입력 버튼 = btn_commentEnroll
    //RecyclerView
    @BindView(R.id.commentList)
    RecyclerView commentList;
    @BindView(R.id.infoCommentText)
    EditText infoCommentText;
    @BindView(R.id.infoCommentButton)
    Button infoCommentButton;
    @BindView(R.id.qnaboardCommentCount)
    TextView InfoCommentCount;
    @BindView(R.id.noComment_notice)
    RelativeLayout noComment_notice;
    @BindView(R.id.divideFrame)
    View divideFrame;
    @BindView(R.id.noComment_notice_text)
    TextView noComment_notice_text;

    private LinearLayoutManager linearLayoutManager;
    private CommentAdapter commentAdapter;
    private ArrayList<CommentItem> commentitem = new ArrayList<>();
    private int Get_commentNo;
    CommentPresenter commentPresent;
    String TextAddWriter;
    int comment_position;
    private boolean commentBoolean = false;

    private boolean  recommendResponse = false;
    boolean commentDeleteRecommentDelete = false;
    private NoticeDialog noticeDialog;

    //keyboard
    Keyboard keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);
        ButterKnife.bind(this);

        keyboard = new Keyboard(this);

        String data = LoginSharedPreferences.LoginUserLoad(this, "LoginAccount");
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);
        accountNick = loginSessionItem.getAccountNick();
        accountNo = loginSessionItem.getAccountNo();


        infoProgress = findViewById(R.id.infoDetailProgress);
        infoDetailPresenter = new InfoDetailPresenter(this);
        infoDetailPresenter.setView(this);


        // getData sent from InfoFragment & sharedPreference
        getIntentInfo();
        infoDetailPresenter.getExistingData();

        // loadAdditionalData
        infoDetailPresenter.getAdditionalData(accountNo, postNo);
        Log.e(TAG, "onCreate: send accnoutNo: " + accountNo + " , postNo: " + postNo);


        //댓글 관련부분
        infoCommentButton.setOnClickListener(this);
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
        commentPresent = new CommentPresenter(this,this);
        commentPresent.setView(this);
        commentPresent.loadData(infoItem.getPostNo());
        commentList.setAdapter(commentAdapter);


    }

    @Override
    public void onClick(View view) {
        Log.e(TAG, "onClick: " + view.getId() + "clicked" );

        switch (view.getId()) {
            case R.id.subActionBarLeftImage:
                backToFragments();
                break;
            case R.id.infoDetailBookmark:
                updateBookmarkView(hasBookmarked, bookmarkNo);
                break;
            case R.id.infoDetailShare:
                noticeDialog = new NoticeDialog(this, getString(R.string.notice_dialog_preparing),getString(R.string.all_button_close),positiveListener);
                noticeDialog.show();
                //Toast.makeText(this, "이 게시물 공유하기",Toast.LENGTH_LONG).show();
                break;
            //댓글 작성 버튼
            case R.id.infoCommentButton:
                commentEnroll();
                break;
        }
    }

    private View.OnClickListener positiveListener = new View.OnClickListener() {
        public void onClick(View v) {
            noticeDialog.dismiss();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);         // 액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        // 액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);         // 홈 아이콘을 숨김처리합니다.
        actionBar.setElevation(0); // 그림자 없애기

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.sub_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        subActionBarRight = findViewById(R.id.subActionBarRight);
        subActionBarLeft = findViewById(R.id.subActionBarLeft);
        subActionBarLeftImage = findViewById(R.id.subActionBarLeftImage);
        subActionBarTitle = findViewById(R.id.subActionBarTitle);

        subActionBarLeft.setVisibility(View.INVISIBLE);
        subActionBarRight.setVisibility(View.INVISIBLE);
        subActionBarTitle.setVisibility(View.INVISIBLE);
        subActionBarLeftImage.setVisibility(View.VISIBLE);
        subActionBarLeftImage.setOnClickListener(this);

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // reset flags ??
    }

    @Override
    public void getDataSuccess(ArrayList<InfoContentText> infoText, int bookmarkNo, boolean hasBookmarked) {

        this.infoText = infoText;
        this.bookmarkNo = bookmarkNo;
        this.hasBookmarked = hasBookmarked;

        // 조회수 업데이트하기
        int updateViews = infoItem.getViews() + 1;
        infoItem.setViews(updateViews);
        infoDetailView.setText(String.valueOf(infoItem.getViews()));

        // View 여러개 생성해두기
        TextView texts[] = new TextView[infoText.size()];
        ImageView ivs[] = new ImageView[infoText.size()];

        // Type: 부제목 0, 내용 1, 이미지 2
        for (int i = 0; i < infoText.size(); i++) {

            int type = infoText.get(i).getType();
            switch (type) {
                case 0:
                    Log.d(TAG, "getText: 0 " + infoText.get(i).getText());
                    InflateSubtitle inflateSubtitle = new InflateSubtitle(getApplicationContext());
                    infoDetailContent.addView(inflateSubtitle);
                    texts[i] = inflateSubtitle.findViewById(R.id.subtitle_tv);

                    // 부제목 글자 속성 변경
                    texts[i].setTextColor(getResources().getColor(R.color.black, null));
                    texts[i].setTypeface(null, Typeface.BOLD);
                    texts[i].setTextSize(13);

                    texts[i].setText(infoText.get(i).getText());
                    break;
                case 1:
                    Log.d(TAG, "getText: 1 " +  infoText.get(i).getText());
                    InflateBody inflateBody = new InflateBody(getApplicationContext());
                    infoDetailContent.addView(inflateBody);
                    texts[i] = inflateBody.findViewById(R.id.body_tv);
                    texts[i].setText(infoText.get(i).getText());
                    break;
                case 2:
                    Log.d(TAG, "getText: 2 " + infoText.get(i).getText());
                    InflateImage inflateImage = new InflateImage(getApplicationContext());
                    infoDetailContent.addView(inflateImage);
                    ivs[i] = inflateImage.findViewById(R.id.image_iv);
                    /* 이미지뷰 width랑 height를 match_parent -> wrap_content로 변경하니 glide에 이미지가 나옴*/
                    Glide.with(inflateImage)
                            .load(Whatisthis.serverIp+infoText.get(i).getText())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e(TAG, "onLoadFailed: " );
                                    return false;
                                }
                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    Log.e(TAG, "onResourceReady:" );
                                    return false;
                                }
                            })
                            .into(ivs[i]);
            }
        }


        // 사용자가 이미 북마크한 경우 북마크 부분 글자 색 변경 후 (서버에서 받아온) 북마크 수 적용하기
        if (hasBookmarked) {
            Log.e(TAG, "getDataSuccess: hasBookmarked? " + hasBookmarked);
            infoDetailBookmarkCount.setTextColor(ContextCompat.getColor(InfoDetailActivity.this,R.color.greenMain));
            infoDetailBookmarkLabel.setTextColor(ContextCompat.getColor(InfoDetailActivity.this,R.color.greenMain));
        }

        // 북마크 수 + 사용자의 북마크 여부 확인
        infoDetailBookmarkCount.setText(String.valueOf(bookmarkNo));
        infoDetailBookmark.setOnClickListener(this);

    }
    @Override
    public boolean updateBookmarkView(boolean flag, int bookmarkCount) {

        hasBookmarked = flag;
        bookmarkNo = bookmarkCount;

        if(hasBookmarked) {

            /*
            북마크수 -1,
            글자색 다시 회색으로 변경,
            서버에 postNo랑 accountNo 담아서 update 요청 보내기,
            북마크변수(bool) 변경 */

            Log.e(TAG, "getDataSuccess: bookmarkStatus 1 " + hasBookmarked + " bookmarkNo " + bookmarkNo);
            bookmarkNo--;
            infoDetailBookmarkCount.setText(String.valueOf(bookmarkNo));
            infoDetailBookmarkCount.setTextColor(ContextCompat.getColor(InfoDetailActivity.this,R.color.grayMain));
            infoDetailBookmarkLabel.setTextColor(ContextCompat.getColor(InfoDetailActivity.this,R.color.grayMain));
            hasBookmarked = false;

        } else {

            Log.e(TAG, "getDataSuccess: bookmarkStatus 0 " + hasBookmarked + " bookmarkNo " + bookmarkNo);
            bookmarkNo++;
            infoDetailBookmarkCount.setText(String.valueOf(bookmarkNo));
            infoDetailBookmarkCount.setTextColor(ContextCompat.getColor(InfoDetailActivity.this,R.color.greenMain));
            infoDetailBookmarkLabel.setTextColor(ContextCompat.getColor(InfoDetailActivity.this,R.color.greenMain));
            hasBookmarked = true;
        }

        Log.e(TAG, "getDataSuccess: bookmarkStatus submit" + hasBookmarked + " bookmarkNo " + bookmarkNo);
        infoDetailPresenter.updateBookmarkStatus();

        bookmarkChanged = true;
        return bookmarkChanged;

    }



    @Override
    public void getSessionData(int accountNo, String formattedTime) {
        this.accountNo = accountNo;
        this.formattedTime = formattedTime;
    }

    @Override
    public void notifyBookmarkChange(int postNo, int bookmarkNo, boolean hasBookmarked) {
        int changedPNo = postNo;
        int changedBNo = bookmarkNo;
        boolean hasChanged = hasBookmarked;

        if (hasChanged) {
            //TODO: forResult로 보내줄 때 변경사항 표시해주자.
        }
    }


    // 로딩 관련
    @Override
    public void showLoading() {
        infoProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        infoProgress.setVisibility(View.GONE);
    }


    // 백버튼 처리
    @Override
    public void onBackPressed(){
        backToFragments();
    }

    // 인포 프래그먼트에서 넘겨준 값 받아오기
    public void getIntentInfo(){

        infoItem = getIntent().getParcelableExtra("infoItem");
        infoPosition = getIntent().getIntExtra("infoPosition", 7);
        fromActivity = getIntent().getIntExtra("fromActivity", 35);
        actionKind = getIntent().getIntExtra("actionKind", 48);

        infoDetailPresenter.getIntentFromActivity(infoItem);


        // 태그 표시
        if(infoItem.getPostTag().length() > 0){

            if(infoItem.getPostTag().contains("@##@")){
                tags = infoItem.getPostTag().split("@##@");
                for (int i = 0; i < tags.length; i++) {
                    tagArray.add(tags[i]);
                }
            } else {
                tagArray.add(infoItem.getPostTag());
            }

            QnaBoardTagAdapter tagAdapter = new QnaBoardTagAdapter(getApplicationContext(), tagArray, 1);
            tagRecycler.setAdapter(tagAdapter);
            tagRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            tagRecycler.addItemDecoration(new RecyclerDecoration(32)); // 태그 사이 간격 추가

        }

        // 게시물 정보 표시
        infoDetailTitle.setText(infoItem.getTitle());
        infoDetailWriter.setText(infoItem.getWriter());
        InfoCommentCount.setText(String.valueOf(infoItem.getComments()));

        String[] splitDate = infoItem.getDate().split("\\s");
        String newDate = splitDate[0].replace("-", ".");
        String newHour = splitDate[1].replace("-", ":");
        Log.e(TAG, "getIntentInfo: Strings" + newDate + " newHour " + newHour);
        String dateForm = newDate+" "+newHour;
        infoDetailDate.setText(dateForm);
        postNo = infoItem.getPostNo();

    }


    // 뒤로 가기 버튼 누르면 변경사항을 이전 프래그먼트로 보내고 상세보기 액티비티 닫기
    public void backToFragments() {
        // 인포 프래그먼트로 보내는 경우
        // 마이페이지 북마크로 보내는 경우

        Intent intent = new Intent();
        intent.putExtra("infoItem", infoItem);
        if (bookmarkChanged){
            // 북마크 변경사항이 생긴 경우
            if (!hasBookmarked) {
                // 북마크 O -> 북마크 X : 삭제(1)
                // 이건 마이페이지 북마크에서만 적용됨
                intent.putExtra("actionKind", 1);
            } else {
                // 북마크 X -> 북마크 O : 아이템 업데이트(2)
                // TODO : 댓글수 변경 시??
                intent.putExtra("actionKind", 2);
            }
        }
        intent.putExtra("infoPosition", infoPosition);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setResult(RESULT_OK, intent);
        finish();



    }

    @Override
    public void getDataFail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    //이하 댓글 관련부분
    public void commentEnroll()
    {
        String commentContent = infoCommentText.getText().toString();
        if(commentContent.length() == 0)
        {
            Toast.makeText(this,"댓글을 입력해주세요.",Toast.LENGTH_LONG).show();
        }
        else
        {
            if (Get_commentNo == 0)
            {
                commentPresent.commentRequest(postNo, accountNo, commentContent);
                infoCommentText.getText().clear();
            }
            else
            {
                commentPresent.recommentRequest(postNo,Get_commentNo,accountNo,commentContent);
                infoCommentText.getText().clear();
            }
        }

        keyboard.hideKeyboard(infoCommentText);
    }
    @Override
    public void getCommentDataSuccess(ArrayList<CommentItem> commentitem,int position)
    {

        this.commentitem = commentitem;


        commentAdapter = new CommentAdapter(InfoDetailActivity.this,this.commentitem,this,postNo,accountNo,accountNick);
        commentPresent.setView(this);
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
                        infoCommentText.getText().clear();
                        infoCommentText.setText(TextAddWriter);
                        infoCommentText.setSelection(infoCommentText.length());
                        EditText_commentWirte_tag();
                    }
                    else if(infoCommentText.getHint().toString().equals(TextAddWriter))
                    {
                        infoCommentText.getText().clear();
                        infoCommentText.setHint("댓글을 입력해주세요");
                    }
                    else
                    {
                        infoCommentText.getText().clear();
                        infoCommentText.setHint("댓글을 입력해주세요");
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
                commentPresent.commentDeleteRequest(postNo,commentCount,0);
            }

            @Override
            public void toss_likeRequest_activity(int postNo, int commentNo, int accountNo, int like_check, int commentORrecomment, int recommentNo)
            {
                commentPresent.likeRequest(postNo,commentNo,accountNo,like_check,commentORrecomment,recommentNo);
            }
        });

    }

    public void EditText_commentWirte_tag()
    {

        infoCommentText.addTextChangedListener(new TextWatcher()
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
                    infoCommentText.getText().clear();
                    Get_commentNo = 0;
                    //첫번째 문제 해결책 removeTextChangeListener
                    infoCommentText.removeTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void setCommentList()
    {
        if(commentitem.size() == 0)
        {
            int topPadding =
                    (int) this.getResources().getDimension(R.dimen.infoDetail_commentList_noComment_text_padding);
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

    @Override
    public void getCommentDataFail(String message) {

    }

    @Override
    public void moveToPage(Intent intent, int requestCode) {

    }

    @Override
    public void commentInsertGoResponse(int response, int commentCount, CommentItem commentItem)
    {
        //댓글 작성
        Log.d(TAG, "commentInsertGoResponse: " + "안타냐");
        if (response == 0)
        {
            commentitem.add(commentItem);
            commentAdapter.notifyItemChanged(0);
            scrollToView(comment_layout_top,body,0);

            setCommentList();
            infoItem.setComments(commentCount);
            InfoCommentCount.setText(String.valueOf(commentCount));
            commentBoolean = true;
        }
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
    public void recommentInsertGoResponse(int response, RecommentItem recommentItem, int commentNo)
    {
        //대댓글 작성
        if (response == 0)
        {
            commentAdapter.recommentInsertGoResponse(response,recommentItem,comment_position);
            commentList.requestFocus(commentNo - 1);
        }
    }

    @Override
    public void likeGoResponse(int response) {
    }

    @Override
    public void CommentOrRecommentActivity(int commentNo) {

    }

    @Override
    public void commentDeleteGoResponse(int response, int commentCount,int commentNo)
    {
        //댓글 삭제
        if (response == 0)
        {
            infoItem.setComments(commentCount);
            InfoCommentCount.setText(String.valueOf(commentCount));
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


    // 공유하기
    public void share(){
        ShareCompat.IntentBuilder.from(mActivity)
                .setType("text/plain")
                .setChooserTitle("Share URL")
                .setText("http://www.url.com")
                .startChooser();
    }
}
