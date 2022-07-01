
package com.data.usage.manager.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SubscriptionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.data.usage.manager.activities.SettingsActivity
import com.data.usage.manager.constants.Constants
import com.data.usage.manager.interfaces.DateSetListener
import com.data.usage.manager.interfaces.DialogueClickListner
import com.data.usage.manager.modelclasses.ModelClass_DataPlan
import com.data.usage.manager.R
import com.data.usage.manager.sharedpreferences.MySharedPreferences
import com.data.usage.manager.usefullclasses.MyDatePickerDialogue
import com.data.usage.manager.usefullclasses.MyDialogueBoxes
import java.text.SimpleDateFormat
import java.util.*


class Fragment_Plan_Data : Fragment(),DialogueClickListner {
    lateinit var mySharedPreferences:MySharedPreferences
    var myDialogueBoxes:MyDialogueBoxes?=null
    var wifiDataPlan=false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =inflater.inflate(R.layout.fragment_plan, container, false)
        mySharedPreferences=MySharedPreferences(context!!)
        myDialogueBoxes=MyDialogueBoxes(context!!,context!!,layoutInflater,this)
        if(mySharedPreferences.isWifiDataPlan()){
            view.findViewById<ConstraintLayout>(R.id.cl_selectsim).visibility=View.GONE
            wifiDataPlan=true
            mySharedPreferences.setWifiDataPlan(false)
            if(!mySharedPreferences.isWifiDataPlanSet()){
                createViewNoDataSetPlanned(view)
            }else{
                createViewToSetDataPlan(view)
            }
        }else if(!mySharedPreferences.isDataPlanSet()){
            createViewNoDataSetPlanned(view)
        }else{
            createViewToSetDataPlan(view)
        }
        val iv_settings=view.findViewById<ImageView>(R.id.mi_settings)
        iv_settings.setOnClickListener{
            val intent= Intent(context, SettingsActivity::class.java)
            context?.startActivity(intent)
        }
        return view
    }

    override fun onPause() {
        super.onPause()
        mySharedPreferences.setWifiDataPlan(false)
        wifiDataPlan=false
    }

    override fun onDestroy() {
        super.onDestroy()
        mySharedPreferences.setWifiDataPlan(false)
        wifiDataPlan=false
    }
    private fun createViewNoDataSetPlanned(view: View?) {
        var cl_setdataplan = view?.findViewById<ConstraintLayout>(R.id.dataplanset)
        var cl_nodataplan =view?.findViewById<ConstraintLayout>(R.id.nodataplanset)
        cl_nodataplan?.visibility=View.VISIBLE
        cl_setdataplan?.visibility=View.INVISIBLE
        var btn_setdataplan=view?.findViewById<Button>(R.id.btn_setdataplan)
        btn_setdataplan?.setOnClickListener {
            createViewToSetDataPlan(view)
        }
    }

    private fun createViewToSetDataPlan(view: View?) {
        var selectedSim=-1
        var dualSim=false
        var cl_setdataplan = view?.findViewById<ConstraintLayout>(R.id.dataplanset)
        var cl_nodataplan =view?.findViewById<ConstraintLayout>(R.id.nodataplanset)
        var cl_selectSim = view?.findViewById<ConstraintLayout>(R.id.cl_selectsim)
        cl_setdataplan?.visibility=View.VISIBLE
        cl_nodataplan?.visibility=View.INVISIBLE
        //check no of sim slots in device
        if(!wifiDataPlan&&checkNoOfSimSlots()>=1){
            //make sim selection visible
            cl_selectSim?.visibility=View.VISIBLE
            dualSim=true
            //if android 10 return 0
        }else{
            //remove sim selection layout
            cl_selectSim?.visibility=View.GONE
            dualSim=false
            selectedSim=-1
        }

        var et_daysValidFor=view?.findViewById<EditText>(R.id.et_daysvalidfor)
        et_daysValidFor?.setText("")
        var et_dataLimit=view?.findViewById<EditText>(R.id.et_datalimit)
        et_dataLimit?.setText("")
        var btn_setdataplan=view?.findViewById<Button>(R.id.btn_setplan)
        btn_setdataplan?.setText("SET DATA PLAN")
        var btn_skip=view?.findViewById<Button>(R.id.btn_skip)
        btn_skip?.setText("SKIP")
        var sp_dataplantype=view?.findViewById<Spinner>(R.id.datatype)
        var sp_simSlot:Spinner?=null
        sp_dataplantype?.setSelection(0)
        if(dualSim){
           sp_simSlot=view?.findViewById<Spinner>(R.id.sp_selected_simslot)
            sp_simSlot?.setSelection(0)
            selectedSim=-1
        }
        var iv_startingDate=view?.findViewById<ImageView>(R.id.iv_startingdate)
        var tv_startingDate=view?.findViewById<TextView>(R.id.tv_startingdate)
        //set current date
        var date= Calendar.getInstance().get(Calendar.DATE)
        var month= Calendar.getInstance().get(Calendar.MONTH)
        var year= Calendar.getInstance().get(Calendar.YEAR)
        var todayDate=getProperDate(date,month,year)
        tv_startingDate?.text=todayDate
        intiViewWithSavedPlan(view)
        btn_setdataplan?.setOnClickListener{
            var dataLimitInBytes:Long?=0
            val dataPlanType=sp_dataplantype?.selectedItem.toString()
           if(dualSim){
               val selectedsimslot=sp_simSlot?.selectedItemPosition
               if(selectedsimslot?.equals(Constants.SIM_SLOT_1) == true){
                   selectedSim=Constants.SIM_SLOT_1
               }else{
                   selectedSim=Constants.SIM_SLOT_2
               }
           }
            val daysValidFor=et_daysValidFor?.text.toString()
            val dataLimit=et_dataLimit?.text.toString()
            var error=false
            try{
            daysValidFor.toInt()
            }catch(ex : java.lang.NumberFormatException){
                error=true;
            }
            if(error){
                et_daysValidFor?.error="plz enter again"
            }else if((daysValidFor?.trim().isEmpty()&&!daysValidFor?.isDigitsOnly())){
                et_daysValidFor?.error="plz enter again"
                error=true
            }else if(daysValidFor.equals("0")){
                et_daysValidFor?.error="days can't be zero"
                error=true
            }else if(daysValidFor.length>3){
                et_daysValidFor?.error="plan validity can't increase 999 days"
                error=true
            }
            var er=false;
            try{
                dataLimit.toInt()
            }catch (ex: java.lang.NumberFormatException){
                er=true
            }
            if(er){
                et_dataLimit?.error="plz enter again"
                error=true
            }else if((!dataLimit?.trim().isNotEmpty()||!dataLimit?.isDigitsOnly()||dataLimit?.equals("0"))){
                et_dataLimit?.error="plz enter again"
                error=true
            }else if(dataLimit.length>10){
                et_dataLimit?.error="plan validity can't increase 9999999999 MB/GB"
                error=true
            }else {
                if(dataPlanType.equals(Constants.GB_DATA_TYPE)){
                    dataLimitInBytes=convertGBToBytes(dataLimit.toLong())
                }else{
                    dataLimitInBytes=convertMBToBytes(dataLimit.toLong())
                }
                if (getAlertBytes(dataLimitInBytes!!.toDouble()) <= 0) {
                    et_dataLimit?.error = "min 201 Mb is supported"
                    error = true
                }
            }
            var startingDate="${tv_startingDate?.text.toString()}"
            var s_date=Date(SimpleDateFormat("dd/MM/yyyy").parse("$startingDate 00:00:00").time)
            var s_cal = Calendar.getInstance()
            s_cal.time=s_date
            var t_date=Date(SimpleDateFormat("dd/MM/yyyy").parse(todayDate).time)
            var t_cal = Calendar.getInstance()
            t_cal.time=t_date
            if(s_cal.timeInMillis>t_cal.timeInMillis){
                tv_startingDate!!.error="Plz input Correct date"
                error=true
            }

            if(!error){

                //save the data in

                //convert datalimit to byes
                if(dataPlanType.equals(Constants.GB_DATA_TYPE)){
                    dataLimitInBytes=convertGBToBytes(dataLimit.toLong())
                }else{
                    dataLimitInBytes=convertMBToBytes(dataLimit.toLong())
                }
                var date=Date(SimpleDateFormat("dd/MM/yyyy").parse(startingDate).time)
                val c = Calendar.getInstance()
                c.time = date
                var daysvalidity: Int
                try{
                    daysvalidity=daysValidFor.toInt()
                }catch(excep:NumberFormatException){
                    daysvalidity=1
                }
                c.add(Calendar.DATE,daysvalidity )
                val ending_date=SimpleDateFormat("dd/MM/yyyy").format(c.time).toString()
                c.time = date
                try{
                    if(daysValidFor.toInt()!=0){
                        c.add(Calendar.DATE, daysValidFor.toInt() - 1)
                    }
                }catch (ex:java.lang.NumberFormatException){
                    c.add(Calendar.DATE, 1)
                }
                val alertDate=SimpleDateFormat("dd/MM/yyyy").format(c.time).toString()

                //getsimslot

                //now create instance of model class
                var modelclassDataplan=ModelClass_DataPlan()
                modelclassDataplan.planStartingDate=startingDate
                modelclassDataplan.dataLimitBytes= dataLimitInBytes!!.toDouble()
                modelclassDataplan.daysPlanValidFor=daysValidFor.toInt()
                modelclassDataplan.dataType=dataPlanType
                modelclassDataplan.planEndingDate=ending_date
                modelclassDataplan.alerDate=alertDate
                if(dualSim){
                    modelclassDataplan.simSlot=selectedSim
                }
                modelclassDataplan.alertbytes=getAlertBytes(dataLimitInBytes!!.toDouble())
                if(wifiDataPlan){
                    if(mySharedPreferences.saveWifiDataPlan(modelclassDataplan)) {
                        mySharedPreferences.setWifiDataPlanSet(true)
                        myDialogueBoxes?.dataPlanSet()
                       // Toast.makeText(context, "Data Plan Set & Saved.", Toast.LENGTH_SHORT).show()
                        btn_setdataplan?.setText("UPDATE PLAN")
                        btn_skip?.setText("DELETE")
                        hideKeyboardFrom(context!!,view!!)
                    } else{

                    }
                      //  Toast.makeText(context, "Data Plan Not Set & Saved.", Toast.LENGTH_SHORT).show()
                }else if(mySharedPreferences.saveDataPlan(modelclassDataplan)) {
                    myDialogueBoxes?.dataPlanSet()
                   // Toast.makeText(context, "Data Plan Set & Saved.", Toast.LENGTH_SHORT).show()
                    btn_setdataplan?.setText("UPDATE PLAN")
                    btn_skip?.setText("DELETE")
                    hideKeyboardFrom(context!!,view!!)
                } else{

                }
                   // Toast.makeText(context, "Data Plan Not Set & Saved.", Toast.LENGTH_SHORT).show()
            }

        }//handle delete click listeners
        btn_skip?.setOnClickListener{
            if(wifiDataPlan&&!mySharedPreferences.isWifiDataPlanSet()){
                createViewNoDataSetPlanned(view)
            }else if(wifiDataPlan){
                //deleting data plan
                    myDialogueBoxes?.dataPlanDelete()

            }else if(!wifiDataPlan&&!mySharedPreferences.isDataPlanSet()){
                createViewNoDataSetPlanned(view)
            }else if(!wifiDataPlan){
                //what should i do
            // delete current data plan
                myDialogueBoxes?.dataPlanDelete()

            }
        }
        view?.findViewById<LinearLayout>(R.id.ll_datepicker)?.setOnClickListener{
               Log.d(Constants.TAG, "createViewToSetDataPlan: iv clicked")
            var splitedDate=getSplittedDateArray(tv_startingDate?.text.toString())
            Log.d(
                Constants.TAG,
                "d : ${splitedDate[0]} , m : ${splitedDate[1]}, y : ${splitedDate[2]}"
            )
            val myDatePickerDialogue=MyDatePickerDialogue(
                year = splitedDate[2],
                month = splitedDate[1],
                day = splitedDate[0],
                object : DateSetListener {
                    override fun dateSetListener(y: Int, m: Int, d: Int) {
                        tv_startingDate?.text = getProperDate(d,m,y)
                        Log.d(Constants.TAG, "dateSetListener: interface event")
                    }
                })
            myDatePickerDialogue.show(childFragmentManager, "DatePicker")
        }
       /* iv_startingDate?.setOnClickListener{
            //show dialogue box to show datePicker
         *//*   Log.d(Constants.TAG, "createViewToSetDataPlan: iv clicked")
            var splitedDate=getSplittedDateArray(tv_startingDate?.text.toString())
            Log.d(
                Constants.TAG,
                "d : ${splitedDate[0]} , m : ${splitedDate[1]}, y : ${splitedDate[2]}"
            )
            val myDatePickerDialogue=MyDatePickerDialogue(
                year = splitedDate[2],
                month = splitedDate[1],
                day = splitedDate[0],
                object : DateSetListener {
                    override fun dateSetListener(y: Int, m: Int, d: Int) {
                        tv_startingDate?.text = getProperDate(d,m,y)
                        Log.d(Constants.TAG, "dateSetListener: interface event")
                    }
                })
            myDatePickerDialogue.show(childFragmentManager, "DatePicker")*//*
        }*/

    }

    private fun checkNoOfSimSlots(): Int {
       if(Build.VERSION.SDK_INT>=29){
           return 1
       }else{
           var subscriptionManager=context!!.applicationContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
           return subscriptionManager.activeSubscriptionInfoCountMax
       }
    }

    private fun getProperDate(date: Int, month: Int, year: Int): String {
        var d=""
        var m=""
        var y=""
        if(date<=9){
            d="0$date"
        }else{
            d="$date"
        }
        var mm=month
        mm+=1
        if(mm<=9){
            m="0$mm"
        }else{
            m="$mm"
        }
        if(year<=9){
            y="0$year"
        }else{
            y="$year"
        }

        return "$d/$m/$y"
    }

    private fun getAlertBytes(usedBytes: Double): Double {
        var alertLimit=204800000
        return usedBytes-alertLimit
    }

    private fun getSplittedDateArray(date: String): List<String> {
        return date.split("/")
    }

    private fun intiViewWithSavedPlan(view: View?) {

        //get plan fromPreferences and load
        var et_daysValidFor=view?.findViewById<EditText>(R.id.et_daysvalidfor)
        var et_dataLimit=view?.findViewById<EditText>(R.id.et_datalimit)
        var sp_dataplantype=view?.findViewById<Spinner>(R.id.datatype)
        var tv_startingDate=view?.findViewById<TextView>(R.id.tv_startingdate)
        var sp_selectsim=view?.findViewById<Spinner>(R.id.sp_selected_simslot)

        if(wifiDataPlan&&mySharedPreferences.isWifiDataPlanSet()){
           // Toast.makeText(context, "present", Toast.LENGTH_SHORT).show()
            //if already have a plan then update the ui to update the plan
            val btn_setdataplan=view?.findViewById<Button>(R.id.btn_setplan)
            btn_setdataplan?.setText("UPDATE PLAN")
            val btn_skip=view?.findViewById<Button>(R.id.btn_skip)
//            btn_skip?.visibility=View.GONE
            btn_skip?.text="DELETE"

            val modelClassDataPlan=mySharedPreferences.getWifiDataPlan()
            if(modelClassDataPlan!=null) {
                et_daysValidFor?.setText(modelClassDataPlan.daysPlanValidFor.toString())
                if(modelClassDataPlan.dataType.equals(Constants.GB_DATA_TYPE)){
                    et_dataLimit?.setText(convertBytesToGB(modelClassDataPlan.dataLimitBytes.toLong()))
                    sp_dataplantype?.setSelection(0)
                }else{
                    et_dataLimit?.setText(convertBytesToMB(modelClassDataPlan.dataLimitBytes.toLong()))
                    sp_dataplantype?.setSelection(1)
                }
                tv_startingDate?.setText(modelClassDataPlan.planStartingDate)
            }
        }else if(!wifiDataPlan&&mySharedPreferences.isDataPlanSet()){
           // Toast.makeText(context, "present", Toast.LENGTH_SHORT).show()
            //if already have a plan then update the ui to update the plan
                if(mySharedPreferences.getSimSlot().equals(Constants.SIM_SLOT_1)){
                    sp_selectsim?.setSelection(0)
                }else{
                    sp_selectsim?.setSelection(1)
                }
            val btn_setdataplan=view?.findViewById<Button>(R.id.btn_setplan)
            btn_setdataplan?.setText("UPDATE PLAN")
            val btn_skip=view?.findViewById<Button>(R.id.btn_skip)
//            btn_skip?.visibility=View.GONE
            btn_skip?.text="DELETE"

            val modelClassDataPlan=mySharedPreferences.getDataPlan()
            if(modelClassDataPlan!=null) {
                et_daysValidFor?.setText(modelClassDataPlan.daysPlanValidFor.toString())
                if(modelClassDataPlan.dataType.equals(Constants.GB_DATA_TYPE)){
                    et_dataLimit?.setText(convertBytesToGB(modelClassDataPlan.dataLimitBytes.toLong()))
                    sp_dataplantype?.setSelection(0)
                }else{
                    et_dataLimit?.setText(convertBytesToMB(modelClassDataPlan.dataLimitBytes.toLong()))
                    sp_dataplantype?.setSelection(1)
                }
                tv_startingDate?.setText(modelClassDataPlan.planStartingDate)
            }
        }//update ui
//        else{
//            val btn_skip=view?.findViewById<Button>(R.id.btn_skip)
//            btn_skip?.visibility=View.VISIBLE
//        }
    }

    private fun convertBytesToMB(dataLimitBytes: Long): String{
        var MB_Data=dataLimitBytes.div(1024000)
        return MB_Data.toString()
    }

    private fun convertBytesToGB(dataLimitBytes: Long): String {
        var GB_Data=dataLimitBytes.div(1024000000)
        return GB_Data.toString()
    }

    private fun convertMBToBytes(dataLimit: Long?): Long? {
       var noOfBytes: Long? = dataLimit?.times(1024000)
        return noOfBytes
    }

    private fun convertGBToBytes(dataLimit: Long?): Long? {
        var noOfBytes: Long? = dataLimit?.times(1024000000)
        return noOfBytes
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun positiveRunTimeButton() {

    }

    override fun negativeRunTimeButton() {

    }

    override fun positiveSyatemLevelButton() {

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
       if(wifiDataPlan){
           if(mySharedPreferences.setWifiDataPlanSet(false)){
              // Toast.makeText(context, "Data Plan Deleted.", Toast.LENGTH_SHORT).show()
               createViewNoDataSetPlanned(view)
           }else{
            //   Toast.makeText(context, "Unable to Delete Data Plan.", Toast.LENGTH_SHORT).show()
           }
       }else if(mySharedPreferences.setDataPlan(false)){
           // Toast.makeText(context, "Data Plan Deleted.", Toast.LENGTH_SHORT).show()
            createViewNoDataSetPlanned(view)
        }else{
         //   Toast.makeText(context, "Unable to Delete Data Plan.", Toast.LENGTH_SHORT).show()
        }
    }
}