package com.bai.van.radixe.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.bai.van.radixe.constantdata.ConstantValues;
import com.bai.van.radixe.datastru.TimeTableInf;
import com.bai.van.radixe.datastru.UserInf;
import com.bai.van.radixe.entry.Entry;
import com.bai.van.radixe.sqlite.TimeTableSqliteHandle;
import com.bai.van.radixe.userdata.UserInformation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

/**
 * @author van
 * @date 2018/2/11
 */

public class TimeTableAlarmSetting {

    @SuppressLint("SimpleDateFormat")
    public static void createTimeTableAlarm(Context context, List<List<TimeTableInf>> timeTableList) {

//        cancelTimeTableAlarm(context, timeTableList);
//        TimeTableAlarmReceiver.isReceiveTimeTableAlarm = false;
//        Log.d("isReceiveTimeTableAlarm", TimeTableAlarmReceiver.isReceiveTimeTableAlarm + "");

        if (UserInformation.timeTableList.size() == 0) {
            TimeTableSqliteHandle.loadData(context);
        }

        Intent intent = new Intent(ConstantValues.TIME_TABLE_ALARM_ACTION);

        PendingIntent pendingIntent;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        TimeTableInf timeTableInf;
        Calendar calendar;
        int week;

        int remindInterval = context.getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getInt(Entry.SharedPreferencesEntry.ALARM_REMIND_TIME_INTERVAL, 15);
        int campus = context.getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getInt(Entry.SharedPreferencesEntry.CAMPUS_TYPE, 1);
        try {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < timeTableList.get(i).size(); j++) {
                    timeTableInf = timeTableList.get(i).get(j);
                    intent.putExtra("courseName", timeTableInf.className);
                    intent.putExtra("courseAdd", timeTableInf.address);
                    intent.putExtra("dayInWeek", timeTableInf.dayInWeek - 1);
                    intent.putExtra("requestCode", i * 10 + j);
                    intent.putExtra("minKnob", timeTableInf.minKnob);
                    intent.putExtra("maxKnob", timeTableInf.maxKnob);
                    intent.putExtra("weekStr", timeTableInf.weekStr);

                    calendar = Calendar.getInstance();
                    calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    week = calendar.get(Calendar.DAY_OF_WEEK);
                    calendar.add(Calendar.DAY_OF_MONTH, -((week + 5) % 7));
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.add(Calendar.DAY_OF_MONTH, i);
                    if (campus == 1) {
                        calendar.set(Calendar.HOUR_OF_DAY, ConstantValues.HOUR_TIME_TABLE.get(timeTableInf.minKnob));
                        calendar.set(Calendar.MINUTE, ConstantValues.MINUTE_TIME_TABLE.get(timeTableInf.minKnob));
                    }else {
                        calendar.set(Calendar.HOUR_OF_DAY, ConstantValues.HOUR_TIME_TABLE_GD.get(timeTableInf.minKnob));
                        calendar.set(Calendar.MINUTE, ConstantValues.MINUTE_TIME_TABLE_GD.get(timeTableInf.minKnob));
                    }
                    calendar.add(Calendar.MINUTE, -remindInterval);
//                    calendar.add(Calendar.MINUTE, -66);

                    if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                        calendar.add(Calendar.DAY_OF_MONTH, 7);
                    }
                    pendingIntent = PendingIntent.getBroadcast(context, i * 10 + j, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        assert alarmManager != null;
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                        Log.d("setExactAndAllow", new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date(calendar.getTimeInMillis()))
                                + " " + timeTableInf.className);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        assert alarmManager != null;
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                        Log.d("setExact", new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date(calendar.getTimeInMillis()))
                                + " " + timeTableInf.className);
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        Log.d("TimeTableAlarmSetting", "CreateTimeTableAlarm");
    }

    public static void cancelTimeTableAlarm(Context context, List<List<TimeTableInf>> timeTableList) {
        Intent intent = new Intent(ConstantValues.TIME_TABLE_ALARM_ACTION);
        PendingIntent pendingIntent;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        try {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < timeTableList.get(i).size(); j++) {
                    pendingIntent = PendingIntent.getBroadcast(context, i * 10 + j, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if (alarmManager != null) {
                        alarmManager.cancel(pendingIntent);
                        pendingIntent.cancel();
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        Log.d("TimeTableAlarmSetting", "CancelTimeTableAlarm");
    }
}
