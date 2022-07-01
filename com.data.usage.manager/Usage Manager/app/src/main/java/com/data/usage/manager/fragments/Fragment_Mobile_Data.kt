package com.data.usage.manager.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.data.usage.manager.myadapters.MyAdapter
import com.data.usage.manager.activities.SettingsActivity
import com.data.usage.manager.constants.Constants
import com.data.usage.manager.interfaces.DataUsageInterface
import com.data.usage.manager.interfaces.DialogueClickListner
import com.data.usage.manager.interfaces.InteractWithUi
import com.data.usage.manager.modelclasses.AppsInfo_ModelClass
import com.data.usage.manager.R
import com.data.usage.manager.sharedpreferences.MySharedPreferences
import com.data.usage.manager.usefullclasses.MyAppPermissions
import com.data.usage.manager.usefullclasses.MyDialogueBoxes
import com.data.usage.manager.usefullclasses.NetworkStatsHelper
import com.data.usage.manager.adds.IntersitialAdds
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Fragment_Mobile_Data : Fragment(), DataUsageInterface, DialogueClickListner {
    //init views
    private val TAG = "92727"
    lateinit var pbar_dataConsumed: ProgressBar
    lateinit var pbar_todayUsage: ProgressBar
    lateinit var pbar_totalDataPlan: ProgressBar
    lateinit var txt_dataConsumed: TextView
    lateinit var txt_todayUsage: TextView
    lateinit var txt_totaldataplan: TextView
    lateinit var rc_appsDataUsage: RecyclerView
    lateinit var pbar_allAppsDataUsage: ProgressBar

    var allAppsInfo = ArrayList<AppsInfo_ModelClass>()
    lateinit var myAdapter: MyAdapter
    var loading = false;
    var tv_appProgress: TextView? = null
    var appsCount = 0
    lateinit var interactWithUi: InteractWithUi
    lateinit var dialogueBoxes: MyDialogueBoxes

    lateinit var myAppPermissions: MyAppPermissions
    lateinit var mySharedPreferences: MySharedPreferences
    lateinit var myDialogueBoxes: MyDialogueBoxes
    var cl_nodata: ConstraintLayout? = null
    var cl_appsdata: ConstraintLayout? = null

    //
    var myView: View? = null
    var networkStatsHelper1: NetworkStatsHelper? = null
    var networkStatsHelper2: NetworkStatsHelper? = null
    var handler: Handler? = null
    var r: Runnable? = null

    var noofbytes: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_datausage, container, false)
        //reference views
        loading = false
        mySharedPreferences = MySharedPreferences(context!!)
        myAppPermissions = MyAppPermissions(
            appContext = context!!.applicationContext,
            activityContext = activity!!
        )
        myDialogueBoxes = MyDialogueBoxes(activity!!, context!!, layoutInflater)
        allAppsInfo.clear()
        pbar_dataConsumed = view.findViewById(R.id.pbar_dataconsumed)
        pbar_todayUsage = view.findViewById(R.id.pbar_todayusage)
        pbar_totalDataPlan = view.findViewById(R.id.pbar_totaldataplan)
        txt_dataConsumed = view.findViewById(R.id.txt_dataconsumed)
        txt_todayUsage = view.findViewById(R.id.txt_todayusage)
        txt_totaldataplan = view.findViewById(R.id.txt_totaldataplan)
        rc_appsDataUsage = view.findViewById(R.id.rc_appdatausage)
        pbar_allAppsDataUsage = view.findViewById(R.id.pbar_appdatausage)
        cl_appsdata = view.findViewById(R.id.cl_appsdata)
        cl_nodata = view.findViewById(R.id.cl_nodataavailable)
        cl_nodata?.visibility = View.INVISIBLE
        cl_appsdata?.visibility = View.INVISIBLE
        tv_appProgress = view.findViewById(R.id.tv_progressapps)
        tv_appProgress?.setText("0%\n\nLoading Apps Data Usage")
        var linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rc_appsDataUsage.layoutManager = linearLayoutManager
        dialogueBoxes = MyDialogueBoxes(activity!!, context!!, inflater)
        val iv_refresh = view.findViewById<ImageView>(R.id.mi_refresh)
        iv_refresh.setOnClickListener {
            if (!loading) {
                cl_nodata?.visibility = View.INVISIBLE
                cl_appsdata?.visibility = View.INVISIBLE
                allAppsInfo.clear()
                load(view)
            }
        }
        val iv_settings = view.findViewById<ImageView>(R.id.mi_settings)
        iv_settings.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            context?.startActivity(intent)
        }

        val txt_setdataplan = view.findViewById<TextView>(R.id.txt_setdataplan)
        txt_setdataplan.setOnClickListener {
            interactWithUi.goToPlanActivity()
        }
        myAdapter = MyAdapter(context!!, allAppsInfo)

        rc_appsDataUsage.adapter = myAdapter
        r = Runnable {
            load(view)
        }
        handler = Handler()
        handler?.postDelayed(r, 1400)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        IntersitialAdds.showAdd()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

//    override fun onDetach() {
//        super.onDetach()
//        Log.d(TAG, "onDetach: ")
//        networkStatsHelper1?.clearEveryThing()
//        networkStatsHelper2?.clearEveryThing()
//    }

    override fun onStop() {
        Log.d(TAG, "onStop: ")
        handler?.removeCallbacksAndMessages(r)
        networkStatsHelper1?.clearEveryThing()
        networkStatsHelper2?.clearEveryThing()
        super.onStop()
    }

    private fun load(view: View) {
        if (activity == null)
            return

        var mySharedPreferences = MySharedPreferences(activity!!)

        cl_appsdata?.visibility = View.INVISIBLE
        pbar_allAppsDataUsage.visibility = View.VISIBLE
        tv_appProgress?.visibility = View.VISIBLE

        if (mySharedPreferences.isDataPlanSet()) {
            val txt_dctext = view.findViewById<TextView>(R.id.wifiornot)
            txt_dctext.setText("Data Consumed")
            txt_totaldataplan.setText(convertToproperStorageType(mySharedPreferences.getDataPlan()?.dataLimitBytes))
            pbar_totalDataPlan.visibility = View.INVISIBLE
            txt_totaldataplan.visibility = View.VISIBLE
            var x = mySharedPreferences.getSimSlot()
            x += 1
            view.findViewById<TextView>(R.id.tv_datausage).setText("Data Usage SIM ${x}")
            if (!loading)
                loadStats()
        } else {
            val txt_dctext = view.findViewById<TextView>(R.id.wifiornot)
            txt_dctext.setText("Monthly Usage")
            pbar_totalDataPlan.visibility = View.INVISIBLE
            txt_totaldataplan.visibility = View.VISIBLE
            txt_totaldataplan.text = "0.0 KB"
            var x = mySharedPreferences.getSimSlot()
            x += 1
            view.findViewById<TextView>(R.id.tv_datausage).setText("Data Usage SIM ${x}")
            if (!loading)
                loadNormalStats()
        }
    }

    private fun getProperDate(date: Int, month: Int, year: Int): String {
        var d = ""
        var m = ""
        var y = ""
        if (date <= 9) {
            d = "0$date"
        } else {
            d = "$date"
        }
        var mm = month
        mm += 1
        if (mm <= 9) {
            m = "0$mm"
        } else {
            m = "$mm"
        }
        if (year <= 9) {
            y = "0$year"
        } else {
            y = "$year"
        }

        return "$d/$m/$y"
    }

    private fun loadNormalStats() {
        loading = true
        allAppsInfo.clear()
        MyAdapter.total_noofBytes = 0.0
        //Total data consumed by the device this month

        var b = checkForPermisions()
        if (b) {
            if (context == null || activity == null)
                return
            networkStatsHelper2 = NetworkStatsHelper(
                appContext = context!!.applicationContext,
                activityContext = activity!!
            )
            networkStatsHelper2?.setNetworktype(Constants.CELLULAR_NETWORK)

            var date = 1
            var month = Calendar.getInstance().get(Calendar.MONTH)
            var year = Calendar.getInstance().get(Calendar.YEAR)
            //####//
            var starting_date = getProperDate(date, month, year)
            Log.d(TAG, "onCreateView:$starting_date")
            networkStatsHelper2?.startDate =
                getDateinMilies("$starting_date 00:00:00")//pass today date
            networkStatsHelper2?.endDate = System.currentTimeMillis()//pass current time
            networkStatsHelper2?.setQuerry(Constants.QUERY_SUMMERY_FOR_DEVICE)
            networkStatsHelper2?.getDeviceDataUsage(this)//it will return today data used
//            networkStatsHelper2?.getRemovedAppsDataUsage(this)
//            networkStatsHelper2?.getTethringDataUsage(this)
//            networkStatsHelper2?.getDataUsedBySystemApps(this)
            networkStatsHelper2?.getDataUsedByAllTheApps(this)
        }
        //Total data consumed by the device form today 00:00:00 Am to till now
        if (b) {
            networkStatsHelper1 = NetworkStatsHelper(
                appContext = context!!.applicationContext,
                activityContext = activity!!
            )
            networkStatsHelper1?.setNetworktype(Constants.CELLULAR_NETWORK)
            var date = Calendar.getInstance().get(Calendar.DATE)
            var month = Calendar.getInstance().get(Calendar.MONTH)
            var year = Calendar.getInstance().get(Calendar.YEAR)
            var starting_date = getProperDate(date, month, year)
            networkStatsHelper1?.startDate =
                getDateinMilies("$starting_date 00:00:00")//pass data plan starting date
            networkStatsHelper1?.endDate = System.currentTimeMillis()//pass current time
            networkStatsHelper1?.setQuerry(Constants.QUERRY_TODAY_DATA_USAGE)
            networkStatsHelper1?.getDeviceDataUsage(this)

        } else {
            dialogueBoxes.systemLevelPermissionsWarning(
                "Permission Required",
                "This Application needs Usage Access Permission to perform data Monitoring.\nPlz Allow Usage Access Permission to continue!!!"
            )

        }
    }

    private fun loadStats() {
        loading = true;
        allAppsInfo.clear()
        MyAdapter.total_noofBytes = 0.0
        var mySharedPreferences = MySharedPreferences(activity!!)
        //Total data consumed by the device form today 00:00:00 Am to till now
        if (checkForPermisions()) {
            if (activity == null || context == null)
                return
            val networkStatsHelper = NetworkStatsHelper(
                appContext = context!!.applicationContext,
                activityContext = activity!!
            )
            networkStatsHelper.setNetworktype(Constants.CELLULAR_NETWORK)
            //get Today's Date
            var date = Calendar.getInstance().get(Calendar.DATE)
            var month = Calendar.getInstance().get(Calendar.MONTH)
            var year = Calendar.getInstance().get(Calendar.YEAR)
            //####//
            val starting_date = getProperDate(date, month, year)
            Log.d(TAG, "onCreateView: $starting_date")
            networkStatsHelper.startDate =
                getDateinMilies("$starting_date 00:00:00")//pass today date
            networkStatsHelper.endDate = System.currentTimeMillis()//pass current time
            networkStatsHelper.setQuerry(Constants.QUERRY_TODAY_DATA_USAGE)
            networkStatsHelper.getDeviceDataUsage(this)//it will return today data used
        } else {
            dialogueBoxes.systemLevelPermissionsWarning(
                "Permission Required",
                "This Application needs Usage Access Permission to perform data Monitoring. Plz Allow Usage Access Permission to continue!!!"
            )
        }
        //Total data consumed by the device form startiting date of plan to till now
        if (checkForPermisions()) {
            if (activity == null || context == null)
                return
            val networkStatsHelper = NetworkStatsHelper(
                appContext = context!!.applicationContext,
                activityContext = activity!!
            )
            networkStatsHelper.setNetworktype(Constants.CELLULAR_NETWORK)
            var starting_date = mySharedPreferences.getDataPlan()?.planStartingDate
            networkStatsHelper.startDate =
                getDateinMilies("$starting_date 00:00:00")//pass data plan starting date
            networkStatsHelper.endDate = System.currentTimeMillis()//pass current time
            networkStatsHelper.setQuerry(Constants.QUERY_SUMMERY_FOR_DEVICE)
            networkStatsHelper.getDeviceDataUsage(this)
//            networkStatsHelper.getRemovedAppsDataUsage(this)
//            networkStatsHelper.getTethringDataUsage(this)
//            networkStatsHelper.getDataUsedBySystemApps(this)
            networkStatsHelper.getDataUsedByAllTheApps(this)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        interactWithUi = context as InteractWithUi
    }


    private fun checkForPermisions(): Boolean {
        if (activity == null || context == null)
            return false
        val permissions: MyAppPermissions = MyAppPermissions(
            appContext = context!!.applicationContext,
            activityContext = activity!!
        )
        if (permissions.requestForRunTimePermissions()) {
            return permissions.isSystemLevelPermissionGiven()
        }
        return false
    }

    private fun getDateinMilies(date: String?): Long {
        var milliseconds: Long = 0;
        var f = SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            var d = f.parse(date);
            milliseconds = d.getTime();
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return milliseconds
    }

    override fun preDataConsumedByDevice() {
        pbar_dataConsumed.visibility = View.VISIBLE
        txt_dataConsumed.visibility = View.INVISIBLE
    }

    override fun postDataConsumedByDevice(retrivedData: HashMap<String, String>) {
        pbar_dataConsumed.visibility = View.INVISIBLE
        txt_dataConsumed.visibility = View.VISIBLE
        val s = retrivedData.get(Constants.TOTAL_DATA_USAGE_PER_DEVICE)
        val ss1 = SpannableString(s)
        ss1.setSpan(RelativeSizeSpan(0.6f), s!!.length - 2, s!!.length, 0) // set size
        Log.d(TAG, "postDataConsumdByDevice: $ss1")
        txt_dataConsumed.text = ss1
        //noofbytes=retrivedData?.get(Constants.TOTAL_NOOF_BYTES_USED)

    }

    override fun preTodayDataUsage() {
        pbar_todayUsage.visibility = View.VISIBLE
        txt_todayUsage.visibility = View.INVISIBLE
    }

    override fun postTodayDataUsage(retrivedData: HashMap<String, String>) {
        pbar_todayUsage.visibility = View.INVISIBLE
        txt_todayUsage.visibility = View.VISIBLE
        val s = retrivedData.get(Constants.TOTAL_DATA_USAGE_PER_DEVICE)
        val ss1 = SpannableString(s)
        ss1.setSpan(RelativeSizeSpan(0.6f), s!!.length - 2, s!!.length, 0) // set size
        txt_todayUsage.text = ss1
    }

    override fun preDataConsumedByApp() {
        cl_appsdata?.visibility = View.INVISIBLE
        pbar_allAppsDataUsage.visibility = View.VISIBLE
        tv_appProgress?.visibility = View.VISIBLE
    }

    override fun postDataConsumedByApp(
        retrievedData: java.util.HashMap<String, String>,
        appsInfo_modelClass: AppsInfo_ModelClass?,
        index: Int,
        size: Int
    ) {

        var count = index + 1
        var percent = (count.times(100)).div(appsCount)
        //Toast.makeText(context,"ac $appsCount p $percent c $count",Toast.LENGTH_SHORT).show()
        var length = percent.toString().length
        val ss1 = SpannableString(
            "$percent%\n" +
                    "\n" +
                    "Loading Apps Data Usag"
        )
        ss1.setSpan(RelativeSizeSpan(2f), 0, length, 0) // set size
        tv_appProgress?.setText("$ss1")

        if (retrievedData.get(Constants.TOTAL_DATA_USAGE_PER_APP).equals("0.00 B")) {
            if (size - 1 == index) {
                tv_appProgress!!.visibility = View.INVISIBLE
                sortAppsOnthebasisofDataUsage()
                rc_appsDataUsage.visibility = View.VISIBLE
                pbar_allAppsDataUsage.visibility = View.GONE
                if (allAppsInfo.isEmpty()) {
                    cl_nodata?.visibility = View.VISIBLE
                    if (cl_appsdata != null) {
                        cl_appsdata!!.visibility = View.INVISIBLE
                    }
                } else {
                    cl_nodata?.visibility = View.INVISIBLE
                    if (cl_appsdata != null) {
                        cl_appsdata!!.visibility = View.VISIBLE
                    }
                }
                val bytesused = retrievedData.get(Constants.TOTAL_NOOF_BYTES_USED)
                noofbytes = bytesused
                /* val dataConsumed=txt_dataConsumed.text.toString()*/
                MyAdapter.total_noofBytes =
                    MyAdapter.total_noofBytes.plus(noofbytes?.toDouble()!!)!!
//                    getTotalNoOfBytes(dataConsumed)
                myAdapter.notifyItemRangeChanged(0, allAppsInfo.size)
                myAdapter.notifyDataSetChanged()
                loading = false;
            }
            return;
        }
        if (size - 1 == index) {
            tv_appProgress!!.visibility = View.INVISIBLE
            allAppsInfo.add(appsInfo_modelClass!!)
            sortAppsOnthebasisofDataUsage()
            rc_appsDataUsage.visibility = View.VISIBLE
            pbar_allAppsDataUsage.visibility = View.INVISIBLE
            if (allAppsInfo.isEmpty()) {
                cl_nodata?.visibility = View.VISIBLE
                cl_appsdata?.visibility = View.GONE
            } else {
                cl_nodata?.visibility = View.INVISIBLE
                if (cl_appsdata != null) {
                    cl_appsdata!!.visibility = View.VISIBLE
                }
            }
            val bytesused = retrievedData.get(Constants.TOTAL_NOOF_BYTES_USED)
            noofbytes = bytesused
            /* val dataConsumed=txt_dataConsumed.text.toString()*/
            MyAdapter.total_noofBytes = MyAdapter.total_noofBytes.plus(noofbytes?.toDouble()!!)!!
//                    getTotalNoOfBytes(dataConsumed)
            myAdapter.notifyItemRangeChanged(0, allAppsInfo.size)
            myAdapter.notifyDataSetChanged()
            loading = false;
        } else {

            val bytesused = retrievedData.get(Constants.TOTAL_NOOF_BYTES_USED)
            noofbytes = bytesused
            /* val dataConsumed=txt_dataConsumed.text.toString()*/
            MyAdapter.total_noofBytes = MyAdapter.total_noofBytes.plus(noofbytes?.toDouble()!!)!!
//                    getTotalNoOfBytes(dataConsumed)
            // myAdapter.notifyDataSetChanged()

            allAppsInfo.add(appsInfo_modelClass!!)
        }


    }

    /* private fun getTotalNoOfBytes(dataConsumed: String?): Double {
         val type=dataConsumed?.substring(dataConsumed!!.length-2,dataConsumed?.length)
        // Toast.makeText(context,"${type}tt",Toast.LENGTH_SHORT).show()
         if(type.equals("GB")){
             return convertGbToBytes(dataConsumed?.substring(0,dataConsumed!!.length-3))
         }else if(type.equals("MB")){
             return convertMbToBytes(dataConsumed?.substring(0,dataConsumed!!.length-3))
         }else if(type.equals("KB")){
             return convertKbToBytes(dataConsumed?.substring(0,dataConsumed!!.length-3))
         }
         return 0.0
     }*/

    /*   private fun convertKbToBytes(substring: String?): Double {
           return try{
               (substring!!.toFloat()*1024.0).toDouble()
           }catch (exception:NumberFormatException){
               0.0
           }
       }

       private fun convertMbToBytes(substring: String?): Double {
           return try{
               (substring!!.toFloat()*1024.0*1000.0).toDouble()
           }catch (exception:NumberFormatException){
               0.0
           }
       }

       private fun convertGbToBytes(substring: String?): Double {
           return try{
               (substring!!.toFloat()*1024.0*1000.0*1000.0).toDouble()
           }catch (exception:NumberFormatException){
               0.0
           }
       }*/

    override fun preTethringDataUsage() {
        pbar_dataConsumed.visibility = View.VISIBLE
        txt_dataConsumed.visibility = View.INVISIBLE
    }

    override fun postTethringDataUsage(retrivedData: HashMap<String, String>) {
        pbar_dataConsumed.visibility = View.INVISIBLE
        txt_dataConsumed.visibility = View.VISIBLE
        Log.d(TAG, "postTethringDataUsage: ${retrivedData.get(Constants.TOTAL_DATA_USAGE)}")
        Log.d(TAG, "postTethringDataUsage: ${retrivedData.get(Constants.TOTAL_DATA_USAGE)}")
        //val s = addTheDataUsage(txt_dataConsumed.text.toString(),retrivedData.get(Constants.TOTAL_NOOF_BYTES_USED))
        //  val ss1 = SpannableString(s)
        // ss1.setSpan(RelativeSizeSpan(0.6f), s!!.length-2, s!!.length, 0) // set size
        // Log.d(TAG, "postDataConsumdByDevice: $ss1")
        // txt_dataConsumed.text=ss1
    }

    /*private fun addTheDataUsage(pre: String, new: String?) : String{
        var newpre=pre.substring(0, pre.length - 3)
        var type=pre.substring(pre.length - 3, pre.length - 1)
        if(type==Constants.GB_DATA_TYPE){
            var prebytes=newpre.toDouble().times(1024000000.0)
            prebytes=new!!.toDouble()+prebytes
            return convertToproperStorageType(prebytes)
        }else{
            var prebytes=newpre.toDouble().times(1024000.0)
            prebytes=new!!.toDouble()+prebytes

            return convertToproperStorageType(prebytes)
        }
    }*/

    override fun preRemovedAppsDataUsage() {
        pbar_dataConsumed.visibility = View.VISIBLE
        txt_dataConsumed.visibility = View.INVISIBLE
    }

    override fun postRemovedAppsDataUsage(retrivedData: HashMap<String, String>) {
        pbar_dataConsumed.visibility = View.INVISIBLE
        txt_dataConsumed.visibility = View.VISIBLE
        Log.d(TAG, "postRemovedAppsDataUsage: ${retrivedData.get(Constants.TOTAL_DATA_USAGE)}")
        Log.d(TAG, "postTethringDataUsage: ${retrivedData.get(Constants.TOTAL_DATA_USAGE)}")
//        val s = addTheDataUsage(txt_dataConsumed.text.toString(),retrivedData.get(Constants.TOTAL_NOOF_BYTES_USED))
//        val ss1 = SpannableString(s)
//        ss1.setSpan(RelativeSizeSpan(0.6f), s!!.length-2, s!!.length, 0) // set size
//        Log.d(TAG, "postDataConsumdByDevice: $ss1")
//        txt_dataConsumed.text=ss1
    }

    override fun preSystemAppsDataUsed() {
        pbar_dataConsumed.visibility = View.VISIBLE
        txt_dataConsumed.visibility = View.INVISIBLE
    }

    override fun postSystemAppsDataUsed(retrivedData: HashMap<String, String>) {
        pbar_dataConsumed.visibility = View.INVISIBLE
        txt_dataConsumed.visibility = View.VISIBLE
        Log.d(TAG, "postSystemAppsDataUsed: ${retrivedData.get(Constants.TOTAL_DATA_USAGE)}")
        Log.d(TAG, "postTethringDataUsage: ${retrivedData.get(Constants.TOTAL_DATA_USAGE)}")
//        val s = addTheDataUsage(txt_dataConsumed.text.toString(),retrivedData.get(Constants.TOTAL_NOOF_BYTES_USED))
//        val ss1 = SpannableString(s)
//        ss1.setSpan(RelativeSizeSpan(0.6f), s!!.length-2, s!!.length, 0) // set size
//        Log.d(TAG, "postDataConsumdByDevice: $ss1")
//        txt_dataConsumed.text=ss1
    }

    override fun totalNoOfPackages(count: Int) {
        appsCount = count
        //Toast.makeText(context,"$appsCount",Toast.LENGTH_SHORT).show()
        tv_appProgress?.setText("0%\n\nLoading Apps Data Usage")
    }

    fun convertToproperStorageType(data: Double?): String {
        var d: Double = 0.0
        if (data!! >= (1024.0 * 1000.0 * 1000.0 * 1000.0)) {
            d = data / (1024.0 * 1000.0 * 1000.0 * 1000.0);
            return String.format("%.2f", d) + " TB";
        }
        if (data!! >= (1024.0 * 1000.0 * 1000.0)) {
            d = data / (1024.0 * 1000.0 * 1000.0)
            return String.format("%.2f", d) + " GB";
        }
        if (data!! >= (1024.0 * 1000.0)) {
            d = data / (1024.0 * 1000.0);
            return String.format("%.2f", d) + " MB";
        }
        if (data!! >= 1024.0) {
            d = data / 1024.0;
            return String.format("%.2f", d) + " KB";
        }
        return String.format("%.2f", d) + " B";
    }


    private fun sortAppsOnthebasisofDataUsage() {
        for (i in 0 until allAppsInfo.size) {
            for (j in i + 1 until allAppsInfo.size) {
                if (allAppsInfo.get(i).getTotalDataUsed()!! < allAppsInfo.get(j)
                        .getTotalDataUsed()!!
                ) {
                    var temp = allAppsInfo.get(i);
                    allAppsInfo.set(i, allAppsInfo.get(j));
                    allAppsInfo.set(j, temp);
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        if(requestCode==9876||requestCode==Constants.REQUEST_LOCATION_TURNON){
//            super.onRequestPermissionsResult(requestCode,permissions,grantResults)
//            return
//        }
        if (grantResults.size == 0 || permissions.size == 0)
            return
        else if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (!myAppPermissions.isSystemLevelPermissionGiven()) {
                dialogueBoxes.systemLevelPermissionsWarning(
                    "Permission Required",
                    "This Application needs Usage Access Permission to perform data Monitoring. Plz Allow Usage Access Permission to continue!!!"
                )
            }
            //                       else{
//                           proceedToMainActivity(Constants.FRAGMENT_MOBILE)
//                       }
        } else {
            val showRationale = shouldShowRequestPermissionRationale(permissions[0])
            if (!showRationale) {
                val editor = mySharedPreferences.getPreferencesEditor()
                editor.putBoolean(Constants.ASK_FOR_PERMISSION, false)
                editor.commit()
            } else {
                if (!myAppPermissions.isSystemLevelPermissionGiven()) {
                    myDialogueBoxes.systemLevelPermissionsWarning(
                        "Permission Required",
                        "This Application needs Usage Access Permission to perform data Monitoring. Plz Allow Usage Access Permission to continue!!!"
                    )
                }
                //                       else{
//                           proceedToMainActivity(Constants.FRAGMENT_MOBILE)
//                       }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    override fun positiveRunTimeButton() {
        val myAppPermissions = MyAppPermissions(context!!.applicationContext, activity!!)
        myAppPermissions.requestForRunTimePermissions()
    }

    override fun negativeRunTimeButton() {

    }

    override fun positiveSyatemLevelButton() {
        startActivityForResult(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), 7724);
    }

    override fun negativeSyatemLevelButton() {
    }

    override fun turnPermissionsOn() {
    }

    override fun dismissed() {
    }

    override fun ignoreBatteryOptimization() {
    }

    override fun ignoreDismissed() {
    }

    override fun systemRemoved() {
    }

    override fun sysytemdismissed() {
    }

    override fun cancelListener() {
    }

    override fun retrySpeedTest() {
    }

    override fun deletePlan() {
    }
}

