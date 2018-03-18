package com.bai.van.radixe;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.bai.van.radixe.adapters.ScoreAnalyseRecyclerViewAdapter;
import com.bai.van.radixe.baseclass.BaseActivity;
import com.bai.van.radixe.urlrequests.UserInfRequest;
import com.bai.van.radixe.userdata.UserInformation;

/**
 * @author van
 */
public class ScoreAnalyseActivity extends BaseActivity {

    private Handler handler;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_analyse);
        handler = new Handler();
        initial();
    }
    private void initial(){
        RelativeLayout scoreAnalyseLayout = findViewById(R.id.scoreAnalyseBackLayout);
        recyclerView = findViewById(R.id.scoreAnalyseRecyclerView);
        scoreAnalyseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        MainActivity.mMainThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                UserInfRequest.requestGradesInf();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadRecyclerView();
                    }
                });
            }
        });
    }
    private void loadRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        ScoreAnalyseRecyclerViewAdapter scoreAnalyseRecyclerViewAdapter = new ScoreAnalyseRecyclerViewAdapter(UserInformation.scoreAnalyseInfList);
        recyclerView.setAdapter(scoreAnalyseRecyclerViewAdapter);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
