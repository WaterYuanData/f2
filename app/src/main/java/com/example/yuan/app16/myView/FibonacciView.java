package com.example.yuan.app16.myView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.yuan.app16.R;

// https://www.jianshu.com/p/c84693096e41
public class FibonacciView extends View {
    private static final String TAG = "FibonacciView";
    private int defalutSize;
    private Paint mPaint;
    private static final double RATIO = 0.618;
    private float STROKE_WIDTH = 3;
    private float mRectX1;
    private float mCenterX;
    private float mRadius;
    private float mRectX2;
    private float mRectY2;
    private float mRectY1;
    private float mCenterY;
    private int mStartAngle;
    private float mSweepAngle;
    private boolean mUseCenter;
    private int mFlag;

    public FibonacciView(Context context) {
        super(context);
        init();
    }

    public FibonacciView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 在我们的自定义的View里面把我们自定义的属性的值取出来
        // 第二个参数就是我们在styles.xml文件中的<declare-styleable>标签
        // 即属性集合的标签，在R文件中名称为R.styleable+name
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FibonacciView);
        // 第一个参数为属性集合里面的属性，R文件名称：R.styleable+属性集合名称+下划线+属性名称
        // 第二个参数为，如果没有设置这个属性，则设置的默认的值
        defalutSize = a.getDimensionPixelSize(R.styleable.FibonacciView_default_size, 4);
        // 最后记得将TypedArray对象回收
        a.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        mPaint.setStyle(Paint.Style.STROKE);//空心

        mStartAngle = 0;
        mSweepAngle = 90;
        mUseCenter = false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制黄金螺旋线,即斐波那契螺旋线
        // drawSpiral(canvas);

        drawLeftSpiral(canvas);

        drawWithPoint(canvas, 10);
    }

    private void drawSpiral(Canvas canvas) {
        int measuredWidth = getMeasuredWidth();
        int width = getWidth();
        int height = getHeight();
        // 得到的left top包含父布局的padding
        // 得到的right bottom包含自己的margin
        int left = getLeft();
        int top = getTop();
        int right = getRight();
        int bottom = getBottom();
        Log.d(TAG, "onDraw: measuredWidth=" + measuredWidth);
        Log.d(TAG, "onDraw: width=" + width);
        Log.d(TAG, "onDraw: height=" + height);
        Log.d(TAG, "onDraw: left=" + left);
        Log.d(TAG, "onDraw: top=" + top);
        Log.d(TAG, "onDraw: right=" + right);
        Log.d(TAG, "onDraw: bottom=" + bottom);


        // 判断是否需要交换宽高
        // if (width > height) {
        //     int temp = width;
        //     width = height;
        //     height = temp;
        // }

        // 调整边界坐标
        // double offset = (height - width * (1 + RATIO)) / 2;
        // if (offset > 0) {
        //     Log.e(TAG, "drawSpiral: 需要调整边界纵坐标y");
        // } else {
        //     Log.e(TAG, "drawSpiral: 需要调整边界横坐标x");
        //     offset = (width - height / (1 + RATIO)) / 2;
        //     left += offset;
        //     right -= offset;
        //     width = right - left;
        //     Log.d(TAG, "drawSpiral: offset=" + offset);
        //     Log.d(TAG, "onDraw: left=" + left);
        //     Log.d(TAG, "onDraw: right=" + right);
        //     Log.d(TAG, "onDraw: width=" + width);
        // }

        // 注意:canvas是以FibonacciView的左上为原点而非父布局
        // canvas.translate(STROKE_WIDTH, STROKE_WIDTH);
        // canvas.drawCircle(0, 0, 20, mPaint);
        // canvas.drawArc(-200, -200, 200, 200, 0, 90, useCenter, mPaint);
        // canvas.translate(-STROKE_WIDTH, -STROKE_WIDTH);
        // 误区:right = left + width, width以FibonacciView为原点,right以屏幕为原点
        // canvas.drawArc(width- 200 - STROKE_WIDTH , -200 + STROKE_WIDTH, width + 200 - STROKE_WIDTH, 200 + STROKE_WIDTH, 90, 90, useCenter, mPaint);

        // FibonacciView边界坐标(考虑线宽) 误区:width以FibonacciView为原点,right以屏幕为原点
        float x1 = 0 + STROKE_WIDTH / 2;
        float x2 = width - STROKE_WIDTH / 2;
        float y1 = 0 + STROKE_WIDTH / 2;
        float y2 = height - STROKE_WIDTH / 2;

        /*
         * 扇形所在圆的外接正方形的边界坐标
         * STROKE_WIDTH / 2 : 左边界
         * width - STROKE_WIDTH / 2 : 横轴中心
         * centerX - rectX1 : 半径
         * centerX + radius : 右边界
         * height - STROKE_WIDTH / 2 : 下边界
         * rectY2 - radius * 2 : 上边界
         * */
        mStartAngle += mSweepAngle;
        mRectX1 = STROKE_WIDTH / 2;
        mCenterX = width - STROKE_WIDTH / 2;
        mRadius = mCenterX - mRectX1;
        mRectX2 = mCenterX + mRadius;
        mRectY2 = height - STROKE_WIDTH / 2;
        mRectY1 = mRectY2 - mRadius * 2;
        mCenterY = (mRectY2 + mRectY1) / 2;
        canvas.drawArc(mRectX1, mRectY1, mRectX2, mRectY2, mStartAngle, mSweepAngle, mUseCenter, mPaint);
        Log.e(TAG, "drawSpiral: " + ((mRectX2 - mRectX1 == mRectX2 - mRectX1) ? " 是正方形 " : " 非正方形 "));
        Log.d(TAG, "drawSpiral: radius=" + mRadius);
        Log.d(TAG, "drawSpiral: centerX=" + mCenterX);
        Log.d(TAG, "drawSpiral: centerY=" + mCenterY);

        // ==========================
        // startAngle += sweepAngle;
        // radius *= RATIO;
        // centerX *= RATIO;
        // rectX1 = centerX - radius;
        // rectX2 = centerX + radius;
        // rectY1 = centerY - radius;
        // rectY2 = centerY + radius;
        // canvas.drawArc(rectX1, rectY1, rectX2, rectY2, startAngle, sweepAngle, useCenter, mPaint);
        // Log.e(TAG, "drawSpiral: " + ((rectX2 - rectX1 == rectX2 - rectX1) ? " 是正方形 " : " 非正方形 "));
        // Log.d(TAG, "drawSpiral: radius=" + radius);
        // Log.d(TAG, "drawSpiral: centerX=" + centerX);
        // Log.d(TAG, "drawSpiral: centerY=" + centerY);

        // ==========================
        mStartAngle += mSweepAngle;
        mRectX1 = STROKE_WIDTH / 2;
        float lastRadius = mRadius;
        mRadius = height - mRadius - STROKE_WIDTH;
        mCenterX = mRectX1 + mRadius;
        mRectX2 = mCenterX + mRadius;
        mRectY1 = mCenterY - mRadius;
        mRectY2 = mCenterY + mRadius;
        canvas.drawArc(mRectX1, mRectY1, mRectX2, mRectY2, mStartAngle, mSweepAngle, mUseCenter, mPaint);
        Log.e(TAG, "drawSpiral: " + ((mRectX2 - mRectX1 == mRectX2 - mRectX1) ? " 是正方形 " : " 非正方形 "));
        Log.d(TAG, "drawSpiral: radius=" + mRadius);
        Log.d(TAG, "drawSpiral: centerX=" + mCenterX);
        Log.d(TAG, "drawSpiral: centerY=" + mCenterY);

        // ==========================
        mStartAngle += mSweepAngle;
        lastRadius = mRadius;
        mRadius = width - mRadius - STROKE_WIDTH;
        mRectY1 = STROKE_WIDTH / 2;
        mCenterY = mRectY1 + mRadius;
        mRectY2 = mCenterY + mRadius;
        mRectX1 = mCenterX - mRadius;
        mRectX2 = mCenterX + mRadius;
        canvas.drawArc(mRectX1, mRectY1, mRectX2, mRectY2, mStartAngle, mSweepAngle, mUseCenter, mPaint);
        Log.e(TAG, "drawSpiral: " + ((mRectX2 - mRectX1 == mRectX2 - mRectX1) ? " 是正方形 " : " 非正方形 "));
        Log.d(TAG, "drawSpiral: radius=" + mRadius);
        Log.d(TAG, "drawSpiral: centerX=" + mCenterX);
        Log.d(TAG, "drawSpiral: centerY=" + mCenterY);

        // ==========================
        // startAngle += sweepAngle;
        // float temp = lastRadius - radius;
        // lastRadius = radius;
        // radius = temp - STROKE_WIDTH;
        // rectX2 = width - STROKE_WIDTH / 2;
        // centerX = rectX2 - radius;
        // rectX1 = centerX - radius;
        // rectY1 = centerY - radius;
        // rectY2 = centerY + radius;
        // canvas.drawArc(rectX1, rectY1, rectX2, rectY2, startAngle, sweepAngle, useCenter, mPaint);
        // Log.e(TAG, "drawSpiral: " + ((rectX2 - rectX1 == rectX2 - rectX1) ? " 是正方形 " : " 非正方形 "));
        // Log.d(TAG, "drawSpiral: radius=" + radius);
        // Log.d(TAG, "drawSpiral: centerX=" + centerX);
        // Log.d(TAG, "drawSpiral: centerY=" + centerY);

        // ==========================
        // mStartAngle += mSweepAngle;
        // float temp = lastRadius - mRadius;
        // if (temp - STROKE_WIDTH <= mRadius / 2) {
        //     Log.d(TAG, "drawSpiral: 特殊/////////");
        //     lastRadius = mRadius;
        //     mRadius *= RATIO;
        // }
        // mRectX2 = width - STROKE_WIDTH / 2;
        // mCenterX = mRectX2 - mRadius;
        // mRectX1 = mCenterX - mRadius;
        // mRectY1 = mCenterY - mRadius;
        // mRectY2 = mCenterY + mRadius;
        // canvas.drawArc(mRectX1, mRectY1, mRectX2, mRectY2, mStartAngle, mSweepAngle, mUseCenter, mPaint);
        // Log.e(TAG, "drawSpiral: " + ((mRectX2 - mRectX1 == mRectX2 - mRectX1) ? " 是正方形 " : " 非正方形 "));
        // Log.d(TAG, "drawSpiral: radius=" + mRadius);
        // Log.d(TAG, "drawSpiral: centerX=" + mCenterX);
        // Log.d(TAG, "drawSpiral: centerY=" + mCenterY);


    }

    private void drawLeftSpiral(Canvas canvas) {
        mPaint.setColor(Color.BLUE);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredWidth > measuredHeight) {
            int temp = measuredHeight;
            measuredHeight = measuredWidth;
            measuredWidth = temp;
        }
        float temp = (float) (measuredHeight - (measuredWidth * (1 + RATIO)));
        if (temp >= 0) {
            // 上下没超过 左右占满 调整上下 todo
            mRadius = measuredWidth;
            mCenterX = measuredWidth - STROKE_WIDTH / 2;
            mCenterY = measuredHeight - STROKE_WIDTH / 2 - mRadius;
        } else {
            // 上下超过了 通过缩小半径使上下占满 调整左右
            mRadius = (float) (measuredHeight / (1 + RATIO));
            float offSet = measuredWidth - mRadius;
            mCenterX = measuredWidth - offSet / 2 - STROKE_WIDTH / 2;
            mCenterY = measuredHeight - STROKE_WIDTH / 2 - mRadius;
            Log.e(TAG, "drawLeftSpiral: 上下超过 offSet=" + offSet);
        }
        Log.d(TAG, "drawLeftSpiral: measuredWidth=" + measuredWidth);
        Log.d(TAG, "drawLeftSpiral: measuredHeight=" + measuredHeight);
        mStartAngle = 0;
        mFlag = 0;
        for (int j = 0; j < 10; j++) {
            doSpiral(canvas);
        }
    }

    private void doSpiral(Canvas canvas) {
        mStartAngle += mSweepAngle;
        mStartAngle %= 360;
        Log.e(TAG, "drawS: mStartAngle=" + mStartAngle);
        switch (mStartAngle) {
            case 0:
                // mRectX2 mCenterY不变
                mRadius = (float) (mRadius * RATIO - STROKE_WIDTH);
                mCenterX = mRectX2 - mRadius;
                mRectX1 = mCenterX - mRadius;
                mRectY1 = mCenterY - mRadius;
                mRectY2 = mCenterY + mRadius;
                canvas.drawArc(mRectX1, mRectY1, mRectX2, mRectY2, mStartAngle, mSweepAngle, mUseCenter, mPaint);
                break;
            case 90:
                if (mFlag == 0) {
                    Log.d(TAG, "doSpiral: 1111111111");
                    mFlag++;
                    mRectX1 = mCenterX - mRadius;
                    mRectX2 = mCenterX + mRadius;
                    mRectY1 = mCenterY - mRadius;
                    mRectY2 = mCenterY + mRadius;
                    canvas.drawArc(mRectX1, mRectY1, mRectX2, mRectY2, mStartAngle, mSweepAngle, mUseCenter, mPaint);
                } else {
                    Log.d(TAG, "doSpiral: 222222222");
                    // mRectY2 mCenterX不变
                    mRadius = (float) (mRadius * RATIO - STROKE_WIDTH);
                    mRectX1 = mCenterX - mRadius;
                    mRectX2 = mCenterX + mRadius;
                    mCenterY = mRectY2 - mRadius;
                    mRectY1 = mCenterY - mRadius;
                    canvas.drawArc(mRectX1, mRectY1, mRectX2, mRectY2, mStartAngle, mSweepAngle, mUseCenter, mPaint);
                }
                break;
            case 180:
                // mRectX1 mCenterY不变
                mRadius = (float) (mRadius * RATIO - STROKE_WIDTH);
                mCenterX = mRectX1 + mRadius;
                mRectX2 = mCenterX + mRadius;
                mRectY1 = mCenterY - mRadius;
                mRectY2 = mCenterY + mRadius;
                canvas.drawArc(mRectX1, mRectY1, mRectX2, mRectY2, mStartAngle, mSweepAngle, mUseCenter, mPaint);
                break;
            case 270:
                // mRectY1 mCenterX不变
                mRadius = (float) (mRadius * RATIO - STROKE_WIDTH);
                mRectX1 = mCenterX - mRadius;
                mRectX2 = mCenterX + mRadius;
                mCenterY = mRectY1 + mRadius;
                mRectY2 = mCenterY + mRadius;
                canvas.drawArc(mRectX1, mRectY1, mRectX2, mRectY2, mStartAngle, mSweepAngle, mUseCenter, mPaint);
                break;
            default:
                Log.e(TAG, "drawS: ******************");
        }

        Log.d(TAG, "doSpiral: mRadius=" + mRadius);
        Log.d(TAG, "doSpiral: mCenterX=" + mCenterX);
        Log.d(TAG, "doSpiral: mCenterY=" + mCenterY);
    }

    private void drawWithPoint(Canvas canvas, int deep) {
        float x0 = this.getMeasuredWidth() / 2;
        float y0 = this.getMeasuredHeight() / 2;
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        for (int i = 0; i < 6145; i++) {
            double angle = i * Math.PI / 512;
            double radius = 0.3 * angle;
            int y = (int) Math.round(radius * angle * Math.cos(angle));
            int x = (int) Math.round(radius * angle * Math.sin(angle));
            canvas.drawPoint(x0 + x, y0 + y, paint);
        }
    }

    int i = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Log.d(TAG, "=======================" + (i++) + " defalutSize=" + defalutSize);
        // Log.d(TAG, "宽");
        int width = getMySize(defalutSize, widthMeasureSpec);
        // Log.d(TAG, "高");
        int height = getMySize(defalutSize, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                // match_parent或者固定值
                mySize = size;
                break;
            case MeasureSpec.AT_MOST:
                // wrap_content
                mySize = size;
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                //如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
        }
        // Log.d(TAG, "getMySize: measureSpec=" + measureSpec);
        // Log.d(TAG, "getMySize: mode=" + mode);
        // Log.d(TAG, "getMySize: size=" + size);
        return mySize;
    }

}
