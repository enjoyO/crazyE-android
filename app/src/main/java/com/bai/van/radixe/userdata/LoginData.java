package com.bai.van.radixe.userdata;

import android.graphics.Bitmap;

/**
 *
 * @author van
 * @date 2018/1/13
 */

public class LoginData {
    public static String lt = "";
    public static String jsessionId = "";
    public static String route = "";


    public static String location = "";
    public static String modAuthCas = "";

    public static String weu = "";
    public static String weuReal = "";

    public static int loginResponseCode;

    public static Bitmap captcha = null;
    public static String ifNeedCaptcha = "";
    public static int loginDe;

    public static void clearValue(){
        lt = "";
        jsessionId = "";
        route = "";


        location = "";
        modAuthCas = "";

        weu = "";
        weuReal = "";

        loginResponseCode = -1;

        captcha = null;
        ifNeedCaptcha = "";
        loginDe = -1;
    }
}
