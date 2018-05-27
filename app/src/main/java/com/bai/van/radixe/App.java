package com.bai.van.radixe;

import android.app.Application;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.lang.reflect.Field;

/**
 * @author van
 * @date 2018/5/26
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.setLogEnabled(false);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }
}
