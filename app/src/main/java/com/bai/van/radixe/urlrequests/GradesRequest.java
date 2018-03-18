package com.bai.van.radixe.urlrequests;

import android.util.Log;

import com.bai.van.radixe.constantdata.ConstantUrls;
import com.bai.van.radixe.constantdata.StaticMethod;
import com.bai.van.radixe.datastru.ExamScoreInf;
import com.bai.van.radixe.userdata.LoginData;
import com.bai.van.radixe.userdata.UserInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author van
 * @date 2018/1/13
 */

public class GradesRequest {
    public static void getWeu(){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://ehall.ynu.edu.cn/jwapp/sys/czjl/*default/index.do?amp_sec_version_=1" +
                    "&gid_=Wmc2YWE0c0VrdzJULzdSSU1jYWt1NTRHNG5hdExGa1hRbStOUVNvUk1MNVQweUkwNSt0Z1kzMXl3UU1DM2Fwd0pKcUZRVWNlU3ZXVGpEUzdzTHBEWmc9PQ&EMAP_LANG=zh&THEME=indigo");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
            connection.setRequestProperty("Cookie", LoginData.modAuthCas);

            List<String> cookieStrs = connection.getHeaderFields().get("Set-Cookie");

            Log.d("getWeu", cookieStrs.toString());
            LoginData.weu = cookieStrs.get(1).split(";")[0];

            Log.d("Authority", LoginData.weu);
            GradesRequest.getWeuReal();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null){
                connection.disconnect();
            }
        }
    }
    public static void getWeuReal(){
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://ehall.ynu.edu.cn" +
                    "/jwapp/sys/funauthapp/api/getAppConfig/czjl-4769553753604771.do?v=020560880937966197");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");

            connection.setRequestProperty("Cookie", LoginData.weu + "; " + LoginData.modAuthCas);

            List<String> cookieStrs = connection.getHeaderFields().get("Set-Cookie");
            for (String string : cookieStrs) {
                if (string.startsWith("_WEU")) {
                    LoginData.weuReal = string.split(";")[0];
                }
            }
            Log.d("Authority", LoginData.weuReal);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
    }
    public static void postRequestCurrentTerm(){
        String currentTermJson = UserInfRequest.postInf("http://ehall.ynu.edu.cn/jwapp/sys/studentWdksapApp/modules/wdksap/dqxnxq.do", "", "POST");
        try {
            JSONObject jsonObject = new JSONObject(currentTermJson);
            JSONObject jsonObjectData = jsonObject.getJSONObject("datas");
            JSONObject jsonObjectDqxnxq = jsonObjectData.getJSONObject("dqxnxq");
            JSONArray jsonArray = jsonObjectDqxnxq.getJSONArray("rows");
            JSONObject jsonObjectInf = (JSONObject) jsonArray.get(0);
            UserInformation.currentTerm = jsonObjectInf.getString("DM");
            UserInformation.currentTermChar = jsonObjectInf.getString("MC");

            Log.d("currentTerm", UserInformation.currentTerm);
            Log.d("currentTermChar", UserInformation.currentTermChar);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void requestGradesAll(){
        String gradeJson = postGradesRequest("querySetting=[]&pageSize=1000&pageNumber=1");
//        Log.d("Grades", gradeJson);
        UserInformation.userSemesterList = new ArrayList<>();
        UserInformation.examScoreInfList = new ArrayList<>();
        UserInformation.userSemesterDeList = new ArrayList<>();
        try {
            JSONObject jsonObjectRoot = new JSONObject(gradeJson);
            JSONObject jsonObjectDatas = jsonObjectRoot.getJSONObject("datas");
            JSONObject jsonObjectXscjcx = jsonObjectDatas.getJSONObject("xscjcx");
            JSONArray jsonArrayRows = jsonObjectXscjcx.getJSONArray("rows");

            for (int i = 0; i < jsonArrayRows.length(); i++){
                ExamScoreInf examScoreInf = new ExamScoreInf();
                JSONObject jsonObjectExam = jsonArrayRows.getJSONObject(i);

                examScoreInf.examName = jsonObjectExam.getString("KCM");
                examScoreInf.examScore = jsonObjectExam.getString("ZCJ").split("\\.")[0];
                examScoreInf.examGpa = jsonObjectExam.getString("XFJD");
                examScoreInf.examCredit = jsonObjectExam.getString("XF");
                examScoreInf.examSemeter = jsonObjectExam.getString("XNXQDM_DISPLAY");

                examScoreInf.examAcademy = jsonObjectExam.getString("KKDWDM_DISPLAY");
                examScoreInf.examPass = jsonObjectExam.getString("SFJG_DISPLAY");
                examScoreInf.examValidity = jsonObjectExam.getString("SFYX_DISPLAY");
                examScoreInf.examType = jsonObjectExam.getString("KCLBDM_DISPLAY");

                examScoreInf.examWay = jsonObjectExam.getString("XDFSDM_DISPLAY");
                examScoreInf.examPeriod = jsonObjectExam.getString("XS");
                examScoreInf.examTime = jsonObjectExam.getString("KSSJ");
                examScoreInf.examNatu = jsonObjectExam.getString("KCXZDM_DISPLAY");

                examScoreInf.examUsualPerformance = jsonObjectExam.getString("PSCJ");
                examScoreInf.examMidtermPerformance = jsonObjectExam.getString("QZCJ");
                examScoreInf.examFinalPerformance = jsonObjectExam.getString("QMCJ");
                examScoreInf.examUsualPerformanceRatio = jsonObjectExam.getString("PSCJXS");
                examScoreInf.examMidtermPerformanceRatio = jsonObjectExam.getString("QZCJXS");
                examScoreInf.examFinalPerformanceRatio = jsonObjectExam.getString("QMCJXS");

                boolean isAdd = true;
                for (int j = 0; j < UserInformation.userSemesterList.size(); j++){
                    if (UserInformation.userSemesterList.get(j).equals(examScoreInf.examSemeter)){
                        isAdd = false;
                        break;
                    }
                }
                if (isAdd){
                    UserInformation.userSemesterList.add(examScoreInf.examSemeter);
                }
                UserInformation.examScoreInfList.add(examScoreInf);
            }
            Log.d("Grades size", UserInformation.examScoreInfList.size() + "");
            Log.d("Semester size", UserInformation.userSemesterList.size() + "");
            Collections.reverse(UserInformation.userSemesterList);

            for (int i = 0; i < UserInformation.userSemesterList.size(); i++){
                UserInformation.userSemesterDeList.add(StaticMethod.semesterTran(UserInformation.userSemesterList.get(i)));
            }
            Log.d("semester", UserInformation.userSemesterList.toString());
            Log.d("semester", UserInformation.userSemesterDeList.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String postGradesRequest(String postStr){
        HttpURLConnection connections = null;
        BufferedReader reader = null;
        String gradeJson = "";
        try {
            URL url = new URL(ConstantUrls.GRADES_REQUEST_URL);
            connections = (HttpURLConnection) url.openConnection();
            connections.setRequestMethod("GET");
            connections.setConnectTimeout(8000);
            connections.setReadTimeout(8000);

            connections.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connections.setRequestProperty("Cookie", LoginData.weuReal + "; " + LoginData.modAuthCas);

            connections.setDoOutput(true);
            connections.setDoInput(true);
            connections.setUseCaches(false);
            connections.setInstanceFollowRedirects(false);


//            PrintWriter out = new PrintWriter(new OutputStreamWriter(connections.getOutputStream(), "UTF-8"));
//            out.println(postStr);
//            out.flush();

            InputStream inputStream = connections.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            gradeJson = response.toString();
//            Log.d("gradesJson", gradeJson);

//            out.close();
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
        return gradeJson;
    }
}
