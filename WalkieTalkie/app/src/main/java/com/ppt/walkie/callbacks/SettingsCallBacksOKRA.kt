package com.ppt.walkie.callbacks

import com.ppt.walkie.utils.SettingsModelClassOKRA

interface SettingsCallBacksOKRA {
    fun stopMyService()
    fun itemClicked(item:SettingsModelClassOKRA)
}