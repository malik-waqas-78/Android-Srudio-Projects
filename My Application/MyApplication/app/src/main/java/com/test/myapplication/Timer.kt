package com.test.myapplication

import android.os.Parcel
import android.os.Parcelable

class Timer() :Parcelable{
    var totalTimeInSec=50L
    var timeSpentInSec=0L

    constructor(parcel: Parcel) : this() {
        totalTimeInSec = parcel.readLong()
        timeSpentInSec = parcel.readLong()
    }

    fun addBonusTime(){
        totalTimeInSec+=10L
    }
    fun aSecPassed(){
        timeSpentInSec++
    }
    fun isGameOver():Boolean{
        return timeSpentInSec==totalTimeInSec
    }
    fun getRemainingTime():Long{
        return totalTimeInSec-timeSpentInSec
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(totalTimeInSec)
        parcel.writeLong(timeSpentInSec)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Timer> {
        override fun createFromParcel(parcel: Parcel): Timer {
            return Timer(parcel)
        }

        override fun newArray(size: Int): Array<Timer?> {
            return arrayOfNulls(size)
        }
    }
}