package com.bai.van.radixe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;

import com.bai.van.radixe.baseclass.BaseActivity;
import com.bai.van.radixe.baseclass.BottomNavigationViewHelper;
import com.bai.van.radixe.datastru.UserInf;
import com.bai.van.radixe.fragment.ExamScoreFragment;
import com.bai.van.radixe.fragment.ExamScoreRecyclerViewFragment;
import com.bai.van.radixe.fragment.MessageFragment;
import com.bai.van.radixe.fragment.OtherFragment;
import com.bai.van.radixe.fragment.TimeTableFragment;
import com.bai.van.radixe.urlrequests.GradesRequest;
import com.bai.van.radixe.userdata.LoginData;
import com.bai.van.radixe.userdata.UserInformation;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.inapp.IUmengInAppMsgCloseCallback;
import com.umeng.message.inapp.InAppMessageManager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.nfc.tech.MifareClassic.BLOCK_SIZE;

/**
 * @author van
 * @date 2018/1/13
 */

public class MainActivity extends BaseActivity implements
        MessageFragment.OnFragmentInteractionListener,
        ExamScoreRecyclerViewFragment.OnFragmentInteractionListener,
        ExamScoreFragment.OnFragmentInteractionListener,
        TimeTableFragment.OnFragmentInteractionListener,
        OtherFragment.OnFragmentInteractionListener{

    public static ThreadPoolExecutor mMainThreadPoolExecutor;

    public static Handler mMainHandler;
    public static MainActivity mainActivity;

    private Fragment currentFragment, examScoreFragment, messageFragment, timeTableFragement, otherFragement;
    FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (currentFragment != messageFragment){
                        switchFragment(messageFragment).commit();
                    }
                    return true;
                case R.id.navigation_timetable:
                    if (currentFragment != timeTableFragement){
                        switchFragment(timeTableFragement).commit();
                    }
                    return true;
                case R.id.navigation_dashboard:
                    if (currentFragment != examScoreFragment) {
                        switchFragment(examScoreFragment).commit();
                    }
                    return true;
                case R.id.navigation_notifications:
                    if (currentFragment != otherFragement) {
                        switchFragment(otherFragement).commit();
                    }
                    return true;
                default:
            }
            return false;
        }
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        mainActivity = this;

        mMainThreadPoolExecutor = new ThreadPoolExecutor(2, 4, 2,
                TimeUnit. MILLISECONDS, new ArrayBlockingQueue<Runnable>(BLOCK_SIZE),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        mMainThreadPoolExecutor.allowCoreThreadTimeOut(true);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(navigation.getMenu().getItem(1).getItemId());

        getWeu();
        mMainHandler = new Handler();

        mMainThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if ("".equals(UserInformation.currentTerm)){
                    GradesRequest.postRequestCurrentTerm();
                }
                GradesRequest.requestGradesAll();
            }
        });
        timeTableFragement = new TimeTableFragment();
        messageFragment = new MessageFragment();

        switchFragment(timeTableFragement).commit();
        currentFragment = timeTableFragement;

        examScoreFragment = new ExamScoreFragment();
        otherFragement = new OtherFragment();

        MobclickAgent.onProfileSignIn(UserInformation.username);


        InAppMessageManager.getInstance(this).setInAppMsgDebugMode(true);
        InAppMessageManager.getInstance(this).showCardMessage(this, "main",
                new IUmengInAppMsgCloseCallback() {
                    //插屏消息关闭时，会回调该方法
                    @Override
                    public void onColse() {
                        Log.i("UMENG", "card message close");
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void getWeu(){
        if ("".equals(LoginData.weu)){
            mMainThreadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    GradesRequest.getWeu();
                }
            });
        }else if ("".equals(LoginData.weuReal)){
            mMainThreadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    GradesRequest.getWeuReal();
                }
            });
        }
    }
    private FragmentTransaction switchFragment(Fragment targetFragment) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction().setTransition(FragmentTransaction.TRANSIT_NONE);
        if (!targetFragment.isAdded()) {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.mainFrameLayout, targetFragment, targetFragment.getClass().getName());

        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
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
    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
