package com.screen.mirror.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.screen.mirror.Fragments.Intro_Fragment;
import com.screen.mirror.R;
import com.screen.mirror.Utils.SharedPrefHelperCA;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import static com.screen.mirror.Utils.Constants_CA.IS_FIRST_RUN;

public class UserGuideActivity_OKRA extends IntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(true);
        setButtonCtaVisible(false);

        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);

        AddSlide(getResources().getString(R.string.intro_slider_1_title)
                , getResources().getString(R.string.intro_slider_1_desc)
                , R.drawable.intro_slider_1);

        AddSlide(getResources().getString(R.string.intro_slider_2_title)
                , getResources().getString(R.string.intro_slider_2_desc)
                , R.drawable.intro_slider_2);

        AddSlide(getResources().getString(R.string.intro_slider_3_title)
                , getResources().getString(R.string.intro_slider_3_desc)
                , R.drawable.intro_slider_3);
    }

    private void AddSlide(String title, String desc, int imageId) {
        addSlide(new FragmentSlide.Builder()
                .background(R.color.color_2)
                .backgroundDark(R.color.color_3)
                .fragment(new Intro_Fragment(title, desc, imageId))
                .build());
    }

    @Override
    public Intent onSendActivityResult(int result) {
        boolean isFirstRun = new SharedPrefHelperCA(this).Get_Boolean_SM(IS_FIRST_RUN, true);
        if (isFirstRun) {
            startActivity(new Intent(this, SplashActivity_OKRA.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            new SharedPrefHelperCA(this).Set_Boolean_SM(IS_FIRST_RUN, false);
            finish();
        }
        return super.onSendActivityResult(result);
    }

    @Override
    public void onBackPressed() {

    }
}
