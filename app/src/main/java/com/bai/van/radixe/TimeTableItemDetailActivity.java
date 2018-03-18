package com.bai.van.radixe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bai.van.radixe.baseclass.BaseActivity;
import com.bai.van.radixe.constantdata.ConstantValues;
import com.bai.van.radixe.datastru.TimeTableInf;
import com.bai.van.radixe.sqlite.TimeTableSqliteHandle;
import com.bai.van.radixe.userdata.UserInformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author van
 * @date 2018/1/23
 */

public class TimeTableItemDetailActivity extends BaseActivity {

    private TextView nameText, locationText, weekText, timeText, ideaText,
                    nameText0, locationText0, weekText0, timeText0, ideaText0;
    private LinearLayout linearLayoutSec;

    private String name;
    private int week;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_item_detail);

        Intent intent = getIntent();
        name = intent.getStringExtra("courseName").split("@")[0];
        week = intent.getIntExtra("dayInWeek", -1);

        initial();

        if (week != -1){
            if (UserInformation.timeTableList.size() == 0){
                Log.d("timetabledetail", "refresh");
                TimeTableSqliteHandle.loadData(this);
                displayInf();
            }else {
                displayInf();
            }
        }else {
            Log.d("TimeTableItemDetail", "error");
        }

    }
    private void displayInf(){
        TimeTableInf timeTableInf = null;
        for (int i = 0; i < UserInformation.timeTableList.get(week).size(); i++){
            if (name.equals(UserInformation.timeTableList.get(week).get(i).className)){
                timeTableInf = UserInformation.timeTableList.get(week).get(i);
            }
        }
        List<TimeTableInf> timeTableInfList = new ArrayList<>();
        for (int i = 0; i < UserInformation.timeTableList.get(week).size(); i++){
            if (timeTableInf.minKnob == UserInformation.timeTableList.get(week).get(i).minKnob){
                timeTableInfList.add(UserInformation.timeTableList.get(week).get(i));
            }
        }
        if (!timeTableInfList.get(0).className.equals(name)){
            Collections.reverse(timeTableInfList);
        }
        if (timeTableInfList.size() > 0){
            timeTableInf = timeTableInfList.get(0);
            nameText.setText(timeTableInf.className);
            locationText.setText(timeTableInf.address);
            weekText.setText(timeTableInf.weekTime);
            timeText.setText("周" + ConstantValues.DAY_IN_WEEK_CHAR.get(week) + " " + timeTableInf.minKnob + "-" + timeTableInf.maxKnob + "节");
            ideaText.setText(timeTableInf.teacher.equals("null") ? "无" : timeTableInf.teacher);
            if (timeTableInfList.size() > 1){
                timeTableInf = timeTableInfList.get(1);
                nameText0.setText(timeTableInf.className);
                locationText0.setText(timeTableInf.address);
                weekText0.setText(timeTableInf.weekTime);
                timeText0.setText("周" + ConstantValues.DAY_IN_WEEK_CHAR.get(week) + " " + timeTableInf.minKnob + "-" + timeTableInf.maxKnob + "节");
                ideaText0.setText(timeTableInf.teacher.equals("null") ? "无" : timeTableInf.teacher);
                linearLayoutSec.setVisibility(View.VISIBLE);
            }
        }else {
            Log.d("TimeTableItemDetail", "error1");
        }
    }

    private void initial(){
        linearLayoutSec = (LinearLayout) findViewById(R.id.linearLayoutSec);

        nameText = (TextView) findViewById(R.id.nameText);
        locationText = (TextView) findViewById(R.id.locationText);
        weekText = (TextView) findViewById(R.id.weekText);
        timeText = (TextView) findViewById(R.id.timeText);
        ideaText = (TextView) findViewById(R.id.ideaText);

        nameText0 = (TextView) findViewById(R.id.nameText0);
        locationText0 = (TextView) findViewById(R.id.locationText0);
        weekText0 = (TextView) findViewById(R.id.weekText0);
        timeText0 = (TextView) findViewById(R.id.timeText0);
        ideaText0 = (TextView) findViewById(R.id.ideaText0);

        RelativeLayout timetableBack = (RelativeLayout) findViewById(R.id.timeTableDetailBack);
        timetableBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
