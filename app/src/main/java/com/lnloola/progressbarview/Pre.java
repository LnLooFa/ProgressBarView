package com.lnloola.progressbarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.text.DecimalFormat;

public class Pre extends View {

    private float iNum = 50;                     //进(左)的数量
    private int iColor = Color.RED;              //进的颜色
    private float oNum = 50;                     //出(右)的数量
    private int oColor = Color.GREEN;            //出的颜色
    private int mInclination = 40;               //两柱中间的倾斜度
    private int iTextColor = Color.WHITE;        //进的百分比数字颜色
    private int oTextColor = Color.WHITE;        //出的百分比数字颜色
    private int TextSize = 30;                   //百分比字体大小


    private float iPre;
    private float oPre;

    private String txtiPre;                      //显示进的百分比
    private String txtoPre;                      //显示出的百分比

    private Paint mPaint;
    private Rect mBound;                        //包含文字的框

    public Pre(Context context) {
        this(context, null);
    }

    public Pre(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Pre(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray arry = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyPre,defStyleAttr,0);
        int n = arry.getIndexCount();

        for(int i=0;i<n;i++)
        {
            int attr = arry.getIndex(i);
            switch (attr)
            {
                case R.styleable.MyPre_iNum:
                    iNum = arry.getFloat(attr, 50);
                    break;
                case R.styleable.MyPre_iColor:
                    iColor = arry.getColor(attr, Color.RED);
                    break;
                case R.styleable.MyPre_oNum:
                    oNum = arry.getFloat(attr, 50);
                    break;
                case R.styleable.MyPre_oColor:
                    oColor = arry.getColor(attr,Color.GREEN);
                    break;
                case R.styleable.MyPre_Inclination:
                    mInclination = arry.getInt(attr,40);
                    break;
                case R.styleable.MyPre_iTextColor:
                    iTextColor = arry.getColor(attr,Color.WHITE);
                    break;
                case R.styleable.MyPre_oTextColor:
                    oTextColor = arry.getColor(attr,Color.WHITE);
                    break;
                case R.styleable.MyPre_TextSize:
                    TextSize = arry.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }

        arry.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
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

        if(widthMode==MeasureSpec.EXACTLY)
        {
            width = widthSize;
        }else
        {
            width = getPaddingLeft() + getWidth() + getPaddingRight();
        }

        if(heightMode==MeasureSpec.EXACTLY)
        {
            height = heightSize;
        }else
        {
            height = getPaddingTop() + getHeight() + getPaddingBottom();
        }

        setMeasuredDimension(width,height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        iPre = (iNum /(iNum + oNum)) * getWidth();
        oPre = (oNum /(iNum + oNum)) * getWidth();

        //Log.e("mPre", "iPre:" + iPre + "  oPre:" + oPre + "width" + getWidth());

        //如果进值或出值有一个为0，则另一个就会占满整个进度条，这时就不需要倾斜角度了
        if(iNum==0 || oPre==0)
        {
            mInclination = 0;
        }

        Path iPath = new Path();
        iPath.moveTo(0, 0);
        iPath.lineTo(iPre + mInclination, 0);
        iPath.lineTo(iPre, getHeight());
        iPath.lineTo(0, getHeight());
        iPath.close();

        mPaint.setColor(iColor);
        canvas.drawPath(iPath, mPaint);


        Path oPath = new Path();
        oPath.moveTo(iPre + mInclination, 0);
        oPath.lineTo(getWidth(), 0);
        oPath.lineTo(getWidth(), getHeight());
        oPath.lineTo(iPre - mInclination, getHeight());
        oPath.close();

        mPaint.setColor(oColor);
        canvas.drawPath(oPath, mPaint);

        txtiPre = getProValText(iNum /(iNum + oNum) * 100);
        txtoPre = getProValText(oNum /(iNum + oNum) * 100);


        mPaint.setColor(iTextColor);
        mPaint.setTextSize(TextSize);

        mPaint.getTextBounds(txtiPre, 0, txtiPre.length(), mBound);
        //判断一下，如果进值为0则不显示，如果进值不为空而出值为0，则进值的数值显示居中显示
        if(iNum!=0 && oNum!=0) {

            canvas.drawText(txtiPre, 20, getHeight() / 2 + mBound.height() / 2, mPaint);

        }else if(iNum!=0 && oNum==0){

            canvas.drawText(txtiPre, getWidth()/2 - mBound.width()/2, getHeight() / 2 + mBound.height() / 2, mPaint);

        }

        mPaint.setColor(oTextColor);
        mPaint.getTextBounds(txtoPre, 0, txtoPre.length(), mBound);
        if(oNum!=0 && iNum!=0) {

            canvas.drawText(txtoPre, getWidth() - 20 - mBound.width(), getHeight() / 2 + mBound.height() / 2, mPaint);

        }else if(oNum!=0 && iNum==0){


            canvas.drawText(txtoPre, getWidth()/2 - mBound.width()/2, getHeight() / 2 + mBound.height() / 2, mPaint);
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
     * @param iNum
     */
    public void setINum(float iNum)
    {
        this.iNum = iNum;
        postInvalidate();
    }

    /**
     * 动态设置出值
     * @param oNum
     */
    public void setONum(float oNum)
    {
        this.oNum = oNum;
        postInvalidate();
    }

}
