package com.lnloola.progressbarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.DecimalFormat;

public class ProportionView extends View {
    private int homeColorStart;  //左边起始渐变色
    private int awayColorStart;  //右边起始渐变色
    private int homeColorEnd;  //左边结束渐变色
    private int awayColorEnd;  //右边结束渐变色
//    private float round;
    private float homeNum;
    private float awayNum;
    private int textColor;
    private float TextSize;
    private float mInclination;
//    private RectF mHomeGroundRect;
//    private RectF mAwayGroundRect;

    private LinearGradient backGradientHome;
    private LinearGradient backGradientAway;

    private float iPre;
    private float oPre;

    private String txtiPre;                      //显示进的百分比
    private String txtoPre;                      //显示出的百分比

//    private Paint mPaint;
//    private Rect mBound;                        //包含文字的框


    //默认画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintText = new Paint();
    private Paint mPaintCircle = new Paint();
    private Rect mBound;
    public ProportionView(Context context) {
        this(context, null);
    }

    public ProportionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProportionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyProgress,defStyleAttr,0);
            homeColorStart = typedArray.getColor(R.styleable.MyProgress_homeColorStart, getResources().getColor(R.color.homeStart));
            homeColorEnd = typedArray.getColor(R.styleable.MyProgress_homeColorEnd, getResources().getColor(R.color.homeEnd));
            awayColorStart = typedArray.getColor(R.styleable.MyProgress_awayColorStart, getResources().getColor(R.color.awayStart));
            awayColorEnd = typedArray.getColor(R.styleable.MyProgress_awayColorEnd, getResources().getColor(R.color.awayEnd));
//            round = typedArray.getDimension(R.styleable.MyPre_round, ArmsUtils.dip2px(context, 10));
            homeNum = typedArray.getDimension(R.styleable.MyProgress_homeNum, Utils.dip2px(context, 50));
            awayNum = typedArray.getDimension(R.styleable.MyProgress_awayNum, Utils.dip2px(context, 50));
            textColor = typedArray.getColor(R.styleable.MyProgress_textColor, Color.WHITE);
            TextSize = typedArray.getDimension(R.styleable.MyProgress_allTextSize, Utils.dip2px(context, 10));
            mInclination = typedArray.getInteger(R.styleable.MyProgress_allInclination, 13);
            typedArray.recycle();
        }


        //设置抗锯齿
        mPaint.setAntiAlias(true);
        mPaintCircle.setAntiAlias(true);
        //设置防抖动
        mPaint.setDither(true);
        mPaintCircle.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaintCircle.setStyle(Paint.Style.FILL);

        mPaintText.setAntiAlias(true);
        mPaintText.setDither(true);
        mPaint.setStrokeWidth(5);

        mBound = new Rect();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width ;
        int height ;

        if(widthMode== MeasureSpec.EXACTLY) {
            width = widthSize;
        }else {
            width = getPaddingLeft() + getWidth() + getPaddingRight();
        }

        if(heightMode== MeasureSpec.EXACTLY) {
            height = heightSize;
        }else {
            height = getPaddingTop() + getHeight() + getPaddingBottom();
        }

        setMeasuredDimension(width,height);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        iPre = (homeNum /(homeNum + awayNum)) * getWidth();
        oPre = (awayNum /(homeNum + awayNum)) * getWidth();

        Log.e("onDraw","iPre:" + iPre + "  oPre:" + oPre + "width" + getWidth());

        //如果进值或出值有一个为0，则另一个就会占满整个进度条，这时就不需要倾斜角度了
        if(homeNum==0 || oPre==0) {
            mInclination = 0;
        }

        mPaintCircle.setColor(homeColorStart);
        canvas.drawCircle(getHeight()/2, getHeight()/2, getHeight()/2, mPaintCircle);

//        mHomeGroundRect = new RectF(0, 0, getWidth(), getHeight());
        backGradientHome = new LinearGradient(0, 0, getWidth(), 0, new int[]{homeColorStart, homeColorEnd}, null, Shader.TileMode.CLAMP);
        mPaint.setShader(backGradientHome);
        //绘制背景 圆角矩形


        Path iPath = new Path();
        iPath.moveTo(getHeight()/2, 0);
        iPath.lineTo(iPre + mInclination, 0);
        iPath.lineTo(iPre, getHeight());
        iPath.lineTo(getHeight()/2, getHeight());
        iPath.close();
        canvas.drawPath(iPath, mPaint);

        mPaintCircle.setColor(awayColorEnd);
        canvas.drawCircle(iPre+oPre-getHeight()/2, getHeight()/2, getHeight()/2, mPaintCircle);


//        mAwayGroundRect = new RectF(iPre + mInclination, 0, getWidth(), getHeight());

        backGradientAway = new LinearGradient(iPre + mInclination, 0, getWidth(), 0, new int[]{awayColorStart, awayColorEnd}, null, Shader.TileMode.CLAMP);

        mPaint.setShader(backGradientAway);
        //绘制背景 圆角矩形
//        if (mAwayGroundRect != null) {
//            canvas.drawRoundRect(mAwayGroundRect, round, round, mPaint);
//        }

        Path oPath = new Path();
        oPath.moveTo(iPre + mInclination, 0);
        oPath.lineTo(getWidth()-getHeight()/2, 0);
        oPath.lineTo(getWidth()-getHeight()/2, getHeight());
        oPath.lineTo(iPre - mInclination, getHeight());
        oPath.close();
        canvas.drawPath(oPath, mPaint);

        txtiPre = getProValText(homeNum /(homeNum + awayNum) * 100);
        txtoPre = getProValText(awayNum /(homeNum + awayNum) * 100);


        mPaintText.setColor(textColor);
        mPaintText.setTextSize(TextSize);

        mPaintText.getTextBounds(txtiPre, 0, txtiPre.length(), mBound);
        //判断一下，如果进值为0则不显示，如果进值不为空而出值为0，则进值的数值显示居中显示
        if(homeNum!=0 && awayNum!=0) {
            canvas.drawText(txtiPre, 20, getHeight() / 2 + mBound.height() / 2, mPaintText);
        }else if(homeNum!=0 && awayNum==0){
            canvas.drawText(txtiPre, getWidth()/2 - mBound.width()/2, getHeight() / 2 + mBound.height() / 2, mPaintText);
        }

        mPaintText.setColor(textColor);
        mPaintText.getTextBounds(txtoPre, 0, txtoPre.length(), mBound);
        if(awayNum!=0 && homeNum!=0) {
            canvas.drawText(txtoPre, getWidth() - 20 - mBound.width(), getHeight() / 2 + mBound.height() / 2, mPaintText);
        }else if(awayNum!=0 && homeNum==0){
            canvas.drawText(txtoPre, getWidth()/2 - mBound.width()/2, getHeight() / 2 + mBound.height() / 2, mPaintText);
        }
    }

    /**
     * 格式化显示的百分比
     * @param proValue
     * @return
     */
    private String getProValText(float proValue)
    {
        DecimalFormat format = new DecimalFormat("#0.0");
        return format.format(proValue) + "%";
    }

    /**
     * 动态设置进值
     * @param homeNum
     */
    public void setINum(float homeNum) {
        this.homeNum = homeNum;
        postInvalidate();
    }

    /**
     * 动态设置出值
     * @param awayNum
     */
    public void setONum(float awayNum) {
        this.awayNum = awayNum;
        postInvalidate();
    }

}
