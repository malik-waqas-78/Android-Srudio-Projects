package com.phoneclone.data.datautils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.provider.CalendarContract
import android.util.Log
import com.phoneclone.data.interfaces.UtilsCallBacks
import com.phoneclone.data.modelclasses.CalendarEventsModel
import com.phoneclone.data.interfaces.MyInterFace
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class CalendarEventsUtils(var context: Context, var myInterFace: MyInterFace?) {
    companion object{
        var calendarEventsList = ArrayList<CalendarEventsModel>()
    }
    var handlersActionNotifier = context as UtilsCallBacks
    var totalSize = 0

    private val EVENT_PROJECTION: Array<String> = arrayOf(
        CalendarContract.Calendars._ID,                     // 0
    )

    fun getListSize(): Int {
        return calendarEventsList.size
    }


    fun loadData() {
        AsyncTaskToLoadData().execute()

    }

    inner class AsyncTaskToLoadData : AsyncTask<String, Int, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {
            if(calendarEventsList.isEmpty()){
                getCalendarEvents()
            }
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handlersActionNotifier.doneLoadingCalendarsEvents()
        }
    }

    private fun getCalendarEvents() {
        calendarEventsList.clear()
        val uri: Uri = CalendarContract.Calendars.CONTENT_URI
//        val selection: String = "${CalendarContract.Calendars.ACCOUNT_NAME} = ?"
//        val selectionArgs: Array<String> = arrayOf("Phone")
        val cursor: Cursor? = context.contentResolver.query(uri,EVENT_PROJECTION, null, null, null)
        val calendarIds = HashSet<String>()
        try {
            Log.d("92727", "Count=" + cursor!!.getCount())
            if (cursor!!.getCount() > 0) {
                Log.d("92727", "the control is just inside of the cursor.count loop")
                while (cursor!!.moveToNext()) {
                    val _id: String = cursor!!.getString(0)
                    // Log.d("92727", "Id: $_id Display Name: $displayName Selected: $selected")
                    calendarIds.add(_id)
                }
            }
        } catch (ex: AssertionError) {
            ex.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor!!.close()
        }


        // For each calendar, display all the events from the previous week to the end of next week.
        for (id in calendarIds) {

            //Uri.Builder builder = Uri.parse("content://com.android.calendar/calendars").buildUpon();
            val eventCursor: Cursor? = context.contentResolver.query(
                CalendarContract.Events.CONTENT_URI,
                null,
                "${CalendarContract.Events.CALENDAR_ID} = ?",
                arrayOf(id),
                null
            )

            if (eventCursor != null && eventCursor?.count!! > 0) {
                val index_title = eventCursor.getColumnIndex(CalendarContract.Events.TITLE)
                val index_dstart = eventCursor.getColumnIndex(CalendarContract.Events.DTSTART)
                val index_dend = eventCursor.getColumnIndex(CalendarContract.Events.DTEND)
                val index_allDay = eventCursor.getColumnIndex(CalendarContract.Events.ALL_DAY)
                val index_organizer = eventCursor.getColumnIndex(CalendarContract.Events.ORGANIZER)
                val index_location =
                    eventCursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)
                val index_description =
                    eventCursor.getColumnIndex(CalendarContract.Events.DESCRIPTION)
                val index_reminder =
                    eventCursor.getColumnIndex(CalendarContract.Events.ALLOWED_REMINDERS)
                val index_duration = eventCursor.getColumnIndex(CalendarContract.Events.DURATION)
                val index_timezone =
                    eventCursor.getColumnIndex(CalendarContract.Events.EVENT_TIMEZONE)
                Log.d("92727", "eventCursor count=" + eventCursor!!.getCount())
                if (eventCursor.moveToFirst()) {
                    do {
                        val event = CalendarEventsModel()
                        event.title = eventCursor.getString(index_title)
                        event.start = eventCursor.getLong(index_dstart).toString()
                        if (event.start == null || event.start!!.isEmpty()) {
                            continue
                        }
                        event.end = eventCursor.getLong(index_dend).toString()
                        event.all_day = eventCursor.getString(index_allDay).equals("0").toString()
                        event.organizer = eventCursor.getString(index_organizer)
                        event.location = eventCursor.getString(index_location)
                        event.timezone = eventCursor.getString(index_timezone)
                        event.description = eventCursor.getString(index_description)
                        event.reminder = eventCursor.getString(index_reminder)
                        event.duration = eventCursor.getString(index_duration)
                        calendarEventsList.add(event)

                        Log.d("92727", "Title:${event.title}")
                        Log.d("92727", "Begin:${event.start}")
                        Log.d("92727", "End:${event.end}")
                        Log.d("92727", "All Day:${event.all_day}")
                    } while (eventCursor.moveToNext())
                }
            }
            eventCursor!!.close()

        }
    }

}

