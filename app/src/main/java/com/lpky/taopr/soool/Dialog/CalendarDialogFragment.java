package com.lpky.taopr.soool.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lpky.taopr.soool.Calendar.EventDecorator;
import com.lpky.taopr.soool.Calendar.SooolCalendar;
import com.lpky.taopr.soool.Object.CalendarItem;
import com.lpky.taopr.soool.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CalendarDialogFragment extends DialogFragment
        implements OnDateSelectedListener,View.OnClickListener
{
    private TextView schDialogSelectButton,schDialogCancelButton;
    private  final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
    public  static final String DATE_EXTRA = "selectedDay";
    public  static final String CALENDAR_LIST_EXTRA = "calendarItems";
    private  Collection<CalendarDay> eventDates = new HashSet<>();
    private  SooolCalendar sooolCalendar = new SooolCalendar();
    private  final String TAG = "캘린더 다이얼로그 ";
    public  View view;
    public  Date selectedDay;
    public  HashMap<String,CalendarItem> calendarItemMap;
    private MaterialCalendarView schDialogCalendar;

    private CalendarDialogListener mCallback;

    public interface CalendarDialogListener {
        void onDateClicked(Date selectedDate);
    }

    public static CalendarDialogFragment newInstance(long selectedDateTime, HashMap<String,CalendarItem> calendarItemMap){
        Bundle bundle = new Bundle();
        bundle.putLong(DATE_EXTRA, selectedDateTime);
        bundle.putSerializable(CALENDAR_LIST_EXTRA,calendarItemMap);
        CalendarDialogFragment fragment = new CalendarDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedDay = new Date(getArguments().getLong(DATE_EXTRA, 0));
            calendarItemMap = (HashMap<String,CalendarItem>)getArguments().getSerializable(CALENDAR_LIST_EXTRA);
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //inflate custom layout and get views
        //pass null as parent view because will be in dialog layout
        view = inflater.inflate(R.layout.dialog_calendar_sch, null);

        schDialogSelectButton = view.findViewById(R.id.schDialogSelectButton);
        schDialogCancelButton = view.findViewById(R.id.schDialogCancelButton);

        schDialogSelectButton.setOnClickListener(this);
        schDialogCancelButton.setOnClickListener(this);

        schDialogCalendar = view.findViewById(R.id.schDialogCalendar);

        schDialogCalendar.setOnDateChangedListener(this);

        // 달력 타이틀에서 월 표시 폼 변경 메서드
        schDialogCalendar.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        schDialogCalendar.setTitleAnimationOrientation(1);

        schDialogCalendar.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)  // week 시작하는 날짜 mon / sun
                .setMinimumDate(CalendarDay.from(1916, 4, 3))  // 달력의 범위 min
                .setMaximumDate(CalendarDay.from(2116, 5, 12)) // 달력의 범위 max
                .setCalendarDisplayMode(CalendarMode.MONTHS) // 달력 보여주는 단위
                // .setSaveCurrentPosition(true)
                .commit();

        schDialogCalendar.setCurrentDate(selectedDay);


        // 이벤트 작성한 날짜 이벤트 표시
        showEventDate();

        return new AlertDialog.Builder(getActivity())
               // .setTitle("날짜 선택")
                .setView(view)
                .create();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    // 날짜를 선택을 했을 때 '선택'버튼을 활성화함으로써 예외처리
    @Override
    public void onDateSelected(
            @NonNull MaterialCalendarView widget,
            @NonNull CalendarDay date,
            boolean selected) {
        selectedDay = date.getDate();
        schDialogSelectButton.setTextColor(ContextCompat.getColor(view.getContext(),R.color.greenDark));
        schDialogSelectButton.setClickable(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.schDialogSelectButton:
                mCallback = (CalendarDialogListener) getActivity();
                mCallback.onDateClicked(selectedDay);
                dismiss();
                break;
            case R.id.schDialogCancelButton:
                dismiss();
                break;
        }
    }

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

        DayViewDecorator dayViewDecorator = new EventDecorator(ContextCompat.getColor(view.getContext(),R.color.greenMain),eventDates,view.getContext());

        // 가공한 decorator 변수를 캘린더뷰에 추가
        // 추가하기 전에 달력 리셋하는 이유는 addDecorator 함수는 eventDates 에 날짜로 이벤트를 리셋하는 것이라 이전에 추가한 것이 있다면
        // 그 위에 더 추가하기 때문에 리셋을 한 후에 추가를 해야지 삭제한 이벤트에 대해서 동기화가 가능하다

        schDialogCalendar.addDecorator(dayViewDecorator);

    }

}

