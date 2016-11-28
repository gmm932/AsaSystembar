package com.gmm.asasystembar;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.gmm.asasystembar.systembar.AsaSystemBar;

public class AsaSystemBarTestActivity extends AppCompatActivity {
    private LinearLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_status_bar_test);
        view = (LinearLayout) findViewById(R.id.ll_view);

        //4.4.4
/*        AsaSystemBar.from(this)
                .setTransparentStatusBar(true)
                .process();*/
        //5.0
        /*AsaSystemBar.from(this)
                .setTransparentStatusBar(true)
                .setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent))
                .process();*/
        //6.0
        AsaSystemBar.from(this)
                .setTransparentStatusBar(true)
                .setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setLightStatusBar(true)
                .process();

//        AsaSystemBar.from(this)
//                .setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent))
//                .setActionbarView(view)
//                .setActionbarPadding(true).process();

       /* AsaSystemBar.from(this)
                .setTransparentStatusBar(true)
                .addStatusBarView(true)
                .setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent))
                //或者在xml中设置fitsSystemWindows属性为处，或者设置marginTop
                .setActionbarView(view)
                .setActionbarPadding(true)

                .process();*/

//        AsaSystemBar.from(this)
//                .setTransparentStatusBar(true)
//                .addStatusBarView(true)
//                .setStatusBarDrawable(getResources().getDrawable(R.mipmap.ic_launcher))
//                .process();

    }
}
