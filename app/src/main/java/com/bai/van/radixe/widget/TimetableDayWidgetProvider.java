package com.bai.van.radixe.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.bai.van.radixe.R;
import com.bai.van.radixe.constantdata.ConstantValues;
import com.bai.van.radixe.entry.Entry;
import com.bai.van.radixe.sqlite.TimeTableSqliteHandle;
import com.bai.van.radixe.userdata.UserInformation;
import com.bai.van.radixe.widget.adapters.WidgetDayItem;

import java.util.Calendar;

/**
 * @author van
 * @date 2018/5/23
 */
public class TimetableDayWidgetProvider extends AppWidgetProvider {
    public static String dayInWeek = "day_in_week";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        if (UserInformation.timeTableList.size() == 0) {
            Log.d("timetabledetail", "refresh");
            TimeTableSqliteHandle.loadData(context);
        }

        Calendar calendarNow = Calendar.getInstance();
        int week = calendarNow.get(Calendar.DAY_OF_WEEK);

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

        for(int i = 0; i < appWidgetIds.length; i++){
            Intent intent = new Intent(context, WidgetDayItem.class);
            intent.putExtra(dayInWeek, ((week + 5) % 7));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timetable_day_widget);
            views.setRemoteAdapter(R.id.widget_day_listview, intent);
            views.setTextViewText(R.id.widget_day_week, "周".concat(ConstantValues.DAY_IN_WEEK_CHAR.get(((week + 5) % 7))));

            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }
}
