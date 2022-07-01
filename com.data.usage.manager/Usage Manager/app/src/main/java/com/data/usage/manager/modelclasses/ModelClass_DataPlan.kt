package com.data.usage.manager.modelclasses

class ModelClass_DataPlan() {
    var daysPlanValidFor=0
        get() = field
        set(value) {
            field = value
        }
    var dataLimitBytes :Double=0.0
        get() = field
        set(value) {
            field = value
        }
    var planStartingDate:String=""
        get() = field
        set(value) {
            field = value
        }
    var dataType:String?=""
        get() = field
        set(value) {
            field = value
        }
    var planEndingDate:String?=null
        get() = field
        set(value) {
            field = value
        }
    var alerDate:String=""
        get() = field
        set(value) {
            field = value
        }
    var alertbytes:Double=0.0
        get() = field
        set(value) {
            field = value
        }
    var simSlot=-1


}