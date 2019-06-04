package com.example.taopr.soool.View;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taopr.soool.Adapter.ProfileSpinnerAdapter;
import com.example.taopr.soool.Object.ProfileInfo;
import com.example.taopr.soool.Presenter.ProfilePresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.Whatisthis;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.ImageAdapter;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTextChanged;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, ProfilePresenter.View{

    @BindView(R.id.profileAccountEmail)
    EditText profileAcEmail;
    @BindView(R.id.profileAccountNick)
    EditText profileAcNick;
    @BindView(R.id.profileAccountPW)
    EditText profileAcPW;
    @BindView(R.id.profileCapacity)
    Spinner capacitySpinner;
    @BindView(R.id.profileFavoriteDrinking)
    Spinner favoriteSpinner;
    @BindView(R.id.profileReasonOfDrinking)
    Spinner reasonSpinner;
    @BindView(R.id.profileImage)
    ImageView profileImage;

    private final String TAG = "마이 프로필 액티비티";

    private ImageView subActionBarLeftImage ;
    private TextView subActionBarRight;
    private TextView subActionBarLeft ;
    private TextView subActionBarRight2 ;
    private ProfileSpinnerAdapter<CharSequence> favoriteAdapter;
    private ProfileSpinnerAdapter<CharSequence> capacityAdapter;
    private ProfileSpinnerAdapter<CharSequence> reasonAdapter;
    private ProfilePresenter profilePresenter;
    private int capacitySpiPosition,favoriteSpiPosition, reasonSpiPosition;

    private boolean isChangeProfile = false; // 프로필 정보를 수정했다면 true, 아니라면 false

    private ProfileInfo profileInfo;

    int accountNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(Color.rgb(255,255,255)); // 원형 백그라운드 색상 (디폴트 검정색)
        profileImage.setBackground(drawable);
        profileImage.setClipToOutline(true);

        profilePresenter = new ProfilePresenter();
        profilePresenter.setView(this,this);

        getProfileIntent();
    }

    // 인텐트 받는 부분
    // 인텐트에서 유저의 회원넘버를 전달 받아
    // 서버에 전송해 유저의 프로필 정보를 받는다
    private void getProfileIntent(){
        Intent intent = getIntent();
        accountNo = intent.getIntExtra("accountNo",0);
        profilePresenter.getProfileInfo(accountNo);

    }


    // 저장한 프로필 이미지가 있을 경우에는 서버 경로를 통해 뷰로 보여주고
    // 그렇기 않은 경우에는 기본 이미지를 보여준다

    private void showProfileImage(String accountImage){
        Log.i(TAG, "showProfileImage: ");
        if(!accountImage.equals("soool_default")) {
            Log.i(TAG, "showProfileImage: 다름");
            String imageUri = Whatisthis.serverIp + accountImage;
            Glide.with(this)
                    .load(imageUri)
                    .centerCrop()
                    .into(profileImage);
        }
        else{
            profileImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.profile_default));
        }
    }


    // 유저 프로필 정보를 성공적을 받아온 후 정보를 뷰로 보여주기
    // 받아온 유저 프로필 정보에서 유저가 수정한 내용이 있을 경우에만 '저장하기' 버튼을 활성화
    @Override
    public void getProfileInfoSuccess(ProfileInfo profileInfo) {
        profileAcEmail.setText(profileInfo.getAccountEmail());
        profileAcNick.setText(profileInfo.getAccountNick());
        profileAcPW.setText(profileInfo.getAccountPw());
        if(!profileInfo.getAccountImage().equals("soool_default")){
            showProfileImage(profileInfo.getAccountImage());
        }
        this.profileInfo = profileInfo;
        this.capacitySpiPosition = profileInfo.getCapacity();
        this.favoriteSpiPosition = profileInfo.getFavorite();
        this.reasonSpiPosition = profileInfo.getReason();

        spinnerSetting();
    }

    // 서버로 부터 데이터를 전달받는데 오류가 생긴 경우
    // 0 => 프로필 정보 가져오기
    // 1 => 프로필 이미지 수정하기
    // 2 => 프로필 이미지 삭제하기
    // 3 => 프로필 정보 수정하기
    @Override
    public void getDataFail(int dataKind) {
        // TODO: 서버로 부터 Response 받는 것에 실패 했을 때 어떻게 처리할지
    }

    // 프로필 이미지를 서버에 성공적으로 저장했다면
    // 변경한 이미지를 보여주는 부분.
    @Override
    public void changeProfileImgSuccess(String accountImage) {
        isChangeProfile = true;
        profileInfo.setAccountImage(accountImage);
        showProfileImage(accountImage);
    }

    // 회원 정보 수정이 성공적으로 이루어졌을 때
    // profileInfo 변수를 변경하고 isChangeProfile 변수에 true 값을 넣어줌으로써
    // 프로필에 변화가 있다는 걸 알려준다
    // 그리고 액티비티가 finish 될때 intent 에 profileInfo 변수를 답아야 하기 때문에

    @Override
    public void changeProfileInfoSuccess(ProfileInfo changedProfileInfo) {
        isChangeProfile = true;
        this.profileInfo = changedProfileInfo;
        Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show();
        activateSaveButton(0);
    }

    @Override
    public void deleteProfileImgSuccess() {
        isChangeProfile = true;
        profileInfo.setAccountImage("soool_default");
        showProfileImage("soool_default");
    }


    // 저장하기 버튼 동기화 비동기화 처리 하는 부분
    // 작성 폼에서 작성 이벤트나 스피너에서 클릭 이벤트 발생 시
    // 유저가 작성한 내용 기존에 서버에 저장한 내용과 일치하지 않은 경우( isModify = 1)
    // '저장하기' 버튼 활성화를 해서 저장이 가능하도록
    // 그렇지 않을 경우 비활성화 ( isModify = 0 )
    private void activateSaveButton(int isModify){
        switch (isModify){
            case 0:
                subActionBarRight.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.grayMid));
                subActionBarRight.setClickable(false);
                break;
            case 1:
                subActionBarRight.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.greenDark));
                subActionBarRight.setClickable(true);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profilePresenter.onUnSubscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        profilePresenter.clearSubscribe();
    }

    // oncreate 시에는 유저가 변경한 내용이 없을 것이기 때문에 '저장하기' 버튼을 비활성화
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.
        actionBar.setElevation(0); // 액션바 하단 그림자 없애기

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.sub_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        subActionBarRight = findViewById(R.id.subActionBarRight);
        subActionBarLeft = findViewById(R.id.subActionBarLeft);

        subActionBarLeft.setText(R.string.all_button_cancel);
        subActionBarRight.setText(R.string.all_button_save);

        subActionBarLeft.setOnClickListener(this);
        subActionBarRight.setOnClickListener(this);

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.subActionBarLeft:
                pageFinish();
                break;
            case R.id.subActionBarRight:
                ProfileInfo changeProfileInfo = new ProfileInfo(profileAcEmail.getText().toString(),
                        profileAcNick.getText().toString(),profileInfo.getAccountImage(),
                        profileAcPW.getText().toString(),capacitySpiPosition,favoriteSpiPosition,reasonSpiPosition);

                profilePresenter.changeProfileInfo(changeProfileInfo);

                break;
        }
    }

    @Override
    public void onBackPressed() {
        pageFinish();
    }

    // 페이지를 종료할 때 프로필을 변경한 사항이 있다면 ( isChangeProfile = true )
    // intent 에 RESULT_OK 값과 profileInfo 값을 전달한다
    // 변경한 사항이 없다면 finish(); ( isChangeProfile = false )

    private void pageFinish(){
        if (isChangeProfile){
            Intent intent = new Intent();
            intent.putExtra("profileInfo", profileInfo);
            setResult(RESULT_OK, intent);
            finish();
        }
        else{
            finish();
        }
    }

    // 프로필 이미지 변경하는 부분
    // 이미지나 '프로필 사진 변경하기' 버튼 모두 클릭 했을 때 갤러리로 넘어가게 설정
    // 이미지는 하나만 선택이 가능하게 설정

    @OnClick({R.id.profileChangeImage,R.id.profileImage})
    public void ChangeImage(){
        FishBun.with(ProfileActivity.this)
                .setImageAdapter(new GlideAdapter())
                .setMinCount(0)
                .setMaxCount(1)
                .setActionBarColor(ContextCompat.getColor(ProfileActivity.this,R.color.greenMain))
                .startAlbum();
    }

    // 프로필 이미지 삭제부분
    @OnLongClick({R.id.profileChangeImage,R.id.profileImage})
    public boolean deleteImage(){

        // TODO: 이미지를 길게 눌렀을 때 삭제하겠냐고 묻는 다이얼로그를 띄워주고
        // TODO: 삭제하겠다 했을 때 프로필 이미지를 서버에서 삭제
        profilePresenter.deleteProfileImg();

        return true;
    }


    // 사용자가 이미지 선택을 했을 경우 다이얼로그를 띄워 프로필 이미지 변경을 할 것인지 묻는다.
    // 이미지 변경을 응했을 때 presenter 에 이미지 URI 를 전송한다
    // presenter 에서 서버에 이미지 file 과 accountNo 를 전송해 서버에 프로필 이미지를 저장하고
    // 저장이 성공적으로 이루어졌다면 프로필 이미지의 서버에 저장한 경로를 보낸다
    // 경로를 받아 뷰로 보여준다

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Define.ALBUM_REQUEST_CODE:
                    ArrayList<Uri> path = imageData.getParcelableArrayListExtra(Define.INTENT_PATH);
                    Uri imageUri = path.get(0);

                    profilePresenter.changeProfileImage(imageUri);
                    break;
            }
        }
    }

    // 이메일 , 닉네임 , 비밀번호를 수정한 값이 기존에 저장한 값과 일치할 경우에는
    // '저장하기' 버튼 비활성화
    // 일치하지 않을 경우네는
    // '저장하기' 버튼 활성화
    @OnTextChanged(R.id.profileAccountEmail)
    void onEditEmail(CharSequence text){
        if (profileInfo != null) {
            Log.i(TAG, "onEditEmail: " + text +" // " + profileInfo.getAccountEmail());
            if (profileAcEmail.getText().toString().equals(profileInfo.getAccountEmail())) {
                activateSaveButton(0);
                Log.i(TAG, "onEditEmail: same");
            }
            else {
                activateSaveButton(1);
                Log.i(TAG, "onEditEmail: diff");
            }
        }
    }
    @OnTextChanged(R.id.profileAccountNick)
    void onEditNick(CharSequence text){

        if(profileInfo != null) {
            Log.i(TAG, "onEditNick: " + text +" // " + profileInfo.getAccountNick());
            if (text.equals(profileInfo.getAccountEmail()))
                activateSaveButton(0);
            else
                activateSaveButton(1);
        }
    }
    @OnTextChanged(R.id.profileAccountPW)
    void onEditPW(CharSequence text){
        if(profileInfo != null) {
            Log.i(TAG, "onEditPW: " + text + " // " + profileInfo.getAccountPw());
            if (text.equals(profileInfo.getAccountEmail()))
                activateSaveButton(0);
            else
                activateSaveButton(1);
        }
    }


    // 스피너
    // 기존에 리스트에서 선택한 값이 있다면 보여주기
    // 스피너에서 아이템 클릭있는 경우
    // 선택한 값이 기존에 작성했던 프로필이랑 같을 경우
    // activateSaveButton() 에 0 값 전달
    // 선택한 값이 기존에 작성했던 프로필이랑 다를 경우
    // activateSaveButton() 에 1 값 전달

    private void spinnerSetting() {
        String[] favoriteList = getResources().getStringArray(R.array.favorite_array);
        String[] capacityList = getResources().getStringArray(R.array.capacity_array);
        String[] reasonList = getResources().getStringArray(R.array.reason_array);

        capacityAdapter = new ProfileSpinnerAdapter<CharSequence>(ProfileActivity.this,capacityList);
        capacitySpinner.setAdapter(capacityAdapter);
        capacitySpinner.setSelection(capacitySpiPosition);
        capacitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                capacitySpiPosition = i;
               if(profileInfo.getCapacity() == i){
                   activateSaveButton(0);
               }
               else{
                   activateSaveButton(1);
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        favoriteAdapter = new ProfileSpinnerAdapter<CharSequence>(ProfileActivity.this,favoriteList);
        favoriteSpinner.setAdapter(favoriteAdapter);
        favoriteSpinner.setSelection(favoriteSpiPosition);
        favoriteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                favoriteSpiPosition = i;
                if(profileInfo.getFavorite() == i){
                    activateSaveButton(0);
                }
                else{
                    activateSaveButton(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        reasonAdapter = new ProfileSpinnerAdapter<CharSequence>(ProfileActivity.this,reasonList);
        reasonSpinner.setAdapter(reasonAdapter);
        reasonSpinner.setSelection(reasonSpiPosition);
        reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reasonSpiPosition = i;
                if(profileInfo.getReason() == i){
                    activateSaveButton(0);
                }
                else{
                    activateSaveButton(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}

