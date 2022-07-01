package com.test.myapplication

import android.os.Parcel
import android.os.Parcelable

enum class Compare{
    LessThan,
    EqualTo,
    GreaterThan
}

class Equation() :Parcelable {
    private var numberOfTerms:Int=2
    var terms= ArrayList<Int>()
    var op=ArrayList<Char>()
    var correctAnswer:Int = 0

    constructor(parcel: Parcel) : this() {
        numberOfTerms = parcel.readInt()
        correctAnswer = parcel.readInt()
    }

    init {
        numberOfTerms=(2..4).random()
        generateEquation()
        validateEquation()
    }

    fun getPrintableEquation():String {

        return when (numberOfTerms) {
            2 -> {
                 "  ${terms[0]} ${op[0]} ${terms[1]}"
            }
            3 -> {
                " ( ${terms[0]} ${op[0]} ${terms[1]} ) ${op[1]} ${terms[2]}"
            }
            4 -> {
                "(( ${terms[0]} ${op[0]} ${terms[1]} ) ${op[1]} ${terms[2]} ) ${op[2]} ${terms[3]} "
            }
            else -> ""
        }

    }



    fun compareLEQWithREQ(rEq:Equation):Compare{
        return when {
            this.correctAnswer==rEq.correctAnswer -> {
               Compare.EqualTo
            }
            this.correctAnswer>=rEq.correctAnswer -> {
                 Compare.GreaterThan
            }
            else -> {
                 Compare.LessThan
            }
        }
    }

    private fun generateEquation(){

        for(i in 0 until numberOfTerms){
            terms.add(i,(1..20).random())
        }

        for(i in 0 until (numberOfTerms-1)){
            op.add(i,getRandomOperator())
        }
    }

    private fun isResultLessThan100(operand1:Int, operand2:Int, operator:Char):Boolean{
       return getResult(operand1,operand2,operator)<=100
    }

    private fun isResultAnInt(operand1:Int, operand2:Int, operator:Char):Boolean{
        return if(operator=='/'){
            operand1 % operand2 == 0
        }else  true
    }

    private fun getRandomOperator():Char{
        var opString="+-*/"
        var opIndex=(0 until 4).random()
        return opString[opIndex]
    }

    private fun validateEquation(){
        var operand1=terms[0]
        var termsIndex=1
        var operatorIndex=0
        while (termsIndex<numberOfTerms){
            var operand2=terms[termsIndex]
            if(isResultAnInt(operand1,operand2,op[operatorIndex])&&
                isResultLessThan100(operand1,operand2,op[operatorIndex])){

                operand1=getResult(operand1,operand2,op[operatorIndex])
                termsIndex++
                operatorIndex++

            }else{
                terms[termsIndex]=(1..20).random()
            }
        }
        correctAnswer=operand1
    }

    private fun getResult(operand1: Int, operand2: Int, op: Char): Int {
        return when(op){
            '+' -> operand1 + operand2
            '-' -> operand1 - operand2
            '*' -> operand1 * operand2
            '/' -> operand1 / operand2
            else -> 0
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(numberOfTerms)
        parcel.writeInt(correctAnswer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Equation> {
        override fun createFromParcel(parcel: Parcel): Equation {
            return Equation(parcel)
        }

        override fun newArray(size: Int): Array<Equation?> {
            return arrayOfNulls(size)
        }
    }


}