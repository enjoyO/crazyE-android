package com.bai.van.radixe.baseclass;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;

/**
 *
 * @author van
 * @date 2017/10/11
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
}
