package com.data.usage.manager.fragments


import android.os.AsyncTask
import android.os.Build
import android.os.Bundle


import android.util.Log


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView

import androidx.fragment.app.Fragment
import com.data.usage.manager.R
import com.data.usage.manager.core.Speedtest
import com.data.usage.manager.core.config.SpeedtestConfig
import com.data.usage.manager.core.config.TelemetryConfig
import com.data.usage.manager.core.serverSelector.TestPoint
import com.data.usage.manager.modelclasses.SpeedTestModel


import com.github.anastr.speedviewlib.Speedometer
import org.json.JSONArray
import org.json.JSONObject
import params.com.stepprogressview.StepProgressView
import java.io.BufferedReader
import java.io.EOFException
import java.io.InputStreamReader

import java.text.DecimalFormat
import java.util.*


class Fragment_Speed_Data : Fragment() {


    private val TAG: String = "waqas"
    var speedTest: SpeedTestModel = SpeedTestModel()

    lateinit var tvUpload: TextView
    lateinit var tvDownload: TextView
    lateinit var tvPing: TextView
    lateinit var tvHeading: TextView
    lateinit var stepProgressView: StepProgressView
    lateinit var speedoMeter: Speedometer
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_speed, container, false)

        val dec = DecimalFormat("#.##")

        tvUpload = view.findViewById(R.id.uploadspeed)
        tvDownload = view.findViewById(R.id.downloadspeed)
        tvPing = view.findViewById(R.id.tv_ping)
        stepProgressView = view.findViewById(R.id.step_progressbar)
        speedoMeter = view.findViewById(R.id.speedView)
        tvHeading = view.findViewById(R.id.testheader)

        view.findViewById<TextView>(R.id.network_name).isSelected = true
        stepProgressView.currentProgress = 0




        startTest()
        view.findViewById<ImageView>(R.id.mi_refresh).setOnClickListener {
            if (!isTestRunning) {
                startTest()
            }
        }

        return view
    }

    var isTestRunning = false;
    private fun startTest() {
        updateStepProgress(0)
        updatePingTextView((0).toDouble())
        updateDownloadTextView((0).toDouble())
        updateUploadTextView((0).toDouble())
        object : Thread() {
            override fun run() {
                try {
                    sleep(1500)
                } catch (t: Throwable) {
                }
                page_init()
            }
        }.start()
    }

    private var st: Speedtest? = null

    private fun page_init() {
        updateHeading("Test Started")
        SpeedTestTask().execute()
    }


    inner class SpeedTestTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            updateHeading("Finding Server")
            var config: SpeedtestConfig? = null
            var telemetryConfig: TelemetryConfig? = null
            var servers: Array<TestPoint?>? = null
            try {
                var c = readFileFromAssets("SpeedtestConfig.json")
                var o = JSONObject(c)
                config = SpeedtestConfig(o)
                c = readFileFromAssets("TelemetryConfig.json")
                o = JSONObject(c)
                telemetryConfig = TelemetryConfig(o)
                if (telemetryConfig.getTelemetryLevel()
                        .equals(TelemetryConfig.LEVEL_DISABLED)
                ) {

                }
                if (st != null) {
                    try {
                        st?.abort()
                    } catch (e: Throwable) {
                    }
                }
                st = Speedtest()
                st?.setSpeedtestConfig(config)
                st?.setTelemetryConfig(telemetryConfig)
                c = readFileFromAssets("ServerList.json")
                if (c.startsWith("\"") || c.startsWith("'")) { //fetch server list from URL
                    if (st?.loadServerList(c.subSequence(1, c.length - 1).toString()) != true) {
                        throw java.lang.Exception("Failed to load server list")
                    }
                } else {
                    val a = JSONArray(c)
                    if (a.length() == 0) throw java.lang.Exception("No test points")
                    val s: ArrayList<TestPoint> = ArrayList<TestPoint>()
                    for (i in 0 until a.length()) s.add(TestPoint(a.getJSONObject(i)))
                    servers = s.toArray(arrayOfNulls<TestPoint>(0))
                    st?.addTestPoints(servers)
                }
                updateHeading("Server found")
                /* val testOrder: String = config.getTest_order()*/
                /*runOnUiThread {
                    if (!testOrder.contains("D")) {
                        //  hideView(R.id.dlArea)
                    }
                    if (!testOrder.contains("U")) {
                        //   hideView(R.id.ulArea)
                    }
                    if (!testOrder.contains("P")) {
                        //   hideView(R.id.pingArea)
                    }
                    if (!testOrder.contains("I")) {
                        //   hideView(R.id.ipInfo)
                    }
                }*/
            } catch (e: Throwable) {
                System.err.println(e)
                st = null
/*
                    runOnUiThread {

                    }*/
                return null
            }
            updateHeading("Selecting Best Server")
            try {
                st?.selectServer(object : Speedtest.ServerSelectedHandler() {
                    override fun onServerSelected(server: TestPoint?) {
                        updateHeading("Server Selected")
                        if (server == null) {
                            page_init()

                        } else {
                            st?.testPoints?.let { page_serverSelect(server, it) }
                        }

                    }


                })
            } catch (e: Exception) {

            }
            return null
        }

    }


    private fun page_serverSelect(selected: TestPoint?, servers: Array<TestPoint>) {
        //transition(R.id.page_serverSelect, TRANSITION_LENGTH)
        reinitOnResume = true
        val availableServers: ArrayList<TestPoint?> = ArrayList<TestPoint?>()
        for (t in servers) {
            if (t.ping != -1f) availableServers.add(t)
        }
        var selectedId = availableServers.indexOf(selected)
        if (selectedId < 0 || selectedId >= availableServers.size) {
            selectedId = 0;
        }
        if (availableServers.isNotEmpty()) {
            try {
                availableServers[selectedId]?.let { page_test(it) }
            } catch (e: java.lang.Exception) {
                if (availableServers.size > 0)
                    availableServers[0]?.let { page_test(it) }
            }
        }

    }


    private fun page_test(selected: TestPoint) {
        updateHeading("Starting Test")
        st?.setSelectedServer(selected)
        st?.start(object : Speedtest.SpeedtestHandler() {
            override fun onDownloadUpdate(dl: Double, progress: Double) {

                updateHeading("Testing Download Speed")

                Log.d("malik_waqas", "onDownloadUpdate: $dl  $progress")
                dl.format()?.toDouble()?.let { updateDownloadTextView(it) }
                dl.format()?.let { updateSpeed(it.toFloat()) }
            }

            override fun onUploadUpdate(ul: Double, progress: Double) {
                updateHeading("Testing Upload Speed")
                Log.d("malik_waqas", "upload: $ul  $progress")
                ul?.format()?.toDouble()?.let { updateUploadTextView(it) }
                ul?.format()?.toFloat()?.let { updateSpeed(it) }

            }

            override fun onPingJitterUpdate(ping: Double, jitter: Double, progress: Double) {
                updateHeading("Testing Ping")
                Log.d("malik_waqas", "jitter ping: $ping $jitter $progress")
                ping.format()?.toDouble()?.let { updatePingTextView(it) }

            }

            override fun onIPInfoUpdate(ipInfo: String?) {
                updateHeading("Retrieved Ip Info")
                Log.d("malik_waqas", "ip info: $ipInfo")
                if (ipInfo != null) {
                    activity?.runOnUiThread {
                        view?.findViewById<TextView>(R.id.network_name)?.text = ipInfo
                    }
                    updateHeading(ipInfo)
                }

            }

            override fun onTestIDReceived(id: String?, shareURL: String?) {
                if (shareURL == null || shareURL.isEmpty() || id == null || id.isEmpty()) return
                activity?.runOnUiThread {
                }
            }

            override fun onEnd() {
                updateHeading("Test Finished")
                isTestRunning = false
                activity?.runOnUiThread {
                    stepProgressView.currentProgress = 100
                }
                updateSpeed(0f)
            }

            override fun onCriticalFailure(err: String?) {
                activity?.runOnUiThread {
                    Log.d("malik_waqas", "onDownloadUpdate: failed $err")

                }
            }
        })
    }

    private fun Double.format(): String? {
        var l: Locale? = null
        l = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            resources.configuration.locale
        }
        if (this < 10) return String.format(l, "%.2f", this)
        return if (this < 100) String.format(l, "%.1f", this) else "" + Math.round(this)
    }


    @Throws(java.lang.Exception::class)
    private fun readFileFromAssets(name: String): String {
        val b = BufferedReader(InputStreamReader(context?.assets?.open(name)))
        var ret = ""
        try {
            while (true) {
                val s = b.readLine() ?: break
                ret += s
            }
        } catch (e: EOFException) {
        }
        return ret
    }


    private var reinitOnResume = false
    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            st?.abort()
        } catch (t: Throwable) {
        }
    }

    private fun updateStepProgress(i: Int) {
        activity?.runOnUiThread {
            stepProgressView.currentProgress = i
        }
    }

    private fun updateHeading(s: String) {
        activity?.runOnUiThread {
            tvHeading.text = s
        }
    }

    fun updateStepProgress() {
        activity?.runOnUiThread {

        }
    }


    private fun updateSpeed(speed: Float) {
        activity?.runOnUiThread {
            if (speed >= (speedoMeter.maxSpeed - 2))
                speedoMeter.maxSpeed = speedoMeter.maxSpeed + 10
            speedoMeter.speedTo(speed, 2000)
        }
    }

    fun updateUploadTextView(rate: Double) {

        if (rate == (0).toDouble()) {
            return
        }

        activity?.runOnUiThread {
            if ((stepProgressView.currentProgress + 1) < (99 - 4)) {
                updateStepProgress(stepProgressView.currentProgress + 1)
            }
            tvUpload.text = rate.toString() + " Mb/s"
        }
    }

    fun updateDownloadTextView(rate: Double) {
        activity?.runOnUiThread {
            if ((stepProgressView.currentProgress + 1) < (66 - 4)) {
                updateStepProgress(stepProgressView.currentProgress + 1)
            }
            tvDownload.text = rate.toString() + " Mb/s"
        }
    }

    fun updatePingTextView(rate: Double) {
        activity?.runOnUiThread {
            tvPing.text = "Ping : " + rate.toString() + " ms"
        }
    }


}




