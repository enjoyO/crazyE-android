package com.bai.van.radixe.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RemoteViews;

import com.bai.van.radixe.R;
import com.bai.van.radixe.constantdata.StaticMethod;
import com.bai.van.radixe.datastru.TimeTableInf;
import com.bai.van.radixe.entry.Entry;
import com.bai.van.radixe.sqlite.TimeTableSqliteHandle;
import com.bai.van.radixe.userdata.UserInformation;

import java.util.Calendar;

/**
 * @author van
 * @date 2018/5/23
 */
public class TableBigWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d("TableBigWidgetProvider", "onUpdate");

        Calendar calendarNow = Calendar.getInstance();
        int week = calendarNow.get(Calendar.DAY_OF_WEEK);

        if (UserInformation.timeTableList.size() == 0) {
            Log.d("timetabledetail", "refresh");
            TimeTableSqliteHandle.loadData(context);
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        int currentWeekNo = sharedPreferences.getInt(Entry.SharedPreferencesEntry.CURRENT_WEEK_NO, 1);
        long currentWeekTimeInMile = sharedPreferences.getLong(Entry.SharedPreferencesEntry.CURRENT_WEEK_TIMEINMILLIS, 0);

        int weekDif = (int) ((System.currentTimeMillis() - currentWeekTimeInMile) / 604800000);

        if (weekDif >= 1) {
            currentWeekNo += weekDif;
            sharedPreferences.edit().putInt(Entry.SharedPreferencesEntry.CURRENT_WEEK_NO, currentWeekNo).apply();

            Calendar calendarTemp = Calendar.getInstance();
            calendarTemp.add(Calendar.DAY_OF_MONTH, -((week + 5) % 7));
            calendarTemp.set(Calendar.HOUR_OF_DAY, 0);
            calendarTemp.set(Calendar.MINUTE, 0);
            calendarTemp.set(Calendar.MILLISECOND, 0);
            calendarTemp.set(Calendar.SECOND, 0);

            sharedPreferences.edit().putLong(Entry.SharedPreferencesEntry.CURRENT_WEEK_TIMEINMILLIS, calendarTemp.getTimeInMillis()).apply();
        }

        String userName = sharedPreferences.getString(Entry.SharedPreferencesEntry.USER_NAME, "");
        int userNameAttr = 1;
        if (!"".endsWith(userName)){
            userNameAttr = Integer.parseInt(userName.substring(userName.length() - 1)) + 1;

            userNameAttr = userNameAttr == 10 || userNameAttr == 5 ? 8 : userNameAttr;
        }

        if (!TimeTableSqliteHandle.isTimeTableEmpty(context)){
            for (int appWidgetId : appWidgetIds) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timetable_big_widget);

                views.removeAllViews(R.id.weekDisplayLayout0_widget);
                views.removeAllViews(R.id.weekDisplayLayout1_widget);
                views.removeAllViews(R.id.weekDisplayLayout2_widget);
                views.removeAllViews(R.id.weekDisplayLayout3_widget);
                views.removeAllViews(R.id.weekDisplayLayout4_widget);
                views.removeAllViews(R.id.weekDisplayLayout5_widget);
                views.removeAllViews(R.id.weekDisplayLayout6_widget);
                views.setTextViewText(R.id.weekMonth_widget, String.format("%d月", calendarNow.get(Calendar.MONTH) + 1));
                calendarNow.add(Calendar.DAY_OF_MONTH, -((week + 5) % 7));
                views.setTextViewText(R.id.weekDate0_widget, String.format("%d日", calendarNow.get(Calendar.DAY_OF_MONTH)));
                calendarNow.add(Calendar.DAY_OF_MONTH, 1);
                views.setTextViewText(R.id.weekDate1_widget, String.format("%d日", calendarNow.get(Calendar.DAY_OF_MONTH)));
                calendarNow.add(Calendar.DAY_OF_MONTH, 1);
                views.setTextViewText(R.id.weekDate2_widget, String.format("%d日", calendarNow.get(Calendar.DAY_OF_MONTH)));
                calendarNow.add(Calendar.DAY_OF_MONTH, 1);
                views.setTextViewText(R.id.weekDate3_widget, String.format("%d日", calendarNow.get(Calendar.DAY_OF_MONTH)));
                calendarNow.add(Calendar.DAY_OF_MONTH, 1);
                views.setTextViewText(R.id.weekDate4_widget, String.format("%d日", calendarNow.get(Calendar.DAY_OF_MONTH)));
                calendarNow.add(Calendar.DAY_OF_MONTH, 1);
                views.setTextViewText(R.id.weekDate5_widget, String.format("%d日", calendarNow.get(Calendar.DAY_OF_MONTH)));
                calendarNow.add(Calendar.DAY_OF_MONTH, 1);
                views.setTextViewText(R.id.weekDate6_widget, String.format("%d日", calendarNow.get(Calendar.DAY_OF_MONTH)));

                calendarNow.add(Calendar.DAY_OF_MONTH, 1);

                TimeTableInf timeTableInf;
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < UserInformation.timeTableList.get(i).size(); j++) {
                        timeTableInf = UserInformation.timeTableList.get(i).get(j);
                        if (StaticMethod.isCurrentWeek(currentWeekNo, timeTableInf.weekStr)) {
                            addWeekChoose(context.getPackageName(), views, timeTableInf.dayInWeek - 1,
                                    timeTableInf.minKnob, timeTableInf.className, timeTableInf.address, userNameAttr);
                        }
                    }
                }

                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }


    }
    private void addWeekChoose(String packageName, RemoteViews remoteViewsParent, int dayInWeek, int knob, String name, String room, int userNameAttr){
        switch (dayInWeek){
            case 0:
                remoteViewsParent.addView(R.id.weekDisplayLayout0_widget, addKnobPosition(packageName, knob, name, room, userNameAttr));
                break;
            case 1:
                remoteViewsParent.addView(R.id.weekDisplayLayout1_widget, addKnobPosition(packageName, knob, name, room, userNameAttr));
                break;
            case 2:
                remoteViewsParent.addView(R.id.weekDisplayLayout2_widget, addKnobPosition(packageName, knob, name, room, userNameAttr));
                break;
            case 3:
                remoteViewsParent.addView(R.id.weekDisplayLayout3_widget, addKnobPosition(packageName, knob, name, room, userNameAttr));
                break;
            case 4:
                remoteViewsParent.addView(R.id.weekDisplayLayout4_widget, addKnobPosition(packageName, knob, name, room, userNameAttr));
                break;
            case 5:
                remoteViewsParent.addView(R.id.weekDisplayLayout5_widget, addKnobPosition(packageName, knob, name, room, userNameAttr));
                break;
            case 6:
                remoteViewsParent.addView(R.id.weekDisplayLayout6_widget, addKnobPosition(packageName, knob, name, room, userNameAttr));
                break;
            default:
                break;
        }
    }
    private RemoteViews addKnobPosition(String packageName, int knob, String name, String room, int userNameAttr){
        RemoteViews remoteViewsChild;
        switch (knob){
            case 1:
                remoteViewsChild = new RemoteViews(packageName, R.layout.timetable_big_widget_item_top_0);
                break;
            case 3:
                remoteViewsChild = new RemoteViews(packageName, R.layout.timetable_big_widget_item_top_2);
                break;
            case 5:
                remoteViewsChild = new RemoteViews(packageName, R.layout.timetable_big_widget_item_top_4);
                break;
            case 7:
                remoteViewsChild = new RemoteViews(packageName, R.layout.timetable_big_widget_item_top_6);
                break;
            case 9:
                remoteViewsChild = new RemoteViews(packageName, R.layout.timetable_big_widget_item_top_8);
                break;
            case 11:
                remoteViewsChild = new RemoteViews(packageName, R.layout.timetable_big_widget_item_top_10);
                break;
            default:
                remoteViewsChild = null;
                break;
        }
        if (remoteViewsChild != null){
            remoteViewsChild.setTextViewText(R.id.timetable_big_widget_text_name, name);
            remoteViewsChild.setTextViewText(R.id.timetable_big_widget_text_room, "@".concat(room));

            remoteViewsChild.setImageViewResource(R.id.timetable_big_widget_imageback, getTextBackground(name, userNameAttr));
            return remoteViewsChild;
        }else {
            return null;
        }
    }
    private int getTextBackground(String name, int userNameAttr) {
        int colorChoPar = 1;

        colorChoPar = (((int) (name.charAt(0)) % 20 + 1) * userNameAttr);
        colorChoPar = colorChoPar % 10 + 1 == 10 ? 1 : colorChoPar % 10 + 1;

        switch (colorChoPar) {
            case 1:
                return R.drawable.bittersweet_color_imag;
            case 2:
                return R.drawable.mandy_color_imag;

            case 3:
                return R.drawable.flamenco_color_imag;

            case 4:
                return R.drawable.java_color_imag;

            case 5:
                return R.drawable.seagreen_color_imag;

            case 6:
                return R.drawable.fuchsiablue_color_imag;

            case 7:
                return R.drawable.tuliptree_color_imag;

            case 8:
                return R.drawable.lima_color_imag;

            case 9:
                return R.drawable.dodgerblue_color_imag;

            case 10:
                return R.drawable.larioja_color_imag;

            default:
                return R.drawable.fuchsiablue_color_imag;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("TableBigWidgetProvider", "onReceive");
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d("TableBigWidgetProvider", "onEnabled");
    }
}
