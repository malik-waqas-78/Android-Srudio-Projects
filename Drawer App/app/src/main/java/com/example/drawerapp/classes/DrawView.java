package com.example.drawerapp.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View implements View.OnTouchListener {

    List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint();

    public DrawView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        paint.setColor(Color.BLACK);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Point point : points) {
            canvas.drawCircle(point.x, point.y, 2, paint);
        }
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        Point point = new Point();
        point.x = (int) event.getX();
        point.y= (int) event.getY();
        points.add(point);
        invalidate();
        return true;
    }
}