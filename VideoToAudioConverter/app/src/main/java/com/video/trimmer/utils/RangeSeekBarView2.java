/*
 * MIT License
 *
 * Copyright (c) 2016 Knowledge, education for life.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.video.trimmer.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


import com.video.trimmer.R;
import com.video.trimmer.interfaces.OnRangeSeekBarListener;

import java.util.ArrayList;
import java.util.List;


public class RangeSeekBarView2 extends View {

    public static final String TAG = RangeSeekBarView2.class.getSimpleName();

    public int mHeightTimeLine;
    public List<Thumb2> mThumb2s;
    public List<OnRangeSeekBarListener> mListeners;
    public float mMaxWidth;
    public float mThumbWidth;
    public float mThumbHeight;
    public int mViewWidth;
    public float mPixelRangeMin;
    public float mPixelRangeMax;
    public float mScaleRangeMax;
    public boolean mFirstRun;

    public final Paint mShadow = new Paint();
    public final Paint mLine = new Paint();

    public RangeSeekBarView2(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RangeSeekBarView2(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mThumb2s = Thumb2.initThumbs(getResources(),getContext());
        mThumbWidth = Thumb2.getWidthBitmap(mThumb2s);
        mThumbHeight = Thumb2.getHeightBitmap(mThumb2s);

        mScaleRangeMax = 100;
        mHeightTimeLine = 0;

        setFocusable(true);
        setFocusableInTouchMode(true);

        mFirstRun = true;

        int shadowColor = ContextCompat.getColor(getContext(), R.color.shadow_color);
        mShadow.setAntiAlias(true);
        mShadow.setColor(shadowColor);
        mShadow.setAlpha(177);

        int lineColor = ContextCompat.getColor(getContext(), R.color.line_color);
        mLine.setAntiAlias(true);
        mLine.setColor(lineColor);
       // mLine.setAlpha(200);
    }

    public void initMaxWidth() {
        mMaxWidth = mThumb2s.get(1).getPos() - mThumb2s.get(0).getPos();

        onSeekStop(this, 0, mThumb2s.get(0).getVal());
        onSeekStop(this, 1, mThumb2s.get(1).getVal());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int minW = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mViewWidth = resolveSizeAndState(minW, widthMeasureSpec, 1);
        }

        int minH = getPaddingBottom() + getPaddingTop() + (int) mThumbHeight + mHeightTimeLine;
        int viewHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            viewHeight = resolveSizeAndState(minH, heightMeasureSpec, 1);
        }

        setMeasuredDimension(mViewWidth, viewHeight);

        mPixelRangeMin = 0;
        mPixelRangeMax = mViewWidth - mThumbWidth;

        if (mFirstRun) {
            for (int i = 0; i < mThumb2s.size(); i++) {
                Thumb2 th = mThumb2s.get(i);
                th.setVal(mScaleRangeMax * i);
                th.setPos(mPixelRangeMax * i);
            }
            // Fire listener callback
            onCreate(this, currentThumb, getThumbValue(currentThumb));
            mFirstRun = false;
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        drawShadow(canvas);
        drawThumbs(canvas);
    }

    public int currentThumb = 0;

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        final Thumb2 mThumb;
        final Thumb2 mThumb22;
        final float coordinate = ev.getX();
        final int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                // Remember where we started
                currentThumb = getClosestThumb(coordinate);

                if (currentThumb == -1) {
                    return false;
                }

                mThumb = mThumb2s.get(currentThumb);
                mThumb.setLastTouchX(coordinate);
                onSeekStart(this, currentThumb, mThumb.getVal());
                return true;
            }
            case MotionEvent.ACTION_UP: {

                if (currentThumb == -1) {
                    return false;
                }

                mThumb = mThumb2s.get(currentThumb);
                onSeekStop(this, currentThumb, mThumb.getVal());
                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                mThumb = mThumb2s.get(currentThumb);
                mThumb22 = mThumb2s.get(currentThumb == 0 ? 1 : 0);
                // Calculate the distance moved
                final float dx = coordinate - mThumb.getLastTouchX();
                final float newX = mThumb.getPos() + dx;
                if (currentThumb == 0) {

                    if ((newX + mThumb.getWidthBitmap()) >= mThumb22.getPos()) {
                        mThumb.setPos(mThumb22.getPos() - mThumb.getWidthBitmap());
                    } else if (newX <= mPixelRangeMin) {
                        mThumb.setPos(mPixelRangeMin);
                    } else {
                        //Check if thumb is not out of max width
                        checkPositionThumb(mThumb, mThumb22, dx, true);
                        // Move the object
                        mThumb.setPos(mThumb.getPos() + dx);

                        // Remember this touch position for the next move event
                        mThumb.setLastTouchX(coordinate);
                    }

                } else {
                    if (newX <= mThumb22.getPos() + mThumb22.getWidthBitmap()) {
                        mThumb.setPos(mThumb22.getPos() + mThumb.getWidthBitmap());
                    } else if (newX >= mPixelRangeMax) {
                        mThumb.setPos(mPixelRangeMax);
                    } else {
                        //Check if thumb is not out of max width
                        checkPositionThumb(mThumb22, mThumb, dx, false);
                        // Move the object
                        mThumb.setPos(mThumb.getPos() + dx);
                        // Remember this touch position for the next move event
                        mThumb.setLastTouchX(coordinate);
                    }
                }

                setThumbPos(currentThumb, mThumb.getPos());

                // Invalidate to request a redraw
                invalidate();
                return true;
            }
        }
        return false;
    }

    public void checkPositionThumb(@NonNull Thumb2 mThumb2Left, @NonNull Thumb2 mThumb2Right, float dx, boolean isLeftMove) {
        if (isLeftMove && dx < 0) {
            if ((mThumb2Right.getPos() - (mThumb2Left.getPos() + dx)) > mMaxWidth) {
                mThumb2Right.setPos(mThumb2Left.getPos() + dx + mMaxWidth);
                setThumbPos(1, mThumb2Right.getPos());
            }
        } else if (!isLeftMove && dx > 0) {
            if (((mThumb2Right.getPos() + dx) - mThumb2Left.getPos()) > mMaxWidth) {
                mThumb2Left.setPos(mThumb2Right.getPos() + dx - mMaxWidth);
                setThumbPos(0, mThumb2Left.getPos());
            }
        }
    }

    public int getUnstuckFrom(int index) {
        int unstuck = 0;
        float lastVal = mThumb2s.get(index).getVal();
        for (int i = index - 1; i >= 0; i--) {
            Thumb2 th = mThumb2s.get(i);
            if (th.getVal() != lastVal)
                return i + 1;
        }
        return unstuck;
    }

    public float pixelToScale(int index, float pixelValue) {
        float scale = (pixelValue * 100) / mPixelRangeMax;
        if (index == 0) {
            float pxThumb = (scale * mThumbWidth) / 100;
            return scale + (pxThumb * 100) / mPixelRangeMax;
        } else {
            float pxThumb = ((100 - scale) * mThumbWidth) / 100;
            return scale - (pxThumb * 100) / mPixelRangeMax;
        }
    }

    public float scaleToPixel(int index, float scaleValue) {
        float px = (scaleValue * mPixelRangeMax) / 100;
        if (index == 0) {
            float pxThumb = (scaleValue * mThumbWidth) / 100;
            return px - pxThumb;
        } else {
            float pxThumb = ((100 - scaleValue) * mThumbWidth) / 100;
            return px + pxThumb;
        }
    }

    public void calculateThumbValue(int index) {
        if (index < mThumb2s.size() && !mThumb2s.isEmpty()) {
            Thumb2 th = mThumb2s.get(index);
            th.setVal(pixelToScale(index, th.getPos()));
            onSeek(this, index, th.getVal());
        }
    }

    public void calculateThumbPos(int index) {
        if (index < mThumb2s.size() && !mThumb2s.isEmpty()) {
            Thumb2 th = mThumb2s.get(index);
            th.setPos(scaleToPixel(index, th.getVal()));
        }
    }

    public float getThumbValue(int index) {
        return mThumb2s.get(index).getVal();
    }

    public void setThumbValue(int index, float value) {
        mThumb2s.get(index).setVal(value);
        calculateThumbPos(index);
        // Tell the view we want a complete redraw
        invalidate();
    }

    public void setThumbPos(int index, float pos) {
        mThumb2s.get(index).setPos(pos);
        calculateThumbValue(index);
        // Tell the view we want a complete redraw
        invalidate();
    }

    public int getClosestThumb(float coordinate) {
        int closest = -1;
        if (!mThumb2s.isEmpty()) {
            for (int i = 0; i < mThumb2s.size(); i++) {
                // Find thumb closest to x coordinate
                final float tcoordinate = mThumb2s.get(i).getPos() + mThumbWidth;
                if (coordinate >= mThumb2s.get(i).getPos() && coordinate <= tcoordinate) {
                    closest = mThumb2s.get(i).getIndex();
                }
            }
        }
        return closest;
    }

    public void drawShadow(@NonNull Canvas canvas) {
        if (!mThumb2s.isEmpty()) {

            for (Thumb2 th : mThumb2s) {
                if (th.getIndex() == 0) {
                    final float x = th.getPos() + getPaddingLeft();
                    if (x > mPixelRangeMin) {
                        Rect mRect = new Rect((int) mThumbWidth, 0, (int) (x + mThumbWidth), mHeightTimeLine);
                        canvas.drawRect(mRect, mShadow);
                    }
                } else {
                    final float x = th.getPos() - getPaddingRight();
                    if (x < mPixelRangeMax) {
                        Rect mRect = new Rect((int) x, 0, (int) (mViewWidth - mThumbWidth), mHeightTimeLine);
                        canvas.drawRect(mRect, mShadow);
                    }
                }
            }
        }
    }

    public void drawThumbs(@NonNull Canvas canvas) {

        if (!mThumb2s.isEmpty()) {
            for (Thumb2 th : mThumb2s) {
                if (th.getIndex() == 0) {
                    canvas.drawBitmap(th.getBitmap(), th.getPos() + getPaddingLeft(), getPaddingTop() + mHeightTimeLine, null);
                } else {
                    canvas.drawBitmap(th.getBitmap(), th.getPos() - getPaddingRight(), getPaddingTop() + mHeightTimeLine, null);
                }
            }
        }
    }

    public void addOnRangeSeekBarListener(OnRangeSeekBarListener listener) {

        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }

        mListeners.add(listener);
    }

    public void onCreate(RangeSeekBarView2 rangeSeekBarView2, int index, float value) {
        if (mListeners == null)
            return;

        for (OnRangeSeekBarListener item : mListeners) {
            item.onCreate(rangeSeekBarView2, index, value);
        }
    }

    public void onSeek(RangeSeekBarView2 rangeSeekBarView2, int index, float value) {
        if (mListeners == null)
            return;

        for (OnRangeSeekBarListener item : mListeners) {
            item.onSeek(rangeSeekBarView2, index, value);
        }
    }

    public void onSeekStart(RangeSeekBarView2 rangeSeekBarView2, int index, float value) {
        if (mListeners == null)
            return;

        for (OnRangeSeekBarListener item : mListeners) {
            item.onSeekStart(rangeSeekBarView2, index, value);
        }
    }

    public void onSeekStop(RangeSeekBarView2 rangeSeekBarView2, int index, float value) {
        if (mListeners == null)
            return;

        for (OnRangeSeekBarListener item : mListeners) {
            item.onSeekStop(rangeSeekBarView2, index, value);
        }
    }

    public List<Thumb2> getThumbs() {
        return mThumb2s;
    }


}