package com.lpky.taopr.soool.Calendar;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lpky.taopr.soool.Object.CalendarItem;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CalendarDB {

    SQLiteDatabase chat_db = null;
    static final String TAG = "캘린더 데이터베이스";
    Activity mActivity;
    String db_name ="soool";

    public CalendarDB(Activity mActivity){

        this.mActivity = mActivity;

    }

    // 이벤트 디비에 추가
    public void eventInsert(String accountNo, String eventDate, String eventContent) {

        // 데이터 베이스가 없으면 생성
        chat_db = mActivity.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        String table_name ="soool_" +accountNo;

        //테이블 존재하지 않으면 생성
        String query = "create table if not exists " + table_name + "(eventDate varchar(8) primary key , " +
                "eventContent text)";
        Log.i(TAG, "sqlite_save: 테이블 생성 쿼리 " + query);

        chat_db.execSQL(query);

        /// 메세지 sqlite에 저장
        query = "insert into " +table_name + " (eventDate , eventContent ) " +
                " values ('"+eventDate  +"','" + eventContent +"')";
        Log.i(TAG, "eventInsert: " + query);

        chat_db.execSQL(query);
        chat_db.close();
    }

    // 작성한 이벤트 가져오는 부분
    public ArrayList<CalendarItem> eventSelect(String accountNo){

        chat_db = mActivity.openOrCreateDatabase(db_name,MODE_PRIVATE,null);

        String table_name ="soool_" +accountNo;
        //테이블 존재하지 않으면 생성
        String query = "create table if not exists " + table_name + " (eventDate varchar(8) primary key , " +
                "eventContent text)";

        chat_db.execSQL(query);


        query = "select * from " + table_name;
        ArrayList<CalendarItem> calendarItems = new ArrayList<>();
        
        Cursor row = chat_db.rawQuery(query,null);
        if(row != null){
            if(row.moveToFirst()){
                do {
                    String eventContent = row.getString(row.getColumnIndex("eventContent"));
                    String eventDate = row.getString(row.getColumnIndex("eventDate"));
                    CalendarItem calendarItem = new CalendarItem(eventDate,eventContent);
                    calendarItems.add(calendarItem);
                    Log.i(TAG, "캘린더 db select: event 내용 eventContent :" + eventContent + ", 시간 :" + eventDate );

                } while(row.moveToNext());
            }

        }
        return calendarItems;
    }

    // 이전에 작성했던 이벤트 작성한 내용으로 수정
    public void eventUpdate(String accountNo, String eventDate, String eventContent) {

        // 데이터 베이스가 없으면 생성
        chat_db = mActivity.openOrCreateDatabase(db_name,MODE_PRIVATE,null);

        String table_name ="soool_" +accountNo;
        // 내용 업데이트
        String query = "UPDATE " + table_name +" SET eventContent='" + eventContent + "' WHERE eventDate='" + eventDate + "';";

        Log.i(TAG, "eventUpdate: " + query);

        chat_db.execSQL(query);
        chat_db.close();
    }

    // 작성했던 이벤트 삭제
    public void eventDelete(String accountNo, String eventDate ) {

        // 데이터 베이스가 없으면 생성
        chat_db = mActivity.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        String table_name ="soool_" +accountNo;
        // 내용 업데이트
        String query = "DELETE FROM "+ table_name + " WHERE eventDate='" + eventDate + "'";

        Log.i(TAG, "eventDelete: " + query);

        chat_db.execSQL(query);
        chat_db.close();
    }
}
