package com.bai.van.radixe.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.bai.van.radixe.R;
import com.bai.van.radixe.TimeTableItemDetailActivity;
import com.bai.van.radixe.constantdata.ConstantValues;
import com.bai.van.radixe.entry.Entry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author van
 * @date 2018/2/11
 */

public class TimeTableAlarmReceiver extends BroadcastReceiver {
    public static boolean isReceiveTimeTableAlarm = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isReceiveTimeTableAlarm && ConstantValues.TIME_TABLE_ALARM_ACTION.equals(intent.getAction())) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent mPendingIntent = PendingIntent.getBroadcast(context,
                    intent.getIntExtra("requestCode", 0), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                assert alarmManager != null;
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + AlarmManager.INTERVAL_DAY * 7, mPendingIntent);

                Log.d("setNext", new SimpleDateFormat("yyyy-MM-dd HH-mm-ss")
                        .format(new Date(System.currentTimeMillis() + AlarmManager.INTERVAL_DAY * 7))
                        + " " + intent.getStringExtra("courseName"));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                assert alarmManager != null;
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,

                        System.currentTimeMillis() + AlarmManager.INTERVAL_DAY * 7, mPendingIntent);
                Log.d("setNext", new SimpleDateFormat("yyyy-MM-dd HH-mm-ss")
                        .format(new Date(System.currentTimeMillis() + AlarmManager.INTERVAL_DAY * 7))
                        + " " + intent.getStringExtra("courseName"));
            }

            SharedPreferences sharedPreferences = context.getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            long currentWeekTimeInMile = sharedPreferences.getLong(Entry.SharedPreferencesEntry.CURRENT_WEEK_TIMEINMILLIS, 0);
            int weekDif = (int) ((System.currentTimeMillis() - currentWeekTimeInMile) / 604800000);

            int currentWeekNo = sharedPreferences.getInt(Entry.SharedPreferencesEntry.CURRENT_WEEK_NO, 1);

            if (weekDif >= 1) {
                currentWeekNo += weekDif;
                sharedPreferences.edit().putInt(Entry.SharedPreferencesEntry.CURRENT_WEEK_NO, currentWeekNo).apply();

                Calendar calendar = Calendar.getInstance();
                int week = calendar.get(Calendar.DAY_OF_WEEK);
                calendar.add(Calendar.DAY_OF_MONTH, -((week + 5) % 7));
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);

                sharedPreferences.edit().putLong(Entry.SharedPreferencesEntry.CURRENT_WEEK_TIMEINMILLIS, calendar.getTimeInMillis()).apply();
            }

            /*
             * 测试中
             */

            int minWeek = intent.getIntExtra("minWeek", 1);
            int maxWeek = intent.getIntExtra("maxWeek", 18);
            int weekHow = intent.getIntExtra("weekHow", 1);
            String isThisWeek = "[非当前周] ";

            if (currentWeekNo >= minWeek && currentWeekNo <= maxWeek
                    && ((weekHow == ConstantValues.TIMETABLE_WEEK_SINGLE && currentWeekNo % 2 == 1)
                    || (weekHow == ConstantValues.TIMETABLE_WEEK_DOUBLE && currentWeekNo % 2 == 0)
                    || weekHow == ConstantValues.TIMETABLE_WEEK_ALL)) {
                // 当前周
                isThisWeek = "[当前周] ";

                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Intent mIntent = new Intent(context, TimeTableItemDetailActivity.class);

                Notification.Builder builder;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                  Android O Notification
                    NotificationChannel channel = new NotificationChannel("1",
                            "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
                    channel.enableLights(true);
                    channel.setLightColor(Color.GREEN);
                    channel.setShowBadge(true);

                    builder = new Notification.Builder(context, "1");

                    if (mNotificationManager != null) {
                        mNotificationManager.createNotificationChannel(channel);
                    }
                }else {
                    builder = new Notification.Builder(context);
                }

                mIntent.putExtra("courseName", intent.getStringExtra("courseName"));
                mIntent.putExtra("dayInWeek", intent.getIntExtra("dayInWeek", -1));
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(pendingIntent);
                builder.setSmallIcon(R.drawable.ic_small_icon_white);
                builder.setAutoCancel(true);
                builder.setDefaults(Notification.DEFAULT_ALL);
                builder.setWhen(System.currentTimeMillis());
                builder.setShowWhen(true);
                builder.setNumber(3);
                builder.setContentTitle(intent.getStringExtra("courseName"));

                builder.setContentText(ConstantValues.TIME_TIME_TABLE.get(intent.getIntExtra("minKnob", 0)).concat("-")
                        .concat(ConstantValues.TIME_FIN_TIME_TABLE.get(intent.getIntExtra("maxKnob", 0)))
                        .concat("  ").concat(intent.getStringExtra("courseAdd")));

                if (mNotificationManager != null) {
                    mNotificationManager.notify(intent.getIntExtra("requestCode", 0), builder.build());
                }
            }

            Log.d("TimeTableAlarmReceiver", isThisWeek);
        }
        Log.d("TimeTableAlarmReceiver", intent.getAction() + intent.getStringExtra("courseName"));
    }
}
