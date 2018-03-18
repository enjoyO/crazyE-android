package com.bai.van.radixe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bai.van.radixe.baseclass.BaseActivity;

/**
 * @author van
 * @date 2018/2/3
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initial();
    }
    private void initial(){
        RelativeLayout aboutBack = findViewById(R.id.aboutBack);
        LinearLayout githubViewLayout = findViewById(R.id.githubUrlLayout);
        LinearLayout appVersionLayout = findViewById(R.id.appVersionLayout);

        aboutBack.setOnClickListener(this);
        githubViewLayout.setOnClickListener(this);
        appVersionLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.aboutBack:
                finish();
                break;
            case R.id.githubUrlLayout:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.app_github_url))));
                break;
            case R.id.appVersionLayout:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.app_github_release_url))));
                break;
            default:
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
