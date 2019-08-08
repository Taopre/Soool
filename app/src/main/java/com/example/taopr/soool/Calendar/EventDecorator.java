package com.example.taopr.soool.Calendar;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.example.taopr.soool.Object.CalendarItem;
import com.example.taopr.soool.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

// 캘린더의 날짜에 이벤트를 넣어주는 클래스
// 원하는 설정을 decorate() 함수에서 작업

public class EventDecorator implements DayViewDecorator {


    private final int color;
    private final HashSet<CalendarDay> dates;
    SooolCalendar sooolCalendar = new SooolCalendar();
    private static Collection<CalendarDay> eventDates = new HashSet<>();
    Context context;

    public EventDecorator(int color, Collection<CalendarDay> dates, Context context){
        this.color = color;
        this.dates = new HashSet<>(dates);
        this.context = context;
    }
    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        return dates.contains(calendarDay);
    }

    @Override
    public void decorate(DayViewFacade dayViewFacade) {
        dayViewFacade.addSpan(new DotSpan(10, color));  // 이벤트 날짜에 도트 추가
        // dayViewFacade.addSpan(new StyleSpan(Typeface.BOLD));  /// 글씨 타입 변경
        // dayViewFacade.addSpan(new RelativeSizeSpan(1.1f)); 글씨 크기 사이즈 업

      /* dayViewFacade.setBackgroundDrawable(context.getResources().
               getDrawable(R.drawable.ic_launcher_background,context.getApplicationContext().getTheme()));  // 백그라운드 이미지, 글자 보임*/
/*
      dayViewFacade.setSelectionDrawable(context.getResources().
              getDrawable(R.drawable.ic_launcher_background,context.getApplicationContext().getTheme()));  // 백그라운들와 차이점을 모르겠음
*/
    }

    public DayViewDecorator getDatViewDecorator(HashMap<String,CalendarItem> calendarItemMap,Context context){
        DayViewDecorator dayViewDecorator=null;

        Set<String> set = calendarItemMap.keySet();
        Iterator<String> iter = set.iterator();

        eventDates.clear();
        while(iter.hasNext()){
            String key = ((String)iter.next());
            CalendarItem valueCalendarItem = calendarItemMap.get(key);

            Date date = sooolCalendar.getDate(valueCalendarItem.getEventDate());
            eventDates.add(CalendarDay.from(date));
        }

        dayViewDecorator = new EventDecorator(ContextCompat.getColor(context, R.color.colorAccent),eventDates,context);

        return dayViewDecorator;
    }
}
