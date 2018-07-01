package com.bai.van.radixe;

import android.app.Application;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;


/**
 * @author van
 * @date 2018/5/26
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.setLogEnabled(false);
        UMConfigure.setEncryptEnabled(true);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                Log.d("device token", deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }
}
