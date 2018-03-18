package com.bai.van.radixe.constantdata;

/**
 *
 * @author van
 * @date 2018/1/13
 */

public class ConstantUrls {
    public final static String REQUEST_LOGIN_CODE_URL = "http://ids.ynu.edu.cn/authserver/login?service=" +
            "http:%2F%2Fehall.ynu.edu.cn%2Flogin?service%3Dhttp:%2F%2Fehall.ynu.edu.cn%2Fnew%2Findex.html";
    public final static String LOGIN_ADRESS_URL = "http://ids.ynu.edu.cn/authserver/login?service=" +
            "http%3A%2F%2Fehall.ynu.edu.cn%2Flogin%3Fservice%3Dhttp%3A%2F%2Fehall.ynu.edu.cn%2Fnew%2Findex.html";

    public final static String GRADES_AUTHOR_REQUEST_URL = "http://ehall.ynu.edu.cn/jwapp/sys/emappagelog/config/cjcx.do";
    public final static String GRADES_REQUEST_URL = "http://ehall.ynu.edu.cn/jwapp/sys/cjcx/modules/cjcx/xscjcx.do";

    public final static String CAPTCHA_URL = "http://ids.ynu.edu.cn/authserver/captcha.html";
    public final static String NEED_CAPTCHA_URL = "http://ids.ynu.edu.cn/authserver/needCaptcha.html?username=";

    public final static String USER_BASEI_NF_URL = "http://ehall.ynu.edu.cn/jwapp/sys/czjl/modules/czjl/cxxsjbxx.do";
    public final static String USER_STATUS_URL = "http://ehall.ynu.edu.cn/jwapp/sys/czjl/modules/czjl/cxxsxjxx.do";
    public final static String USER_ENTRANCE_URL = "http://ehall.ynu.edu.cn/jwapp/sys/czjl/modules/czjl/cxxsrxxx.do";

    public final static String GRADES_INF_URL = "http://ehall.ynu.edu.cn/jwapp/sys/czjl/modules/czjl/cxxscjtj.do";

    public final static String USER_EXAM_SCHEDULED_URL = "http://ehall.ynu.edu.cn/jwapp/sys/studentWdksapApp/modules/wdksap/wdksap.do";
    public final static String USER_EXAM_IN_SCHEDULE_URL = "http://ehall.ynu.edu.cn/jwapp/sys/studentWdksapApp/modules/wdksap/wapks.do";

    public final static String TIME_TABLE_URL = "http://ehall.ynu.edu.cn/jwapp/sys/wdkb/modules/xskcb/xskcb.do";
    public final static String SEMESTER_URL = "http://ehall.ynu.edu.cn/jwapp/sys/wdkb/modules/jshkcb/xnxqcx.do";
    public final static String CURRENT_WEEK_NO_URL = "http://ehall.ynu.edu.cn/jwapp/sys/wdkb/modules/jshkcb/dqzc.do";

    public final static String LOGIN_FAILED_USERNAME_OR_PASSWORD = "用户名或者密码错误";
    public final static String LOGIN_FAILED_VERIFICODE = "验证码错误";
    public final static String LOGIN_NETWORK_ERROR = "当前无网络";

    public final static int LOGIN_SUCESS_CODE = 302;
    public final static int REQUEST_SUCESS_CODE = 200;
}