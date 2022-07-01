package com.data.usage.manager.interfaces

interface DialogueClickListner {
    fun positiveRunTimeButton()
    fun negativeRunTimeButton()
    fun positiveSyatemLevelButton()
    fun negativeSyatemLevelButton()
    fun turnPermissionsOn()
    fun dismissed()
    fun ignoreBatteryOptimization()
    fun ignoreDismissed()
    fun systemRemoved()
    fun sysytemdismissed()
    fun cancelListener()
    fun retrySpeedTest()
    fun deletePlan()
}
