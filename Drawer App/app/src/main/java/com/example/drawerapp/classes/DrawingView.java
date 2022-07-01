package com.example.drawerapp.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
    //drawing path
    private Path drawPath, circlePath, mPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint, circlePaint, mBitmapPaint;
    //initial color
    private int paintColor = 0xFF000000, paintAlpha = 255;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    //brush sizes
    private float brushSize, lastBrushSize;
    //erase flag
    private boolean erase = false;
    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }
    public DrawingView(Context context){
        super(context);
    }
    //setup drawing
    private void setupDrawing(){
        //prepare for drawing and setup paint stroke properties
        brushSize = 5;
        lastBrushSize = brushSize;
        drawPath = new Path();
        mPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.parseColor("#f4802b"));
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(1f);
    }
    //size assigned to view
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvasBitmap.eraseColor(Color.TRANSPARENT);
        drawCanvas = new Canvas(canvasBitmap);
        //            drawCanvas.drawColor(Color.BLACK);
    }
    //draw the view - will be called after touch event
    @Override
    protected void onDraw(Canvas canvas) {
        if(erase) {
            canvas.drawBitmap(canvasBitmap, 0, 0, mBitmapPaint);
            //                  canvas.drawPath(mPath, drawPaint);
            canvas.drawPath(circlePath, circlePaint);
        } else {
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            canvas.drawPath(drawPath, drawPaint);
        }
    }
    float mX, mY;
    private static final float TOUCH_TOLERANCE = 1;
    //register user touches as drawing action
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        //respond to down, move and up events
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(erase) {
                    mPath.reset();
                    mPath.moveTo(touchX, touchY);
                    mX = touchX;
                    mY = touchY;
                    drawPaint.setStyle(Paint.Style.FILL);
                    drawPaint.setColor(Color.BLACK);
                    drawCanvas.drawCircle(touchX, touchY, 25, drawPaint);
                } else {
                    drawPath.moveTo(touchX, touchY);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(erase) {
                    float dx = Math.abs(touchX - mX);
                    float dy = Math.abs(touchY - mY);
                    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                        mPath.quadTo(mX, mY, (touchX + mX)/2, (touchY + mY)/2);
                        mX = touchX;
                        mY = touchY;
                        circlePath.reset();
                        circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
                    }
                    drawPaint.setStyle(Paint.Style.FILL);
                    drawPaint.setColor(Color.BLACK);
                    drawCanvas.drawCircle(touchX, touchY, 25, drawPaint);
                } else {
                    drawPath.lineTo(touchX, touchY);
                }
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                circlePath.reset();
                drawPath.reset();
                break;
            default:
                return false;
        }
        //redraw
        invalidate();
        return true;
    }
    //update color
    public void setColor(String newColor){
        invalidate();
        //check whether color value or pattern name
        if(newColor.startsWith("#")){
            paintColor = Color.parseColor(newColor);
            drawPaint.setColor(paintColor);
            drawPaint.setShader(null);
        } else {
            //pattern
            int patternID = getResources().getIdentifier(newColor, "drawable", "com.thots");
            //decode
            Bitmap patternBMP = BitmapFactory.decodeResource(getResources(), patternID);
            //create shader
            BitmapShader patternBMPshader = new BitmapShader(patternBMP, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            //color and shader
            drawPaint.setColor(0xFFFFFFFF);
            drawPaint.setShader(patternBMPshader);
        }
    }
    //set brush size
    public void setBrushSize(float newSize){
        //            float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newSize, getResources().getDisplayMetrics());
        //            brushSize=pixelAmount;
        brushSize=newSize;
        drawPaint.setStrokeWidth(brushSize);
    }
    //get and set last brush size
    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }
    //set erase true or false
    public void setErase(boolean isErase){
        erase=isErase;
        if(erase) {
           // drawPaint.setStyle(Paint.Style.FILL);
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setColor(paintColor);
            drawPaint.setXfermode(null);
        }
    }
    //start new drawing
    public void startNew() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }
    //return current alpha
    public int getPaintAlpha(){
        return Math.round((float)paintAlpha/255*100);
    }
    //set alpha
    public void setPaintAlpha(int newAlpha){
        paintAlpha=Math.round((float)newAlpha/100*255);
        drawPaint.setColor(paintColor);
        drawPaint.setAlpha(paintAlpha);
    }
}