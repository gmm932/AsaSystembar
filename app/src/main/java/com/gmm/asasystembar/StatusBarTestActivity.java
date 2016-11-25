package com.gmm.asasystembar;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatusBarTestActivity extends AppCompatActivity {
    private LinearLayout view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_status_bar_test);
        view = (LinearLayout) findViewById(R.id.ll_view);

        /*StatusBarUtils.from(this)
                .setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent)).process();*/

       /* StatusBarUtils.from(this)
                .setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setActionbarView(view)
                .setActionbarPadding(true).process();*/
        /*StatusBarUtils.from(this)
                .setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent))
                .process();*/
        StatusBarUtils.from(this)
                .addStatusBarView(true)
                .process();
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatusBarUtils.from(StatusBarTestActivity.this)
                        .addStatusBarView(true)
                        .process();
            }
        });

    }
}
