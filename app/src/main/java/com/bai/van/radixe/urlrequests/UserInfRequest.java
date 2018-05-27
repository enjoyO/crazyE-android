package com.bai.van.radixe.urlrequests;

import android.util.Log;

import com.bai.van.radixe.constantdata.ConstantUrls;
import com.bai.van.radixe.constantdata.ConstantValues;
import com.bai.van.radixe.datastru.ExamInf;
import com.bai.van.radixe.datastru.ScoreAnalyseInf;
import com.bai.van.radixe.datastru.UserInf;
import com.bai.van.radixe.userdata.LoginData;
import com.bai.van.radixe.userdata.UserInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author van
 * @date 2018/1/15
 */

public class UserInfRequest {
    public static void requestGradesInf(){
        String scoreAnalyseJson = UserInfRequest.postInf(ConstantUrls.GRADES_INF_URL, "XH=".concat(UserInformation.username).concat("&TJLX=02&*order=%2BXNXQDM"), "POST");

        UserInformation.scoreAnalyseInfList = new ArrayList<>();
        try {
            JSONObject jsonObjectRoot = new JSONObject(scoreAnalyseJson);
            JSONObject jsonObjectDatas = jsonObjectRoot.getJSONObject("datas");
            JSONObject jsonObjectCxxsjbxx = jsonObjectDatas.getJSONObject("cxxscjtj");
            JSONArray jsonArrayRows = jsonObjectCxxsjbxx.getJSONArray("rows");
            JSONObject jsonObjectInf;
            ScoreAnalyseInf scoreAnalyseInf;
            for (int i = 0; i < jsonArrayRows.length(); i++){
                scoreAnalyseInf = new ScoreAnalyseInf();
                jsonObjectInf = jsonArrayRows.getJSONObject(i);
                scoreAnalyseInf.unPassedCount = jsonObjectInf.getInt("BJGMS");
                scoreAnalyseInf.gpa = jsonObjectInf.getDouble("GPA");
                scoreAnalyseInf.gainedCredit = jsonObjectInf.getDouble("HDXF");
                scoreAnalyseInf.electivedCreditCount = jsonObjectInf.getDouble("XKXF");
                scoreAnalyseInf.weightedGrdes = jsonObjectInf.getDouble("JDF");
                scoreAnalyseInf.semesterStr = jsonObjectInf.getString("XNXQMC");

                UserInformation.scoreAnalyseInfList.add(scoreAnalyseInf);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void requestGradesInfAll(){
        String scoreAnalyseAllJson = UserInfRequest.postInf(ConstantUrls.GRADES_INF_URL, "XH=".concat(UserInformation.username).concat("&XNXQDM=*&TJLX=01"), "POST");

        try {
            JSONObject jsonObjectRoot = new JSONObject(scoreAnalyseAllJson);
            JSONObject jsonObjectDatas = jsonObjectRoot.getJSONObject("datas");
            JSONObject jsonObjectCxxsjbxx = jsonObjectDatas.getJSONObject("cxxscjtj");
            JSONArray jsonArrayRows = jsonObjectCxxsjbxx.getJSONArray("rows");
            JSONObject jsonObjectInf = (JSONObject) jsonArrayRows.get(0);

            UserInformation.unPassedCountAll = jsonObjectInf.getInt("BJGMS");
            UserInformation.gpaAll = jsonObjectInf.getDouble("GPA");
            UserInformation.gainedCreditAll = jsonObjectInf.getDouble("HDXF");
            UserInformation.electivedCreditAll = jsonObjectInf.getDouble("XKXF");
            UserInformation.weightedGrdesAll = jsonObjectInf.getDouble("JDF");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void requestUserBaseInf(){
        String userBaseJson = postInf(ConstantUrls.USER_BASEI_NF_URL, "XH=".concat(UserInformation.username), "POST");

        UserInformation.userInf = new UserInf();
        try {
            JSONObject jsonObjectRoot = new JSONObject(userBaseJson);
            JSONObject jsonObjectDatas = jsonObjectRoot.getJSONObject("datas");
            JSONObject jsonObjectCxxsjbxx = jsonObjectDatas.getJSONObject("cxxsjbxx");
            JSONArray jsonArrayRows = jsonObjectCxxsjbxx.getJSONArray("rows");
            JSONObject jsonObjectInf = (JSONObject) jsonArrayRows.get(0);

            UserInformation.usernameChar = jsonObjectInf.getString("XM");
            UserInformation.userInf.baseName = jsonObjectInf.getString("XM");
            UserInformation.userInf.baseNameSpell = jsonObjectInf.getString("XMPY");
            UserInformation.userInf.baseStuNumber = jsonObjectInf.getString("XH");
            UserInformation.userInf.baseGender = jsonObjectInf.getString("XBDM_DISPLAY");
            UserInformation.userInf.baseBornDate = jsonObjectInf.getString("CSRQ");
            UserInformation.userInf.baseFrom = jsonObjectInf.getString("JG_DISPLAY");
            UserInformation.userInf.basePoliticsStatus = jsonObjectInf.getString("ZZMMDM_DISPLAY");
            UserInformation.userInf.baseNation = jsonObjectInf.getString("MZDM_DISPLAY");
            UserInformation.userInf.baseCountry = jsonObjectInf.getString("GJDQDM_DISPLAY");
            UserInformation.userInf.baseIdCardNO = jsonObjectInf.getString("SFZJH");
            UserInformation.userInf.baseIdCardType = jsonObjectInf.getString("SFZJLXDM_DISPLAY");

//            Log.d("UserInf", UserInformation.usernameChar);

            UserInfRequest.requestUserStatusInf();
            UserInfRequest.requestUserEntranInf();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void requestUserStatusInf(){
        String userStatusJson = postInf(ConstantUrls.USER_STATUS_URL, "XH=".concat(UserInformation.username), "POST");

        try {
            JSONObject jsonObjectRoot = new JSONObject(userStatusJson);
            JSONObject jsonObjectDatas = jsonObjectRoot.getJSONObject("datas");
            JSONObject jsonObjectCxxsjbxx = jsonObjectDatas.getJSONObject("cxxsxjxx");
            JSONArray jsonArrayRows = jsonObjectCxxsjbxx.getJSONArray("rows");
            JSONObject jsonObjectInf = (JSONObject) jsonArrayRows.get(0);

            UserInformation.userAcademy = jsonObjectInf.getString("YXDM_DISPLAY");
            UserInformation.userMajor = jsonObjectInf.getString("ZYDM_DISPLAY");
            UserInformation.userStaYear = Integer.parseInt(jsonObjectInf.getString("XZNJ"));

            UserInformation.userInf.rollGrade = jsonObjectInf.getString("XZNJ_DISPLAY");
            UserInformation.userInf.rollAcademy = jsonObjectInf.getString("YXDM_DISPLAY");
            UserInformation.userInf.rollMajor = jsonObjectInf.getString("ZYDM_DISPLAY");
            UserInformation.userInf.rollMajorDirection = jsonObjectInf.getString("ZYFXDM_DISPLAY");
            UserInformation.userInf.rollClass = jsonObjectInf.getString("BJDM_DISPLAY");
            UserInformation.userInf.rollLengthSys = jsonObjectInf.getString("XZ");
            UserInformation.userInf.rollType = jsonObjectInf.getString("XSLBDM_DISPLAY");
            UserInformation.userInf.rollLearnType = jsonObjectInf.getString("XKMLDM_DISPLAY");
            UserInformation.userInf.rollIsInRoll = jsonObjectInf.getString("SFZJ_DISPLAY");
            UserInformation.userInf.rollIsInSchool = jsonObjectInf.getString("SFZX_DISPLAY");
            UserInformation.userInf.rollIsHaveRoll = jsonObjectInf.getString("XJZTDM_DISPLAY");


//            Log.d("UserInf", UserInformation.userAcademy);
//            Log.d("UserInf", UserInformation.userMajor);

        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
    public static void requestUserEntranInf(){
        String userEntranJson = postInf(ConstantUrls.USER_ENTRANCE_URL, "XH=".concat(UserInformation.username), "POST");

        try {
            JSONObject jsonObjectRoot = new JSONObject(userEntranJson);
            JSONObject jsonObjectDatas = jsonObjectRoot.getJSONObject("datas");
            JSONObject jsonObjectCxxsrxxx = jsonObjectDatas.getJSONObject("cxxsrxxx");
            JSONArray jsonArrayRows = jsonObjectCxxsrxxx.getJSONArray("rows");
            JSONObject jsonObjectInf = (JSONObject) jsonArrayRows.get(0);

            UserInformation.userInf.entranDate = jsonObjectInf.getString("RXNY");
            UserInformation.userInf.entranGrade = jsonObjectInf.getString("RXNJ");
            UserInformation.userInf.entranFrom = jsonObjectInf.getString("SYDDM_DISPLAY");
            UserInformation.userInf.entranForeignLanType = jsonObjectInf.getString("WYYZDM_DISPLAY");
            UserInformation.userInf.entranLearnType = jsonObjectInf.getString("XXXSDM_DISPLAY");
            UserInformation.userInf.entranSpecialStuType = jsonObjectInf.getString("TSXSLXDM_DISPLAY");
            UserInformation.userInf.entranTrainType = jsonObjectInf.getString("PYCCDM_DISPLAY");
            UserInformation.userInf.entranEnrollYear = jsonObjectInf.getString("ZSND");
            UserInformation.userInf.entranEnrollScore = jsonObjectInf.getString("GKCJ");
            UserInformation.userInf.entranEnrollNO = jsonObjectInf.getString("KSH");
            UserInformation.userInf.entranMidSchool = jsonObjectInf.getString("ZSZXMC");

            Log.d("MidSchool", UserInformation.userInf.entranMidSchool);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void requestExamScheduled(){
        String userExamScheduled = postInf(ConstantUrls.USER_EXAM_SCHEDULED_URL, "XNXQDM=" + UserInformation.currentTerm + "&KSRWZT=1&*order=-KSRQ", "POST");
        UserInformation.examScheduledFinishList = new ArrayList<>();
        UserInformation.examScheduledUnfinishList = new ArrayList<>();
        try {
            JSONObject jsonObjectRoot = new JSONObject(userExamScheduled);
            JSONObject jsonObjectDatas = jsonObjectRoot.getJSONObject("datas");
            JSONObject jsonObjectWdksap = jsonObjectDatas.getJSONObject("wdksap");
            JSONArray jsonArrayRows = jsonObjectWdksap.getJSONArray("rows");

            for (int i = 0; i < jsonArrayRows.length(); i++){
                ExamInf examInf = new ExamInf();
                JSONObject jsonObjectExam = jsonArrayRows.getJSONObject(i);
                examInf.name = jsonObjectExam.getString("KCM");
                examInf.type = jsonObjectExam.getString("KSMC");
                examInf.address = jsonObjectExam.getString("JASMC");
                examInf.times = jsonObjectExam.getString("KSSJMS");
                examInf.id = jsonObjectExam.getString("KCH");

                if (dateSmallThanNow(examInf.times.split(" ")[0])){
                    UserInformation.examScheduledFinishList.add(examInf);
                }else {
                    UserInformation.examScheduledUnfinishList.add(examInf);
                }
            }
            Collections.sort(UserInformation.examScheduledUnfinishList);
            Collections.sort(UserInformation.examScheduledFinishList);

//            UserInformation.examScheduledUnfinishList = UserInformation.examScheduledFinishList;

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static void requestExamInSchedule(){
        String userExamInSchedule = postInf(ConstantUrls.USER_EXAM_IN_SCHEDULE_URL, "querySetting=%5B%7B%22name%22%3A%22XNXQDM%22%2C%22linkopt%22%3A%22AND%22%2C%22builder%22%3A%22equal%22%2C%22value%22%3A%22"
                + UserInformation.currentTerm + "%22%7D%5D", "POST");
//        Log.d("inscheduled", userExamInSchedule);
        UserInformation.examInScheduleList = new ArrayList<>();
        try {
            JSONObject jsonObjectRoot = new JSONObject(userExamInSchedule);
            JSONObject jsonObjectDatas = jsonObjectRoot.getJSONObject("datas");
            JSONObject jsonObjectWapks = jsonObjectDatas.getJSONObject("wapks");
            JSONArray jsonArrayRows = jsonObjectWapks.getJSONArray("rows");

            ExamInf examInf;
            for (int i = 0; i < jsonArrayRows.length(); i++){
                examInf = new ExamInf();
                JSONObject jsonObjectExam = jsonArrayRows.getJSONObject(i);
                examInf.name = jsonObjectExam.getString("KCM");
                examInf.id = jsonObjectExam.getString("KCH");
                UserInformation.examInScheduleList.add(examInf);
            }
            UserInformation.examInScheduleList.removeAll(UserInformation.examScheduledFinishList);
            UserInformation.examInScheduleList.removeAll(UserInformation.examScheduledUnfinishList);
//            Log.d("InSchedule Size", UserInformation.examInScheduleList.size() + "");
//            Log.d("InSchedule", UserInformation.examInScheduleList.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String postInf(String urls, String postStr, String method) {
        HttpURLConnection connections = null;
        BufferedReader reader = null;
        String returnStr = "";
        try {
            URL url = new URL(urls);
            connections = (HttpURLConnection) url.openConnection();
            connections.setRequestMethod(method);
            connections.setConnectTimeout(8000);
            connections.setReadTimeout(8000);

            connections.setRequestProperty("Cookie", LoginData.weuReal + "; " + LoginData.modAuthCas);
            connections.setRequestProperty("User-Agent", "Mozilla/5.0.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
            connections.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            connections.setDoOutput(true);
            connections.setDoInput(true);
            connections.setUseCaches(false);
            connections.setInstanceFollowRedirects(false);

            OutputStream out = connections.getOutputStream();
            out.write(postStr.getBytes());
            out.flush();

//            Log.d("postStr", postStr);

            InputStream inputStream = connections.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            returnStr = response.toString();
//            Log.d("postInf", returnStr);
            out.close();

            if (connections.getResponseCode() == ConstantValues.REQUEST_INF_FAILED){
                LoginRequest.sendGet(ConstantUrls.REQUEST_LOGIN_CODE_URL);
                LoginRequest.sendPost(ConstantUrls.LOGIN_ADRESS_URL, UserInformation.username, UserInformation.password,
                        LoginData.jsessionId + "; " + LoginData.route, LoginData.lt);
                if (LoginData.loginResponseCode == ConstantUrls.LOGIN_SUCESS_CODE){
                    GradesRequest.getWeu();
                }
                returnStr = UserInfRequest.postInf(urls, postStr, method);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connections != null){
                connections.disconnect();
            }
        }
        return returnStr;
    }
    public static boolean dateSmallThanNow(String dateStr) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(dateStr);
            Date now = new Date();
            if (date.getTime() < now.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
}