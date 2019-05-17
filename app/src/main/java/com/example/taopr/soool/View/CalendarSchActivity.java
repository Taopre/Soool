package com.example.taopr.soool.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.taopr.soool.Calendar.CalendarDB;
import com.example.taopr.soool.Dialog.CalendarDialogFragment;
import com.example.taopr.soool.Object.CalendarItem;
import com.example.taopr.soool.Presenter.CalendarSchPresenter;
import com.example.taopr.soool.R;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarSchActivity extends CalendarSchBaseActivity implements View.OnClickListener,
        CalendarDialogFragment.CalendarDialogListener,CalendarSchPresenter.View{

    @BindView(R.id.calendarSchTextContent)
    TextView calendarSchTextContent;

    private static Boolean changedEvent = false; // false 값 일때는 CalendarAddSch 액티비티에서 이벤트를 수정,추가,삭제 경우가 없을 경우
                                                 //  true 값 일때는 위 경우 중 하나라도 있을 경우
    private static final int CALENDAR_SCH_ADD_INT = 4310;
    private static final int DIALOG_FRAGMENT = 4320;
    private ImageView subActionBarLeftImage ;
    private TextView subActionBarRight;
    private TextView subActionBarLeft ;
    private TextView subActionBarRight2 ;
    private CalendarSchPresenter calendarSchPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_sch,0);

        ButterKnife.bind(this);

        if(getIntent()!=null){
            receiveSelectDay();
        }

        calendarSchPresenter = new CalendarSchPresenter(this.mActivity,this,accountNo);
        calendarSchPresenter.setView((CalendarSchPresenter.View) this);

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


    private void onDateChanged(){
        daySetting();
        isExistEvent();
    }


    // 액션바 클릭 리스너
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subActionBarRight:
                Intent intent = new Intent(mActivity, CalendarAddSchActivity.class);
                intent.putExtra(DATE_EXTRA, selectedDay.getTime());
                intent.putExtra(ACCOUNT_NO_EXTRA,accountNo);
                intent.putExtra(CALENDAR_LIST_EXTRA,(Serializable) calendarItemMap);
                startActivityForResult(intent,CALENDAR_SCH_ADD_INT);
                break;

            case R.id.subActionBarLeftImage:
                moveToPreviousPage();
                break;

            case R.id.subActionBarRight2:
                Log.i(TAG, "onClick: 삭제하기 버튼");

                calendarSchPresenter.eventDelete(selectedDay,calendarItemMap);
                break;
        }
    }

    // 저장하거나 수정한 내용이 있을 경우 저장하거나 수정한 내용을 업데이트한 CalendarItemMap(HashMap) 을 전달한다
    // 전체 hashMap을 전달하는 이유는 유저가 하루의 날짜에만 이벤트를 작성하지 않고 여러 날짜에 이벤트를 작성했을 경우
    // 리스트로 전달하는 경우가 생길 수 있기 때문에 애초에 HashMap으로 전달하려함

    private void moveToPreviousPage() {
        Intent intent = new Intent();
        if(changedEvent){
            Log.i(TAG, "moveToPreviousPage: 추가 o");
            setResult(RESULT_OK, intent);

            intent.putExtra(DATE_EXTRA,selectedDayDate);
            intent.putExtra(CALENDAR_LIST_EXTRA,(Serializable) calendarItemMap);
        }
        else{
            Log.i(TAG, "moveToPreviousPage: 추가 x");
            setResult(RESULT_CANCELED,intent);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case CALENDAR_SCH_ADD_INT:
                    // 추가를 했다면 changedEvent 값을 true 로 변경하여 뒤로 가기 버튼을 눌렀을 때 RESULT_OK 값을 전달하게 한다
                    selectedDay = new Date(data.getLongExtra(DATE_EXTRA, 0));

                    calendarItemMap = (HashMap<String,CalendarItem>) data.getSerializableExtra(CALENDAR_LIST_EXTRA);
                    Log.i(TAG, "receiveSelectedDay: date :" + selectedDay.toString() + " +  이벤트 날짜 수 " + calendarItemMap.size());
                    changedEvent = true;
                    daySetting();
                  //  SetSelectedDayEventContent();
                    isExistEvent();
                    break;
                case DIALOG_FRAGMENT:
                    Log.i(TAG, "onActivityResult: 프래그먼트");
                    break;
            }

        }
    }






    // 선택된 날짜에 작성한 내용이 있다면
    // 액션바 우측 상단에 삭제하기와 수정하기 버튼이 생성되고
    // 작성한 내용 텍스트뷰를 통해 보여주기
    // 내용이 없다면 우측 상단에 추가하기 버튼이 생성되고
    // 작성한 내용이 없다는 문구를 텍스트뷰에 띄워준다

    private void isExistEvent(){
        if(calendarItemMap.get(selectedDayDate)!=null){
            subActionBarRight.setText(R.string.all_button_modify);
            subActionBarRight2.setVisibility(View.VISIBLE);
            subActionBarRight2.setText(R.string.all_button_delete);

            calendarSchTextContent.setText(calendarItemMap.get(selectedDayDate).getEventContent());
        }
        else{
            subActionBarRight.setText(R.string.all_button_add);
            subActionBarRight2.setVisibility(View.GONE);

            calendarSchTextContent.setText(R.string.calendar_notice_no_write_sch);
        }
    }

    // 안드로이드 백버튼 처리
    @Override
    public void onBackPressed() {
        moveToPreviousPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        Log.i(TAG, "onCreateOptionsMenu: 액션바");
        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.sub_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);



        subActionBarLeftImage = findViewById(R.id.subActionBarLeftImage);
        subActionBarRight = findViewById(R.id.subActionBarRight);
        subActionBarLeft = findViewById(R.id.subActionBarLeft);
        subActionBarRight2 = findViewById(R.id.subActionBarRight2);


        subActionBarLeft.setVisibility(View.GONE);
        subActionBarLeftImage.setVisibility(View.VISIBLE);
        // subActionBarRight.setText(R.string.);
        subActionBarLeftImage.setOnClickListener(this);
        subActionBarRight.setOnClickListener(this);
        subActionBarRight2.setOnClickListener(this);

        isExistEvent();

        return true;
    }

    // 달력 다이얼로그와 액티비티 사이 인터페이스 역할을 하는 콜백 리스너
    // 달력 다이얼로그에서 날짜 선택 시 , 선택한 날짜에 맞춰 뷰를 동기화( 선택한 날짜, 선택한 이전 날짜, 다음 날짜 등등 )

    @Override
    public void onDateClicked(Date selectedDate) {
        selectedDay = selectedDate;
        onDateChanged();
    }

    // 선택한 날짜의 내용이 삭제를 끝낸 후
    // 선택된 날짜에 이벤트가 없기 때문에 삭제하기 버튼을 숨기고 수정하기 버튼을 추가하기 버튼으로 교환
    // changedEvent를 true 로 값을 변경 하여 이벤트에 변화가 있었음을 알려준다
    @Override
    public void getDataSuccess(HashMap<String, CalendarItem> calendarItemMap) {
        this.calendarItemMap = calendarItemMap;
        changedEvent = true;
        isExistEvent();
    }

    @Override
    public void getDataFail(String message) {

    }
}
