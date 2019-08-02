package com.lpky.taopr.soool.View;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.lpky.taopr.soool.Calendar.SooolCalendar;
import com.lpky.taopr.soool.Object.CalendarItem;
import com.lpky.taopr.soool.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;

public class CalendarSchBaseActivity extends AppCompatActivity {

    Activity mActivity;
    String TAG = "캘린더 액티비티 ";
    public int layoutKind; // calendarSch 액티비티일 경우 0, calendarAddSch일 경우 1
    public Date selectedDay,previousDay,nextDay; // 선택한 날짜, 선택한 날짜의 이전 날, 다음 날을 Date 변수타입으로 가지는 변수
    public final String DATE_EXTRA = "selectedDay";
    public final String CALENDAR_LIST_EXTRA = "calendarItems";
    public final String ACCOUNT_NO_EXTRA = "accountNo";
    private String monthYearPattern = " MMMM yyyy ";
    private String dayPattern = "dd";
    public String accountNo;

    public ArrayList<CalendarItem> calendarItems= new ArrayList<CalendarItem>();
    HashMap<String,CalendarItem> calendarItemMap;

    public String selectedDayDate; // 선택한 날짜를 "yyyymmdd" 형식으로 가지고 있는 string 변수


    SooolCalendar sooolCalendar = new SooolCalendar();

    @BindView(R.id.calendarSelectedDay)
    TextView calendarSelectedDay;
    @BindView(R.id.calendarSelectedDayOfWeek)
    TextView calendarSelectedDayOfWeek;
    @BindView(R.id.calendarPreviousDay)
    TextView calendarPreviousDay;
    @BindView(R.id.calendarNextDay)
    TextView calendarNextDay;
    @BindView(R.id.calendarSelectedMonthYear)
    TextView calendarSelectedMonthYear;

    // 이전 날짜를 선택하면 selectedDay 가 previousDay 값을 가지고
    // previousDay 와 nextDay 값을 변경된 selectedDay 값에 맞춰서 변경

    // 'month year' 타이틀을 선택했을 경우 달력을 담은 다이얼로그를 띄워주고
    // 사용자가 원하는 날짜를 선택할 수 있게 제공



    // view를 받아오는 부분
    // 액션바의 왼쪽 버튼 값과 오른쪽 값을 전달받아 보여준다
    public void setContentView(@LayoutRes int layoutResID, int layoutKind) {
        super.setContentView(layoutResID);
        mActivity = this;
        this.layoutKind = layoutKind;

    }


    // intent 전달받는 부분
    // 유저가 선택한 날짜와 유저가 작성했던 event 날짜와 event 내용을 담고 있는 CalenderItem 객체의 리스트를 전달 받는다.
    // 전달 받은 후 유저가 선택한 날짜에 작성했던 event가 있는지 확인 후 있을 경우 event 내용을 보여주고,
    // CalendarSchAct 에서는 상단 우측 메뉴에서는 삭제하기와 수정하기 버튼을 보여준다

    public void receiveSelectDay() {
        Intent intent = getIntent();
        selectedDay = new Date(intent.getLongExtra(DATE_EXTRA, 0));
        accountNo = intent.getStringExtra(ACCOUNT_NO_EXTRA);
        calendarItemMap = (HashMap<String,CalendarItem>) getIntent().getSerializableExtra(CALENDAR_LIST_EXTRA);
        daySetting();
    }


    // 가운데 년/월/날짜 보여주는 부분

    public void daySetting(){

        // 선택한 날짜를 변수타입을 Date 로 가지고 있는 selectedDay 를 hashMap 에서 매핑하기 위해 key 값이 있어야 하는데
        // key 값이 날짜를 format("yyyymmdd")을 가지는 String 타입이기 때문에 selectedDay 를 string으로 변환해준 값을
        // selectedDayDate 에 저장한다.

        selectedDayDate = sooolCalendar.getDateFormat(selectedDay);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(monthYearPattern);

        String monthYear = simpleDateFormat.format(selectedDay);
        calendarSelectedMonthYear.setText(monthYear);

        calendarSelectedDayOfWeek.setText(sooolCalendar.getDayOfWeek(selectedDay));
        // 선택한 날짜 selectedDay date 객체를 생성한 후에 다음 날, 이전 날 ( nextDay , previous )를 생성한다

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDay);


        // 전 날
        calendar.add(Calendar.DATE,-1);
        previousDay = calendar.getTime();

        //다음 날
        calendar.setTime(selectedDay);
        calendar.add(Calendar.DATE,+1);
        nextDay = calendar.getTime();


        // 선택한 날짜, 이전 날, 다음 날에 대한 날짜 보여주기

        simpleDateFormat = new SimpleDateFormat(dayPattern);

        calendarSelectedDay.setText(simpleDateFormat.format(selectedDay));
        calendarPreviousDay.setText(simpleDateFormat.format(previousDay));
        calendarNextDay.setText(simpleDateFormat.format(nextDay));
    }

}
