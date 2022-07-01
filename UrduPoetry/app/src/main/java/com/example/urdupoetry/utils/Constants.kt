package com.example.urdupoetry.utils

import com.example.urdupoetry.modelclasses.Poetry

class Constants {
    companion object{
        val poetryTypes= arrayOf("آنسو","اداس","انتظار","بارش","بدنام",
        "پیسہ","تنہائی","جدائ","خوبصورتی","خودی","درد","دکھی","دھوکہ",
            "دوستی","زندگی","غم","محبت","نفرت","ہمسفر","یاد")
        val poetries:HashMap<String,ArrayList<Poetry>> = HashMap<String,ArrayList<Poetry>>(20)

    }
}