package com.example.taopr.soool.View;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taopr.soool.Adapter.QnaBoardTagAdapter;
import com.example.taopr.soool.Adapter.QnaBoardVoteAdapter;
import com.example.taopr.soool.Adapter.VoteImageAdapter;
import com.example.taopr.soool.Decorater.RecyclerDecoration;
import com.example.taopr.soool.Dialog.BottomSheetDialog;
import com.example.taopr.soool.Dialog.BottomSheetDialogVoteSelect;
import com.example.taopr.soool.Dialog.NoticeDialog;
import com.example.taopr.soool.Presenter.Interface.QnaBoardInter;
import com.example.taopr.soool.Util.ExifUtils;
import com.example.taopr.soool.Object.GridVoteItem;
import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Object.QnaBoardVoteItem;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Presenter.QnaBoardPresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.example.taopr.soool.Util.Keyboard;
import com.example.taopr.soool.Util.Whatisthis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class QnaBoardActivity extends AppCompatActivity implements
        View.OnClickListener, QnaBoardPresenter.View, AdapterView.OnItemClickListener, 
        VoteImageAdapter.GridviewItemClickListner, TextWatcher {

    private final String TAG = "QnaBoardActivity";
    private static final int MY_PERMISSION_STORAGE = 1111;
    private final int QNA_MOVE_TO_DETAIL= 3100;
    private final int QNA_MOVE_TO_WRITE = 3200;

    TextView tv_qnaboardBeforeTag, tv_drawupBack, tv_drawupEnroll, tv_drawupDelete;
    EditText et_qnaboardTitle, et_qnaboardContent;
    ImageView iv_qnaImageVoteRemove, iv_qnaboardAddBtn, iv_qnaboardImage, iv_qnaboardAddTag, iv_qnaboardDeleteBtn, iv_qnaboardImagebtn, iv_qnaboardVoteBtn, iv_qnaTextVoteRemove;
    HorizontalScrollView h_scrollView;
    LinearLayout ll_qnaVoteTextLayout, ll_qnaVoteImageLayout, ll_belowLayout;
    RelativeLayout rl_qnaTextAddLayout, rl_spaceLayout, rl_drawupBackLayout;
    FrameLayout imageLayout;
    RecyclerView recyclerView, rc_qnaboardTag;
    GridView gridView;
    ProgressBar pb_qnaBoardProgress;

    public ArrayList<QnaBoardVoteItem> editModelArrayList;
    public ArrayList<GridVoteItem> gridVoteItemArrayList = new ArrayList<>();
    QnaBoardVoteAdapter qnaBoardVoteAdapter;
    QnaBoardTagAdapter qnaBoardTagAdapter;

    ArrayList<String> tagArray = new ArrayList<>();
    ArrayList<Uri> path = new ArrayList<>();
    ArrayList<String> voteImage = new ArrayList<>();
    ArrayList<String> voteText = new ArrayList<>();

    VoteImageAdapter voteImageAdapter;
    QnaBoardPresenter qnaBoardPresenter;
    QnaBoardVoteItem qnaBoardVoteItem = new QnaBoardVoteItem();
    QnaBoardItem receiveQnaBoardItem = new QnaBoardItem();
    QnaVoteItem qnaVoteItem = new QnaVoteItem();
    QnaItem qnaItem = new QnaItem();
    GridVoteItem gridVoteItem;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetDialogVoteSelect bottomSheetDialogVoteSelect;
    Keyboard keyboard;

    String tag = "";
    String UploadImgPath, boardImagePath, accountNick;
    String[] tagData = new String[0];
    int maybeDeletePosition = 999, voteFlag = 1, actionKind = 9999, qnaListPosition, count = 2, accountNo, voteSelect = 2;
    boolean boardImageSelect = false, reSelectVoteImage = false, loading = false;


    private NoticeDialog CanclenoticeDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qnaboard);

        DoBinding(); // ui 선언 및 presenter 선언, presenter에서 넘어올 응답에 대한 변화 view? 선언까지
        checkPermission();

        keyboard = new Keyboard(this);
        keyboard.hideKeyboard(et_qnaboardTitle);

        String data = LoginSharedPreferences.LoginUserLoad(this, "LoginAccount");
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);
        accountNo = loginSessionItem.getAccountNo();
        accountNick = loginSessionItem.getAccountNick();

        Intent intent = getIntent();

        if (intent != null) {
            actionKind = intent.getIntExtra("actionKind", 9999);
            qnaListPosition = intent.getIntExtra("qnaListPosition", 9999);
            if (actionKind == 1) {
                receiveQnaBoardItem = intent.getParcelableExtra("qnaBoardItem");

                if (receiveQnaBoardItem != null)
                    if (accountNo == receiveQnaBoardItem.getAccountNo())
                        tv_drawupDelete.setVisibility(View.VISIBLE);

                tv_qnaboardBeforeTag.setVisibility(View.GONE);
                h_scrollView.setVisibility(View.VISIBLE);
                if (receiveQnaBoardItem.getTag().contains("@##@")) {
                    tagData = receiveQnaBoardItem.getTag().split("@##@");
                    for (int i = 0; i < tagData.length; i++) {
                        tagArray.add(tagData[i]);
                    }
                } else {
                    tagArray.add((receiveQnaBoardItem.getTag()));
                }

                tag = receiveQnaBoardItem.getTag();

                qnaBoardTagAdapter = new QnaBoardTagAdapter(this, tagArray, 0, new QnaBoardTagAdapter.ClickListener() {
                    @Override
                    public void ListClick(int position, View view) {
                        tagArray.remove(position);
                        qnaBoardTagAdapter.notifyDataSetChanged();
                    }
                });
                rc_qnaboardTag.setAdapter(qnaBoardTagAdapter);
                rc_qnaboardTag.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                et_qnaboardTitle.setText(receiveQnaBoardItem.getTitle());
                et_qnaboardContent.setText(receiveQnaBoardItem.getContent());

                if (receiveQnaBoardItem.getImage() == null) {
                    imageLayout.setVisibility(View.GONE);
                } else {
                    imageLayout.setVisibility(View.VISIBLE);
                    Glide.with(this)
                            .load(Whatisthis.serverIp+receiveQnaBoardItem.getImage())
                            .override(100, 100)
                            .centerCrop()
                            .into(iv_qnaboardImage);
                }

                iv_qnaboardVoteBtn.setVisibility(View.GONE);
            }
        }

        editModelArrayList = populateList();
        qnaBoardVoteAdapter = new QnaBoardVoteAdapter(this,editModelArrayList, new QnaBoardVoteAdapter.QnaBoardVoteListener() {
            @Override
            public void voteContentClickListner(int position, View view) {
                editModelArrayList.remove(position);
                qnaBoardVoteAdapter.notifyDataSetChanged();
            }
        });
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
        et_qnaboardTitle = findViewById(R.id.qnaboardTitle);
        et_qnaboardContent = findViewById(R.id.qnaboardContent);
        iv_qnaboardImagebtn = findViewById(R.id.qnaboardImageBtn);
        iv_qnaboardImage = findViewById(R.id.qnaboardImage);
        iv_qnaboardDeleteBtn = findViewById(R.id.qnaboardDeleteBtn);
        iv_qnaboardVoteBtn = findViewById(R.id.qnaboardVoteBtn);
        ll_qnaVoteTextLayout = findViewById(R.id.qnaVoteTextLayout);
        ll_qnaVoteImageLayout = findViewById(R.id.qnaVoteImageLayout);
        iv_qnaboardAddBtn = findViewById(R.id.qnaboardAddBtn);
        recyclerView = findViewById(R.id.recycler);
        gridView = findViewById(R.id.gridview);
        tv_qnaboardBeforeTag = findViewById(R.id.qnaboardBeforeTag);
        iv_qnaboardAddTag = findViewById(R.id.qnaboardAddTag);
        rc_qnaboardTag = findViewById(R.id.qnaboardTag);
        rc_qnaboardTag.addItemDecoration(new RecyclerDecoration(16));
        h_scrollView = findViewById(R.id.h_scrollView);
        imageLayout = findViewById(R.id.imageLayout);
        iv_qnaTextVoteRemove = findViewById(R.id.qnaTextVoteRemove);
        rl_qnaTextAddLayout = findViewById(R.id.qnaTextAddLayout);
        iv_qnaImageVoteRemove = findViewById(R.id.qnaImageVoteRemove);
        tv_drawupBack = findViewById(R.id.drawupBack);
        tv_drawupEnroll = findViewById(R.id.drawupEnroll);
        tv_drawupDelete = findViewById(R.id.drawupDelete);
        pb_qnaBoardProgress = findViewById(R.id.qnaBoardProgress);
        ll_belowLayout = findViewById(R.id.belowLayout);
        rl_spaceLayout = findViewById(R.id.spaceLayout);
        rl_drawupBackLayout = findViewById(R.id.drawupBackLayout);

        imageLayout.setVisibility(View.GONE);
        ll_qnaVoteTextLayout.setVisibility(View.GONE);
        ll_qnaVoteImageLayout.setVisibility(View.GONE);

        // 뷰의 리스너 선언 부분입니다.
        iv_qnaboardImagebtn.setOnClickListener(this);
        iv_qnaboardDeleteBtn.setOnClickListener(this);
        iv_qnaboardVoteBtn.setOnClickListener(this);
        iv_qnaboardAddBtn.setOnClickListener(this);
        iv_qnaboardAddTag.setOnClickListener(this);
        iv_qnaTextVoteRemove.setOnClickListener(this);
        iv_qnaImageVoteRemove.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
        rl_drawupBackLayout.setOnClickListener(this);
        tv_drawupEnroll.setOnClickListener(this);
        tv_drawupDelete.setOnClickListener(this);
        rl_spaceLayout.setOnClickListener(this);
        et_qnaboardContent.addTextChangedListener(this);
    }

    // 권한 묻는 부분.
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 다시 보지 않기 버튼을 만드려면 이 부분에 바로 요청을 하도록 하면 됨 (아래 else{..} 부분 제거)

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
    protected void onDestroy() {
        super.onDestroy();
        voteSelect = 2; // 투표 0이면 텍스트 1이면 이미지이므로 아무것도 아닌 상태 2로 저장해둠.
        voteFlag = 1;
        boardImageSelect = false;
    }

    public void showLoading() {
        pb_qnaBoardProgress.setVisibility(View.VISIBLE);
        tv_drawupEnroll.setClickable(false);
        iv_qnaboardAddTag.setClickable(false);
        et_qnaboardTitle.setClickable(false);
        et_qnaboardContent.setClickable(false);
        iv_qnaTextVoteRemove.setClickable(false);
        iv_qnaImageVoteRemove.setClickable(false);
        iv_qnaboardAddBtn.setClickable(false);
        iv_qnaboardImagebtn.setClickable(false);
        iv_qnaboardVoteBtn.setClickable(false);
        // voteImageAdapter 클릭리스너를 어떻게 막을까...
        loading = true;
    }

    public void hideLoading() {
        pb_qnaBoardProgress.setVisibility(View.GONE);
        tv_drawupEnroll.setClickable(true);
        iv_qnaboardAddTag.setClickable(true);
        et_qnaboardTitle.setClickable(true);
        et_qnaboardContent.setClickable(true);
        iv_qnaTextVoteRemove.setClickable(true);
        iv_qnaImageVoteRemove.setClickable(true);
        iv_qnaboardAddBtn.setClickable(true);
        iv_qnaboardImagebtn.setClickable(true);
        iv_qnaboardVoteBtn.setClickable(true);

        loading = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qnaImageVoteRemove:
                ll_qnaVoteImageLayout.setVisibility(View.GONE);

                // 항목에 값이 있다면 값을 다 없애주자.
                try {
                    gridVoteItemArrayList.clear();

                    voteImageAdapter.notifyDataSetChanged();

                    voteImage.clear();

                } catch (NullPointerException e) {
                    Log.d(TAG, "Null pointer");
                }

                iv_qnaboardVoteBtn.setClickable(true);
                break;
            case R.id.qnaTextVoteRemove:
                ll_qnaVoteTextLayout.setVisibility(View.GONE);

                // 항목에 값이 있다면 값을 다 없애주자.
                try {
                    editModelArrayList.clear();

                    for (int i=0; i<2; i++) {
                        QnaBoardVoteItem editModel = new QnaBoardVoteItem();
                        editModel.setEditTextValue("");
                        editModelArrayList.add(editModel);
                    }

                    qnaBoardVoteAdapter.notifyDataSetChanged();

                    voteText.clear();
                } catch (NullPointerException e) {
                    Log.d(TAG, "Null pointer");
                }

                iv_qnaboardVoteBtn.setClickable(true);
                 break;
            case R.id.qnaboardImageBtn:
                imageLayout.setVisibility(View.VISIBLE);
                //앨범 연동.
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
                        .setNeutralButton("취소", cancelListener)
                        .setNegativeButton("앨범선택", albumListener)
                        .show();
                break;
            case R.id.qnaboardDeleteBtn:
                Toast.makeText(this, "이미지가 삭제됩니다.", Toast.LENGTH_SHORT).show();
                iv_qnaboardImage.setImageBitmap(null);
                imageLayout.setVisibility(View.GONE);

                UploadImgPath = null;
                qnaItem.setImage(UploadImgPath);

                break;
            case R.id.drawupBackLayout:
                //이전 액티비티로 인탠트 사용해줘야할 부분.
                confirmCancel();
                break;
            case R.id.drawupEnroll:


                // 태그 값 / 제목 / 내용 / 이미지 순으로의 예외 처리부분.
                // 이미지의 경우 선택할 수 있도록 예외 처리를 해주었습니다.
                // 예외 처리 이후에 모델로 넘어가 서버에 저장할 수 있도록 처리하였습니다.
                // 투표기능 이 화면에 추가되었으므로 텍스트 / 이미지 인지 구별자를 만들어놔야할것 같다.
                // 투표가 구별이 되었다면 그 값들 또한 예외처리 이후에 객체에 저장을 해주어야한다.

                // 2019/4/26 태그 ui부분 변경해줬으므로 예외처리 부분도 변경해야합니다.

                // 위 방식으로 변경해야합니다.
                // 아직 적용하지 않은 이유는 객체의 데이터가 변경되야하므로 팀원들과 상의후에 변경하기 위해 아직 주석에가 남겨둡니다.
                // 객체 tag 변수를 ArrayList<String>으로 변경해야함을 회의해야한다.

                if (actionKind == 0) {
                    if (tag.equals("")) {
                        Toast.makeText(v.getContext(),"태그 값을 선택해주세요.",Toast.LENGTH_SHORT).show();

                    } else if (et_qnaboardTitle.getText().length() == 0) {
                        Toast.makeText(v.getContext(),"제목을 입력해주세요.",Toast.LENGTH_SHORT).show();

                    } else if (et_qnaboardContent.getText().length() == 0) {
                        Toast.makeText(v.getContext(),"내용을 입력해주세요.",Toast.LENGTH_SHORT).show();

                    } else if (boardImagePath == null) {

                        // 1. 투표가 표함되는지 안되는지 선 체크
                        // 2. 투표가 있다면 이미지 / 텍스트 중 어떤 투표인지 -> 투표는 다른 디비로 보낼것 이기 때문
                        // 3. 투표가 없다면
                        // 이 세가지를 구별해야 할듯

                        // 투표가 존재한다면 투표객체도 넘겨주어야한다.
                        // 투표 경우에 따른 예외처리 해줘야 한다.

                        if (voteFlag == 0) {

                            // 투표가 존재하는 경우.

                            if (voteSelect == 0) {

                                // 텍스트 투표 인 경우.

                                for (int i = 0; i < editModelArrayList.size(); i++) {

                                    // 텍스트 투표 예외처리 부분.

                                    if (editModelArrayList.get(i).getEditTextValue().length() == 0) {
                                        Toast.makeText(v.getContext(),i+1+"번째 내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
                                    }
                                    voteText.add(editModelArrayList.get(i).getEditTextValue());
                                }

                                qnaItem = new QnaItem(accountNo, voteFlag, tag, et_qnaboardTitle.getText().toString(),
                                        et_qnaboardContent.getText().toString(), voteText, voteSelect);

                                qnaBoardPresenter.enrollmentBoardReq(qnaItem);

                                showLoading();
                            } else if (voteSelect == 1) {

                                // 이미지 투표 인 경우.

                                qnaItem = new QnaItem(accountNo, voteFlag, tag, et_qnaboardTitle.getText().toString(),
                                        et_qnaboardContent.getText().toString(), voteSelect, voteImage);

                                qnaBoardPresenter.enrollmentBoardReq(qnaItem);

                                showLoading();
                            }
                        } else if (voteFlag == 1) {

                            // 투표가 존재하지 않는 경우.


                            qnaItem = new QnaItem(accountNo, voteFlag, tag, et_qnaboardTitle.getText().toString(),
                                    et_qnaboardContent.getText().toString());

                            qnaBoardPresenter.enrollmentBoardReq(qnaItem);
;
                            showLoading();
                        }

                    } else {


                        // 1. 투표가 표함되는지 안되는지 선 체크
                        // 2. 투표가 있다면 이미지 / 텍스트 중 어떤 투표인지 -> 투표는 다른 디비로 보낼것 이기 때문
                        // 3. 투표가 없다면
                        // 이 세가지를 구별해야 할듯?



                        if (voteFlag == 0) {
                            if (voteSelect == 0) {

                                for (int i = 0; i < editModelArrayList.size(); i++) {

                                    // 텍스트 투표 예외처리 부분.

                                    if (editModelArrayList.get(i).getEditTextValue().length() == 0) {
                                        Toast.makeText(v.getContext(),i+1+"번째 내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
                                    }
                                    voteText.add(editModelArrayList.get(i).getEditTextValue());
                                }

                                qnaItem = new QnaItem(accountNo, voteFlag, tag, et_qnaboardTitle.getText().toString(),
                                        et_qnaboardContent.getText().toString(), boardImagePath, voteText, voteSelect);

                                qnaBoardPresenter.enrollmentBoardReq(qnaItem);

                                showLoading();
                            } else if (voteSelect == 1) {

                                qnaItem = new QnaItem(accountNo, voteFlag, tag, et_qnaboardTitle.getText().toString(),
                                        et_qnaboardContent.getText().toString(), boardImagePath, voteSelect, voteImage);

                                qnaBoardPresenter.enrollmentBoardReq(qnaItem);

                                showLoading();
                            }
                        } else if (voteFlag == 1) {

                            qnaItem = new QnaItem(accountNo, voteFlag, tag, et_qnaboardTitle.getText().toString(),
                                    et_qnaboardContent.getText().toString(), boardImagePath);

                            qnaBoardPresenter.enrollmentBoardReq(qnaItem);

                            showLoading();
                        }
                    }
                } else if (actionKind == 1) {
                    // receiveQnaBoardItem 수정되면 모델로 넘겨서 서버에 저장
                    if (tag.equals(null)) {
                        Toast.makeText(v.getContext(),"태그 값을 선택해주세요.",Toast.LENGTH_SHORT).show();
                    } else if (et_qnaboardTitle.getText().length() == 0) {
                        Toast.makeText(v.getContext(),"제목을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    } else if (et_qnaboardContent.getText().length() == 0) {
                        Toast.makeText(v.getContext(),"내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    }

                    try {
                        receiveQnaBoardItem.setTag(tag);
                        receiveQnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
                        receiveQnaBoardItem.setContent(et_qnaboardContent.getText().toString());
                        receiveQnaBoardItem.setImage(boardImagePath);

                        qnaBoardPresenter.modifyBoardReq(receiveQnaBoardItem);

                        showLoading();
                    }catch (NullPointerException e) {
                        receiveQnaBoardItem.setTag(tag);
                        receiveQnaBoardItem.setTitle(et_qnaboardTitle.getText().toString());
                        receiveQnaBoardItem.setContent(et_qnaboardContent.getText().toString());

                        qnaBoardPresenter.modifyBoardReq(receiveQnaBoardItem);

                        showLoading();
                    }

                }

                break;
            case R.id.qnaboardVoteBtn:
                iv_qnaboardVoteBtn.setVisibility(View.GONE);

                bottomSheetDialogVoteSelect = BottomSheetDialogVoteSelect.getInstance().getInstance();
                bottomSheetDialogVoteSelect.setDialogListener(new BottomSheetDialogVoteSelect.BottomSheetDialogVoteSelectDialoggListener() {
                    @Override
                    public void onSelectVoteStatus(int voteSelectFromDialog) {
                        iv_qnaboardVoteBtn.setClickable(false);

                        voteSelect = voteSelectFromDialog;

                        if (voteSelect == 0) {
                            ll_qnaVoteTextLayout.setVisibility(View.VISIBLE);

                        } else {
                            ll_qnaVoteImageLayout.setVisibility(View.VISIBLE);
                            // 다중 이미지 테스트 성공.
                            // 리싸이클러뷰로 이미지 최대 5개 보여지는지 데모앱아닌 여기서 테스트 해봐야함.
                            // 라디오버튼 클릭되면 앨범으로 바로 이동.
                            // 5개로 제한두도록 메시지 처리해줘야함.
                            // 이미지 셀렉하면 셀렉된 이미지 리싸이클러뷰로 보여지도록 처리해야함.
                            // 여기까지 완료된다면 예외처리부분 처리해준다.
                            // 다 된다면 mvp로 서버로 보내주는 처리를 해준다면 작성하는 곳은 완료.

                            FishBun.with(QnaBoardActivity.this).setImageAdapter(new GlideAdapter()).setMaxCount(6).setMinCount(2).startAlbum();
                        }
                    }

                    @Override
                    public void noVotesoReturn(boolean flag) {
                        if (flag == true) {
                            iv_qnaboardVoteBtn.setVisibility(View.VISIBLE);
                        } else {

                        }
                    }
                });
                bottomSheetDialogVoteSelect.show(getSupportFragmentManager(), "bottomSheet");

                voteFlag = 0;
                break;
            case R.id.qnaboardAddBtn:
                count++;
                if(count > 6) {
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
                            if (arrayList.size() == 0) {
                                tv_qnaboardBeforeTag.setVisibility(View.VISIBLE);
                                h_scrollView.setVisibility(View.GONE);

                                if (tagArray.size() > 0) {
                                    tv_qnaboardBeforeTag.setVisibility(View.GONE);
                                    h_scrollView.setVisibility(View.VISIBLE);
                                }
                            } else {
                                tv_qnaboardBeforeTag.setVisibility(View.GONE);
                                h_scrollView.setVisibility(View.VISIBLE);
                                tagArray = arrayList;
                            }

                            qnaBoardTagAdapter = new QnaBoardTagAdapter(v.getContext(), tagArray, 0, new QnaBoardTagAdapter.ClickListener(){
                                @Override
                                public void ListClick(int position, View view) {
                                    tag ="";
                                    tagArray.remove(position);
                                    qnaBoardTagAdapter.notifyDataSetChanged();
                                    for (int i=0; i<tagArray.size(); i++) {
                                        if (i == tagArray.size() - 1)
                                            tag += tagArray.get(i);
                                        else if (i < tagArray.size())
                                            tag += tagArray.get(i) + "@##@";
                                    }
                                }
                            });
                            rc_qnaboardTag.setAdapter(qnaBoardTagAdapter);
                            rc_qnaboardTag.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                            for (int i = 0; i < tagArray.size(); i++) {
                                if (i == tagArray.size() - 1)
                                    tag += tagArray.get(i);
                                else if (i < tagArray.size())
                                    tag += tagArray.get(i) + "@##@";
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
                            if (arrayList.size() == 0) {
                                tv_qnaboardBeforeTag.setVisibility(View.VISIBLE);
                                h_scrollView.setVisibility(View.GONE);
                            } else {
                                tv_qnaboardBeforeTag.setVisibility(View.GONE);
                                h_scrollView.setVisibility(View.VISIBLE);
                            }

                            qnaBoardTagAdapter.data = arrayList;
                            qnaBoardTagAdapter.notifyDataSetChanged();
                            tagArray = arrayList;

                            if (!tag.equals(""))
                                tag = "";

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
                qnaBoardPresenter.deleteBoardReq(receiveQnaBoardItem.getPostNo());
                showLoading();
                break;
            case R.id.spaceLayout:
                keyboard = new Keyboard(v.getContext());
                keyboard.showKeyboard(et_qnaboardContent);
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

                    path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    if (!boardImageSelect) {
                        if (!reSelectVoteImage) {

                            for (int i=0; i<path.size(); i++) {
                                // path안의 값들을 이름 절대경로 뽑아서 저장하자

                                Uri imageUri = path.get(i);
                                String[] filePath = { MediaStore.Images.Media.DATA };
                                Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
                                cursor.moveToFirst();
                                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                                // 리사이징 부분
                                Bitmap bitmap = resizeBitmap(imagePath,324,186);
                                bitmap = ExifUtils.rotateBitmap(imagePath,bitmap);

                                cursor = getContentResolver().query(getImageUri(this,bitmap), filePath, null, null, null);
                                cursor.moveToFirst();
                                imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                                voteImage.add(imagePath);
                                File file = new File(voteImage.get(i));
                                Uri test = Uri.fromFile(file);

                                gridVoteItem = new GridVoteItem(true, "", voteImage.get(i), 0);
                                gridVoteItemArrayList.add(gridVoteItem);
                            }

                            if (path.size() < 6) {
                                Uri uriTest = Uri.parse("");

                                gridVoteItem = new GridVoteItem(false, "항목추가", uriTest, 1);
                                gridVoteItemArrayList.add(gridVoteItem);
                            }

                            voteImageAdapter = new VoteImageAdapter(this, gridVoteItemArrayList, this);
                            gridView.setAdapter(voteImageAdapter);
                        } else {
                            // 이미지 path + grid 합쳐서 6이면 항목추가 항목 삭제하기
                            // 6보다 작으면 먼저 항목추가 아이템 삭제 후, 이미지 추가작업 후에 마지막 아이템에 항목추가 추가하기

                            // 이미지가 최대인 6장일 경우
                            if (path.size() + gridVoteItemArrayList.size() == 6) {

                                for (int i = 0; i < path.size(); i++) {
                                    Uri imageUri = path.get(i);
                                    String[] filePath = {MediaStore.Images.Media.DATA};
                                    Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
                                    cursor.moveToFirst();
                                    String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                                    // 리사이징 부분
                                    Bitmap bitmap = resizeBitmap(imagePath,324,186);
                                    bitmap = ExifUtils.rotateBitmap(imagePath,bitmap);

                                    cursor = getContentResolver().query(getImageUri(this,bitmap), filePath, null, null, null);
                                    cursor.moveToFirst();
                                    imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                                    voteImage.add(imagePath);

                                    File file = new File(voteImage.get(voteImage.size() - 1));
                                    Uri test = Uri.fromFile(file);
                                    gridVoteItem = new GridVoteItem(true, "", voteImage.get(gridVoteItemArrayList.size()), 0);
                                    gridVoteItemArrayList.add(gridVoteItem);
                                }
                            }
                            // 이미지가 6장보다 작을 경우.
                            else {
                                for (int i = 0; i < path.size(); i++) {
                                    Uri imageUri = path.get(i);
                                    String[] filePath = {MediaStore.Images.Media.DATA};
                                    Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
                                    cursor.moveToFirst();
                                    String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                                    // 리사이징 부분
                                    Bitmap bitmap = resizeBitmap(imagePath,324,186);
                                    bitmap = ExifUtils.rotateBitmap(imagePath,bitmap);

                                    cursor = getContentResolver().query(getImageUri(this,bitmap), filePath, null, null, null);
                                    cursor.moveToFirst();
                                    imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                                    voteImage.add(imagePath);

                                    File file = new File(voteImage.get(voteImage.size() - 1));
                                    Uri test = Uri.fromFile(file);

                                    gridVoteItem = new GridVoteItem(true, "", voteImage.get(gridVoteItemArrayList.size()), 0);
                                    gridVoteItemArrayList.add(gridVoteItem);
                                }

                                Uri uriTest = Uri.parse("");
                                gridVoteItem = new GridVoteItem(false, "항목추가", uriTest, 1);
                                gridVoteItemArrayList.add(gridVoteItem);

                            }
                            voteImageAdapter.notifyDataSetChanged();

                            reSelectVoteImage = false;
                        }

                        // 여기서 받아온 이미지 gridview 사용해서 이미지 뿌려주는 것을 적용하자.
                        // gridview item은 텍스트뷰 (항목 숫자식별용) 이미지뷰로 구성해서 만들자.

                    } else {
                        Uri imageUri = path.get(0);
                        String[] filePath = { MediaStore.Images.Media.DATA };
                        Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
                        cursor.moveToFirst();
                        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                        // 리사이징 부분
                        Bitmap bitmap = resizeBitmap(imagePath,324,186);
                        bitmap = ExifUtils.rotateBitmap(imagePath,bitmap);

                        cursor = getContentResolver().query(getImageUri(this,bitmap), filePath, null, null, null);
                        cursor.moveToFirst();
                        imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                        imageLayout.setVisibility(View.VISIBLE);

                        boardImagePath = imagePath;
                        File file = new File(boardImagePath);
                        Uri test = Uri.fromFile(file);
                        Glide.with(this)
                                .load(test)
                                .fitCenter()
                                .into(iv_qnaboardImage);

                        cursor.close();

                        boardImageSelect = false;


                    }
                    break;
                }
        }
    }

    public Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Soool", null);
        return Uri.parse(path);
    }

    // Model에서 서버로 부터 받은 응답 값을 받아서 어떻게 처리할 지 결정하는 메서드.
    @Override
    public void enrollmentBoardRespGoToView(int response, QnaBoardItem qnaBoardItemResponse) {
        hideLoading();
        switch (response) {

            /*
             response = 1 -> 인서트 성공
             response = 2 -> 콜백 실패
             response = 3 -> 전송 실패
            */

            case 1:
                Toast.makeText(this, "게시물 작성 성공", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("qnaBoardItem", qnaBoardItemResponse);
                intent.putExtra("actionKind",actionKind);
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

    }

    @Override
    public void modifyBoardRespGoToView(int response, QnaBoardItem qnaBoardItem) {
        hideLoading();
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
        hideLoading();
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

    }

    @Override
    public void onListImageBtnClick(int position) {
        if (!loading) {
            // 그리드 뷰 버튼 클릭 리스너
            // 그리드 뷰 아이템 (이미지 및 스트링) 삭제해야함.
            voteImage.remove(position);
            gridVoteItemArrayList.remove(position);

            if (gridVoteItemArrayList.size() < 6) {
                Uri uriTest = Uri.parse("");
                gridVoteItem = new GridVoteItem(false, "항목추가", uriTest, 1);
                gridVoteItemArrayList.add(gridVoteItem);
            }
            voteImageAdapter.notifyDataSetChanged();
        }
        else {
        }
    }

    @Override
    public void onListLayoutClick(int position) {
        if (!loading) {
            if (gridVoteItemArrayList.get(position).getImageIden() == 1) {
                reSelectVoteImage = true;
                gridVoteItemArrayList.remove(position);
                voteImageAdapter.notifyDataSetChanged();

                FishBun.with(this).setImageAdapter(new GlideAdapter()).setMaxCount(6 - voteImage.size()).startAlbum();
            }
        }
        else {
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String contentText = s.toString();

        if (contentText.length() > 0)
            tv_drawupEnroll.setTextColor(Color.parseColor("#0ba14a"));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onBackPressed()
    {
        confirmCancel();
    }
    public void confirmCancel()
    {
        View.OnClickListener positiveListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        };

        View.OnClickListener negativeListener = new View.OnClickListener() {
            public void onClick(View v) {
                CanclenoticeDialog.dismiss();
            }
        };

        CanclenoticeDialog = new NoticeDialog(this,
                "작성 중인 글을 취소하시겠습니까?\n 취소 시, 작성 중인 글은 저장되지 않습니다.", false, "예",
                "아니요", positiveListener, negativeListener);
        CanclenoticeDialog.show();
    }
}

