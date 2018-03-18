package com.bai.van.radixe.datastru;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author van
 * @date 2018/1/15
 */

public class ExamInf implements Comparable<ExamInf>{
    public String name = "";
    public String type = "";
    public String times = "";
    public String address = "";
    public String id = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamInf)) {
            return false;
        }

        ExamInf examInf = (ExamInf) o;

        if (name != null ? !name.equals(examInf.name) : examInf.name != null) {
            return false;
        }
        return id != null ? id.equals(examInf.id) : examInf.id == null;
    }

    @Override
    public String toString() {
        return "ExamInf{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", times='" + times + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull ExamInf examInf) {
        DateFormat dateFormatYear = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateThis = dateFormatYear.parse(this.times.split(" ")[0]);
            Date dateCom = dateFormatYear.parse(examInf.times.split(" ")[0]);
            if (dateThis.getTime() < dateCom.getTime()){
                return -1;
            }else if (dateThis.getTime() > dateCom.getTime()){
                return 1;
            }else {
                DateFormat dateFormatHour = new SimpleDateFormat("HH:mm");
                dateThis = dateFormatHour.parse(this.times.split(" ")[1].split("-")[0]);
                dateCom = dateFormatHour.parse(examInf.times.split(" ")[1].split("-")[0]);
                if (dateThis.getTime() < dateCom.getTime()){
                    return -1;
                }else {
                    return 1;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
