package com.niazitvpro.official.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RalewayBoldTextViewGray extends TextView {
    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public RalewayBoldTextViewGray(Context context) {
        super(context);
        applyCustomFont(context, null);
    }

    public RalewayBoldTextViewGray(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context, attrs);
    }

    public RalewayBoldTextViewGray(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        int attributeIntValue = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "textStyle", 0);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Raleway_Bold.ttf"));
        setTextColor(getResources().getColor(android.R.color.darker_gray));
    }
}
