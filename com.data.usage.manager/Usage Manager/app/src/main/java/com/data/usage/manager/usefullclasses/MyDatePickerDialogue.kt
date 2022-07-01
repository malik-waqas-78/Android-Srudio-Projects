package com.data.usage.manager.usefullclasses

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.data.usage.manager.interfaces.DateSetListener


class MyDatePickerDialogue(var year:String, var month:String, var day:String,var datesetListener: DateSetListener)
    : DialogFragment() , DatePickerDialog.OnDateSetListener{

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity!!, this, year.toInt(), month.toInt()-1, day.toInt())
    }
    override fun onDateSet(p0: DatePicker?, y: Int, m: Int, d: Int) {
        //save the date in text view starting date
        datesetListener.dateSetListener(y,m,d)
    }
}