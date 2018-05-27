package com.bai.van.radixe.urlrequests;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import com.bai.van.radixe.constantdata.ConstantUrls;
import com.bai.van.radixe.LoginActivity;
import com.bai.van.radixe.userdata.LoginData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

/**
 *
 * @author van
 * @date 2018/1/13
 */

public class LoginRequest {
    public static void sendGet(String urls) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String[] str = new String[3];
        try {
            URL url = new URL(urls);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2)" +
                    " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");

            connection.connect();

            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }

            List<String> cookieStrs = connection.getHeaderFields().get("Set-Cookie");
            for (String string : cookieStrs) {
                str[cookieStrs.indexOf(string)] = string.split(";")[0];
            }

            LoginData.jsessionId = str[0];
            LoginData.route = str[1];

            Document doc = Jsoup.parse(response.toString());
            Element form = doc.getElementsByTag("form").get(0);

//            Log.d("lt = ", form.select("input[name=lt]").get(0).val());
//            Log.d("execution = ", form.select("input[name=execution]").get(0).val());
//            Log.d("_eventId = ", form.select("input[name=_eventId]").get(0).val());

            LoginData.lt = form.select("input[name=lt]").get(0).val();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
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
            if (connection != null){
                connection.disconnect();
            }
        }
    }
    public static void getModAuthCas(){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(LoginData.location);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2)" +
                    " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(false);
            connection.connect();

            List<String> cookieStrs = connection.getHeaderFields().get("Set-Cookie");
            LoginData.modAuthCas = cookieStrs.get(0).split(";")[0];
//            Log.d("MOD_", LoginData.modAuthCas);

//            for (String str : connection.getHeaderFields().keySet()) {
//                System.out.println(str + "=" + connection.getHeaderFields().get(str));
//            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch(IOException e) {
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

    public static void sendPost(String urls, String userName, String passWord, String cookie, String lt) {
        HttpURLConnection connections = null;
        try {
            URL url = new URL(urls);
            connections = (HttpURLConnection) url.openConnection();
            connections.setRequestMethod("POST");
            connections.setConnectTimeout(8000);
            connections.setReadTimeout(8000);

//            URLEncoder.encode(userName, "UTF-8")
            String postString = "username=" + userName
                    + "&password=" + passWord
                    + "&lt=" + lt
                    + "&dllt=userNamePasswordLogin&execution=e1s1&_eventId=submit&rmShown=1";

//            Log.d("sendPost", postString);
//            Log.d("loginCookies", cookie);


            connections.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connections.setRequestProperty("Host", "ids.ynu.edu.cn");
            connections.setRequestProperty("Proxy-Connection", "keep-alive");
            //connections.setRequestProperty("Content-Length", String.valueOf(postString.getBytes().length));
            connections.setRequestProperty("Cache-Control", "max-age=0");
            connections.setRequestProperty("Origin", "http://ids.ynu.edu.cn");
            connections.setRequestProperty("Upgrade-Insecure-Requests", "1");
            connections.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            connections.setRequestProperty("Referer", "http://ids.ynu.edu.cn/authserver/login?service=http://ehall.ynu.edu.cn/login?service=http://ehall.ynu.edu.cn/new/index.html");
            connections.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connections.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7");
            connections.setRequestProperty("Cookie", cookie);
            connections.setRequestProperty("User-Agent", "Mozilla/5.0.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");


            connections.setDoOutput(true);
            connections.setDoInput(true);
            connections.setUseCaches(false);
            connections.setInstanceFollowRedirects(false);

            connections.connect();

            OutputStream out = connections.getOutputStream();
            out.write(postString.getBytes());
            out.flush();

            LoginData.location = connections.getHeaderField("Location");
            LoginData.loginDe = Integer.parseInt(connections.getHeaderField("Content-Length"));

//            Log.d("LOGIN Location", LoginData.location);
//
//            Log.d("loginCode", connections.getResponseCode() + "");
            if (connections.getResponseCode() == ConstantUrls.LOGIN_SUCESS_CODE){
                LoginData.loginResponseCode = ConstantUrls.LOGIN_SUCESS_CODE;
                LoginRequest.getModAuthCas();
            }else {
                LoginData.loginResponseCode = ConstantUrls.REQUEST_SUCESS_CODE;
                Log.e("LOPGIN", "failed");
            }
            out.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connections != null){
                connections.disconnect();
            }
        }
    }
    public static void requestCaptcha(){
        HttpURLConnection connections = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(ConstantUrls.CAPTCHA_URL);
            connections = (HttpURLConnection) url.openConnection();
            connections.setRequestMethod("GET");
            connections.setConnectTimeout(8000);
            connections.setReadTimeout(8000);
            connections.setRequestProperty("Cookie", LoginData.route + "; " + LoginData.route);
            connections.setRequestProperty("User-Agent", "Mozilla/5.0.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");

            connections.connect();

            InputStream inputStream = connections.getInputStream();

            LoginData.captcha = BitmapFactory.decodeStream(inputStream);
            LoginActivity.loginHandler.post(new Runnable() {
                @Override
                public void run() {
                    LoginActivity.verifiCodeImage.setImageBitmap(LoginData.captcha);
                    LoginActivity.verifiCodeEdit.setText("");
                    Log.d("LOGIN", "verificode refresh sucess");
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connections != null){
                connections.disconnect();
            }
        }
    }
    public static void needCaptcha(String username){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(ConstantUrls.NEED_CAPTCHA_URL + username + "&_=1515983801045");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");

            connection.connect();

            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }

            LoginData.ifNeedCaptcha = response.toString();
//            Log.d("LOGIN", "needCaptcha? " + LoginData.ifNeedCaptcha);

            LoginActivity.loginHandler.post(new Runnable() {
                @Override
                public void run() {
                    if ("true".equals(LoginData.ifNeedCaptcha)){
                        LoginActivity.verifiCodeLayout.setVisibility(View.VISIBLE);
                    }else {
                        LoginActivity.verifiCodeLayout.setVisibility(View.GONE);
                    }
                }
            });

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
            if (connection != null){
                connection.disconnect();
            }
        }
    }
}
