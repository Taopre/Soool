package com.example.taopr.soool.View.MyPageFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taopr.soool.Calendar.CalendarDB;
import com.example.taopr.soool.Calendar.EventDecorator;
import com.example.taopr.soool.Calendar.SooolCalendar;
import com.example.taopr.soool.Object.CalendarItem;
import com.example.taopr.soool.Object.CalendarItemMap;
import com.example.taopr.soool.Presenter.CalendarFmPresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.View.CalendarAddSchActivity;
import com.example.taopr.soool.View.CalendarSchActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;


public class CalendarFragment extends Fragment implements CalendarFmPresenter.View,
        OnDateSelectedListener {

    private  Collection<CalendarDay> eventDates = new HashSet<>();  // 캘린더에 event 표시를 해줘야하는 날짜 리스트
    private  HashMap<String,CalendarItem> calendarItemMap= new HashMap<String,CalendarItem>();
    private  MaterialCalendarView mcv=null;
    private  SooolCalendar sooolCalendar = new SooolCalendar();
    private  View view;
    private  String TAG = "마이페이지 캘린더 프래그먼트";
    private  String accountNo=null;
    private Context context;
    private final String DATE_EXTRA = "selectedDay";
    private final String CALENDAR_LIST_EXTRA = "calendarItems";
    private final String ACCOUNT_NO_EXTRA = "accountNo";
    private final int CALENDAR_ADD_INT = 4300;
    private static Date selectedDay;
    private Unbinder unbinder;
    private CalendarFmPresenter calendarFmPresenter;

    DayViewDecorator dayViewDecorator;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    // 뷰 바인딩과 intent 받는 부분
    // intent 로 회원번호를 받은 뒤 캘린더 DB 에서 작성했던 내용들을 가져온다


    // 선택한 날짜는 디폴트로 현재날짜를 받는다. 날짜 선택 없이 일정추가를 할 경우 현재 날짜에 일정을 추가하므로
    // 그 후, 날짜 선택을 통해 캘린더 상세 페이지나 캘린더 작성 페이지로 넘어갔다가
    // 다시 마이페이지로 이동했을 경우 그 전 페이지에서 작성하던 날짜를 받아와 동기화

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = view.getContext();
        this.view = view;

        selectedDay = CalendarDay.today().getDate();

        mcv = view.findViewById(R.id.mcCalendarView);

        // 날짜 선택, 월 변경 리스너 등록
        mcv.setOnDateChangedListener(this);

        mcv.state().edit()
                .setMinimumDate(CalendarDay.from(1950, 4, 3))  // 달력의 범위 min
                .setMaximumDate(CalendarDay.from(2050, 5, 12)) // 달력의 범위 max
                .commit();


        // 캘린더 이벤트 날짜, 유저 닉네임을 bundle 객체로 받아오기
        // 작성한 데이터 없을 경우 예외처리
        if (accountNo == null) {
            accountNo = getArguments().getString(ACCOUNT_NO_EXTRA);
        }

        //수정
        if (calendarFmPresenter == null) {
            calendarFmPresenter = new CalendarFmPresenter();
            calendarFmPresenter.setView(this);
            calendarFmPresenter.getCalendarItem(getActivity(), accountNo);
        }

        return view;
    }


    // 이벤트 작성한 날짜를 받아와서 달력에 이벤트 표시해주는 부분
    // hashMap<작성 날짜, calendarItem> 구조인 calendarItemMap 의 calendarItem.getEventDate 를 SooolCalendar.getDate() 함수를 통해
    // date 변수 형태('yyyymmdd')로 받은 후에 캘린더에 이벤트 표시를 해줘야 하는 날짜를 담고 있는
    // eventDates 리스트에 추가를 해준 다음 dayViewDecorator 로 캘린더에 표시

    private void showEventDate() {
        Set<String> set = calendarItemMap.keySet();
        Iterator<String> iter = set.iterator();

        eventDates.clear();
        while(iter.hasNext()){
            String key = ((String)iter.next());
            CalendarItem valueCalendarItem = calendarItemMap.get(key);
            Date date = sooolCalendar.getDate(valueCalendarItem.getEventDate());
            eventDates.add(CalendarDay.from(date));
        }

        dayViewDecorator = new EventDecorator(ContextCompat.getColor(view.getContext(),R.color.greenMain),eventDates,view.getContext());

        // 가공한 decorator 변수를 캘린더뷰에 추가
        // 추가하기 전에 달력 리셋하는 이유는 addDecorator 함수는 eventDates 에 날짜로 이벤트를 리셋하는 것이라 이전에 추가한 것이 있다면
        // 그 위에 더 추가하기 때문에 리셋을 한 후에 추가를 해야지 삭제한 이벤트에 대해서 동기화가 가능하다

        mcv.removeDecorators();
        mcv.addDecorator(dayViewDecorator);

    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {

        selectedDay = calendarDay.getDate();
        moveToCalendarSch(0);
    }


    // 캘린더에서 작성한 내용들을 가져오면 달력 해당 날짜들을 표시해준다

    @Override
    public void getCalendarDataResponse(CalendarItemMap calendarItemMap) {
        this.calendarItemMap = calendarItemMap;
        showEventDate();
    }

    @Override
    public void getDataFail(boolean response, int i) {

    }

    @OnClick({R.id.myPageAddEvent})
    void OnClick(View view){
        switch (view.getId()) {
            case R.id.myPageAddEvent:
                moveToCalendarSch(1);
                break;
        }
    }

    // pageKind는 CalendarAddSchActivity , CalendarSchActivity 둘 중 어디로 이동하는 지에 대한 구분자 값
    // CalendarSchActivity 로 이동할 때는 선택한 날짜값을 intent 에 담아서 보낸다
    // CalendarAddSchActivity 로 이동할 때는 당일날 이동이 디폴트이기 때문에 당일날짜를 intent 에 담는다
    // 그리고 두 페이지 모두 해당 페이지에서 날짜 선택을 통해 작성했던 디스크립션을 봐야하기 때문에
    // 날짜+디스크립션이 담겨있는 CalendarItem 객체를 value 로 가지는
    // HashMap<eventDate,Calendar> 구조인 CalendarItemMap 을 전달

    private void moveToCalendarSch(int pageKind){
        Intent intent = null;

        switch (pageKind) {
            case 0:
                intent = new Intent(context, CalendarSchActivity.class);
                intent.putExtra(DATE_EXTRA, selectedDay.getTime());
                intent.putExtra(ACCOUNT_NO_EXTRA,accountNo);
                intent.putExtra(CALENDAR_LIST_EXTRA,(Serializable) calendarItemMap);
                startActivityForResult(intent, CALENDAR_ADD_INT);
                break;
            case 1:
                intent = new Intent(context, CalendarAddSchActivity.class);
                intent.putExtra(DATE_EXTRA, selectedDay.getTime());
                intent.putExtra(ACCOUNT_NO_EXTRA,accountNo);
                intent.putExtra(CALENDAR_LIST_EXTRA,(Serializable) calendarItemMap);
                startActivityForResult(intent, CALENDAR_ADD_INT);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case CALENDAR_ADD_INT:

                    //String selectedDate = data.getStringExtra(DATE_EXTRA);
                    if(data.getStringExtra(DATE_EXTRA) != null) {
                        selectedDay = sooolCalendar.getDate(data.getStringExtra(DATE_EXTRA));
                    }
                    else{
                        long selectedDate = data.getLongExtra(DATE_EXTRA, 0);
                        selectedDay = sooolCalendar.getDate(String.valueOf(selectedDate));
                    }

                    //selectedDay = sooolCalendar.getDate(selectedDate);
                    calendarItemMap = (HashMap<String,CalendarItem>) data.getSerializableExtra(CALENDAR_LIST_EXTRA);
                    showEventDate();
            }

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder!=null) unbinder.unbind();
    }
}
