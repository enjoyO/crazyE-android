package com.bai.van.radixe.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bai.van.radixe.entry.Entry;

/**
 *
 * @author van
 * @date 2018/2/11
 */

public class TimeTableSqliteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "crazyEDataSql_0_1.db";

    public static final int DATABASE_VERSION = 1;

    public TimeTableSqliteHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREAT_PET_TABLE = "CREATE TABLE " + Entry.TimeTableEntry.TABLE_NAME + "("
                + Entry.TimeTableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Entry.TimeTableEntry.COLOUM_TIME_TABLE_CLASS_NAME + " TEXT,"
                + Entry.TimeTableEntry.COLOUM_TIME_TABLE_ADDRESS + " TEXT,"
                + Entry.TimeTableEntry.COLOUM_TIME_TABLE_TEACHER + " TEXT,"
                + Entry.TimeTableEntry.COLOUM_TIME_TABLE_WEEK_TIME + " TEXT,"
                + Entry.TimeTableEntry.COLOUM_TIME_TABLE_CLASS_NO + " TEXT,"
                + Entry.TimeTableEntry.COLOUM_TIME_TABLE_DAY_IN_WEEK + " INTEGER,"
                + Entry.TimeTableEntry.COLOUM_TIME_TABLE_MIN_KNOB + " INTEGER,"
                + Entry.TimeTableEntry.COLOUM_TIME_TABLE_MAX_KNOB + " INTEGER,"
                + Entry.TimeTableEntry.COLOUM_TIME_TABLE_WEEK_STR + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREAT_PET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}
