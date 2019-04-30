package com.example.taopr.soool.View;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.example.taopr.soool.Adapter.DrawUpTagAdapter;
import com.example.taopr.soool.Adapter.QnaBoardTagAdapter;
import com.example.taopr.soool.Adapter.QnaBoardVoteAdapter;
import com.example.taopr.soool.Adapter.VoteImageAdapter;
import com.example.taopr.soool.Dialog.TagDialog;
import com.example.taopr.soool.Object.QnaBoardVoteItem;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Presenter.QnaBoardPresenter;
import com.example.taopr.soool.R;
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


public class QnaBoardActivity extends AppCompatActivity implements View.OnClickListener, QnaBoardPresenter.View, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "QnaBoardActivity";
    private static final int MY_PERMISSION_STORAGE = 1111;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

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
    QnaBoardVoteAdapter qnaBoardVoteAdapter;
    QnaBoardTagAdapter qnaBoardTagAdapter;

    ArrayList<String> tagArray = new ArrayList<>();
    ArrayList<Uri> path = new ArrayList<>();
    ArrayList<Uri> realPath = new ArrayList<>();
    ArrayList<Integer> number = new ArrayList<>();
    ArrayList<String> voteImage = new ArrayList<>();
    ArrayList<String> voteText = new ArrayList<>();

    private Uri mImageCaptureUri;
    private int id_view;
    String absoultePath, voteSelect;
    int count = 2;

    VoteImageAdapter voteImageAdapter;
    DrawUpTagAdapter drawUpTagAdapter;
    QnaBoardPresenter qnaBoardPresenter;
    QnaBoardItem qnaBoardItem = new QnaBoardItem();
    QnaVoteItem qnaVoteItem = new QnaVoteItem();

    Uri image;
    String title = "", content = "", tag = "", imgPath, imgName;
    static String UploadImgPath;
    String boardImagePath;
    int voteFlag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qnaboard);

        DoBinding(); // ui 선언 및 presenter 선언, presenter에서 넘어올 응답에 대한 변화 view? 선언까지
        checkPermission();

        // 아래의 try / catch 문은 res/xml에 있는 스피너 메뉴값을 파싱하기 위해 가져다 쓴 부분입니다.
        try{
            // custom_list.xml 을 가져와 XmlPullParser 에 넣는다.
            XmlPullParser customList = getResources().getXml(R.xml.drawup_tag);

            // 파싱한 xml 이 END_DOCUMENT(종료 태그)가 나올때 까지 반복한다.
            while(customList.getEventType()!=XmlPullParser.END_DOCUMENT){
                // 태그의 첫번째 속성일 때,
                if(customList.getEventType()==XmlPullParser.START_TAG){
                    // 이름이 "custom" 일때, 첫번째 속성값을 ArrayList에 저장
                    if(customList.getName().equals("custom")){
                        tagArray.add(customList.getAttributeValue(0));
                    }

                }
                // 다음 태그로 이동
                customList.next();
            }
        }catch(XmlPullParserException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

//        drawUpTagAdapter = new DrawUpTagAdapter(this, tagArray);
//        sp_qnaboardTag.setAdapter(drawUpTagAdapter);

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

        ib_drawupBack.setOnClickListener(this);
        btn_drawupEnroll.setOnClickListener(this);

        return true;
    }

//    //스피너 리스너 부분
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(this, "선택한 태그 : "+tagArray.get(position), Toast.LENGTH_SHORT).show();
//        tag = tagArray.get(position).toString();
//        Log.d(TAG, "onItemSelected: "+tag);
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

    // 투표 기능 고르는 부분
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(i == R.id.qnaboardVoteText) {
            voteSelect = "text";
            Toast.makeText(this, voteSelect, Toast.LENGTH_SHORT).show();

            rg_qnaboardVoteSelect.setVisibility(View.GONE);
            lay_qnaboardVoteLayout.setVisibility(View.VISIBLE);
            btn_qnaboardAddBtn.setVisibility(View.VISIBLE);
        }else if(i == R.id.qnaboardVoteImage) {
            voteSelect = "image";

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
            FishBun.with(this).setImageAdapter(new GlideAdapter()).setMaxCount(6).setMinCount(3).startAlbum();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        voteSelect = "";
        voteFlag = 1;
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
                        doTakeAlbumAction();
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
                qnaBoardItem.setImage(UploadImgPath);

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


                if(tag.equals(null)) {
                    Log.d(TAG, "onClick: 태그 값을 선택해주세요.");
                }else if(et_qnaboardTitle.getText().length() == 0) {
                    Log.d(TAG, "onClick: 제목을 입력해주세요.");
                }else if(et_qnaboardContent.getText().length() == 0) {
                    Log.d(TAG, "onClick: 내용을 입력해주세요.");
                }else if(voteSelect.equals("text")) {
                    // 텍스트 투표
//                    Toast.makeText(this, editModelArrayList.size()+"", Toast.LENGTH_SHORT).show();
                    for (int i = 1; i < editModelArrayList.size()+1; i++){
                        Log.d(TAG, "onClick: "+ editModelArrayList.get(i).getEditTextValue());

                        if(QnaBoardVoteAdapter.editModelArrayList.get(i).getEditTextValue().length() == 0){
                            Log.d(TAG, "onClick: "+ i +"번째 내용을 입력해주세요.");
                        }
                    }
                }else if(voteSelect.equals("image")) {
                    // 이미지 투표
                    // 이미지 리싸이클러뷰에서 보여지면 예외처리해야함
                    if (voteImage.size() == 0) {
                        Log.d(TAG, "onClick: 이미지를 선택해주세요");
                    }
                }else if(boardImagePath == null) {
                    // 1. 투표가 표함되는지 안되는지 선 체크
                    // 2. 투표가 있다면 이미지 / 텍스트 중 어떤 투표인지 -> 투표는 다른 디비로 보낼것 이기 때문
                    // 3. 투표가 없다면
                    // 이 세가지를 구별해야 할듯

                    // 투표가 존재한다면 투표객체도 넘겨주어야한다.

                    if (voteFlag == 0) {
                        qnaBoardItem.setQnaCate("yes vote");
                        if (voteSelect.equals("text")) {
                            qnaBoardItem.setTag(tag);
                            qnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
                            qnaBoardItem.setContent(et_qnaboardContent.getText().toString());

                            for (int i=0; i<editModelArrayList.size(); i++)
                                voteText.add(editModelArrayList.get(i).getEditTextValue());

                            qnaVoteItem.setVoteText(voteText);
                            qnaVoteItem.setQnaVoteStatus(voteSelect);

                            qnaBoardPresenter.enrollmentBoardReq(qnaBoardItem, qnaVoteItem);

                        }else if (voteSelect.equals("image")){
                            qnaBoardItem.setTag(tag);
                            qnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
                            qnaBoardItem.setContent(et_qnaboardContent.getText().toString());

                            qnaVoteItem.setVoteImage(voteImage);
                            qnaVoteItem.setQnaVoteStatus(voteSelect);

                            qnaBoardPresenter.enrollmentBoardReq(qnaBoardItem, qnaVoteItem);

                        }
                    }else if (voteFlag == 1) {
                        Log.d(TAG, "enroll onClick: " + "태그 : " + tag + " 제목 : "
                                + et_qnaboardTitle.getText().toString() + " 내용 : " + et_qnaboardContent.getText().toString());

                        qnaBoardItem.setTag(tag);
                        qnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
                        qnaBoardItem.setContent(et_qnaboardContent.getText().toString());

                        qnaBoardItem.setQnaCate("no vote");

                        qnaBoardPresenter.enrollmentBoardReq(qnaBoardItem, qnaVoteItem);

                    }
                }else {
                    // 1. 투표가 표함되는지 안되는지 선 체크
                    // 2. 투표가 있다면 이미지 / 텍스트 중 어떤 투표인지 -> 투표는 다른 디비로 보낼것 이기 때문
                    // 3. 투표가 없다면
                    // 이 세가지를 구별해야 할듯?

                    if (voteFlag == 0) {
                        qnaBoardItem.setQnaCate("yes vote");
                        if (voteSelect.equals("text")) {
                            qnaBoardItem.setTag(tag);
                            qnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
                            qnaBoardItem.setContent(et_qnaboardContent.getText().toString());
                            qnaBoardItem.setImage(boardImagePath);

                            for (int i=0; i<editModelArrayList.size(); i++)
                                voteText.add(editModelArrayList.get(i).getEditTextValue());

                            qnaVoteItem.setVoteText(voteText);
                            qnaVoteItem.setQnaVoteStatus(voteSelect);

                            qnaBoardPresenter.enrollmentBoardReq(qnaBoardItem, qnaVoteItem);

                        }else if (voteSelect.equals("image")){
                            qnaBoardItem.setTag(tag);
                            qnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
                            qnaBoardItem.setContent(et_qnaboardContent.getText().toString());
                            qnaBoardItem.setImage(boardImagePath);

                            qnaVoteItem.setVoteImage(voteImage);
                            qnaVoteItem.setQnaVoteStatus(voteSelect);

                            qnaBoardPresenter.enrollmentBoardReq(qnaBoardItem, qnaVoteItem);

                        }
                    }else if (voteFlag == 1) {
                        Log.d(TAG, "enroll onClick: " + "태그 : " + tag + " 제목 : "
                                + et_qnaboardTitle.getText().toString() + " 내용 : " + et_qnaboardContent.getText().toString() + " 이미지 : " + boardImagePath);

                        qnaBoardItem.setTag(tag);
                        qnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
                        qnaBoardItem.setContent(et_qnaboardContent.getText().toString());
                        qnaBoardItem.setImage(boardImagePath);

                        qnaBoardItem.setQnaCate("no vote");

                        qnaBoardPresenter.enrollmentBoardReq(qnaBoardItem, qnaVoteItem);

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
                Intent intent1 = new Intent(this, TagActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);

                // 다이얼로그 방식
                /*
                TagDialog tagDialog = new TagDialog(this);
                tagDialog.setDialogListener(new TagDialog.TagDialogListener() {
                    @Override
                    public void onPositiveClicked(ArrayList<String> arrayLists) {
                        if (arrayLists.size() > 0) {
                            tv_qnaboardBeforeTag.setVisibility(View.GONE);
                            h_scrollView.setVisibility(View.VISIBLE);
                        } else if (arrayLists.size() == 0) {
                            tv_qnaboardBeforeTag.setVisibility(View.VISIBLE);
                            h_scrollView.setVisibility(View.GONE);
                        }

                        for (int i=0; i<arrayLists.size(); i++) {
                            Log.d("main!!!!!!!!!!!", "onPositiveClicked: "+arrayLists.get(i));
                        }

                        tagArray = new ArrayList<>(arrayLists);

                        qnaBoardTagAdapter = new QnaBoardTagAdapter(v.getContext(), tagArray);
                        rc_qnaboardTag.setAdapter(qnaBoardTagAdapter);
                        rc_qnaboardTag.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });
                tagDialog.getWindow().setGravity(Gravity.BOTTOM);
                tagDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


                tagDialog.show();
                */
                break;
        }
    }
    /**

     * 앨범에서 이미지 가져오기

     */
    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode != RESULT_OK)
            return;

        switch(requestCode)
        {
            case PICK_FROM_ALBUM:
            {
                // 스택오버 플로우에서 얻은 이미지 얻는 소스 부분이므로
                // 다시 한번 도전해보자.

//                try {
//                    final Uri imageUri = data.getData();
//                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                    image_view.setImageBitmap(selectedImage);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PostImage.this, "Something went wrong", Toast.LENGTH_LONG).show();
//                }

                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
                String name_Str = getImageNameToUri(data.getData());
                mImageCaptureUri = data.getData();
                image = mImageCaptureUri;

                //절대경로 획득**
                Cursor c = getContentResolver().query(Uri.parse(mImageCaptureUri.toString()), null, null, null, null);
                c.moveToNext();
                String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));

                //이미지 데이터를 비트맵으로 받아옴
                Bitmap image_bitmap = null;
                try {
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ///리사이징
                int height = image_bitmap.getHeight();
                int width = image_bitmap.getWidth();

                Bitmap src = BitmapFactory.decodeFile(absolutePath);
                Bitmap resized = Bitmap.createScaledBitmap( src, width/4, height/4, true );

                saveBitmaptoJpeg(resized, "soool", name_Str);
                ////리사이징

                iv_qnaboardImage.setVisibility(View.VISIBLE);
                btn_qnaboardDeleteBtn.setVisibility(View.VISIBLE);
//                iv_drawupImage.setImageURI(mImageCaptureUri);

                if(!UploadImgPath.equals(null)) {
                    boardImagePath = UploadImgPath;
                    File file = new File(boardImagePath);
                    Uri test = Uri.fromFile(file);
                    iv_qnaboardImage.setImageURI(test);
                }

                Log.d("SmartWheel",mImageCaptureUri.getPath().toString());
            }
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                    // you can get an image path(ArrayList<String>) on <0.6.2

                    path = data.getParcelableArrayListExtra(Define.INTENT_PATH);

                    for (int i=1; i<path.size()+1; i++) {
                        // path안의 값들을 이름 절대경로 뽑아서 저장하자

                        String nameStr = getImageNameToUri(path.get(i-1));
                        //절대경로 획득**
                        Cursor c = getContentResolver().query(Uri.parse(path.get(i-1).toString()), null, null, null, null);
                        c.moveToNext();
                        String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));

                        //이미지 데이터를 비트맵으로 받아옴
                        Bitmap image_bitmap = null;
                        try {
                            image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path.get(i-1));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ///리사이징
                        int height = image_bitmap.getHeight();
                        int width = image_bitmap.getWidth();

                        Bitmap src = BitmapFactory.decodeFile(absolutePath);
                        Bitmap resized = Bitmap.createScaledBitmap( src, width/4, height/4, true );

                        saveBitmaptoJpeg(resized, "soool", nameStr);


                        if(!UploadImgPath.equals(null)) {
                            voteImage.add(UploadImgPath);
                            UploadImgPath = "";
                            File file = new File(voteImage.get(i-1));
                            Uri test = Uri.fromFile(file);

                            realPath.add(test);
                            number.add(i);
                        }
                    }
                    // 여기서 받아온 이미지 gridview 사용해서 이미지 뿌려주는 것을 적용하자.
                    // gridview item은 텍스트뷰 (항목 숫자식별용) 이미지뷰로 구성해서 만들자.
                    voteImageAdapter = new VoteImageAdapter(this, realPath, number);
                    gridView.setAdapter(voteImageAdapter);

                    break;
                }
        }
    }

    /////   Uri 에서 파일명을 추출하는 로직
    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        imgPath = cursor.getString(column_index);
        imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgName;
    }

    //비트맵을 jpg로
    public static void saveBitmaptoJpeg(Bitmap bitmap,String folder, String name){
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String foler_name = "/"+folder+"/";
        String file_name = name;
        String string_path = ex_storage+foler_name;
        UploadImgPath = string_path+file_name;

        File file_path;
        try{
            file_path = new File(string_path);
            if(!file_path.isDirectory()){
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path+file_name);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

        }catch(FileNotFoundException exception){
            Log.e("FileNotFoundException", exception.getMessage());
        }catch(IOException exception){
            Log.e("IOException", exception.getMessage());
        }
    }
    // Model에서 서버로 부터 받은 응답 값을 받아서 어떻게 처리할 지 결정하는 메서드.
    @Override
    public void enrollmentBoardRespGoToView(boolean response, String vote) {
        if (response == true) {
            if (vote.equals("yes vote")) {
                Intent intent = new Intent(this, QnaBoardDetailActivity.class);
                intent.putExtra("QnaBoardItem", qnaBoardItem);
                intent.putExtra("QnaVoteItem", qnaVoteItem);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (vote.equals("no vote")) {
                Intent intent = new Intent(this, QnaBoardDetailActivity.class);
                intent.putExtra("QnaBoardItem", qnaBoardItem);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }else {
            Toast.makeText(this, "Board 게시물 작성에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}

