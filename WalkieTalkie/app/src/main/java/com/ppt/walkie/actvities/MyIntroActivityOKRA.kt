package com.ppt.walkie.actvities


import android.content.Intent
import android.os.Bundle
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide
import com.ppt.walkie.R
import com.ppt.walkie.fragments.Intro_FragmentOKRA
import com.ppt.walkie.utils.SharePrefHelperOKRA

class MyIntroActivityOKRA : IntroActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        isFullscreen = true
        super.onCreate(savedInstanceState)
        isButtonBackVisible = false
        isButtonNextVisible = true
        isButtonCtaVisible = false
        buttonCtaTintMode = BUTTON_CTA_TINT_MODE_TEXT

        val slices_titles = resources.getStringArray(R.array.slices_titles)
        val slices_desc = resources.getStringArray(R.array.slices_desc)
//        AddSlide(slices_titles[0], slices_desc[0], R.drawable.intro_1)
//        AddSlide(slices_titles[1], slices_desc[1], R.drawable.intro_2)
//        AddSlide(slices_titles[2], slices_desc[2], R.drawable.intro_3)
//        AddSlide(slices_titles[3], slices_desc[3], R.drawable.intro_4)
//        AddSlide(slices_titles[4], slices_desc[4], R.drawable.intro_5)

    }

    private fun AddSlide(title: String, desc: String, imageId: Int) {
        addSlide(
            FragmentSlide.Builder()
                .background(R.color.purple_700)
                .backgroundDark(R.color.log_error)
                .fragment(
                    Intro_FragmentOKRA(
                        title,
                        desc,
                        imageId
                    )
                )
                .build()
        )
    }

    override fun onSendActivityResult(result: Int): Intent? {
        val isFirstRun: Boolean = SharePrefHelperOKRA(this).isFirstTime()
        if (isFirstRun) {
            startActivity(Intent(this@MyIntroActivityOKRA,GetUserNameOKRA::class.java))
            finish()
        }
        return super.onSendActivityResult(result)
    }

    override fun onBackPressed() {}
}