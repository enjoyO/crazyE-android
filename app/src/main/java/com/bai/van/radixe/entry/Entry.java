package com.bai.van.radixe.entry;

import android.provider.BaseColumns;

/**
 *
 * @author van
 * @date 2018/2/11
 */

public class Entry {
    public static class TimeTableEntry implements BaseColumns {
        public static final String TABLE_NAME = "timeTable";

        public static final String _ID = BaseColumns._ID;
        public static final String COLOUM_TIME_TABLE_CLASS_NAME = "className";
        public static final String COLOUM_TIME_TABLE_ADDRESS = "address";
        public static final String COLOUM_TIME_TABLE_TEACHER = "teacher";
        public static final String COLOUM_TIME_TABLE_WEEK_TIME = "weekTime";
        public static final String COLOUM_TIME_TABLE_CLASS_NO = "classNo";
        public static final String COLOUM_TIME_TABLE_DAY_IN_WEEK = "dayInWeek";
        public static final String COLOUM_TIME_TABLE_MIN_KNOB = "minKnob";
        public static final String COLOUM_TIME_TABLE_MAX_KNOB = "maxKnob";
        public static final String COLOUM_TIME_TABLE_WEEK_STR = "weekStr";

    }
    public static class SharedPreferencesEntry implements BaseColumns {
        public static final String SHARED_PREFERENCES_NAME = "myspl";
        public static final String USER_NAME = "username";
        public static final String PASS_WORD = "password";
        public static final String IS_AUTO_LOGIN = "autologin";
        public static final String IS_RECEIVE_TIME_TABLE_ALARM = "isReceiveTimeTableAlarm";
        public static final String ALARM_REMIND_TIME_INTERVAL = "alarmRemindTimeInterval";
        public static final String CURRENT_WEEK_NO = "currentWeekNo";
        public static final String CURRENT_WEEK_TIMEINMILLIS = "currentWeekNoTime";
        public static final String CURRENT_TERM = "currentTerm";
        public static final String USER_STA_YEAR = "userStaYear";
        public static final String TIME_TABLE_COLOR_TYPE = "timeTAbleColorType";
    }
}
