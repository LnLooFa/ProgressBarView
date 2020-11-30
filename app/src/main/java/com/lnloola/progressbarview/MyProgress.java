package com.lnloola.progressbarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class MyProgress extends ProgressBar {

    String text;
    Paint mPaint;

    public MyProgress(Context context) {
        super(context);
        System.out.println("1");
        initText();
    }

    public MyProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        System.out.println("2");
        initText();
    }


    public MyProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        System.out.println("3");
        initText();
    }

    @Override
    public synchronized void setProgress(int progress) {
        setText(progress);
        super.setProgress(progress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.text, 10, y, this.mPaint);
    }

    //初始化，画笔
    private void initText(){
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.WHITE);
    }

    private void setText(){
        setText(this.getProgress());
    }

    //设置文字内容
    private void setText(int progress){
//        int i = (progress * 100)/this.getMax();
        this.text = progress+"/"+this.getMax();
    }
}
