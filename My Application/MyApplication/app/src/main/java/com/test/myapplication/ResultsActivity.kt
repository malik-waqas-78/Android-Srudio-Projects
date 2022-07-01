package com.test.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        if(intent!=null ){
            var totalQuestions=intent.getIntExtra("totalAnswers",0)
            var correctQuestions=intent.getIntExtra("correctAnswers",0)
            findViewById<TextView>(R.id.tvTotalQuestions).text="Total Questions : $totalQuestions"
            findViewById<TextView>(R.id.tvCorrectANswers).text="Correct Answers : $correctQuestions"
            findViewById<TextView>(R.id.tvWrongAnswers).text="Wrong Answers : ${totalQuestions-correctQuestions}"
        }
    }
}