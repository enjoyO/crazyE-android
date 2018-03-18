package com.bai.van.radixe.constantdata;

import android.support.v4.app.INotificationSideChannel;

import java.util.HashMap;

/**
 *
 * @author van
 * @date 2018/1/15
 */

public class ConstantValues {

    public final static int EXAM_SCHEDULE_FINISHED = 1;
    public final static int EXAM_SCHEDULE_UNFINISHE = 2;
    public final static int EXAM_IN_SCHEDULE = 3;

    public final static int TIMETABLE_WEEK_ALL = 1;
    public final static int TIMETABLE_WEEK_SINGLE = 2;
    public final static int TIMETABLE_WEEK_DOUBLE = 3;

    public final static HashMap<Integer, String> DAY_IN_WEEK_CHAR = new HashMap<Integer, String>(){{
        put(0, "一");
        put(1, "二");
        put(2, "三");
        put(3, "四");
        put(4, "五");
        put(5, "六");
        put(6, "日");
    }};
    public final static HashMap<Integer, Integer> HOUR_TIME_TABLE = new HashMap<Integer, Integer>(){{
        put(1, 8);
        put(2, 9);
        put(3, 10);
        put(4, 11);
        put(5, 14);
        put(6, 14);
        put(7, 16);
        put(8, 16);
        put(9, 19);
        put(10, 19);
        put(11, 21);
        put(12, 21);
    }};
    public final static HashMap<Integer, Integer> MINUTE_TIME_TABLE = new HashMap<Integer, Integer>(){{
        put(1, 30);
        put(2, 25);
        put(3, 30);
        put(4, 25);
        put(5, 0);
        put(6, 55);
        put(7, 0);
        put(8, 55);
        put(9, 0);
        put(10, 55);
        put(11, 0);
        put(12, 55);
    }};
    public final static HashMap<Integer, String> TIME_TIME_TABLE = new HashMap<Integer, String>(){{
        put(0, "");
        put(1, "08:30");
        put(2, "09:25");
        put(3, "10:30");
        put(4, "11:25");
        put(5, "14:00");
        put(6, "14:55");
        put(7, "16:00");
        put(8, "16:55");
        put(9, "19:00");
        put(10, "19:55");
        put(11, "21:00");
        put(12, "21:55");
    }};
    public final static HashMap<Integer, String> TIME_FIN_TIME_TABLE = new HashMap<Integer, String>(){{
        put(0, "");
        put(1, "09:15");
        put(2, "10:10");
        put(3, "11:15");
        put(4, "12:10");
        put(5, "14:45");
        put(6, "15:40");
        put(7, "16:45");
        put(8, "17:40");
        put(9, "19:45");
        put(10, "20:40");
        put(11, "21:45");
        put(12, "22:40");
    }};
    public static final int REQUEST_INF_FAILED = 302;

    public static final String TIME_TABLE_ALARM_ACTION = "com.bai.van.radixe.service.TimeTableAlarmReceiver";
}
