package com.gmm.asasystembar.systembar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by gmm on 2016/11/24.
 */

public class AsaSystemBar {

    private int useBelowVersion;
    private Window window;
    private boolean lightStatusBar = false;
    private boolean transparentStatusBar = false;
    private boolean transparentNavigationbar = false;
    private boolean isSetActionbarPadding = false;
    private boolean addStatusBarView;
    private View actionBarView;
    private int statusBarColor;
    private Drawable statusBarDrawable;

    private AsaSystemBar(int useBelowVersion, Window window, boolean lightStatusBar, boolean transparentStatusBar,
                         boolean transparentNavigationbar, boolean isSetActionbarPadding, View actionBarView,
                         boolean addStatusBarView, int statusBarColor, Drawable statusBarDrawable) {
        this.useBelowVersion = useBelowVersion;
        this.window = window;
        this.lightStatusBar = lightStatusBar;
        this.transparentStatusBar = transparentStatusBar;
        this.transparentNavigationbar = transparentNavigationbar;
        this.isSetActionbarPadding = isSetActionbarPadding;
        this.actionBarView = actionBarView;
        this.addStatusBarView = addStatusBarView;
        this.statusBarColor = statusBarColor;
        this.statusBarDrawable = statusBarDrawable;
    }

    public static boolean isLessKitkat() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT;
    }

    private void process() {
        if (isLessKitkat()) return;
        //单独处理状态栏图标颜色
        processBarIconColor();
        //5.0~6.0和6.0以上唯一的区别是5.0~6.0不能改变状态栏图标的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            processLollipopUp();
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            processKitkat();
        }
    }

    private boolean isUsePrivateApi = false;
    private void processBarIconColor() {
        //单独处理小米，魅族
        processPrivateAPI();
        //处理6.0以上其他机器
        processMUpAPI();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void processMUpAPI() {
        if (isUsePrivateApi) return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        if (lightStatusBar) {
            int flag = window.getDecorView().getSystemUiVisibility();
            flag |= (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(flag);
        }
    }

    /**
     * 处理4.4沉浸
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void processKitkat() {
        //透明状态栏
        WindowManager.LayoutParams winParams = window.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (transparentStatusBar) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);

        if (isSetActionbarPadding) {
            processActionBar(actionBarView);
        }

        if (addStatusBarView && isUsePrivateApi == false) {
            setupStatusBarView(window.getContext(), (ViewGroup) window.getDecorView());
        }
    }

    /**
     * 处理5.0以上的透明状态栏
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void processLollipopUp() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        int flag = window.getDecorView().getSystemUiVisibility();
        if (transparentStatusBar || statusBarColor != -1) {
            flag |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.setStatusBarColor(statusBarColor != -1 ? statusBarColor : Color.TRANSPARENT);
        }
        if (transparentNavigationbar) {
            flag |= (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            window.setNavigationBarColor(statusBarColor != -1 ? statusBarColor : Color.TRANSPARENT);
        }
        if (isSetActionbarPadding) {
            processActionBar(actionBarView);
        }
        window.getDecorView().setSystemUiVisibility(flag);

        if (addStatusBarView && isUsePrivateApi == false) {
            setupStatusBarView(window.getContext(), (ViewGroup) window.getDecorView());
        }
    }

    /**
     * Default status dp = 24 or 25
     * mhdpi = dp * 1
     * hdpi = dp * 1.5
     * xhdpi = dp * 2
     * xxhdpi = dp * 3
     * eg : 1920x1080, xxhdpi, => status/all = 25/640(dp) = 75/1080(px)
     * <p>
     * don't forget toolbar's dp = 48
     *
     * @return px
     */
    @IntRange(from = 0, to = 75)
    public static int getStatusBarOffsetPx(Context context) {
        if (isLessKitkat()) {
            return 0;
        }
        Context appContext = context.getApplicationContext();
        int result = 0;
        int resourceId =
                appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = appContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 调用私有API处理颜色
     */
    private void processPrivateAPI() {
        try {
            processFlyMe(lightStatusBar);
        } catch (Exception e) {
            try {
                processMIUI(lightStatusBar);
            } catch (Exception e2) {
                //可以继续添加其他的Android系统
            }
        }
    }

    /**
     * 改变小米的状态栏字体颜色为黑色, 要求MIUI6以上
     * Tested on: MIUIV7 5.0 Redmi-Note3
     */
    private void processMIUI(boolean lightStatusBar) throws Exception {
        Class<? extends Window> clazz = window.getClass();
        int darkModeFlag;
        Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
        Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
        darkModeFlag = field.getInt(layoutParams);
        Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
        extraFlagField.invoke(window, lightStatusBar ? darkModeFlag : 0, darkModeFlag);
        isUsePrivateApi = true;
    }

    /**
     * 改变魅族的状态栏字体为黑色，要求FlyMe4以上
     */
    private void processFlyMe(boolean isLightStatusBar) throws Exception {
        WindowManager.LayoutParams lp = window.getAttributes();
        Class<?> instance = Class.forName("android.view.WindowManager$LayoutParams");
        int value = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp);
        Field field = instance.getDeclaredField("meizuFlags");
        field.setAccessible(true);
        int origin = field.getInt(lp);
        if (isLightStatusBar) {
            field.set(lp, origin | value);
        } else {
            field.set(lp, (~value) & origin);
        }

        isUsePrivateApi = true;
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
        if (statusBarDrawable != null) {
            mStatusBarTintView.setBackgroundDrawable(statusBarDrawable);
        } else if (statusBarColor != -1) {
            mStatusBarTintView.setBackgroundColor(statusBarColor);
        }


        decorViewGroup.addView(mStatusBarTintView);
    }


    private void processActionBar(final View view) {
        if (view == null) {
            throw new IllegalArgumentException("'actionBarView' cannot be null.");
        }

        view.post(new Runnable() {
            @Override
            public void run() {
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop()
                        + getStatusBarOffsetPx(view.getContext()),
                        view.getPaddingRight(), view.getPaddingBottom());
                view.getLayoutParams().height += getStatusBarOffsetPx(view.getContext());
            }
        });

    }

    /**
     * 这个方法是清空系统状态栏的状态
     * 主要用于一个Activity中，页面切换时重新设置状态栏的样式
     * @param window
     */
    public static void clearSystemUiVisibility(Window window) {
        window.getDecorView().setSystemUiVisibility(0);
    }

    public static Builder from(Activity activity) {
        return new Builder().setWindow(activity);
    }

    public static class Builder {
        private int useBelowVersion = -1;
        private Window window;
        private boolean lightStatusBar = false;
        private boolean transparentStatusBar = false;
        private boolean transparentNavigationbar = false;
        private boolean isSetActionbarPadding = false;
        private boolean addStatusBarView;
        private View actionBarView;
        private int statusBarColor = -1;
        private Drawable statusBarDrawable;

        /**
         * 低于该版本时使用
         * @param useBelowVersion
         * @return
         */
        public Builder setUseBelow(int useBelowVersion) {
            this.useBelowVersion = useBelowVersion;
            return this;
        }

        //需要支持dialog等
        public Builder setWindow(@NonNull Activity activity) {
            this.window = activity.getWindow();
            return this;
        }

        /**
         * 小米魅族等需要单独设置
         * MIUI 6+ FlyMe 4+
         * @TargetApi M
         * @param lightStatusBar
         * @return
         */
        public Builder setLightStatusBar(boolean lightStatusBar) {
            this.lightStatusBar = lightStatusBar;
            return this;
        }

        public Builder setTransparentStatusBar(boolean transparentStatusBar) {
            this.transparentStatusBar = transparentStatusBar;
            return this;
        }

        public Builder setTransparentNavigationbar(boolean transparentNavigationbar) {
            this.transparentNavigationbar = transparentNavigationbar;
            return this;
        }

        /**
         * setActionbarView  before setActionbarPadding(true)
         * @param isSetActionbarPadding
         * @return
         */
        public Builder setActionbarPadding(boolean isSetActionbarPadding) {
            this.isSetActionbarPadding = isSetActionbarPadding;
            return this;
        }

        public Builder setActionbarView(@NonNull View actionBarView) {
            this.actionBarView = actionBarView;
            return this;
        }

        /**
         * setTransparentStatusBar ture before add a bar view
         * @param addStatusBarView
         * @return
         */
        public Builder addStatusBarView(boolean addStatusBarView) {
            this.addStatusBarView = addStatusBarView;
            return this;
        }

        public Builder setStatusBarColor(int statusBarColor) {
            this.statusBarColor = statusBarColor;
            return this;
        }

        public Builder setStatusBarDrawable(Drawable statusBarDrawable) {
            this.statusBarDrawable = statusBarDrawable;
            return this;
        }

        public void process() {
            //如果不在使用范围内直接返回
            if (useBelowVersion != -1) {
                if (useBelowVersion <= Build.VERSION.SDK_INT) return;
            }


            new AsaSystemBar(useBelowVersion, window, lightStatusBar, transparentStatusBar
                    , transparentNavigationbar, isSetActionbarPadding, actionBarView,
                    addStatusBarView, statusBarColor, statusBarDrawable).process();
        }
    }
}
