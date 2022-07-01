package com.example.hissabbookapp.realmmodals;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SubRecordDataRealm extends RealmObject {
    String remark,totalBalance,time,balance,mainId,date;
    @PrimaryKey
    String id;

    public SubRecordDataRealm(String remark, String totalBalance, String time, String balance, String id,String mainId,String date) {
        this.remark = remark;
        this.totalBalance = totalBalance;
        this.time = time;
        this.balance = balance;
        this.id = id;
        this.mainId=mainId;
        this.date=date;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public SubRecordDataRealm() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
