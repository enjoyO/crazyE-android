package com.bai.van.radixe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bai.van.radixe.baseclass.BaseActivity;
import com.bai.van.radixe.constantdata.ConstantUrls;
import com.bai.van.radixe.entry.Entry;
import com.bai.van.radixe.urlrequests.GradesRequest;
import com.bai.van.radixe.urlrequests.LoginRequest;
import com.bai.van.radixe.userdata.LoginData;
import com.bai.van.radixe.userdata.UserInformation;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.nfc.tech.MifareClassic.BLOCK_SIZE;

/**
 * @author van
 * @date 2018/1/13
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener{

    private AVLoadingIndicatorView loginAvi;
    private EditText userNameEdit, passWordEdit;
    private Button loginButton;
    private TextView gottrouble_text;

    public static EditText  verifiCodeEdit;
    private TextView loginFailedText;
    public static RelativeLayout verifiCodeLayout;
    public static ImageView verifiCodeImage;

    public static Handler loginHandler;

    private String username, password, verificode;

    private ThreadPoolExecutor mLoginThreadPoolExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initialPlatte();
        loginHandler = new Handler();

        mLoginThreadPoolExecutor = new ThreadPoolExecutor(2, 4, 2,
                TimeUnit. MILLISECONDS, new ArrayBlockingQueue<Runnable>(BLOCK_SIZE),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        mLoginThreadPoolExecutor.allowCoreThreadTimeOut(true);
        mLoginThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                LoginRequest.sendGet(ConstantUrls.REQUEST_LOGIN_CODE_URL);
                LoginRequest.requestCaptcha();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initialPlatte(){
        loginAvi = (AVLoadingIndicatorView) findViewById(R.id.loginAviloading);
        verifiCodeLayout = (RelativeLayout) findViewById(R.id.verifiCodeLayout);

        loginFailedText = (TextView) findViewById(R.id.loginFailedText);
        loginButton = (Button) findViewById(R.id.loginButton);
        verifiCodeImage = (ImageView) findViewById(R.id.verifiCodeImage);

        userNameEdit = (EditText) findViewById(R.id.userNameEdit);
        passWordEdit = (EditText) findViewById(R.id.passWordEdit);
        verifiCodeEdit = (EditText) findViewById(R.id.verifiCodeEdit);

        gottrouble_text = (TextView) findViewById(R.id.gottrouble_login);

        userNameEdit.setOnFocusChangeListener(this);
        passWordEdit.setOnFocusChangeListener(this);
        verifiCodeEdit.setOnFocusChangeListener(this);

        loginButton.setOnClickListener(this);
        verifiCodeImage.setOnClickListener(this);
        gottrouble_text.setOnClickListener(this);

        readLoginData();
        userNameEdit.setText(username);
        passWordEdit.setText(password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginButton:
                if (isNetworkConnected(this)){
                    loginFailedText.setText("");
                    loginAvi.show();
                    loginButton.setClickable(false);
                    loginStep();
                }else {
                    loginFailedText.setText(ConstantUrls.LOGIN_NETWORK_ERROR);
                }
                break;
            case R.id.verifiCodeImage:
                mLoginThreadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        LoginRequest.requestCaptcha();
                    }
                });
                break;
            case R.id.gottrouble_login:
                startActivity(new Intent(this, AboutActivity.class));
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            default:
        }
    }
    private void loginStep(){
        boolean gon = true;
        username = userNameEdit.getText().toString();
        password = passWordEdit.getText().toString();

        if (username.length() < 5 || "".equals(password)){
            gon = false;
            Log.d("LOGIN", "username OR password illegal");
            loginAvi.setVisibility(View.INVISIBLE);
        }
        if (verifiCodeLayout.getVisibility() == View.VISIBLE){
            verificode = verifiCodeEdit.getText().toString();
            if (verificode.length() < 3){
                gon = false;
                Log.d("LOGIN", "verificode illegal");
                loginAvi.setVisibility(View.INVISIBLE);
            }
        }

        if (gon){
            mLoginThreadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    String captchas = "";
                    if (verifiCodeLayout.getVisibility() == View.VISIBLE){
                        captchas = "&captchaResponse=" + verificode;
                        Log.d("LOGIN", "log with verificode");
                    }
                    if ("".equals(LoginData.lt)){
                        LoginRequest.sendGet(ConstantUrls.REQUEST_LOGIN_CODE_URL);
                    }
                    LoginRequest.sendPost(ConstantUrls.LOGIN_ADRESS_URL, username, password + captchas,
                            LoginData.jsessionId + "; " + LoginData.route, LoginData.lt);
                    if (LoginData.loginResponseCode == ConstantUrls.LOGIN_SUCESS_CODE){
                        saveLoginData(username, password);
                        GradesRequest.getWeu();
                        UserInformation.username = username;
                        UserInformation.password = password;
                        loginHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                finish();
                            }
                        });
                    }else {
                        LoginRequest.requestCaptcha();
                        loginHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                loginAvi.setVisibility(View.INVISIBLE);
                                if ("true".equals(LoginData.ifNeedCaptcha)){
                                    if (LoginData.loginDe < 5840){
                                        loginFailedText.setText(ConstantUrls.LOGIN_FAILED_VERIFICODE);
                                    }else if (LoginData.loginDe > 5840){
                                        loginFailedText.setText(ConstantUrls.LOGIN_FAILED_USERNAME_OR_PASSWORD);
                                    }
                                }else {
                                    loginFailedText.setText(ConstantUrls.LOGIN_FAILED_USERNAME_OR_PASSWORD);
                                }
                                loginAvi.hide();
                                loginButton.setClickable(true);
                            }
                        });
                        LoginRequest.needCaptcha(username);
                    }
                }
            });
        }else {
            loginAvi.hide();
            loginButton.setClickable(true);
            loginFailedText.setText("输入用户名和密码");
        }
    }
    private void saveLoginData(String username, String password) {
        SharedPreferences sp = getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Entry.SharedPreferencesEntry.USER_NAME, username);
        editor.putString(Entry.SharedPreferencesEntry.PASS_WORD, password);
        editor.apply();
        Log.d("Logon sharedPre", "信息已写入SharedPreference中");
    }
    private void readLoginData() {
        SharedPreferences sp = getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        username = sp.getString(Entry.SharedPreferencesEntry.USER_NAME, "");
        password = sp.getString(Entry.SharedPreferencesEntry.PASS_WORD, "");
        if (!"".equals(username)){
            userNameEdit.setText(username);
        }
        if (!"".equals(password)){
            passWordEdit.setText(password);
        }
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
    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            case R.id.userNameEdit:
                username = userNameEdit.getText().toString();
                if (username.length() > 10){
                    mLoginThreadPoolExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            LoginRequest.needCaptcha(username);
                        }
                    });
                }
                break;
            case R.id.passWordEdit:

                break;
            case R.id.verifiCodeEdit:

                break;
            default:
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
