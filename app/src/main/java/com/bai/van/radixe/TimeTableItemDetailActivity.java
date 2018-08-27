package com.bai.van.radixe;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bai.van.radixe.constantdata.ConstantValues;
import com.bai.van.radixe.constantdata.SharedData;
import com.bai.van.radixe.datastru.ExamScoreInf;
import com.bai.van.radixe.datastru.TimeTableInf;
import com.bai.van.radixe.sqlite.TimeTableSqliteHandle;
import com.bai.van.radixe.userdata.UserInformation;
import com.wonderkiln.blurkit.BlurKit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author van
 * @date 2018/1/23
 */

public class TimeTableItemDetailActivity extends Activity {

    private TextView nameText, locationText, weekText, ideaText, classNo;

    private String name;
    private int week;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_time_table_item_detail);

        if (UserInformation.timeTableList.size() == 0) {
            TimeTableSqliteHandle.loadData(this);
        }

        Intent intent = getIntent();
        int week = intent.getIntExtra("dayInWeek", -1);
        int minKnob = intent.getIntExtra("minKnob", 0);
        String className = intent.getStringExtra("courseName");
        if (week != -1 && minKnob != 0) {
            for (TimeTableInf timeTableInf : UserInformation.timeTableList.get(week)) {
                if (timeTableInf.minKnob == minKnob && timeTableInf.className.endsWith(className)) {
                    SharedData.timeTableInf = timeTableInf;
                }
            }
            Log.d("TimeTableAlarm", "YES");
        }

        initial();

    }

    private void displayInf(){
        nameText.setText(SharedData.timeTableInf.className);
        locationText.setText("null".equals(SharedData.timeTableInf.address) ? ConstantValues.NO_DATA_TEXT : SharedData.timeTableInf.address);
        weekText.setText("周" + ConstantValues.DAY_IN_WEEK_CHAR.get(SharedData.timeTableInf.dayInWeek - 1) + " " + SharedData.timeTableInf.minKnob + "-" + SharedData.timeTableInf.maxKnob + "节");
        ideaText.setText("null".equals(SharedData.timeTableInf.teacher) ? ConstantValues.NO_DATA_TEXT : SharedData.timeTableInf.teacher);
        classNo.setText(SharedData.timeTableInf.classNo);
    }

    private void initial(){
        nameText = (TextView) findViewById(R.id.nameText);
        locationText = (TextView) findViewById(R.id.locationText);
        weekText = (TextView) findViewById(R.id.weekText);
        ideaText = (TextView) findViewById(R.id.ideaText);
        classNo = findViewById(R.id.classNoText);

        LinearLayout timetableBack = findViewById(R.id.timeTableDetailBack);
        timetableBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        displayInf();
    }
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
