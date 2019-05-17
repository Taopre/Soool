package com.example.taopr.soool.Object;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class CalendarItem implements Parcelable{

    // 변수를 static 으로 했을 때 constructor 생성할 때 변수들이 나타나지 않았음
    // ex) public CalendarItem(){} 으로 생성자를 만들수 있었음
    // 이유 나중에 찾아볼 것

    public String eventContent , eventDate;

    protected CalendarItem(Parcel in) {
        eventContent = in.readString();
        eventDate = in.readString();
    }

    public static final Creator<CalendarItem> CREATOR = new Creator<CalendarItem>() {
        @Override
        public CalendarItem createFromParcel(Parcel in) {
            return new CalendarItem(in);
        }

        @Override
        public CalendarItem[] newArray(int size) {
            return new CalendarItem[size];
        }
    };

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public CalendarItem( String eventDate,String eventContent) {

        this.eventContent = eventContent;
        this.eventDate = eventDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(eventContent);
        parcel.writeString(eventDate);
    }
}
