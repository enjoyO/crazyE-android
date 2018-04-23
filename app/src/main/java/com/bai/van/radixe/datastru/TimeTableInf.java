package com.bai.van.radixe.datastru;

import android.support.annotation.NonNull;

import java.util.Arrays;

/**
 *
 * @author van
 * @date 2018/1/21
 */

public class TimeTableInf implements Comparable<TimeTableInf>{
    public String className = "";
    public String address = "";
    public String teacher = "";
    public String weekTime = "";

    public String classNo = "";

//    public String knobTime;
//    public String dayInWeekStr;

    public int dayInWeek = 0;

    public int minKnob = 0;
    public int maxKnob = 0;

    public String weekStr = "";

    @Override
    public int compareTo(@NonNull TimeTableInf timeTableInf) {
        if (this.minKnob >= timeTableInf.minKnob){
            return 1;
        }else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "TimeTableInf{" +
                "className='" + className + '\'' +
                ", address='" + address + '\'' +
                ", teacher='" + teacher + '\'' +
                ", weekTime='" + weekTime + '\'' +
                ", classNo='" + classNo + '\'' +
                ", dayInWeek=" + dayInWeek +
                ", minKnob=" + minKnob +
                ", maxKnob=" + maxKnob +
                ", weekStr='" + weekStr + '\'' +
                '}';
    }
}