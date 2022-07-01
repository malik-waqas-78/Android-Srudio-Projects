package com.phoneclone.data.modelclasses

import com.phoneclone.data.constants.MyConstants
import java.io.Serializable

class CalendarEventsModel :Serializable{

    var isSelected=false

    var organizer:String?=""

    var title:String?=""

    var location:String?=""

    var all_day:String?=""

    var start:String?=""

    var end:String?=""

    var timezone:String?=""

    var description:String?=""

    var reminder:String?=""

    var duration:String?=""

    var fileType=MyConstants.FILE_TYPE_CALENDAR

    companion object{
        val TITLE_INDEX=0
        val ORG_INDEX=1
        val LOC_INDEX=2
        val ALL_DAY_INDEX=3
        val START_INDEX=4
        val END_INDEX=5
        val TIME_ZONE_INDEX=6
        val DESCRIPTION_INDEX=7
        val REMINDER_INDEX=8
        val DURATION_INDEX=9
    }

}