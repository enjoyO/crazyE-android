package com.bai.van.radixe.urlrequests;

import android.util.Log;

import com.bai.van.radixe.constantdata.ConstantUrls;
import com.bai.van.radixe.constantdata.ConstantValues;
import com.bai.van.radixe.datastru.TermInf;
import com.bai.van.radixe.datastru.TimeTableInf;
import com.bai.van.radixe.userdata.UserInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author van
 * @date 2018/1/21
 */

public class TimeTableRequest {
    public static void timetableRequest(String currentTerm){
        String timeTableJson = UserInfRequest.postInf(ConstantUrls.TIME_TABLE_URL,
                "XNXQDM=" + currentTerm, "POST");
//        Log.d("timeTable", timeTableJson);

        UserInformation.timeTableList = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            UserInformation.timeTableList.add(new ArrayList<TimeTableInf>());
        }
        try {
            JSONObject jsonObjectRoot = new JSONObject(timeTableJson);
            JSONObject jsonObjectDatas = jsonObjectRoot.getJSONObject("datas");
            JSONObject jsonObjectWapks = jsonObjectDatas.getJSONObject("xskcb");
            JSONArray jsonArrayRows = jsonObjectWapks.getJSONArray("rows");

            TimeTableInf timeTableInf;
            for (int i = 0; i < jsonArrayRows.length(); i++){
                JSONObject jsonObjectClass = jsonArrayRows.getJSONObject(i);
                timeTableInf = new TimeTableInf();

                timeTableInf.className = jsonObjectClass.getString("KCM");
                timeTableInf.teacher = jsonObjectClass.getString("SKJS");
                timeTableInf.address = jsonObjectClass.getString("JASMC");
                timeTableInf.weekTime = jsonObjectClass.getString("ZCMC");
                timeTableInf.classNo = jsonObjectClass.getString("JXBID");

                timeTableInf.dayInWeek = Integer.parseInt(jsonObjectClass.getString("SKXQ"));
                timeTableInf.minKnob = Integer.parseInt(jsonObjectClass.getString("KSJC"));
                timeTableInf.maxKnob = Integer.parseInt(jsonObjectClass.getString("JSJC"));

                timeTableInf.weekStr = jsonObjectClass.getString("SKZC");

                UserInformation.timeTableList.get(timeTableInf.dayInWeek - 1).add(timeTableInf);


            }
            for (int i = 0; i < UserInformation.timeTableList.size(); i++){
                Collections.sort(UserInformation.timeTableList.get(i));
            }
//            Log.d("depresed Timetable", UserInformation.timeTableList.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void currentWeekNoRequest(){
        if ("".equals(UserInformation.currentTerm)){
            GradesRequest.postRequestCurrentTerm();
        }
        Calendar calendar = Calendar.getInstance();
        String weekNoJson = "";
        try {
            weekNoJson = UserInfRequest.postInf(ConstantUrls.CURRENT_WEEK_NO_URL,
                    "XN=" + UserInformation.currentTerm.substring(0, 9) + "&XQ=" + UserInformation.currentTerm.split("-")[2]
                            + "&RQ=" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH), "POST");
        }catch (StringIndexOutOfBoundsException e){
            e.printStackTrace();
        }

//        Log.d("currentWeekNoRequest", weekNoJson);

        try {
            JSONObject jsonObjectRoot = new JSONObject(weekNoJson);
            JSONObject jsonObjectDatas = jsonObjectRoot.getJSONObject("datas");
            JSONObject jsonObjectCxxsjbxx = jsonObjectDatas.getJSONObject("dqzc");
            JSONArray jsonArrayRows = jsonObjectCxxsjbxx.getJSONArray("rows");
            JSONObject jsonObjectInf = (JSONObject) jsonArrayRows.get(0);

            UserInformation.currentWeekNo = jsonObjectInf.getInt("ZC");

//            Log.d("currentWeekNoRequest", UserInformation.currentWeekNo + "");

            UserInfRequest.requestUserStatusInf();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void requestTermAll(){
        String termAllJson = UserInfRequest.postInf(ConstantUrls.SEMESTER_URL, "*order=%2BDM", "POST");
//        Log.d("termAllJson", termAllJson);

        UserInformation.termInfList = new ArrayList<>();
        UserInformation.termTranMap = new HashMap<>();
        try {
            JSONObject jsonObjectRoot = new JSONObject(termAllJson);
            JSONObject jsonObjectDatas = jsonObjectRoot.getJSONObject("datas");
            JSONObject jsonObjectXnxqcx = jsonObjectDatas.getJSONObject("xnxqcx");
            JSONArray jsonArrayRows = jsonObjectXnxqcx.getJSONArray("rows");

            JSONObject jsonObject;
            TermInf termInf;
            for (int i = 0; i < jsonArrayRows.length(); i++){
                termInf = new TermInf();
                jsonObject = jsonArrayRows.getJSONObject(i);

                termInf.term = jsonObject.getString("DM");
                termInf.termChar = jsonObject.getString("MC");
                UserInformation.termTranMap.put(termInf.termChar, termInf.term);
                UserInformation.termInfList.add(termInf);
            }
            Collections.reverse(UserInformation.termInfList);
//            Log.d("termAllJson", UserInformation.termInfList.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
