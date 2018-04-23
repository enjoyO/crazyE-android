package com.bai.van.radixe.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.bai.van.radixe.datastru.TimeTableInf;
import com.bai.van.radixe.entry.Entry;
import com.bai.van.radixe.userdata.UserInformation;

import java.util.ArrayList;

/**
 *
 * @author van
 * @date 2018/2/11
 */

public class TimeTableSqliteHandle {
    public static void creatOrRefreshSQLite(Context context){
        clearTable(context);

        ContentValues values;
        SQLiteDatabase sqLiteDatabase = new TimeTableSqliteHelper(context).getWritableDatabase();
        TimeTableInf timeTableInf;

        for (int i = 0; i < 7; i++){
            for (int j = 0; j < UserInformation.timeTableList.get(i).size(); j++){
                timeTableInf = UserInformation.timeTableList.get(i).get(j);
                values = new ContentValues();
                values.put(Entry.TimeTableEntry.COLOUM_TIME_TABLE_CLASS_NAME, timeTableInf.className);
                values.put(Entry.TimeTableEntry.COLOUM_TIME_TABLE_ADDRESS, timeTableInf.address);
                values.put(Entry.TimeTableEntry.COLOUM_TIME_TABLE_TEACHER, timeTableInf.teacher);
                values.put(Entry.TimeTableEntry.COLOUM_TIME_TABLE_WEEK_TIME, timeTableInf.weekTime);
                values.put(Entry.TimeTableEntry.COLOUM_TIME_TABLE_CLASS_NO, timeTableInf.classNo);
                values.put(Entry.TimeTableEntry.COLOUM_TIME_TABLE_DAY_IN_WEEK, timeTableInf.dayInWeek);
                values.put(Entry.TimeTableEntry.COLOUM_TIME_TABLE_MIN_KNOB, timeTableInf.minKnob);
                values.put(Entry.TimeTableEntry.COLOUM_TIME_TABLE_MAX_KNOB, timeTableInf.maxKnob);
                values.put(Entry.TimeTableEntry.COLOUM_TIME_TABLE_WEEK_STR, timeTableInf.weekStr);

                long newRowId = sqLiteDatabase.insert(Entry.TimeTableEntry.TABLE_NAME, null, values);

//                if (newRowId == -1){
//                    Log.d("TimeTableSqliteHandle", "Error with save pet");
//                }else {
//                    Log.d("TimeTableSqliteHandle", "Saved with row id " + newRowId);
//                }
            }
        }
        sqLiteDatabase.close();
    }
    public static void loadData(Context context){
        String[] coloums = {
                Entry.TimeTableEntry.COLOUM_TIME_TABLE_CLASS_NAME,
                Entry.TimeTableEntry.COLOUM_TIME_TABLE_ADDRESS,
                Entry.TimeTableEntry.COLOUM_TIME_TABLE_TEACHER,
                Entry.TimeTableEntry.COLOUM_TIME_TABLE_WEEK_TIME,
                Entry.TimeTableEntry.COLOUM_TIME_TABLE_CLASS_NO,
                Entry.TimeTableEntry.COLOUM_TIME_TABLE_DAY_IN_WEEK,
                Entry.TimeTableEntry.COLOUM_TIME_TABLE_MIN_KNOB,
                Entry.TimeTableEntry.COLOUM_TIME_TABLE_MAX_KNOB,
                Entry.TimeTableEntry.COLOUM_TIME_TABLE_WEEK_STR
        };

        SQLiteDatabase sqLiteDatabase = new TimeTableSqliteHelper(context).getWritableDatabase();


        Cursor cursor = sqLiteDatabase.query(Entry.TimeTableEntry.TABLE_NAME, coloums, null, null, null, null, null);

        int classNameColumn = cursor.getColumnIndex(Entry.TimeTableEntry.COLOUM_TIME_TABLE_CLASS_NAME);
        int addressColumn = cursor.getColumnIndex(Entry.TimeTableEntry.COLOUM_TIME_TABLE_ADDRESS);
        int teacherColumn = cursor.getColumnIndex(Entry.TimeTableEntry.COLOUM_TIME_TABLE_TEACHER);
        int weekTimeColumn = cursor.getColumnIndex(Entry.TimeTableEntry.COLOUM_TIME_TABLE_WEEK_TIME);
        int classNoColumn = cursor.getColumnIndex(Entry.TimeTableEntry.COLOUM_TIME_TABLE_CLASS_NO);
        int dayInWeekColumn = cursor.getColumnIndex(Entry.TimeTableEntry.COLOUM_TIME_TABLE_DAY_IN_WEEK);
        int minKnobColumn = cursor.getColumnIndex(Entry.TimeTableEntry.COLOUM_TIME_TABLE_MIN_KNOB);
        int maxKnobColumn = cursor.getColumnIndex(Entry.TimeTableEntry.COLOUM_TIME_TABLE_MAX_KNOB);
        int weekStrColumn = cursor.getColumnIndex(Entry.TimeTableEntry.COLOUM_TIME_TABLE_WEEK_STR);

        UserInformation.timeTableList = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            UserInformation.timeTableList.add(new ArrayList<TimeTableInf>());
        }
        TimeTableInf timeTableInf;
        while (cursor.moveToNext()){
            timeTableInf = new TimeTableInf();
            timeTableInf.className = cursor.getString(classNameColumn);
            timeTableInf.address = cursor.getString(addressColumn);
            timeTableInf.teacher = cursor.getString(teacherColumn);
            timeTableInf.weekTime = cursor.getString(weekTimeColumn);
            timeTableInf.classNo = cursor.getString(classNoColumn);

            timeTableInf.dayInWeek = cursor.getInt(dayInWeekColumn);
            timeTableInf.minKnob = cursor.getInt(minKnobColumn);
            timeTableInf.maxKnob = cursor.getInt(maxKnobColumn);

            timeTableInf.weekStr = cursor.getString(weekStrColumn);

            UserInformation.timeTableList.get(timeTableInf.dayInWeek - 1).add(timeTableInf);
        }
        cursor.close();
        sqLiteDatabase.close();
    }
    public static boolean isTimeTableEmpty(Context context){
        SQLiteDatabase sqLiteDatabase = new TimeTableSqliteHelper(context).getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + Entry.TimeTableEntry.TABLE_NAME, null);
        if (cursor.getCount() == 0){
            sqLiteDatabase.close();
            cursor.close();
            return true;
        }else {
            sqLiteDatabase.close();
            cursor.close();
            return false;
        }
    }
    public static void clearTable(Context context){
        SQLiteDatabase sqLiteDatabase = new TimeTableSqliteHelper(context).getWritableDatabase();
        try {
            sqLiteDatabase.delete(Entry.TimeTableEntry.TABLE_NAME, null, null);
            sqLiteDatabase.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = '" + Entry.TimeTableEntry.TABLE_NAME + "'");
        }catch (SQLiteException e){
            e.printStackTrace();
        }

        sqLiteDatabase.close();
    }
}
