package com.example.taopr.soool.View;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taopr.soool.Adapter.DrawUpTagAdapter;
import com.example.taopr.soool.Adapter.QnaBoardTagAdapter;
import com.example.taopr.soool.Adapter.QnaBoardVoteAdapter;
import com.example.taopr.soool.Adapter.VoteImageAdapter;
import com.example.taopr.soool.Dialog.BottomSheetDialog;
import com.example.taopr.soool.Dialog.TagDialog;
import com.example.taopr.soool.ExifUtils;
import com.example.taopr.soool.Object.GridVoteItem;
import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Object.QnaBoardVoteItem;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Presenter.Interface.QnaBoardInter;
import com.example.taopr.soool.Presenter.QnaBoardPresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class QnaBoardActivity extends AppCompatActivity implements
        View.OnClickListener, QnaBoardPresenter.View, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener, VoteImageAdapter.GridviewItemClickListner {

    private static final String TAG = "QnaBoardActivity";
    private static final int MY_PERMISSION_STORAGE = 1111;
    private final int QNA_MOVE_TO_DETAIL= 3100;
    private final int QNA_MOVE_TO_WRITE = 3200;

//    Spinner sp_qnaboardTag;
    TextView tv_qnaboardBeforeTag;
    EditText et_qnaboardTitle, et_qnaboardContent;
    ImageButton ib_qnaboardImagebtn;
    ImageView iv_qnaboardImage;
    Button btn_qnaboardDeleteBtn, btn_qnaboardVoteBtn, btn_qnaboardAddBtn, btn_qnaboardAddTag;
    HorizontalScrollView h_scrollView;
    LinearLayout lay_qnaboardVoteLayout;
    private RecyclerView recyclerView, rc_qnaboardTag;
    RadioGroup rg_qnaboardVoteSelect;
    RadioButton rb_qnaboardVoteText, rb_qnaboardVoteImage;
    GridView gridView;

    public ArrayList<QnaBoardVoteItem> editModelArrayList;
    public ArrayList<GridVoteItem> gridVoteItemArrayList = new ArrayList<>();
    QnaBoardVoteAdapter qnaBoardVoteAdapter;
    QnaBoardTagAdapter qnaBoardTagAdapter = new QnaBoardTagAdapter();

    ArrayList<String> tagArray = new ArrayList<>();
    ArrayList<Uri> path = new ArrayList<>();
    ArrayList<Uri> realPath = new ArrayList<>();
    ArrayList<Integer> number = new ArrayList<>();
    ArrayList<String> voteImage = new ArrayList<>();
    ArrayList<String> voteText = new ArrayList<>();
    ArrayList<String> arrayList = new ArrayList<>();

    private Uri mImageCaptureUri;
    private int id_view;
    String absoultePath;
    int count = 2, accountNo, voteSelect = 2;

    VoteImageAdapter voteImageAdapter;
    DrawUpTagAdapter drawUpTagAdapter;
    QnaBoardPresenter qnaBoardPresenter;
    QnaBoardItem qnaBoardItem = new QnaBoardItem();
    QnaBoardItem receiveQnaBoardItem = new QnaBoardItem();
    QnaVoteItem qnaVoteItem = new QnaVoteItem();
    QnaItem qnaItem = new QnaItem();
    GridVoteItem gridVoteItem;
    BottomSheetDialog bottomSheetDialog;

    Uri image;
    String title = "", content = "", tag = "", imgPath, imgName;
    static String UploadImgPath;
    String[] tagData = new String[0];
    String boardImagePath, accountNick;
    int voteFlag = 1, actionKind = 9999, qnaListPosition;
    boolean boardImageSelect = false, reSelectVoteImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qnaboard);

        DoBinding(); // ui 선언 및 presenter 선언, presenter에서 넘어올 응답에 대한 변화 view? 선언까지
        checkPermission();

        String data = LoginSharedPreferences.LoginUserLoad(this, "LoginAccount");
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);
        accountNo = loginSessionItem.getAccountNo();
        accountNick = loginSessionItem.getAccountNick();

        qnaBoardTagAdapter.setClickListeners(new QnaBoardTagAdapter.ClickListeners() {
            @Override
            public void onBtnClick(int position, View view) {
                Toast.makeText(view.getContext(), position+"", Toast.LENGTH_SHORT).show();
                tagArray.remove(position);
                qnaBoardTagAdapter.notifyDataSetChanged();
            }
        });

        Intent intent = getIntent();

        if (intent != null) {
            actionKind = intent.getIntExtra("actionKind", 9999);
            qnaListPosition = intent.getIntExtra("qnaListPosition", 9999);
            if (actionKind == 1) {
                receiveQnaBoardItem = intent.getParcelableExtra("QnaBoardItem");

                tv_qnaboardBeforeTag.setVisibility(View.GONE);
                h_scrollView.setVisibility(View.VISIBLE);
                Log.d(TAG, "onCreate 태그값: "+receiveQnaBoardItem.getTag());
                if (receiveQnaBoardItem.getTag().contains("@##@")) {
                    tagData = receiveQnaBoardItem.getTag().split("@##@");
                    for (int i = 0; i < tagData.length; i++) {
                        tagArray.add(tagData[i]);
                    }
                }
                qnaBoardTagAdapter = new QnaBoardTagAdapter(this, tagArray, 0);
                rc_qnaboardTag.setAdapter(qnaBoardTagAdapter);
                rc_qnaboardTag.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                et_qnaboardTitle.setText(receiveQnaBoardItem.getTitle());
                et_qnaboardContent.setText(receiveQnaBoardItem.getContent());

                try {
                    Log.d(TAG, "onCreate: 이미지?? "+receiveQnaBoardItem.getImage());
                    File file = new File(receiveQnaBoardItem.getImage());
                    Uri test = Uri.fromFile(file);
                    Glide.with(this)
                            .load(test)
                            .override(100, 100)
                            .centerCrop()
                            .into(iv_qnaboardImage);
                }catch (NullPointerException e){
                    Log.d(TAG, "이미지 없음");
                }

                btn_qnaboardVoteBtn.setVisibility(View.GONE);
            }
        }

        editModelArrayList = populateList();
        qnaBoardVoteAdapter = new QnaBoardVoteAdapter(this,editModelArrayList);
        recyclerView.setAdapter(qnaBoardVoteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    }

    private ArrayList<QnaBoardVoteItem> populateList(){

        ArrayList<QnaBoardVoteItem> list = new ArrayList<>();

        for(int i = 0; i < 2; i++){
            QnaBoardVoteItem editModel = new QnaBoardVoteItem();
            editModel.setEditTextValue("");
            list.add(editModel);
        }

        return list;
    }

    private void DoBinding() {
        qnaBoardPresenter = new QnaBoardPresenter(this, this);
        qnaBoardPresenter.setView(this);

        // 뷰들 선언하는 부분입니다.
//        sp_qnaboardTag = findViewById(R.id.qnaboardTag);
        et_qnaboardTitle = findViewById(R.id.qnaboardTitle);
        et_qnaboardContent = findViewById(R.id.qnaboardContent);
        ib_qnaboardImagebtn = findViewById(R.id.qnaboardImageBtn);
        iv_qnaboardImage = findViewById(R.id.qnaboardImage);
        btn_qnaboardDeleteBtn = findViewById(R.id.qnaboardDeleteBtn);
        btn_qnaboardVoteBtn = findViewById(R.id.qnaboardVoteBtn);
        lay_qnaboardVoteLayout = findViewById(R.id.qnaboardVoteLayout);
        btn_qnaboardAddBtn = findViewById(R.id.qnaboardAddBtn);
        recyclerView = findViewById(R.id.recycler);
        rg_qnaboardVoteSelect = findViewById(R.id.qnaboardVoteSelect);
        rb_qnaboardVoteText = findViewById(R.id.qnaboardVoteText);
        rb_qnaboardVoteImage = findViewById(R.id.qnaboardVoteImage);
        gridView = findViewById(R.id.gridview);
        tv_qnaboardBeforeTag = findViewById(R.id.qnaboardBeforeTag);
        btn_qnaboardAddTag = findViewById(R.id.qnaboardAddTag);
        rc_qnaboardTag = findViewById(R.id.qnaboardTag);
        h_scrollView = findViewById(R.id.h_scrollView);

        iv_qnaboardImage.setVisibility(View.GONE);
        btn_qnaboardDeleteBtn.setVisibility(View.GONE);
        lay_qnaboardVoteLayout.setVisibility(View.GONE);
        btn_qnaboardAddBtn.setVisibility(View.GONE);

        // 뷰의 리스너 선언 부분입니다.
//        sp_qnaboardTag.setOnItemSelectedListener(this);
        ib_qnaboardImagebtn.setOnClickListener(this);
        btn_qnaboardDeleteBtn.setOnClickListener(this);
        btn_qnaboardVoteBtn.setOnClickListener(this);
        btn_qnaboardAddBtn.setOnClickListener(this);
        rg_qnaboardVoteSelect.setOnCheckedChangeListener(this);
        btn_qnaboardAddTag.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
    }

    // 권한 묻는 부분.
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 다시 보지 않기 버튼을 만드려면 이 부분에 바로 요청을 하도록 하면 됨 (아래 else{..} 부분 제거)
            // ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_CAMERA);

            // 처음 호출시엔 if()안의 부분은 false로 리턴 됨 -> else{..}의 요청으로 넘어감
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_STORAGE:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(QnaBoardActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용했다면 이 부분에서..
                break;
        }
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
        View actionbar = inflater.inflate(R.layout.qna_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        ImageButton ib_drawupBack = findViewById(R.id.drawupBack);
        Button btn_drawupEnroll = findViewById(R.id.drawupEnroll);
        Button btn_drawupDelete = findViewById(R.id.drawupDelete);

        if (receiveQnaBoardItem != null)
            if (accountNo == receiveQnaBoardItem.getAccountNo())
                btn_drawupDelete.setVisibility(View.VISIBLE);


        ib_drawupBack.setOnClickListener(this);
        btn_drawupEnroll.setOnClickListener(this);
        btn_drawupDelete.setOnClickListener(this);

        return true;
    }

    // 투표 기능 고르는 부분
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(i == R.id.qnaboardVoteText) {
            voteSelect = 0;
//            Toast.makeText(this, voteSelect, Toast.LENGTH_SHORT).show();

            rg_qnaboardVoteSelect.setVisibility(View.GONE);
            lay_qnaboardVoteLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            btn_qnaboardAddBtn.setVisibility(View.VISIBLE);
        }else if(i == R.id.qnaboardVoteImage) {
            voteSelect = 1;

            rg_qnaboardVoteSelect.setVisibility(View.GONE);
            lay_qnaboardVoteLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            // 다중 이미지 테스트 성공.
            // 리싸이클러뷰로 이미지 최대 5개 보여지는지 데모앱아닌 여기서 테스트 해봐야함.
            // 라디오버튼 클릭되면 앨범으로 바로 이동.
            // 5개로 제한두도록 메시지 처리해줘야함.
            // 이미지 셀렉하면 셀렉된 이미지 리싸이클러뷰로 보여지도록 처리해야함.
            // 여기까지 완료된다면 예외처리부분 처리해준다.
            // 다 된다면 mvp로 서버로 보내주는 처리를 해준다면 작성하는 곳은 완료.

            FishBun.with(this).setImageAdapter(new GlideAdapter()).setMaxCount(6).setMinCount(2).startAlbum();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        voteSelect = 2; // 투표 0이면 텍스트 1이면 이미지이므로 아무것도 아닌 상태 2로 저장해둠.
        voteFlag = 1;
        boardImageSelect = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qnaboardImageBtn:
                //앨범 연동.
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, 1);
//                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        doTakePhotoAction();
//                    }
//                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //앨범
                        boardImageSelect = true;
                        FishBun.with(QnaBoardActivity.this).setImageAdapter(new GlideAdapter()).setMaxCount(1).setMinCount(1).startAlbum();
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //취소
                        dialog.dismiss();
                    }
                };
                new AlertDialog.Builder(this)
                        .setTitle("업로드할 이미지 선택")
//                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("취소", cancelListener)
                        .setNegativeButton("앨범선택", albumListener)
                        .show();
                break;
            case R.id.qnaboardDeleteBtn:
                Toast.makeText(this, "이미지가 삭제됩니다.", Toast.LENGTH_SHORT).show();
                iv_qnaboardImage.setImageBitmap(null);
                iv_qnaboardImage.setVisibility(View.GONE);
                btn_qnaboardDeleteBtn.setVisibility(View.GONE);

                UploadImgPath = null;
                qnaItem.setImage(UploadImgPath);

                break;
            case R.id.drawupBack:
                Toast.makeText(this, "뒤로가기 클릭", Toast.LENGTH_SHORT).show();
                //이전 액티비티로 인탠트 사용해줘야할 부분.
                Intent intent = new Intent(this, QnaActivity.class);
                finish();
                break;
            case R.id.drawupEnroll:


                // 태그 값 / 제목 / 내용 / 이미지 순으로의 예외 처리부분.
                // 이미지의 경우 선택할 수 있도록 예외 처리를 해주었습니다.
                // 예외 처리 이후에 모델로 넘어가 서버에 저장할 수 있도록 처리하였습니다.
                // 투표기능 이 화면에 추가되었으므로 텍스트 / 이미지 인지 구별자를 만들어놔야할것 같다.
                // 투표가 구별이 되었다면 그 값들 또한 예외처리 이후에 객체에 저장을 해주어야한다.

                // 2019/4/26 태그 ui부분 변경해줬으므로 예외처리 부분도 변경해야합니다.
                /*
                if (tagArray.size() == 0) {
                    Log.d(TAG, "onClick: 태그 값을 선택해주세요.");
                }
                 */
                // 위 방식으로 변경해야합니다.
                // 아직 적용하지 않은 이유는 객체의 데이터가 변경되야하므로 팀원들과 상의후에 변경하기 위해 아직 주석에가 남겨둡니다.
                // 객체 tag 변수를 ArrayList<String>으로 변경해야함을 회의해야한다.

                if (actionKind == 0) {
                    if (tag.equals(null)) {
                        Log.d(TAG, "onClick: 태그 값을 선택해주세요.");
                    } else if (et_qnaboardTitle.getText().length() == 0) {
                        Log.d(TAG, "onClick: 제목을 입력해주세요.");
                    } else if (et_qnaboardContent.getText().length() == 0) {
                        Log.d(TAG, "onClick: 내용을 입력해주세요.");
                    } else if (boardImagePath == null) {

                        // 1. 투표가 표함되는지 안되는지 선 체크
                        // 2. 투표가 있다면 이미지 / 텍스트 중 어떤 투표인지 -> 투표는 다른 디비로 보낼것 이기 때문
                        // 3. 투표가 없다면
                        // 이 세가지를 구별해야 할듯

                        // 투표가 존재한다면 투표객체도 넘겨주어야한다.
                        // 투표 경우에 따른 예외처리 해줘야 한다.

                        if (voteFlag == 0) {

                            // 투표가 존재하는 경우.

//                            qnaBoardItem.setQnaCate(0);
                            if (voteSelect == 0) {

                                // 텍스트 투표 인 경우.

//                                qnaBoardItem.setTag(tag);
//                                qnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
//                                qnaBoardItem.setContent(et_qnaboardContent.getText().toString());

                                for (int i = 0; i < editModelArrayList.size(); i++) {

                                    // 텍스트 투표 예외처리 부분.

                                    if (editModelArrayList.get(i).getEditTextValue().length() == 0) {
                                        Log.d(TAG, "onClick: " + i + 1 + "번째 내용을 입력해주세요.");
                                    }
                                    voteText.add(editModelArrayList.get(i).getEditTextValue());
                                }

//                                if (voteText != null)
//                                    qnaVoteItem.setVoteText(voteText);
//
//                                qnaVoteItem.setQnaVoteStatus(voteSelect);

                                qnaItem = new QnaItem(accountNo, voteFlag, tag, et_qnaboardTitle.getText().toString(),
                                        et_qnaboardContent.getText().toString(), voteText, voteSelect);

                                qnaBoardPresenter.enrollmentBoardReq(qnaItem);

                            } else if (voteSelect == 1) {

                                // 이미지 투표 인 경우.

//                                qnaBoardItem.setTag(tag);
//                                qnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
//                                qnaBoardItem.setContent(et_qnaboardContent.getText().toString());

                                // 이미지 리싸이클러뷰에서 보여지면 예외처리해야함
//                                if (voteImage.size() == 0) {
//                                    Log.d(TAG, "onClick: 이미지를 선택해주세요");
//                                } else {
//                                    qnaVoteItem.setVoteImage(voteImage);
//                                }
//                                qnaVoteItem.setQnaVoteStatus(voteSelect);

                                qnaItem = new QnaItem(accountNo, voteFlag, tag, et_qnaboardTitle.getText().toString(),
                                        et_qnaboardContent.getText().toString(), voteSelect, voteImage);

                                qnaBoardPresenter.enrollmentBoardReq(qnaItem);

                            }
                        } else if (voteFlag == 1) {

                            // 투표가 존재하지 않는 경우.

                            Log.d(TAG, "enroll onClick: " + "태그 : " + tag + " 제목 : "
                                    + et_qnaboardTitle.getText().toString() + " 내용 : " + et_qnaboardContent.getText().toString());

//                            qnaBoardItem.setTag(tag);
//                            qnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
//                            qnaBoardItem.setContent(et_qnaboardContent.getText().toString());
//
//                            qnaBoardItem.setQnaCate(1);
//
//                            qnaVoteItem.setQnaVoteStatus(voteSelect);

                            qnaItem = new QnaItem(accountNo, voteFlag, tag, et_qnaboardTitle.getText().toString(),
                                    et_qnaboardContent.getText().toString());

                            qnaBoardPresenter.enrollmentBoardReq(qnaItem);

                        }

                    } else {
                        // 1. 투표가 표함되는지 안되는지 선 체크
                        // 2. 투표가 있다면 이미지 / 텍스트 중 어떤 투표인지 -> 투표는 다른 디비로 보낼것 이기 때문
                        // 3. 투표가 없다면
                        // 이 세가지를 구별해야 할듯?

                        Log.d(TAG, "onClick: 찍혀라!!!!!!!!!!!!!!1 " + voteFlag + voteSelect);

                        if (voteFlag == 0) {
//                            qnaBoardItem.setQnaCate(0);
                            if (voteSelect == 0) {
//                                qnaBoardItem.setTag(tag);
//                                qnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
//                                qnaBoardItem.setContent(et_qnaboardContent.getText().toString());
//                                qnaBoardItem.setImage(boardImagePath);

                                for (int i = 0; i < editModelArrayList.size(); i++) {

                                    // 텍스트 투표 예외처리 부분.

                                    if (editModelArrayList.get(i).getEditTextValue().length() == 0) {
                                        Log.d(TAG, "onClick: " + i + 1 + "번째 내용을 입력해주세요.");
                                    }
                                    voteText.add(editModelArrayList.get(i).getEditTextValue());
                                }

//                                if (voteText != null)
//                                    qnaVoteItem.setVoteText(voteText);
//
//                                qnaVoteItem.setQnaVoteStatus(voteSelect);

                                qnaItem = new QnaItem(accountNo, voteFlag, tag, et_qnaboardTitle.getText().toString(),
                                        et_qnaboardContent.getText().toString(), boardImagePath, voteText, voteSelect);

                                qnaBoardPresenter.enrollmentBoardReq(qnaItem);

                            } else if (voteSelect == 1) {
//                                qnaBoardItem.setTag(tag);
//                                qnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
//                                qnaBoardItem.setContent(et_qnaboardContent.getText().toString());
//                                qnaBoardItem.setImage(boardImagePath);

                                // 이미지 리싸이클러뷰에서 보여지면 예외처리해야함
//                                if (voteImage.size() == 0) {
//                                    Log.d(TAG, "onClick: 이미지를 선택해주세요");
//                                } else {
//                                    qnaVoteItem.setVoteImage(voteImage);
//                                }
//
//                                qnaVoteItem.setQnaVoteStatus(voteSelect);

                                qnaItem = new QnaItem(accountNo, voteFlag, tag, et_qnaboardTitle.getText().toString(),
                                        et_qnaboardContent.getText().toString(), boardImagePath, voteSelect, voteImage);

                                qnaBoardPresenter.enrollmentBoardReq(qnaItem);

                            }
                        } else if (voteFlag == 1) {
                            Log.d(TAG, "enroll onClick: " + "태그 : " + tag + " 제목 : "
                                    + et_qnaboardTitle.getText().toString() + " 내용 : " + et_qnaboardContent.getText().toString() + " 이미지 : " + boardImagePath);

//                            qnaBoardItem.setTag(tag);
//                            qnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
//                            qnaBoardItem.setContent(et_qnaboardContent.getText().toString());
//                            qnaBoardItem.setImage(boardImagePath);
//
//                            qnaBoardItem.setQnaCate(1);
//
//                            qnaVoteItem.setQnaVoteStatus(voteSelect);

                            qnaItem = new QnaItem(accountNo, voteFlag, tag, et_qnaboardTitle.getText().toString(),
                                    et_qnaboardContent.getText().toString(), boardImagePath);

                            qnaBoardPresenter.enrollmentBoardReq(qnaItem);

                        }
                    }
                } else if (actionKind == 1) {
                    // receiveQnaBoardItem 수정되면 모델로 넘겨서 서버에 저장
                    if (tag.equals(null)) {
                        Log.d(TAG, "onClick: 태그 값을 선택해주세요.");
                    } else if (et_qnaboardTitle.getText().length() == 0) {
                        Log.d(TAG, "onClick: 제목을 입력해주세요.");
                    } else if (et_qnaboardContent.getText().length() == 0) {
                        Log.d(TAG, "onClick: 내용을 입력해주세요.");
                    }

                    try {
                        receiveQnaBoardItem.setTag(tag);
                        receiveQnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
                        receiveQnaBoardItem.setContent(et_qnaboardContent.getText().toString());
                        receiveQnaBoardItem.setImage(boardImagePath);

                        qnaBoardPresenter.modifyBoardReq(receiveQnaBoardItem);
                    }catch (NullPointerException e) {
                        receiveQnaBoardItem.setTag(tag);
                        receiveQnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
                        receiveQnaBoardItem.setContent(et_qnaboardContent.getText().toString());

                        qnaBoardPresenter.modifyBoardReq(receiveQnaBoardItem);
                    }

                }

                break;
            case R.id.qnaboardVoteBtn:
                btn_qnaboardVoteBtn.setVisibility(View.GONE);
                rg_qnaboardVoteSelect.setVisibility(View.VISIBLE);
                voteFlag = 0;
                break;
            case R.id.qnaboardAddBtn:
                count++;
                if(count > 5) {
                    Toast.makeText(this, "더 이상 항목추가는 되지 않습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    QnaBoardVoteItem editModel = new QnaBoardVoteItem();
                    editModel.setEditTextValue("");
                    editModelArrayList.add(editModel);

                    qnaBoardVoteAdapter.notifyItemInserted(count);
                }

                qnaBoardVoteAdapter.notifyDataSetChanged();
                break;
            case R.id.qnaboardAddTag:
                if (actionKind == 0) {
                    bottomSheetDialog = BottomSheetDialog.getInstance();
                    bottomSheetDialog.setDialogListener(new BottomSheetDialog.BottomSheetDialoggListener() {
                        @Override
                        public void onPositiveClicked(ArrayList<String> arrayList) {
                            for (int i = 0; i < arrayList.size(); i++) {
                                Log.d("main!!!!!!!!!!!", "onPositiveClicked: " + arrayList.get(i));
                            }

                            tv_qnaboardBeforeTag.setVisibility(View.GONE);
                            h_scrollView.setVisibility(View.VISIBLE);

                            qnaBoardTagAdapter = new QnaBoardTagAdapter(v.getContext(), arrayList, 0);
                            rc_qnaboardTag.setAdapter(qnaBoardTagAdapter);
                            rc_qnaboardTag.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                            for (int i = 0; i < arrayList.size(); i++) {
                                if (i == arrayList.size() - 1)
                                    tag += arrayList.get(i);
                                else if (i < arrayList.size())
                                    tag += arrayList.get(i) + "@##@";
                            }
                        }

                        @Override
                        public void onNegativeClicked() {

                        }
                    });
                    bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheet");
                } else {
                    bottomSheetDialog = BottomSheetDialog.getInstance();
                    bottomSheetDialog.tagArray = tagArray;
                    bottomSheetDialog.setDialogListener(new BottomSheetDialog.BottomSheetDialoggListener() {
                        @Override
                        public void onPositiveClicked(ArrayList<String> arrayList) {
                            for (int i = 0; i < arrayList.size(); i++) {
                                Log.d("main!!!!!!!!!!!", "onPositiveClicked: " + arrayList.get(i));
                            }

                            tv_qnaboardBeforeTag.setVisibility(View.GONE);
                            h_scrollView.setVisibility(View.VISIBLE);

                            qnaBoardTagAdapter = new QnaBoardTagAdapter(v.getContext(), arrayList, 0);
                            rc_qnaboardTag.setAdapter(qnaBoardTagAdapter);
                            rc_qnaboardTag.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                            for (int i = 0; i < arrayList.size(); i++) {
                                if (i == arrayList.size() - 1)
                                    tag += arrayList.get(i);
                                else if (i < arrayList.size())
                                    tag += arrayList.get(i) + "@##@";
                            }
                        }

                        @Override
                        public void onNegativeClicked() {

                        }
                    });
                    bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheet");
                }

                break;
            case R.id.drawupDelete:
                // 삭제기능
                Log.d(TAG, "삭제 버튼 -> "+receiveQnaBoardItem.getPostNo());
                qnaBoardPresenter.deleteBoardReq(receiveQnaBoardItem.getPostNo());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode != RESULT_OK)
            return;

        switch(requestCode)
        {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                    // you can get an image path(ArrayList<String>) on <0.6.2

                    path = data.getParcelableArrayListExtra(Define.INTENT_PATH);

                    if (boardImageSelect == false) {
                        if (reSelectVoteImage == false) {

                            for (int i=0; i<path.size(); i++) {
                                // path안의 값들을 이름 절대경로 뽑아서 저장하자

                                Uri imageUri = path.get(i);
                                String[] filePath = { MediaStore.Images.Media.DATA };
                                Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
                                cursor.moveToFirst();
                                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);
                                bitmap = ExifUtils.rotateBitmap(imagePath,bitmap);

                                voteImage.add(imagePath);
                                File file = new File(voteImage.get(i));
                                Uri test = Uri.fromFile(file);

                                gridVoteItem = new GridVoteItem("", test);
                                gridVoteItemArrayList.add(gridVoteItem);
                            }

                            if (path.size() < 6) {
                                Uri uriTest = Uri.parse("");

                                gridVoteItem = new GridVoteItem("항목추가", uriTest);
                                gridVoteItemArrayList.add(gridVoteItem);
                            }

                            voteImageAdapter = new VoteImageAdapter(this, gridVoteItemArrayList, this);
                            gridView.setAdapter(voteImageAdapter);
                        } else {
                            for (int i = 0; i < path.size(); i++) {
                                Uri imageUri = path.get(i);
                                String[] filePath = { MediaStore.Images.Media.DATA };
                                Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
                                cursor.moveToFirst();
                                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);
                                bitmap = ExifUtils.rotateBitmap(imagePath,bitmap);

                                voteImage.add(imagePath);

                                File file = new File(voteImage.get(voteImage.size() - 1));
                                Uri test = Uri.fromFile(file);

                                gridVoteItem = new GridVoteItem("a", test);
                                gridVoteItemArrayList.add(gridVoteItem);
                            }

                            if (voteImage.size() < 6) {
                                Uri uriTest = Uri.parse("");
                                gridVoteItem = new GridVoteItem("항목추가", uriTest);
                                gridVoteItemArrayList.add(gridVoteItem);
                            }

                            voteImageAdapter.notifyDataSetChanged();
                            gridView.setAdapter(voteImageAdapter);

                            reSelectVoteImage = false;
                        }

                        // 여기서 받아온 이미지 gridview 사용해서 이미지 뿌려주는 것을 적용하자.
                        // gridview item은 텍스트뷰 (항목 숫자식별용) 이미지뷰로 구성해서 만들자.
//                        voteImageAdapter = new VoteImageAdapter(this, realPath, number);
//                        gridView.setAdapter(voteImageAdapter);

                    } else {
                        Uri imageUri = path.get(0);
                        String[] filePath = { MediaStore.Images.Media.DATA };
                        Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
                        cursor.moveToFirst();
                        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);
                        bitmap = ExifUtils.rotateBitmap(imagePath,bitmap);

                        iv_qnaboardImage.setVisibility(View.VISIBLE);
                        btn_qnaboardDeleteBtn.setVisibility(View.VISIBLE);

                        boardImagePath = imagePath;
                        File file = new File(boardImagePath);
                        Uri test = Uri.fromFile(file);
                        Glide.with(this)
                                .load(test)
                                .override(100, 100)
                                .centerCrop()
                                .into(iv_qnaboardImage);

//                        image.setImageBitmap(bitmap);
                        cursor.close();

                        boardImageSelect = false;
                    }
                    break;
                }
        }
    }

    // Model에서 서버로 부터 받은 응답 값을 받아서 어떻게 처리할 지 결정하는 메서드.
    @Override
    public void enrollmentBoardRespGoToView(int response, QnaBoardItem qnaBoardItemResponse) {
        switch (response) {

            /*
             response = 1 -> 인서트 성공
             response = 2 -> 콜백 실패
             response = 3 -> 전송 실패
            */

            case 1:
                Toast.makeText(this, "게시물 작성 성공", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("QnaBoardItem", qnaBoardItemResponse);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case 2:
                Toast.makeText(this, "콜백 실패 백엔드 개발자를 욕하세요. 망했다리~~", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "전송 실패 와이파이가 안 좋은가??", Toast.LENGTH_SHORT).show();
                // 다시 전송되도록 작성하자.
                break;
        }

//        switch (response) {
//
//            /*
//             response = 1 -> 인서트 성공
//             response = 2 -> 인서트 실패
//             response = 3 -> 콜백 실패
//             response = 4 -> 전송 실패
//
//             vote = 0 -> 투표있음
//             vote = 1 -> 투표없음
//
//             voteStatus = 0 -> 텍스트
//             voteStatus = 1 -> 이미지
//            */
//
//            case 1:
//                Toast.makeText(this, "게시물 작성 성공", Toast.LENGTH_SHORT).show();
//
//                if (vote == 0) {
//                    if (voteStatus == 0) {
//                        Intent intent = new Intent(this, QnaBoardDetailActivity.class);
//                        intent.putExtra("QnaItem", qnaItemResponse);
//                        intent.putExtra("fromActivity", 1);
//                        intent.putExtra("voteStatus", 0);
//                        intent.putExtra("vote", 0);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    }
//                    else if (voteStatus == 1) {
//                        Intent intent = new Intent(this, QnaBoardDetailActivity.class);
//                        intent.putExtra("QnaItem", qnaItemResponse);
//                        intent.putExtra("fromActivity", 1);
//                        intent.putExtra("voteStatus", 1);
//                        intent.putExtra("vote", 0);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    }
//                }
//                else if (vote == 1) {
//                    Intent intent = new Intent(this, QnaBoardDetailActivity.class);
//                    intent.putExtra("QnaItem", qnaItemResponse);
//                    intent.putExtra("fromActivity", 1);
//                    intent.putExtra("vote", 1);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                }
//                break;
//            case 2:
//                Toast.makeText(this, "인서트 실패 프론트 개발자를 욕하세요. 망했다리~~", Toast.LENGTH_SHORT).show();
//                break;
//            case 3:
//                Toast.makeText(this, "콜백 실패 백엔드 개발자를 욕하세요. 망했다리~~", Toast.LENGTH_SHORT).show();
//                // 다시 전송되도록 작성하자.
//                break;
//            case 4:
//                Toast.makeText(this, "전송 실패 와이파이가 안 좋은가??", Toast.LENGTH_SHORT).show();
//                // 다시 전송되도록 작성하자.
//                break;
//        }
    }

    @Override
    public void modifyBoardRespGoToView(int response, QnaBoardItem qnaBoardItem) {
        Intent intent;
        switch (response) {
            /*
            response = 0 -> 수정 성공
            response = 1 -> 콜백 실패
            response = 2 -> 통신 실패
             */
            case 0:
                intent = getIntent();
                intent.putExtra("qnaListPosition", qnaListPosition);
                intent.putExtra("qnaBoardItem", qnaBoardItem);
                intent.putExtra("actionKind", actionKind);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case 1:
                Toast.makeText(this, "콜백 실패 백엔드 개발자를 욕하세요. 망했다리~~", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "전송 실패 와이파이가 안 좋은가??", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void deleteBoardRespGoToView(int response) {
        Intent intent;
        switch (response) {
            case 0:
                intent = getIntent();
                intent.putExtra("qnaListPosition", qnaListPosition);
                intent.putExtra("actionKind", 2);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case 1:
                Toast.makeText(this, "콜백 실패 백엔드 개발자를 욕하세요. 망했다리~~", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "전송 실패 와이파이가 안 좋은가??", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        reSelectVoteImage = true;
        gridVoteItemArrayList.remove(position);
        voteImageAdapter.notifyDataSetChanged();
        FishBun.with(this).setImageAdapter(new GlideAdapter()).setMaxCount(6-voteImage.size()).startAlbum();
    }

    @Override
    public void onListTextClick(int position) {
        Toast.makeText(this, gridVoteItemArrayList.get(position).getStatus(), Toast.LENGTH_SHORT).show();
        // 그리드 뷰 버튼 클릭 리스너
        // 그리드 뷰 아이템 (이미지 및 스트링) 삭제해야함.
        gridVoteItemArrayList.remove(position);
        voteImageAdapter.notifyDataSetChanged();
    }
}

