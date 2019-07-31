package com.lpky.taopr.soool.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.lpky.taopr.soool.Dialog.CalendarDialogFragment;
import com.lpky.taopr.soool.Object.CalendarItem;
import com.lpky.taopr.soool.Presenter.CalendarAddSchPresenter;
import com.lpky.taopr.soool.R;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarAddSchActivity extends CalendarSchBaseActivity
        implements View.OnClickListener,CalendarAddSchPresenter.View, CalendarDialogFragment.CalendarDialogListener {


    @BindView(R.id.addSchTextContent)
    EditText addSchTextContent;
    TextView subActionBarRight;
    TextView subActionBarLeft ;
    ImageView subActionBarLeftImage;
    private CalendarAddSchPresenter calendarAddSchPresenter;

    private final String TAG ="캘린더 추가 Sch 액티비티";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        setContentView(R.layout.activity_calendar_add_sch,1);
        ButterKnife.bind(this);


        // 전달 받아온 intent 가 없을 예외상황 대비
        if(getIntent()!=null) {
            receiveSelectDay();
            showSelectedDayEventContent();

        }

        calendarAddSchPresenter = new CalendarAddSchPresenter(this.mActivity,this,accountNo);
        calendarAddSchPresenter.setView((CalendarAddSchPresenter.View) this);

        // 내용을 작성할 경우 조건에 따라서 '저장하기' 버튼을 활성화
        // 조건은 activateSaveButton 함수에서 설명하고 있음

        addSchTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                activateSaveButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    // 달력 다이얼로그와 액티비티 사이 인터페이스 역할을 하는 콜백 리스너
    // 달력 다이얼로그에서 날짜 선택 시 , 선택한 날짜에 맞춰 뷰를 동기화( 선택한 날짜, 선택한 이전 날짜, 다음 날짜 등등 )

    @Override
    public void onDateClicked(Date selectedDate) {
        selectedDay = selectedDate;
        onDateChanged();
    }


    // 날짜 변환 시 선택된 날짜, 선택된 날짜의 이전 날, 다음 날 동기화 해주고
    // 선택된 날짜에 작성했던 내용이 있다면 edittext 로 보여준다

    private void onDateChanged(){
        daySetting();
        showSelectedDayEventContent();
        activateSaveButton();
    }


    // 이전에 작성했던 내용이 있던 없건, 작성한 내용이 없으면 저장하기 버튼은 비활성화
    // 수정한 내용이 이전에 작성한 내용이 일치할 경우 '저장하기' 버튼 비활성화
    // 수정한 내용이 이전에 작성한 내용이 불일치 할 경우 '저장하기' 버튼 활성화
    // 이전에 작성한 내용이 없고, 작성한 내용이 있을 경우 '저장하기' 버튼 활성화

   // @OnTextChanged({R.id.addSchTextContent})

    public void activateSaveButton(){

        if(addSchTextContent.getText().length()>0) {
            if (calendarItemMap.get(selectedDayDate) != null) { // 이전에 작성했던 내용이 있다면
                Log.i(TAG, "activateSaveButton: 이전에 작성한 내용 : " +calendarItemMap.get(selectedDayDate) +
                "//  현재 작성한 내용 : " + addSchTextContent.getText().toString());
                if (addSchTextContent.getText().toString().equals(calendarItemMap.get(selectedDayDate).getEventContent())) {
                    // 수정한 내용과 이전에 작성한 내용이 일치
                    subActionBarRight.setClickable(false);
                    subActionBarRight.setTextColor(ContextCompat.getColor(CalendarAddSchActivity.this,R.color.grayMid));
                } else {
                    subActionBarRight.setClickable(true);
                    subActionBarRight.setTextColor(ContextCompat.getColor(CalendarAddSchActivity.this,R.color.greenDark));
                }
            } else{
                subActionBarRight.setClickable(true);
                subActionBarRight.setTextColor(ContextCompat.getColor(CalendarAddSchActivity.this,R.color.greenDark));
            }
        }
        else{

            subActionBarRight.setClickable(false);
            subActionBarRight.setTextColor(ContextCompat.getColor(CalendarAddSchActivity.this,R.color.grayMid));
        }
    }


    // 선택한 날짜에 작성한 내용이 있다면 내용을 보여주고
    // 없으면 내용을 작성해달라는 메세지 표시
    // 날짜가 변경했을 경우에는 작성하던 내용을 초기화
    private void showSelectedDayEventContent() {

        if(calendarItemMap.get(selectedDayDate)!=null){

            addSchTextContent.setText(calendarItemMap.get(selectedDayDate).getEventContent());
        }
        else{

            addSchTextContent.setHint(R.string.calendar_hint_write_sch);
            addSchTextContent.setText("");
        }
    }

    @OnClick({R.id.calendarPreviousDay,R.id.calendarNextDay,R.id.calendarSelectedDay,R.id.calendarSelectedMonthYear})
    public void bindOnClick(View view){
        switch (view.getId()) {
            case R.id.calendarPreviousDay:
                // 이전 날로 이동 selectedDay 값을 PreviousDay 로 변경 후
                // daySetting() 함수 통해서 이전 날, 다음 날 동기화
                selectedDay = previousDay;
                onDateChanged();
                break;
            case R.id.calendarNextDay:
                // 다음날로 이동 selectedDay 값을 nextDay 로 변경 후
                // daySetting() 함수 통해서 이전 날, 다음 날 동기화
                selectedDay = nextDay;
                onDateChanged();
                break;
            case R.id.calendarSelectedDay: /// 선택한 날짜
                // 이 부분은 아직 생각안함
                break;

            case R.id.calendarSelectedMonthYear:
                // 달력을 담고 있는 다이얼로그를 띄우고 날짜 선택 값을 받아온다
                // 날짜를 받아온후 받아온 날짜를 selectedDay에 넣어준후 이전 날, 다음 날 동기화해준다
                CalendarDialogFragment calendarSchDialogFragment = CalendarDialogFragment.newInstance(
                        selectedDay.getTime(),calendarItemMap
                );

                calendarSchDialogFragment.show(getSupportFragmentManager(), "test-simple-calendar");
                break;

        }
    }


    // 액션바 클릭 이벤트
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subActionBarRight:
                //eventSave();
                calendarAddSchPresenter.eventSave(addSchTextContent.getText().toString(),selectedDay,calendarItemMap);
                break;
            case R.id.subActionBarLeft:
                mActivity.finish();
            case R.id.subActionBarLeftImage:
                mActivity.finish();
        }
    }

    @Override
    public void onBackPressed() {
        mActivity.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.
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

        subActionBarLeftImage.setVisibility(View.GONE);
        subActionBarLeft.setText(R.string.all_button_cancel);
        subActionBarLeft.setVisibility(View.VISIBLE);
        subActionBarRight.setText(R.string.all_button_save);
        subActionBarLeft.setOnClickListener(this);
        subActionBarRight.setOnClickListener(this);

        activateSaveButton();

        return true;
    }

    // 저장한 후에 작성한 내용 hashMap에 추가해준 후 hashmap과 작성한 날짜 long 타입 변수로 intent에 담아서 전달
    @Override
    public void getDataSuccess(HashMap<String, CalendarItem> calendarItemMap) {
        this.calendarItemMap = calendarItemMap;
        Intent intent = new Intent();
        Log.i(TAG, "getDataSuccess: 작성한 날짜" + selectedDay.getTime());
        intent.putExtra(DATE_EXTRA,selectedDay.getTime());
        intent.putExtra(CALENDAR_LIST_EXTRA,(Serializable) calendarItemMap);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void getDataFail(String message) {

    }
}
