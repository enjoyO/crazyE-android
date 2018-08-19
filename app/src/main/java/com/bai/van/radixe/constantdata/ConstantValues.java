package com.bai.van.radixe.constantdata;

import android.support.v4.app.INotificationSideChannel;

import com.bai.van.radixe.R;

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

    public static final String PRIME_NAME = "综合素质教育选修";

    public static final String NO_DATA_TEXT = "👻";

    public static final int[] HEAD_NAME_BOY = {R.drawable.bun, R.drawable.rice_ball, R.drawable.kiwi, R.drawable.watermelon, R.drawable.pumpkin, R.drawable.chestnut, R.drawable.blueberry, R.drawable.fresh_meat, R.drawable.orange, R.drawable.cabbage};
    public static final int[] HEAD_NAME_GIRL = {R.drawable.pomegranate, R.drawable.octopus, R.drawable.mushroom, R.drawable.dragon_fruit, R.drawable.tomato, R.drawable.coconut, R.drawable.cabbage, R.drawable.bun, R.drawable.orange, R.drawable.rice_ball};

    public static final int[] TIME_TABLE_COLOR_DEFAULT = {R.color.silver_sand, R.color.bittersweet, R.color.medium_sea_green, R.color.fuchsia_blue, R.color.mandy, R.color.java, R.color.flamenco, R.color.tulip_tree, R.color.lima, R.color.dodger_blue, R.color.la_rioja};
    public static final int[] TIME_TABLE_COLOR_LIGHT = {R.color.alto, R.color.spiro_disco_ball, R.color.caribbean_green, R.color.coral, R.color.downy, R.color.deep_sky_blue, R.color.vivid_tangerine, R.color.malachite, R.color.spring_bud, R.color.las_palmas, R.color.yellow_sea};
    public static final int[] TIME_TABLE_COLOR_LIGHTS = {R.color.alto, R.color.flamingo_pink, R.color.glacier, R.color.yourpink, R.color.sprays, R.color.monte_carlo, R.color.columbia_blue, R.color.wewak, R.color.deco, R.color.sweet_corn, R.color.light_sky_blue};

    public static final int[] TIME_TABLE_COLOR_MEDIUM = {R.color.alto, R.color.bright_sun, R.color.caper, R.color.tacao, R.color.vivid_tangerine, R.color.atlantis, R.color.downys, R.color.burnt_siennas, R.color.cornflower_blue, R.color.picton_blue, R.color.downyse};

}
