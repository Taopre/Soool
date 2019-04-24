package com.example.taopr.soool.View.MyPageFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taopr.soool.Calendar.EventDecorator;
import com.example.taopr.soool.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;


public class CalendarFragment extends Fragment implements OnMonthChangedListener,OnDateSelectedListener {

    private OnFragmentInteractionListener mListener;
    Collection<CalendarDay> dates = new HashSet<>();
    MaterialCalendarView mcv;
    String TAG = "달력";
    private Context context;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");

    public CalendarFragment() {
        // Required empty public constructor
    }


    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        context = view.getContext();

        DayViewDecorator dayViewDecorator;
        mcv = view.findViewById(R.id.mcCalendarView);

        // 날짜 선택, 월 변경 리스너 등록
        mcv.setOnDateChangedListener(this);
        mcv.setOnMonthChangedListener(this);

        // 달력 타이틀에서 월 표시 폼 변경 메서드
        mcv.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));


        mcv.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)  // week 시작하는 날짜 mon / sun
                .setMinimumDate(CalendarDay.from(1916, 4, 3))  // 달력의 범위 min
                .setMaximumDate(CalendarDay.from(2116, 5, 12)) // 달력의 범위 max
                .setCalendarDisplayMode(CalendarMode.MONTHS) // 달력 보여주는 단위
                // .setSaveCurrentPosition(true)
                .commit();


        // 이벤트 날짜를 추가
        dates.add(CalendarDay.today());

        // 추가한 이벤트 날짜 리스트와 이벤트 표시할 색상을 전달
        dayViewDecorator = new EventDecorator(ContextCompat.getColor(view.getContext(),R.color.colorAccent),dates,view.getContext());

        // 가공한 decorator 변수를 캘린더뷰에 추가
        mcv.addDecorator(dayViewDecorator);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
        dates.add(calendarDay);
        DayViewDecorator dayViewDecorator = new EventDecorator(ContextCompat.getColor(context,R.color.colorAccent),dates,context);
        Log.i(TAG, "onDateSelected: ");
        mcv.addDecorator(dayViewDecorator);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
        Log.i(TAG, "onMonthChanged: ");
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
