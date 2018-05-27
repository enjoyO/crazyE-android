package com.bai.van.radixe.widget.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bai.van.radixe.R;
import com.bai.van.radixe.constantdata.StaticMethod;
import com.bai.van.radixe.datastru.TimeTableInf;
import com.bai.van.radixe.entry.Entry;
import com.bai.van.radixe.sqlite.TimeTableSqliteHandle;
import com.bai.van.radixe.userdata.UserInformation;
import com.bai.van.radixe.widget.TimetableDayWidgetProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WidgetDayItem extends RemoteViewsService{
    public WidgetDayItem() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetFactory(this.getApplicationContext(), intent);
    }


    public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;
        private List<TimeTableInf> mTermInfList = new ArrayList<>();;
        private int dayInWeek;

        private boolean isEmpty = false;


        public WidgetFactory(Context context, Intent intent) {
            mContext = context;
            dayInWeek = intent.getIntExtra(TimetableDayWidgetProvider.dayInWeek,
                  1);
        }

        private void findtermInf() {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            int currentWeekNo = sharedPreferences.getInt(Entry.SharedPreferencesEntry.CURRENT_WEEK_NO, 1);
            mTermInfList = new ArrayList<>();
            if (UserInformation.timeTableList.size() == 0) {
                Log.d("timetabledetail", "refresh");
                TimeTableSqliteHandle.loadData(mContext);
            }
            for (TimeTableInf timeTableInf : UserInformation.timeTableList.get(dayInWeek)) {
                if (StaticMethod.isCurrentWeek(currentWeekNo, timeTableInf.weekStr)) {
                    mTermInfList.add(timeTableInf);
                }
            }
            Collections.sort(mTermInfList);

            if (mTermInfList.size() == 0) {
                isEmpty = true;
                mTermInfList.add(new TimeTableInf());
            }
        }
        @Override
        public void onCreate() {
            findtermInf();
        }

        @Override
        public void onDataSetChanged() {
            /*
             * appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listview);
             * 使用该通知更新数据源，会调用onDataSetChanged
             */
            findtermInf();
            System.out.println("----onDataSetChanged----");
        }

        @Override
        public void onDestroy() {
            mTermInfList.clear();
        }

        @Override
        public int getCount() {
            return  mTermInfList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = null;
            if (isEmpty) {
                remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_day_empty);
            }else {
                TimeTableInf timeTableInf = mTermInfList.get(position);
                remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_day_item);
                remoteViews.setTextViewText(R.id.widget_day_item_classname, timeTableInf.className);
                remoteViews.setTextViewText(R.id.widget_day_item_classroom, timeTableInf.address);
                remoteViews.setTextViewText(R.id.widget_day_item_classknobs, Integer.toString(timeTableInf.minKnob)
                        .concat("-").concat(Integer.toString(timeTableInf.maxKnob)).concat("节"));
                System.out.println("RemoteViewsService----getViewAt" + position);


//            Bundle extras = new Bundle();
//            extras.putInt(WidgetSetProvider.EXTRA_ITEM, position);
//            Intent fillInIntent = new Intent();
//            fillInIntent.putExtras(extras);
//            /*
//             * android.R.layout.simple_list_item_1 --- id --- text1
//             * listview的item click：将fillInIntent发送，
//             * fillInIntent它默认的就有action 是provider中使用 setPendingIntentTemplate 设置的action
//             */
//            remoteViews.setOnClickFillInIntent(R.id.text1, fillInIntent);
            }
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            /* 在更新界面的时候如果耗时就会显示 正在加载... 的默认字样，但是你可以更改这个界面
             * 如果返回null 显示默认界面
             * 否则 加载自定义的，返回RemoteViews
             */
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

    }

}
