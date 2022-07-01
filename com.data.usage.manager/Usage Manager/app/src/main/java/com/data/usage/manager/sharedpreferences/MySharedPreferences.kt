package com.data.usage.manager.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.data.usage.manager.constants.Constants
import com.data.usage.manager.modelclasses.ModelClass_DataPlan


class MySharedPreferences(context: Context) {

    var sharedPreferences: SharedPreferences? = null
    var context: Context = context


    init {
        sharedPreferences = getMySharedPreferences(context)
    }

    fun getMySharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SH_SHARED_PREFERENCES_FILE_NAME, 0)
    }

    fun saveDataPlan(dataPlan: ModelClass_DataPlan): Boolean {
        var editor = getPreferencesEditor()
        editor.putInt(Constants.SH_DAYS_PLAN_VALID_FOR, dataPlan.daysPlanValidFor)
        editor.putLong(Constants.SH_DATA_LIMIT_VALID_FOR, dataPlan.dataLimitBytes.toLong())
        editor.putString(Constants.SH_PLAN_STARTING_DATE, dataPlan.planStartingDate)
        editor.putString(Constants.SH_DATA_TYPE, dataPlan.dataType)
        if(dataPlan.simSlot!=-1&&dataPlan.simSlot!=0){
            editor.putInt(Constants.SIM_SLOT,dataPlan.simSlot)
        }
        editor.putBoolean(Constants.SH_IS_DATA_PLAN_SET, true)
        editor.putString(Constants.SH_PLAN_ENDING_DATE, dataPlan.planEndingDate)
        editor.putBoolean(Constants.ALERTED_A_DAY_AGO, false)
        editor.putLong(Constants.ALERT_BYTES, dataPlan.alertbytes.toLong())
        editor.putString(Constants.ALERT_DATE, dataPlan.alerDate)
        return editor.commit()
    }

    fun getPreferencesEditor(): SharedPreferences.Editor {
        return if (sharedPreferences != null)
            sharedPreferences!!.edit()
        else {
            sharedPreferences = getMySharedPreferences(context)
            sharedPreferences!!.edit()
        }
    }

    fun getDataPlan(): ModelClass_DataPlan? {
        var modelclassDataplan: ModelClass_DataPlan? = null
        if (!isDataPlanSet())
            return modelclassDataplan
        modelclassDataplan = ModelClass_DataPlan()
        modelclassDataplan.daysPlanValidFor = sharedPreferences!!.getInt(Constants.SH_DAYS_PLAN_VALID_FOR, 0)
        modelclassDataplan.dataLimitBytes = sharedPreferences!!.getLong(Constants.SH_DATA_LIMIT_VALID_FOR, 0).toDouble()
        modelclassDataplan.planStartingDate = sharedPreferences!!.getString(Constants.SH_PLAN_STARTING_DATE, "NO DATE SET")!!
        modelclassDataplan.dataType = sharedPreferences!!.getString(Constants.SH_DATA_TYPE, "GB")
        modelclassDataplan.planEndingDate = sharedPreferences!!.getString(Constants.SH_PLAN_ENDING_DATE, "NO DATE SET")
        modelclassDataplan.alertbytes = sharedPreferences!!.getLong(Constants.ALERT_BYTES, 0).toDouble()
        modelclassDataplan.alerDate = sharedPreferences!!.getString(Constants.ALERT_DATE, "0")!!
        return modelclassDataplan
    }

    fun isDataPlanSet(): Boolean {
        if (sharedPreferences == null) {
            sharedPreferences = getMySharedPreferences(context)
        }
        return sharedPreferences?.getBoolean(Constants.SH_IS_DATA_PLAN_SET, false) == true
    }

    fun setDataPlan(dataplan: Boolean): Boolean {
        var editor = getPreferencesEditor();
        editor.putBoolean(Constants.SH_IS_DATA_PLAN_SET, dataplan)
        return editor.commit()
    }

    fun canAskForPermissions(): Boolean {
        return sharedPreferences!!.getBoolean(Constants.ASK_FOR_PERMISSION, true) == true
    }

    fun setWifiDataPlan(b: Boolean): Boolean {
        var editor = sharedPreferences!!.edit()
        editor.putBoolean(Constants.WIFI_DATA_PLAN, b)
        return editor.commit()
    }

    fun isWifiDataPlan(): Boolean {
        return sharedPreferences!!.getBoolean(Constants.WIFI_DATA_PLAN, false) == true
    }

    fun setWifiDataPlanSet(b: Boolean): Boolean {
        var editor = sharedPreferences!!.edit()
        editor.putBoolean(Constants.SH_IS_DATA_PLAN_SET_WIFI, b)
        return editor.commit()
    }

    fun isWifiDataPlanSet(): Boolean {
        return sharedPreferences!!.getBoolean(Constants.SH_IS_DATA_PLAN_SET_WIFI, false) == true
    }

    fun getWifiDataPlan(): ModelClass_DataPlan? {
        var modelclassDataplan: ModelClass_DataPlan? = null
        if (!isWifiDataPlanSet())
            return modelclassDataplan
        modelclassDataplan = ModelClass_DataPlan()
        modelclassDataplan.daysPlanValidFor = sharedPreferences!!.getInt(Constants.SH_DAYS_PLAN_VALID_FOR_WIFI, 0)
        modelclassDataplan.dataLimitBytes = sharedPreferences!!.getLong(Constants.SH_DATA_LIMIT_VALID_FOR_WIFI, 0).toDouble()
        modelclassDataplan.planStartingDate = sharedPreferences!!.getString(Constants.SH_PLAN_STARTING_DATE_WIFI, "NO DATE SET")!!
        modelclassDataplan.dataType = sharedPreferences!!.getString(Constants.SH_DATA_TYPE_WIFI, "GB")
        modelclassDataplan.planEndingDate = sharedPreferences!!.getString(Constants.SH_PLAN_ENDING_DATE_WIFI, "NO DATE SET")
        modelclassDataplan.alertbytes = sharedPreferences!!.getLong(Constants.ALERT_BYTES_WIFI, 0).toDouble()
        modelclassDataplan.alerDate = sharedPreferences!!.getString(Constants.ALERT_DATE_WIFI, "0")!!
        return modelclassDataplan
    }

    fun saveWifiDataPlan(dataPlan: ModelClass_DataPlan): Boolean {
        var editor = getPreferencesEditor()
        editor.putInt(Constants.SH_DAYS_PLAN_VALID_FOR_WIFI, dataPlan.daysPlanValidFor)
        editor.putLong(Constants.SH_DATA_LIMIT_VALID_FOR_WIFI, dataPlan.dataLimitBytes.toLong())
        editor.putString(Constants.SH_PLAN_STARTING_DATE_WIFI, dataPlan.planStartingDate)
        editor.putString(Constants.SH_DATA_TYPE_WIFI, dataPlan.dataType)
        editor.putBoolean(Constants.SH_IS_DATA_PLAN_SET_WIFI, true)
        editor.putString(Constants.SH_PLAN_ENDING_DATE_WIFI, dataPlan.planEndingDate)
        editor.putBoolean(Constants.ALERTED_A_DAY_AGO_WIFI, false)
        editor.putLong(Constants.ALERT_BYTES_WIFI, dataPlan.alertbytes.toLong())
        editor.putString(Constants.ALERT_DATE_WIFI, dataPlan.alerDate)
        return editor.commit()
    }

    fun getSimSlot(): Int {
        return if(isDataPlanSet())
            sharedPreferences!!.getInt(Constants.SIM_SLOT,0)
        else
            return getDefaultSimSelected()

    }

    fun setDefaultSelectedSim(position: Int) {
        var editor=sharedPreferences?.edit()
        if(position==0){
            editor?.putInt(Constants.SIM_SLOT,Constants.SIM_SLOT_1)
        }else{
            editor?.putInt(Constants.SIM_SLOT,Constants.SIM_SLOT_2)
        }
        editor?.apply()
    }
    fun getDefaultSimSelected():Int{
        return sharedPreferences?.getInt(Constants.SIM_SLOT,0)!!
    }
}
