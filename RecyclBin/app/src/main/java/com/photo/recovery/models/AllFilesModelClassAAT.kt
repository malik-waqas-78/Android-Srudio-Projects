package com.photo.recovery.models

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.photo.recovery.R
import com.photo.recovery.constants.MyConstantsAAT
import java.text.SimpleDateFormat
import java.util.*

class AllFilesModelClassAAT(
    var context: Context,
    var filePath: String,
    var fileName: String = "NoName",
    var date: Long = 0L,
    var fileType: String = MyConstantsAAT.MEDIA_FILE_TYPE,
    var subType: String = "",
    var isSelected: Boolean = false
) {

    val MediaIcon = R.drawable.ic_images
    val DocIcon = R.drawable.ic_docs
    val ImagesIcon = R.drawable.ic_images
    val VideosIcon = R.drawable.ic_videos
    val AudioIcon = R.drawable.ic_audios
    val PdfIcon = R.drawable.ic_pdf
    val WordIcon = R.drawable.ic_word
    val Othericon = R.drawable.ic_excel

    val MediaColor = R.color.white
    val DocColor = R.color.white
    val ImagesColor = R.color.white
    val VideosColor = R.color.white
    val AudioColor = R.color.white
    val PdfColor = R.color.white
    val WordColor = R.color.white
    val OtherColor = R.color.white


    var dateInFormat: String = "01/01/1970"
    var icon: Drawable? = null
    var color: Int? = null

    init {
        dateInFormat = getDateInHumanFormat(date)
        icon =
            when (subType) {
                MyConstantsAAT.VIDEO_FILE_TYPE -> {
                    color = ContextCompat.getColor(context, VideosColor)
                    ContextCompat.getDrawable(context, VideosIcon)
                }
                MyConstantsAAT.AUDIO_FILE_TYPE -> {
                    color = ContextCompat.getColor(context, AudioColor)
                    ContextCompat.getDrawable(context, AudioIcon)
                }
                MyConstantsAAT.IMAGE_FILE_TYPE -> {
                    color = ContextCompat.getColor(context, ImagesColor)
                    ContextCompat.getDrawable(context, ImagesIcon)
                }
                MyConstantsAAT.PDF_FILE_TYPE -> {
                    color = ContextCompat.getColor(context, PdfColor)
                    ContextCompat.getDrawable(context, PdfIcon)
                }
                MyConstantsAAT.WORD_FILE_TYPE -> {
                    color = ContextCompat.getColor(context, WordColor)
                    ContextCompat.getDrawable(context, WordIcon)
                }
                MyConstantsAAT.OTHER_FILE_TYPE -> {
                    color = ContextCompat.getColor(context, OtherColor)
                    ContextCompat.getDrawable(context, Othericon)
                }
                else -> {
                    color = ContextCompat.getColor(context, MediaColor)
                    ContextCompat.getDrawable(context, MediaIcon)
                }
            }
    }


    private fun getDateInHumanFormat(millis: Long): String {
        // Create a DateFormatter object for displaying date in specified format.
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat("dd/MM/yyyy")

        // Create a calendar object that will convert the date and time value in milliseconds to date.

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return formatter.format(calendar.time)
    }
}