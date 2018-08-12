package com.bai.van.radixe;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.bai.van.radixe.baseclass.BaseActivity;
import com.bai.van.radixe.constantdata.ConstantUrls;
import com.bai.van.radixe.entry.Entry;
import com.bai.van.radixe.urlrequests.GradesRequest;
import com.bai.van.radixe.urlrequests.LoginRequest;
import com.bai.van.radixe.userdata.LoginData;
import com.bai.van.radixe.userdata.UserInformation;

/**
 * @author van
 */

public class StartActivity extends BaseActivity {

    private LottieAnimationView lottieAnimationView;
    private boolean isNetworkConnected;
    private Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }

        handler = new Handler();

        SharedPreferences sp = getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        boolean isAutoLogin = sp.getBoolean(Entry.SharedPreferencesEntry.IS_AUTO_LOGIN, true);
        UserInformation.username = sp.getString(Entry.SharedPreferencesEntry.USER_NAME, "");
        UserInformation.password = sp.getString(Entry.SharedPreferencesEntry.PASS_WORD, "");

        isNetworkConnected = isNetworkConnected(this);

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.startAcivityLottieView);

        if (isAutoLogin && !"".equals(UserInformation.username) && !"".equals(UserInformation.password)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LoginRequest.sendGet(ConstantUrls.REQUEST_LOGIN_CODE_URL);
                    LoginRequest.sendPost(ConstantUrls.LOGIN_ADRESS_URL, UserInformation.username, UserInformation.password,
                            LoginData.jsessionId + "; " + LoginData.route, LoginData.lt);
                    if (LoginData.loginResponseCode == ConstantUrls.LOGIN_SUCESS_CODE){
                        GradesRequest.getWeu();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (LoginData.loginResponseCode == ConstantUrls.LOGIN_SUCESS_CODE || !isNetworkConnected){
                                startActivity(new Intent(StartActivity.this, MainActivity.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }else {
                                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }
                        }
                    });
                }

            }).start();
        }else {
            lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        lottieAnimationView.setProgress(0f);
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lottieAnimationView.destroyDrawingCache();
    }

    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            assert mConnectivityManager != null;
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
