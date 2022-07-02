package com.photo.recovery.activites


import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.photo.recovery.R
import com.photo.recovery.fragment.Intro_FragmentAAT
import com.photo.recovery.utils.SharePrefHelperAAT
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide


class MyIntroActivityAAT : IntroActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        isFullscreen = true
        super.onCreate(savedInstanceState)

        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.appcolorgreen)

        isButtonBackVisible = false
        isButtonNextVisible = true
        isButtonCtaVisible = false
        buttonCtaTintMode = BUTTON_CTA_TINT_MODE_TEXT

        val slices_titles = resources.getStringArray(R.array.slices_titles)
        val slices_desc = resources.getStringArray(R.array.slices_desc)
        AddSlide(slices_titles[0], slices_desc[0], R.drawable.intro_slider_1,R.drawable.shapeone)
        AddSlide(slices_titles[1], slices_desc[1], R.drawable.intro_slider_2,R.drawable.shapetwo)
        AddSlide(slices_titles[2], slices_desc[2], R.drawable.intro_slider_3,R.drawable.shapefour)
        AddSlide(slices_titles[3], slices_desc[3], R.drawable.intro_slider_4,R.drawable.shapethree)


    }

    private fun AddSlide(title: String, desc: String, imageId: Int,backID:Int) {
        addSlide(
            FragmentSlide.Builder()
                .background(R.color.bgColor)
                .backgroundDark(R.color.unselected)
                .fragment(
                    Intro_FragmentAAT(
                        title,
                        desc,
                        imageId,
                        backID
                    )
                )
                .build()
        )
    }

    override fun onSendActivityResult(result: Int): Intent? {
        val isFirstRun: Boolean = SharePrefHelperAAT(this).isFirstTime()
        if (isFirstRun) {
            SharePrefHelperAAT(this).setIsFirstTime(false)
            finish()
        }
        return super.onSendActivityResult(result)
    }

    override fun onBackPressed() {}


}