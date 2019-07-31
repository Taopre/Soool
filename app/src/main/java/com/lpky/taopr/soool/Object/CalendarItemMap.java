package com.lpky.taopr.soool.Object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class CalendarItemMap extends HashMap<String,CalendarItem> implements Parcelable {

    String calendarEventDate;
    CalendarItem calendarItem;

    protected CalendarItemMap(Parcel in) {
        calendarEventDate = in.readString();
        calendarItem = in.readParcelable(CalendarItem.class.getClassLoader());
    }

    public static final Creator<CalendarItemMap> CREATOR = new Creator<CalendarItemMap>() {
        @Override
        public CalendarItemMap createFromParcel(Parcel in) {
            return new CalendarItemMap(in);
        }

        @Override
        public CalendarItemMap[] newArray(int size) {
            return new CalendarItemMap[size];
        }
    };

    public String getCalendarEventDate() {
        return calendarEventDate;
    }

    public void setCalendarEventDate(String calendarEventDate) {
        this.calendarEventDate = calendarEventDate;
    }

    public CalendarItem getCalendarItem() {
        return calendarItem;
    }

    public void setCalendarItem(CalendarItem calendarItem) {
        this.calendarItem = calendarItem;
    }

    /*public CalendarItemMap( String calendarEventDate, CalendarItem calendarItem) {
        this.put(calendarEventDate,calendarItem);
    }*/
    public CalendarItemMap(){}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(calendarEventDate);
        parcel.writeParcelable(calendarItem, i);
    }
}
