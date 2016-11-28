package com.gmm.asasystembar;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gmm.asasystembar.systembar.AsaSystemBar;

public class SimpleTintBarActivity extends AppCompatActivity {

    TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_tint_bar);

        //使用这种方式需要在跟布局设置 fitsSystemWindow true
        AsaSystemBar.from(this).setUseBelow(Build.VERSION_CODES.LOLLIPOP)
                .setTransparentStatusBar(true)
                .addStatusBarView(true)
                .setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .process();

        tvTitle = (TextView) findViewById(R.id.tv_title);
        AsaSystemBar.from(this).setUseBelow(Build.VERSION_CODES.LOLLIPOP)
                .setTransparentStatusBar(true)
                .setActionbarView(tvTitle)
                .setActionbarPadding(true)
                .process();
    }
}
