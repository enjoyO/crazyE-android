package com.bai.van.radixe;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bai.van.radixe.baseclass.BaseActivity;
import java.util.Objects;

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
        LinearLayout feedbackLayout = findViewById(R.id.feedbackLayout);

        aboutBack.setOnClickListener(this);
        githubViewLayout.setOnClickListener(this);
        appVersionLayout.setOnClickListener(this);
        feedbackLayout.setOnClickListener(this);
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
            case R.id.feedbackLayout:
                ((ClipboardManager) Objects.requireNonNull(getSystemService(Context.CLIPBOARD_SERVICE)))
                        .setPrimaryClip(ClipData.newPlainText("QQGROUP", "782011508"));
                Toast.makeText(this, "QQ群号已复制到剪切板", Toast. LENGTH_SHORT).show();
            default:
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
