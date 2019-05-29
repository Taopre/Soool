package com.example.taopr.soool.View;

import android.content.Intent;
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

import com.example.taopr.soool.Adapter.ProfileSpinnerAdapter;
import com.example.taopr.soool.Object.ChangeProfile;
import com.example.taopr.soool.Presenter.ProfilePresenter;
import com.example.taopr.soool.R;
import com.sangcomz.fishbun.FishBun;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private final String TAG = "마이 프로필 액티비티";

    private ImageView subActionBarLeftImage ;
    private TextView subActionBarRight;
    private TextView subActionBarLeft ;
    private TextView subActionBarRight2 ;
    private ProfileSpinnerAdapter<CharSequence> favoriteAdapter;
    private ProfileSpinnerAdapter<CharSequence> capacityAdapter;
    private ProfileSpinnerAdapter<CharSequence> reasonAdapter;
    private ProfilePresenter profilePresenter;

    private ChangeProfile changeProfile;

    int accountNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        profilePresenter = new ProfilePresenter();
        profilePresenter.setView(this);

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


    // 유저 프로필 정보를 성공적을 받아온 후 정보를 뷰로 보여주기
    // 받아온 유저 프로필 정보에서 유저가 수정한 내용이 있을 경우에만 '저장하기' 버튼을 활성화
    @Override
    public void getChangeProfileSuccess(ChangeProfile changeProfile) {
        profileAcEmail.setText(changeProfile.getAccountEmail());
        profileAcNick.setText(changeProfile.getAccountNick());
        profileAcPW.setText(changeProfile.getAccountPw());
        this.changeProfile = changeProfile;
        spinnerSetting();
    }

    @Override
    public void getDataFail(int dataKind) {

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
                finish();
                break;
            case R.id.subActionBarRight:
                //TODO: 저장하기 버튼 ChangeProfile 객체를 전송

                break;
        }
    }


    // 프로필 이미지 변경하는 부분
    // 이미지나 '프로필 사진 변경하기' 버튼 모두 클릭 했을 때 갤러리로 넘어가게 설정
    // 이미지는 하나만 선택이 가능하게 설정

    @OnClick({R.id.profileChangeImage,R.id.profileImage})
    public void ChangeImage(View view){
        //FishBun.with(ProfileActivity.this)
    }


    // 스피너
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
        capacitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if(changeProfile.getCapacity() == i){
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
        favoriteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(changeProfile.getFavorite() == i){
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
        reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(changeProfile.getReason() == i){
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
