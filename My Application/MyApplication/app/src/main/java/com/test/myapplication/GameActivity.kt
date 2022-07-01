package com.test.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit


class GameActivity : AppCompatActivity() {

    var tvTime: TextView? = null
    var tvLEQ:TextView? = null
    var tvREQ:TextView? = null
    var btnGreater: Button? = null
    var btnLess:Button?=null
    var btnEqual:Button?=null

    var lEq:Equation?=null
    var rEq:Equation?=null
    var timer:Timer?=null
    var results:Result?=null

    private var mHandler: Handler? = null
    private val mInterval = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tvTime=findViewById(R.id.tvTime)
        tvLEQ=findViewById(R.id.tvLEQ)
        tvREQ=findViewById(R.id.tvREQ)
        btnGreater=findViewById(R.id.btnGreater)
        btnLess=findViewById(R.id.btnLess)
        btnEqual=findViewById(R.id.btnEqual)


        if(savedInstanceState==null) {
            results = Result()
            timer = Timer()
            askAQuestion()
            initStopWatch()
        }else{
            lEq=savedInstanceState.getParcelable("leq")
            rEq=savedInstanceState.getParcelable("req")
            tvLEQ?.text=lEq?.getPrintableEquation()
            tvREQ?.text=rEq?.getPrintableEquation()
            results=savedInstanceState.getParcelable("result")
            timer=savedInstanceState.getParcelable("timer")
            tvTime?.text = "${getFormattedStopWatch(timer?.getRemainingTime()?.times(1000) ?:0)}"
            startTimer()
        }
        btnGreater?.setOnClickListener {
            if(lEq?.compareLEQWithREQ(rEq!!)==Compare.GreaterThan){
                results?.answerIsCorrect()
                showToast(this,"Correct","00FF00")
                if(results?.isBonusAvailable()==true){
                    //Toast.makeText(this,"Bonus added",Toast.LENGTH_SHORT).show()
                    timer?.addBonusTime()
                }
            }else{
                showToast(this,"Wrong","FF0000")
            }
            askAQuestion()

        }


        btnLess?.setOnClickListener {
            if(lEq?.compareLEQWithREQ(rEq!!)==Compare.LessThan){
                results?.answerIsCorrect()
                showToast(this,"Correct","00FF00")
                if(results?.isBonusAvailable()==true){
                    //Toast.makeText(this,"Bonus added",Toast.LENGTH_SHORT).show()
                    timer?.addBonusTime()
                }
            }else{
                showToast(this,"Wrong","FF0000")
            }
            askAQuestion()
        }
        btnEqual?.setOnClickListener {
            if(lEq?.compareLEQWithREQ(rEq!!)==Compare.EqualTo){
                results?.answerIsCorrect()
                showToast(this,"Correct","00FF00")
                if(results?.isBonusAvailable()==true){
                    //Toast.makeText(this,"Bonus added",Toast.LENGTH_SHORT).show()
                    timer?.addBonusTime()
                }
            }else{
                showToast(this,"Wrong","FF0000")
            }
            askAQuestion()
        }



    }
    private fun initStopWatch() {
        tvTime?.text = "${getFormattedStopWatch(timer?.getRemainingTime()?.times(1000) ?:0)}"
        startTimer()
    }
    private fun startTimer() {
        mHandler = Handler(Looper.getMainLooper())
        mStatusChecker.run()
    }
    private fun stopTimer() {
        mHandler?.removeCallbacks(mStatusChecker)
    }

    private fun showToast(context: Context?, info: String, color:String) {
        val toast = Toast.makeText(
            context,
            Html.fromHtml("<font color='#$color' ><b>$info</b></font>"),
            Toast.LENGTH_SHORT
        )
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    private var mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                timer?.aSecPassed()
                updateStopWatchView()
            } finally {
                if(timer?.isGameOver()==true){
                    stopTimer()
                    val intent = Intent(this@GameActivity,ResultsActivity::class.java)
                    intent.putExtra("totalAnswers",results?.totalQuestions)
                    intent.putExtra("correctAnswers",results?.totalCorrectAnswers)
                    startActivity(intent)
                    finish()
                }else {
                    mHandler?.postDelayed(this, mInterval.toLong())
                }
            }

        }
    }

    override fun onDestroy() {
        stopTimer()
        super.onDestroy()

    }
    private fun updateStopWatchView() {
        val formattedTime = getFormattedStopWatch((timer?.getRemainingTime()?.times(1000)))

        tvTime?.text = formattedTime
    }
    private fun askAQuestion() {
        lEq=Equation()
        rEq=Equation()
        tvLEQ?.text=lEq?.getPrintableEquation()
        tvREQ?.text=rEq?.getPrintableEquation()
        results?.countQuestions()
    }
    private fun getFormattedStopWatch(ms: Long?): String {
        if(ms==null){
            return "00:00:00"
        }
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        return "${if (hours < 10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("result",results)
        outState.putParcelable("timer",timer)
        outState.putParcelable("leq",lEq)
        outState.putParcelable("req",rEq)

        super.onSaveInstanceState(outState)
    }
}