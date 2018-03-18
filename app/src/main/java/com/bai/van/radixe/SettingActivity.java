package com.bai.van.radixe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bai.van.radixe.baseclass.BaseActivity;
import com.bai.van.radixe.entry.Entry;
import com.bai.van.radixe.overridemodule.TimeTableRemindIntervalDialog;
import com.bai.van.radixe.service.TimeTableAlarmSetting;
import com.bai.van.radixe.sqlite.TimeTableSqliteHandle;
import com.bai.van.radixe.userdata.LoginData;
import com.bai.van.radixe.userdata.UserInformation;

/**
 * @author van
 * @date 2018/2/10
 */
public class SettingActivity extends BaseActivity implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener{

    private LinearLayout timeTableAlarmSettingLayout;
    private TextView timeTableAlarmIntervalDisplayText;
    private Context context;
    private SharedPreferences sharedPreferences;
    private TimeTableRemindIntervalDialog timeTableRemindIntervalDialog;

    private float widthPixels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sharedPreferences = getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        context = this;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthPixels = displayMetrics.widthPixels;

        initial();
        initialBottomDialog();
    }
    private void initial(){
        RelativeLayout settingBack = findViewById(R.id.settingBack);
        LinearLayout aboutLayout = findViewById(R.id.aboutLayout);
        LinearLayout signOutLayout = findViewById(R.id.signOutLayout);
        timeTableAlarmSettingLayout = findViewById(R.id.timeTableAlarmSettingLayout);
        timeTableAlarmIntervalDisplayText = findViewById(R.id.timeTableAlarmIntervalDisplayText);
        Switch autoLoginSwitch = findViewById(R.id.autoLoginSwitch);
        Switch timeTableAlarmSwitch = findViewById(R.id.timeTableAlarmSwitch);

        settingBack.setOnClickListener(this);
        aboutLayout.setOnClickListener(this);
        signOutLayout.setOnClickListener(this);
        timeTableAlarmSettingLayout.setOnClickListener(this);
        autoLoginSwitch.setOnCheckedChangeListener(this);
        timeTableAlarmSwitch.setOnCheckedChangeListener(this);

        autoLoginSwitch.setChecked(sharedPreferences.getBoolean(Entry.SharedPreferencesEntry.IS_AUTO_LOGIN, true));
        timeTableAlarmSwitch.setChecked(sharedPreferences.getBoolean(Entry.SharedPreferencesEntry.IS_RECEIVE_TIME_TABLE_ALARM, true));
        timeTableAlarmIntervalDisplayText.setText("课前"
                .concat(Integer.toString(sharedPreferences.getInt(Entry.SharedPreferencesEntry.ALARM_REMIND_TIME_INTERVAL, 15)))
                .concat("分钟"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.settingBack:
                finish();
                break;
            case R.id.aboutLayout:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.signOutLayout:
                showLoginOutDialog();
                break;
            case R.id.timeTableAlarmSettingLayout:
                timeTableRemindIntervalDialog.show();
                WindowManager.LayoutParams layoutParams = timeTableRemindIntervalDialog.getWindow().getAttributes();
                layoutParams.width = (int)(widthPixels);
                timeTableRemindIntervalDialog.getWindow().setAttributes(layoutParams);
                Log.d("click", "timeTableAlarmSettingLayout");
                break;
            default:
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    private void showLoginOutDialog(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("退出登录");
        builder.setMessage("是否确认退出登录? \n退出将清除所有数据和取消所有课前提醒");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TimeTableAlarmSetting.cancelTimeTableAlarm(context, UserInformation.currentTimeTableList);
                TimeTableSqliteHandle.clearTable(context);
                recoverAllSetting();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                UserInformation.clearValue();
                LoginData.clearValue();
                finish();
                MainActivity.mainActivity.finish();
            }
        });
        builder.show();
    }
    private void recoverAllSetting(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Entry.SharedPreferencesEntry.IS_AUTO_LOGIN, true);
        editor.putBoolean(Entry.SharedPreferencesEntry.IS_RECEIVE_TIME_TABLE_ALARM, true);
        editor.putInt(Entry.SharedPreferencesEntry.ALARM_REMIND_TIME_INTERVAL, 15);
        editor.apply();
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.autoLoginSwitch:
                sharedPreferences.edit().putBoolean(Entry.SharedPreferencesEntry.IS_AUTO_LOGIN, b).apply();
                break;
            case R.id.timeTableAlarmSwitch:
                sharedPreferences.edit().putBoolean(Entry.SharedPreferencesEntry.IS_RECEIVE_TIME_TABLE_ALARM, b).apply();
                if (b){
                    timeTableAlarmSettingLayout.setVisibility(View.VISIBLE);
                    TimeTableAlarmSetting.createTimeTableAlarm(this, UserInformation.currentTimeTableList);
                }else {
                    timeTableAlarmSettingLayout.setVisibility(View.GONE);
                    TimeTableAlarmSetting.cancelTimeTableAlarm(this, UserInformation.currentTimeTableList);
                }
                break;
            default:
        }
    }
    private void initialBottomDialog(){
        timeTableRemindIntervalDialog = new TimeTableRemindIntervalDialog(context, R.style.BottomDialog, new TimeTableRemindIntervalDialog.MyDialogListener() {
            @Override
            public void onClick(View view) {
                timeTableRemindIntervalDialog.dismiss();
                int remindInterval = Integer.parseInt(((TextView) view).getText().toString().split(" ")[1]);
                sharedPreferences.edit().putInt(Entry.SharedPreferencesEntry.ALARM_REMIND_TIME_INTERVAL, remindInterval).apply();

                TimeTableAlarmSetting.cancelTimeTableAlarm(context, UserInformation.currentTimeTableList);
                TimeTableAlarmSetting.createTimeTableAlarm(context, UserInformation.currentTimeTableList);

                timeTableAlarmIntervalDisplayText.setText("课前".concat(Integer.toString(remindInterval)).concat("分钟"));
            }
        });
        timeTableRemindIntervalDialog.setCanceledOnTouchOutside(true);
        timeTableRemindIntervalDialog.getWindow().setGravity(Gravity.BOTTOM);
        timeTableRemindIntervalDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }
}
