package com.gmm.asasystembar;

import android.graphics.ImageFormat;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmm.asasystembar.slidedetail.SlideDetailsLayout;
import com.gmm.asasystembar.systembar.AsaSystemBar;

public class SlideViewTintActivity extends AppCompatActivity {

    private SlideDetailsLayout slideDetailsLayout;
    private TextView tvButton;
    private LinearLayout llFoo;
    private boolean inited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_view_tint);

        AsaSystemBar.from(SlideViewTintActivity.this)
                .setTransparentStatusBar(true)
                .setActionbarView(llFoo)
                .setActionbarPadding(true)
                .process();
        slideDetailsLayout = (SlideDetailsLayout) findViewById(R.id.slidedetail);
        tvButton = (TextView) findViewById(R.id.tv_button);
        llFoo = (LinearLayout) findViewById(R.id.ll_foo);

        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideDetailsLayout.smoothOpen(true);
            }
        });
        //切换页面时改变颜色
        slideDetailsLayout.setOnSlideDetailsListener(new SlideDetailsLayout.OnSlideDetailsListener() {
            @Override
            public void onStatusChanged(SlideDetailsLayout.Status status) {
                if (status == SlideDetailsLayout.Status.OPEN) {
                    //给statusbar添加view的方法对固定标题的页面有效
                   /*
                   //6.0以上依旧不做处理
                   AsaSystemBar.from(SlideViewTintActivity.this)
                            .setTransparentStatusBar(true)
                            .setLightStatusBar(true)
                            .process();

                    //6.0以下
                    if (!inited) { //这里有个bug，来回开关一直在给状态栏加View，这里仅仅是举例子，正常情况下不会这么写
                        AsaSystemBar.from(SlideViewTintActivity.this)
                                .setUseBelow(Build.VERSION_CODES.M)  //6.0以下使用
                                .setTransparentStatusBar(true)
                                .addStatusBarView(true)
                                .setStatusBarColor(ContextCompat.getColor(SlideViewTintActivity.this, R.color.alpha))
                                .process();
                        inited = true;
                    }*/

                    //添加view + padding的方式

                    //6.0以上依旧不做处理
                    AsaSystemBar.from(SlideViewTintActivity.this)
                            .setTransparentStatusBar(true)
                            .setLightStatusBar(true)
                            .process();
                    //6.0以下的
                    if (!inited) {
                        AsaSystemBar.from(SlideViewTintActivity.this).setUseBelow(Build.VERSION_CODES.M)
                                .setTransparentStatusBar(true)
                                .addStatusBarView(true)
                                .setLightStatusBar(true)
                                .setStatusBarColor(ContextCompat.getColor(SlideViewTintActivity.this, R.color.alpha))
                                .process();
                        inited = true;
                    }


                }else {
                    //首先要清除之前的设置
                    AsaSystemBar.clearSystemUiVisibility(getWindow());
                    AsaSystemBar.from(SlideViewTintActivity.this).setTransparentStatusBar(true).process();
                }
            }
        });
    }
}
