package com.phone.clone.modelclasses

class HSTransferDataModel() {
    var isSelected=false

    var title=""
    var itemInfo=""
    var seleted=false
    var typeReceiving=false
    var progress =0
    var iconsIds=-1
    var sendPressed=false
    constructor(title:String,ItemInfo:String,iconID:Int):this(){
        this.title=title
        this.itemInfo=ItemInfo
        this.iconsIds=iconID
    }
}