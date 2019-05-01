package com.lexot.cenicafe.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class DrawingView extends View {

    boolean haveTouch;
    Paint drawingPaint;
    Rect touchArea;

    public DrawingView(Context context) {
        super(context);
        drawingPaint = new Paint();
        drawingPaint.setColor(Color.GREEN);
        drawingPaint.setStyle(Paint.Style.STROKE);
        drawingPaint.setStrokeWidth(2);

        haveTouch = false;
    }

    public void setHaveTouch(boolean t, Rect tArea){
        haveTouch = t;
        touchArea = tArea;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(haveTouch){
            drawingPaint.setColor(Color.BLUE);
            canvas.drawRect(
                    touchArea.left, touchArea.top, touchArea.right, touchArea.bottom,
                    drawingPaint);
        }
    }

}