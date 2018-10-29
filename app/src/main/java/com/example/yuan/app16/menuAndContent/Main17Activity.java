package com.example.yuan.app16.menuAndContent;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.example.yuan.app16.R;

//public class Main17Activity extends AppCompatActivity implements View.OnTouchListener {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main17);
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        return false;
//    }
//}

//疑问:content默认显示?menu默认隐藏?
public class Main17Activity extends Activity implements View.OnTouchListener {
    private static final String TAG = "Main17Activity";
    private LinearLayout menuLayout;//菜单项
    private LinearLayout contentLayout;//内容项
    private LinearLayout menuTopLayout;
    private LayoutParams menuParams;//菜单项目的参数
    private LayoutParams contentParams;//内容项目的参数contentLayout的宽度值
    private LayoutParams menuTopParams;

    private int disPlayWidth;//手机屏幕分辨率
    private int disPlayHeight;//手机屏幕分辨率
    private float xDown;//手指点下去的横坐标
    private float xMove;//手指移动的横坐标
    private float xUp;//记录手指上抬后的横坐标
    private float yDown;
    private float yMove;
    private float yUp;

    private VelocityTracker mVelocityTracker; // 用于计算手指滑动的速度。
    float velocityX;//手指左右移动的速度
    float velocityY;
    public static final int SNAP_VELOCITY = 400; //滚动显示和隐藏menu时，手指滑动需要达到的速度。

    private boolean menuIsShow = false;//初始化菜单项不可翙
    private boolean menuTopIsShow = false;//初始化菜单项不可翙
    private static final int menuPadding = 400;//menu完成显示，留给content的宽度

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main17);
        initLayoutParams();

    }

    /**
     * 初始化Layout并设置其相应的参数
     */
    private void initLayoutParams() {
        //得到屏幕的大小
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        disPlayWidth = dm.widthPixels;
        disPlayHeight = dm.heightPixels;

        //获得控件
        menuLayout = (LinearLayout) findViewById(R.id.menu);
        contentLayout = (LinearLayout) findViewById(R.id.content);
        menuTopLayout = (LinearLayout) findViewById(R.id.topMenu);
        findViewById(R.id.activity_main17).setOnTouchListener(this);

        //获得控件参数
        menuParams = (LinearLayout.LayoutParams) menuLayout.getLayoutParams();
        contentParams = (LinearLayout.LayoutParams) contentLayout.getLayoutParams();
        menuTopParams = (LinearLayout.LayoutParams) menuTopLayout.getLayoutParams();

        //初始化菜单和内容的宽和边距
        menuParams.width = disPlayWidth - menuPadding;
        menuParams.leftMargin = -menuParams.width;
        contentParams.width = disPlayWidth;
        contentParams.leftMargin = 0;
        menuTopParams.height = disPlayHeight / 3;
        menuTopParams.topMargin = -menuTopParams.height;//初始化隐藏

        //设置参数
        menuLayout.setLayoutParams(menuParams);
        contentLayout.setLayoutParams(contentParams);
        menuTopLayout.setLayoutParams(menuTopParams);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        acquireVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                yMove = event.getRawY();
                isScrollToShowMenu();
                isScrollToShowMenuTop();//滑动过程的动画效果
                break;

            case MotionEvent.ACTION_UP:
                xUp = event.getRawX();
                yUp = event.getRawY();
                isShowMenu();
                isShowMenuTop();
                releaseVelocityTracker();
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "onTouch: ACTION_CANCEL");
                releaseVelocityTracker();
                break;
        }
        return true;
    }

    /**
     * 根据手指按下的距离，判断是否滚动显示菜单
     */
    private void isScrollToShowMenu() {
        int distanceX = (int) (xMove - xDown);
        if (!menuIsShow) {
            scrollToShowMenu(distanceX);
        } else {
            scrollToHideMenu(distanceX);
        }
    }

    private void isScrollToShowMenuTop() {
        int distanceY = (int) (yMove - yDown);
        if (!menuTopIsShow) {
            scrollToShowMenuTop(distanceY);
        } else {
            scrollToHideMenuTop(distanceY);
        }
    }

    /**
     * 手指抬起之后判断是否要显示菜单
     */
    private void isShowMenu() {
        velocityX = getScrollVelocity();
        if (wantToShowMenu()) {
            if (shouldShowMenu()) {
                showMenu();
            } else {
                hideMenu();
            }
        } else if (wantToHideMenu()) {
            if (shouldHideMenu()) {
                hideMenu();
            } else {
                showMenu();
            }
        }
    }

    private void isShowMenuTop() {
        velocityY = getScrollVelocity_Y();
        Log.d(TAG, "isShowMenuTop: =========");
        if (!menuTopIsShow && yUp - yDown > 0) {
            if (yUp - yDown > menuTopParams.height / 2 && velocityY > SNAP_VELOCITY) {
                Log.d(TAG, "isShowMenuTop: 2");
                showMenuTop();//
            } else {
                Log.d(TAG, "isShowMenuTop: 2?");
                hideMenuTop();
            }
        } else if (menuTopIsShow && yUp - yDown < 0) {
            if (yDown - yUp > menuTopParams.height / 2 && velocityY > SNAP_VELOCITY) {
                Log.d(TAG, "isShowMenuTop: 3");
                hideMenuTop();
            } else {
                Log.d(TAG, "isShowMenuTop: 2?2");
                showMenuTop();
            }
        }
    }

    /**
     * 想要显示菜单,当向右移动距离大于0并且菜单不可见
     */
    private boolean wantToShowMenu() {
        return !menuIsShow && xUp - xDown > 0;
    }

    /**
     * 想要隐藏菜单,当向左移动距离大于0并且菜单可见
     */
    private boolean wantToHideMenu() {
        return menuIsShow && xDown - xUp > 0;
    }

    /**
     * 判断应该显示菜单,当向右移动的距离超过菜单的一半或者速度超过给定值
     */
    private boolean shouldShowMenu() {
        return xUp - xDown > menuParams.width / 2 || velocityX > SNAP_VELOCITY;
    }

    /**
     * 判断应该隐藏菜单,当向左移动的距离超过菜单的一半或者速度超过给定值
     */
    private boolean shouldHideMenu() {
        return xDown - xUp > menuParams.width / 2 || velocityX > SNAP_VELOCITY;
    }

    /**
     * 显示菜单栏
     */
    private void showMenu() {
        new showMenuAsyncTask().execute(150);
        menuIsShow = true;
    }

    private void showMenuTop() {
        Log.d(TAG, "showMenuTop: 4");
        menuTopParams.topMargin = 0;
        menuTopLayout.setLayoutParams(menuTopParams);
//        new showMenuTopAsyncTask().execute(50);
        menuTopIsShow = true;
    }

    /**
     * 隐藏菜单栏
     */
    private void hideMenu() {
        new showMenuAsyncTask().execute(-150);
        menuIsShow = false;
    }

    private void hideMenuTop() {
        menuTopParams.topMargin = -menuTopParams.height;
        menuTopLayout.setLayoutParams(menuTopParams);
//        new showMenuTopAsyncTask().execute(-50);
        menuTopIsShow = false;
    }

    /**
     * 指针按着时，滚动将菜单慢慢显示出来
     *
     * @param scrollX 每次滚动移动的距离
     */
    private void scrollToShowMenu(int scrollX) {
        if (scrollX > 0 && scrollX <= menuParams.width)
            menuParams.leftMargin = -menuParams.width + scrollX;
        menuLayout.setLayoutParams(menuParams);
    }

    private void scrollToShowMenuTop(int scrollY) {
        if (scrollY > 0 && scrollY <= menuTopParams.height)
            menuTopParams.topMargin = -menuTopParams.height + scrollY;//???
        menuTopLayout.setLayoutParams(menuTopParams);
    }

    /**
     * 指针按着时，滚动将菜单慢慢隐藏出来
     *
     * @param scrollX 每次滚动移动的距离
     */
    private void scrollToHideMenu(int scrollX) {
        if (scrollX >= -menuParams.width && scrollX < 0)
            menuParams.leftMargin = scrollX;
        menuLayout.setLayoutParams(menuParams);
    }

    private void scrollToHideMenuTop(int scrollY) {
        if (scrollY >= -menuTopParams.height && scrollY < 0)
            menuTopParams.topMargin = scrollY;
        menuTopLayout.setLayoutParams(menuTopParams);
    }


    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event 向VelocityTracker添加MotionEvent
     */
    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    private int getScrollVelocity_Y() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getYVelocity();
        return Math.abs(velocity);
    }

    /**
     * 释放VelocityTracker
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * ：模拟动画过程，让肉眼能看到滚动的效果
     */
    class showMenuAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            int leftMargin = menuParams.leftMargin;
            while (true) {// 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
                leftMargin += params[0];
                if (params[0] > 0 && leftMargin > 0) {
                    leftMargin = 0;
                    break;
                } else if (params[0] < 0 && leftMargin < -menuParams.width) {
                    leftMargin = -menuParams.width;
                    break;
                }
                publishProgress(leftMargin);
                try {
                    Thread.sleep(40);//休眠一下，肉眼才能看到滚动效果
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            menuParams.leftMargin = value[0];
            menuLayout.setLayoutParams(menuParams);
        }

        @Override
        protected void onPostExecute(Integer result) {
            menuParams.leftMargin = result;
            menuLayout.setLayoutParams(menuParams);
        }

    }

    class showMenuTopAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            int topMargin = menuParams.leftMargin;
            while (true) {// 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
                topMargin += params[0];
                if (params[0] > 0 && topMargin > 0) {
                    topMargin = 0;
                    break;
                } else if (params[0] < 0 && topMargin < -menuTopParams.height) {
                    topMargin = -menuTopParams.height;
                    break;
                }
                publishProgress(topMargin);
                try {
                    Thread.sleep(40);//休眠一下，肉眼才能看到滚动效果
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return topMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            menuTopParams.topMargin = value[0];
            menuTopLayout.setLayoutParams(menuTopParams);
        }

        @Override
        protected void onPostExecute(Integer result) {
            menuTopParams.topMargin = result;
            menuTopLayout.setLayoutParams(menuTopParams);
        }

    }
}