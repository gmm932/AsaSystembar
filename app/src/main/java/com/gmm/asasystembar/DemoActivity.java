package com.gmm.asasystembar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);


        //clear all flag
        findViewById(R.id.clear_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().getDecorView().setSystemUiVisibility(0);
            }
        });

        /**
         * show lightStatusBar require api 23
         * 小米魅族等需要单独设置
         * MIUI 6+ FlyMe 4+
         */
        findViewById(R.id.lightStatusBar).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                Window window = getWindow();
                int flag = window.getDecorView().getSystemUiVisibility();
                flag |= (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.setStatusBarColor(Color.TRANSPARENT);
                window.getDecorView().setSystemUiVisibility(flag);
            }
        });

        //show transparentStatusBar require api 21
        findViewById(R.id.transparentStatusBar).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                int flag = getWindow().getDecorView().getSystemUiVisibility();
                flag |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                getWindow().getDecorView().setSystemUiVisibility(flag);
            }
        });

        //add decorview
        findViewById(R.id.add_decocview).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
               /* AsaSystemBar.from(DemoActivity.this)
                        .addStatusBarView(true)
                        .process();*/
                int flag = getWindow().getDecorView().getSystemUiVisibility();
                flag |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                getWindow().getDecorView().setSystemUiVisibility(flag);
                ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
                setupStatusBarView(DemoActivity.this, viewGroup);
            }
        });
    }
    /**
     * see {@link <a href="https://github.com/jgilfelt/SystemBarTint"></a>}
     */
    private View mStatusBarTintView;
    private void setupStatusBarView(Context context, ViewGroup decorViewGroup) {
        mStatusBarTintView = new View(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarOffsetPx(context));
        params.gravity = Gravity.TOP;
     /*   if (mNavBarAvailable && !mConfig.isNavigationAtBottom()) {
            params.rightMargin = mConfig.getNavigationBarWidth();
        }*/
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(Color.RED);
        decorViewGroup.addView(mStatusBarTintView);
    }

    @IntRange(from = 0, to = 75) public static int getStatusBarOffsetPx(Context context) {
        Context appContext = context.getApplicationContext();
        int result = 0;
        int resourceId =
                appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = appContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
