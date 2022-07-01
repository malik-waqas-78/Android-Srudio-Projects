package com.test.myapplication

import android.os.Parcel
import android.os.Parcelable

class Result() :Parcelable{
    var totalQuestions: Int = 0
    var totalCorrectAnswers: Int = 0
    var countAnswersForBonus: Int= 0

    constructor(parcel: Parcel) : this() {
        totalQuestions = parcel.readInt()
        totalCorrectAnswers = parcel.readInt()
        countAnswersForBonus = parcel.readInt()
    }

    private fun resetCountAnswersForBonus(){
        countAnswersForBonus=0
    }
    fun answerIsCorrect(){
        totalCorrectAnswers++
        countAnswersForBonus++
    }
    fun isBonusAvailable():Boolean{
        if(countAnswersForBonus==5){
            resetCountAnswersForBonus()
            return true
        }
        return false
    }
    fun countQuestions(){
        totalQuestions++
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(totalQuestions)
        parcel.writeInt(totalCorrectAnswers)
        parcel.writeInt(countAnswersForBonus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Result> {
        override fun createFromParcel(parcel: Parcel): Result {
            return Result(parcel)
        }

        override fun newArray(size: Int): Array<Result?> {
            return arrayOfNulls(size)
        }
    }


}
