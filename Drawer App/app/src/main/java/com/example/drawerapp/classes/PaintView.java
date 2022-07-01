package com.example.drawerapp.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.example.drawerapp.R;

public class PaintView extends View implements View.OnTouchListener {

    private static final String TAG = "PaintView";
    Bitmap Bitmap1, Bitmap2;
    Bitmap Transparent;
    int X = -100;
    int Y = -100;
    Canvas c2;
    private boolean isTouched = false;

    Paint paint = new Paint();

    Path drawPath = new Path();

    public PaintView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Transparent = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.cake1);
//        Bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.cake2);

        c2 = new Canvas();
        c2.setBitmap(Transparent);
        c2.drawBitmap(Bitmap2, 0, 0, paint);

        paint.setAlpha(0);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setAntiAlias(true);
    }

    private static Point getDisplaySize(final Display display) {
        final Point point = new Point();
        point.x = display.getWidth();
        point.y = display.getHeight();
        return point;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("onDraw");

        if(isTouched)
        {
            canvas.drawBitmap(Bitmap1, 0, 0, null);

        }
        canvas.drawBitmap(Transparent, 0, 0, null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        isTouched = true;
        X = (int) event.getX();
        Y = (int) event.getY();

        paint.setStrokeWidth(60);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(X, Y);
                c2.drawPath(drawPath, paint);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(X, Y);
                c2.drawPath(drawPath, paint);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(X, Y);
                c2.drawPath(drawPath, paint);
                drawPath.reset();
                //count=0;
                break;
            default:
                return false;
        }

        invalidate();
        return true;}}class Point {
    float x, y;

    @Override
    public String toString() {
        return x + ", " + y;
    }}