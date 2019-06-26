package com.example.taopr.soool.View;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taopr.soool.Adapter.QnaBoardTagAdapter;
import com.example.taopr.soool.Decorater.RecyclerDecoration;
import com.example.taopr.soool.Object.InfoContentText;
import com.example.taopr.soool.Object.InfoItem;
import com.example.taopr.soool.Presenter.InfoDetailPresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.View.LayoutInflater.InflateBody;
import com.example.taopr.soool.View.LayoutInflater.InflateImage;
import com.example.taopr.soool.View.LayoutInflater.InflateSubtitle;
import com.example.taopr.soool.Util.Whatisthis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoDetailActivity extends AppCompatActivity implements View.OnClickListener, InfoDetailPresenter.View{

    String TAG = "InfoDetailActivity";

    // Variables
    String [] tags = new String[0];
    String accountNick, formattedTime;
    int accountNo, postNo;
    int infoPosition, fromActivity, actionKind;

    // Bookmark related Variables
    boolean hasBookmarked = false;
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

    @BindView(R.id.infoCommentText)
    EditText infoCommentText;
    @BindView(R.id.infoCommentButton)
    Button infoCommentButton;



    //(ACTION BAR)
    TextView subActionBarRight, subActionBarLeft;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);
        ButterKnife.bind(this);

        infoProgress = findViewById(R.id.infoProgress);
        infoDetailPresenter = new InfoDetailPresenter(this);
        infoDetailPresenter.setView(this);

        // Keyboard(INPUT)
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // dealing with keyboards (i guess)

        // getData sent from InfoFragment & sharedPreference
        getIntentInfo();
        infoDetailPresenter.getExistingData();

        // loadAdditionalData
        infoDetailPresenter.getAdditionalData(accountNo, postNo);

    }

    @Override
    public void onClick(View view) {
        Log.e(TAG, "onClick: " + view.getId() + "clicked" );

        switch (view.getId()) {
            case R.id.subActionBarLeftImage:
                // TODO : forResult로 무언가 보내줘야하는 거 같음 (지금 앱 터짐)
                // java.lang.NullPointerException: Attempt to invoke virtual method 'void android.app.Activity.finish()' on a null object reference
                mActivity.finish();
                break;
            case R.id.infoDetailBookmark:
                updateBookmarkView(hasBookmarked, bookmarkNo);
                break;
            case R.id.infoDetailShare:
                Toast.makeText(this, "이 게시물 공유하기",Toast.LENGTH_LONG).show();
                break;
            case R.id.infoCommentButton:
                break;
        }
    }



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

        subActionBarLeft.setVisibility(View.INVISIBLE);
        subActionBarRight.setVisibility(View.INVISIBLE);
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

        Log.e(TAG, "getDataSuccess: BOOKMARK!!" + bookmarkNo + " | " + hasBookmarked );
        Log.d(TAG, "getDataSuccess: " + infoText.get(0).getText());


        //View subtitle, body, image;
        //LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //bodyInflater.inflate(R.layout.inflate_body, this, true);

        // Type: 부제목 0, 내용 1, 이미지 2
        for (int i = 0; i < infoText.size(); i++) {

            TextView subtitle, body;
            ImageView image;
            int type = infoText.get(i).getType();
            switch (type) {
                case 0:
                    Log.d(TAG, "getText: " + infoText.get(i).getText());
                    //InflateSubtitle inflateSubtitle = new InflateSubtitle(getApplicationContext());
                    //subtitle = inflateSubtitle(getApplicationContext(), infoText.get(i).getText());
                    infoDetailContent.addView(inflateSubtitle(getApplicationContext(), infoText.get(i).getText()));
                    /*
                    subtitle = inflater.inflate(R.layout.inflate_subtitle, null);
                    infoDetailContent.addView(subtitle);
                    TextView subtitle_tv = (TextView)findViewById(R.id.subtitle_tv);
                    subtitle_tv.setText(infoText.get(i).getText());*/
                    break;
                case 1:
                    Log.d(TAG, "getText: " + infoText.get(i).getText());
                    body = inflateBody(getApplicationContext(), infoText.get(i).getText());
                    infoDetailContent.addView(body);
                    /*
                    body = inflater.inflate(R.layout.inflate_body, null);
                    infoDetailContent.addView(body);
                    TextView body_tv = (TextView)findViewById(R.id.body_tv);
                    body_tv.setText(infoText.get(i).getText());*/
                    break;
                case 2:
                    Log.d(TAG, "getText: " + infoText.get(i).getText());
                    image = inflateImage(getApplicationContext(), infoText.get(i).getText());
                    infoDetailContent.addView(image);
                    //InflateImage inflateImage = new InflateImage(getApplicationContext());
                    /*
                    image = inflater.inflate(R.layout.inflate_image, null);
                    infoDetailContent.addView(image);
                    ImageView imageView = (ImageView)findViewById(R.id.image_iv);
                    Glide.with(this)
                            .load(Whatisthis.serverIp+infoText.get(i).getText())
                            .into(imageView);

                    */
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
        // TODO: 레이아웃 이상하게 나온다. 북마크랑 숫자랑 겹쳐서 나오고, 공유하기 버튼도 안나온다.

    }

    public TextView inflateBody (Context context, String text){

        LayoutInflater bodyInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bodyInflater.inflate(R.layout.inflate_body, infoDetailContent, true);
        TextView body = (TextView) findViewById(R.id.body_tv);
        body.setText(text);

        return body;

    }

    public TextView inflateSubtitle (Context context, String text) {
        LayoutInflater subtitleInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        subtitleInflater.inflate(R.layout.inflate_subtitle, infoDetailContent, true);
        TextView subtitle = (TextView) findViewById(R.id.subtitle_tv);
        subtitle.setText(text);

        return subtitle;
    }

    public ImageView inflateImage (Context context, String text) {
        LayoutInflater imageInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageInflater.inflate(R.layout.inflate_image, infoDetailContent, true);
        ImageView imageView = (ImageView) findViewById(R.id.image_iv);
        Glide.with(this)
                .load(Whatisthis.serverIp+text)
                .into(imageView);

        return  imageView;

    }

    @Override
    public void getDataFail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateBookmarkView(boolean flag, int bookmarkCount) {

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

    @Override
    public void showLoading() {
        infoProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        infoProgress.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed(){
        //TODO : if there has been changes in Bookmark Status or Comments, send an intent back to infoFragment with results
        finish();
    }

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
        infoDetailView.setText(String.valueOf(infoItem.getViews()));

        String[] splitDate = infoItem.getDate().split("\\s");
        String newDate = splitDate[0].replace("-", ".");
        String newHour = splitDate[1].replace("-", ":");
        Log.e(TAG, "getIntentInfo: Strings" + newDate + " newHour " + newHour);
        String dateForm = newDate+" "+newHour;
        infoDetailDate.setText(dateForm);
        postNo = infoItem.getPostNo();

    }
}