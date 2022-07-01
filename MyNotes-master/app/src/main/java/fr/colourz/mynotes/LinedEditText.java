package fr.colourz.mynotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;


public class LinedEditText extends android.support.v7.widget.AppCompatEditText {
    private Rect mRect;
    private Paint mPaint;
    private EditActivity _activity;

    public LinedEditText(Context context, AttributeSet attrs, EditActivity activity) {
        super(context, attrs);
        this._activity = activity;

        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getResources().getColor(R.color.line));

        SharedPreferences preferences = getContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        if (preferences.getString("app_theme", "light").matches("dark")) {
            setTextColor(getResources().getColor(R.color.white));
            setHintTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        SharedPreferences preferences = getContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        if (preferences.getString("draw_lines", "true") == "true") {
            int count = getLineCount();
            Rect r = mRect;
            Paint paint = mPaint;

            for (int i = 0; i < count; i++) {
                int baseline = getLineBounds(i, r);

                canvas.drawLine(r.left, baseline + 10, r.right, baseline + 10, paint);
            }
        }

        super.onDraw(canvas);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (_activity != null){

            if(!_activity.isBoldEditing) {
                StyleSpan[] bs = this.getText().getSpans(selStart, selStart, StyleSpan.class);
                boolean isBold = false;
                for (int i = 0; i < bs.length; i++) {
                    if (bs[i].getStyle() == Typeface.BOLD) {
                        isBold = true;
                    }
                }
                if (isBold) {
                    _activity.btn_bold.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    _activity.btn_bold.setColorFilter(getResources().getColor(R.color.white));
                } else {
                    _activity.btn_bold.setBackgroundColor(getResources().getColor(R.color.transparent));
                    _activity.btn_bold.setColorFilter(getResources().getColor(R.color.black));
                }
            }

            if(!_activity.isItalicEditing) {
                StyleSpan[] is = this.getText().getSpans(selStart, selStart, StyleSpan.class);
                boolean isItalic = false;
                for (int i = 0; i < is.length; i++) {
                    if (is[i].getStyle() == Typeface.ITALIC) {
                        isItalic = true;
                    }
                }
                if (isItalic) {
                    _activity.btn_italic.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    _activity.btn_italic.setColorFilter(getResources().getColor(R.color.white));
                } else {
                    _activity.btn_italic.setBackgroundColor(getResources().getColor(R.color.transparent));
                    _activity.btn_italic.setColorFilter(getResources().getColor(R.color.black));
                }
            }

            if(!_activity.isUnderlineEditing) {
                UnderlineSpan[] us = this.getText().getSpans(selStart, selStart, UnderlineSpan.class);
                boolean isUnderlined = false;
                for (int i = 0; i < us.length; i++) {
                    if (us[i] != null) {
                        isUnderlined = true;
                    }
                }
                if (isUnderlined) {
                    _activity.btn_underline.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    _activity.btn_underline.setColorFilter(getResources().getColor(R.color.white));
                } else {
                    _activity.btn_underline.setBackgroundColor(getResources().getColor(R.color.transparent));
                    _activity.btn_underline.setColorFilter(getResources().getColor(R.color.black));
                }
            }

            if(!_activity.isURLEditing) {
                URLSpan[] urls = this.getText().getSpans(selStart, selEnd, URLSpan.class);
                boolean isURL = false;
                for (int i = 0; i < urls.length; i++) {
                    if (urls[i] != null) {
                        isURL = true;
                    }
                }
                if (isURL) {
                    _activity.btn_url.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    _activity.btn_url.setColorFilter(getResources().getColor(R.color.white));
                } else {
                    _activity.btn_url.setBackgroundColor(getResources().getColor(R.color.transparent));
                    _activity.btn_url.setColorFilter(getResources().getColor(R.color.black));
                }
            }

            if(!_activity.isColorEditing) {
                ForegroundColorSpan[] fcs = this.getText().getSpans(selStart, selEnd, ForegroundColorSpan.class);
                boolean isColored = false;
                for (int i = 0; i < fcs.length; i++) {
                    if (fcs[i].getForegroundColor() == getResources().getColor(R.color.color_red)) {
                        isColored = true;
                        _activity.btn_color.setBackgroundColor(getResources().getColor(R.color.color_red));
                    } else if (fcs[i].getForegroundColor() == getResources().getColor(R.color.color_blue)) {
                        isColored = true;
                        _activity.btn_color.setBackgroundColor(getResources().getColor(R.color.color_blue));
                    } else if (fcs[i].getForegroundColor() == getResources().getColor(R.color.color_green)) {
                        isColored = true;
                        _activity.btn_color.setBackgroundColor(getResources().getColor(R.color.color_green));
                    } else {
                        isColored = false;
                        _activity.btn_color.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }
                if (isColored) {
                    _activity.btn_color.setColorFilter(getResources().getColor(R.color.white));
                } else {
                    _activity.btn_color.setBackgroundColor(getResources().getColor(R.color.transparent));
                    _activity.btn_color.setColorFilter(getResources().getColor(R.color.black));
                }
            }
        }
    }
}


