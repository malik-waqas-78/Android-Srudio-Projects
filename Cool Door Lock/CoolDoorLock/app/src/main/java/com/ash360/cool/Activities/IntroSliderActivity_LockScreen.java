package com.ash360.cool.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.ash360.cool.Fragments.Intro_Fragment;
import com.ash360.cool.R;
import com.ash360.cool.Utils.Constants_DoorLock;
import com.ash360.cool.Utils.Shared_Pref_DoorLock;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class IntroSliderActivity_LockScreen extends IntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(true);
        setButtonCtaVisible(false);

        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);

        String[] slices_titles = getResources().getStringArray(R.array.intro_titles);
        String[] slices_desc = getResources().getStringArray(R.array.intro_descriptions);

        AddSlide(slices_titles[0], slices_desc[0], R.drawable.intro_slide_img_1);
        AddSlide(slices_titles[1], slices_desc[1], R.drawable.intro_slide_img_2);
        AddSlide(slices_titles[2], slices_desc[2], R.drawable.intro_slide_img_3);


    }

    private void AddSlide(String title, String desc, int imageId) {
        addSlide(new FragmentSlide.Builder()
                .background(R.color.silver)
                .backgroundDark(R.color.silver_dark)
                .fragment(new Intro_Fragment(title, desc, imageId))
                .build());
    }

    @Override
    public Intent onSendActivityResult(int result) {
        boolean isFirstRun = new Shared_Pref_DoorLock(this).GetBool(Constants_DoorLock.IS_FIRST_RUN, true);
        if (isFirstRun) {
            new Shared_Pref_DoorLock(this).SetBool(Constants_DoorLock.IS_FIRST_RUN, false);
            startActivity(new Intent(this, Settings_DoorLock.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
        return super.onSendActivityResult(result);
    }

    @Override
    public void onBackPressed() {

    }
}
