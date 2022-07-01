package com.example.hissabbookapp.realmmodals;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RecordDataRealm extends RealmObject {

    String remark,totalBalance,time,balance,date,type,day,month,year;
    @PrimaryKey
    String id;

    public RecordDataRealm(String remark, String totalBalance, String time, String balance,String id,String date,String type,String day, String month, String year) {
        this.remark = remark;
        this.totalBalance = totalBalance;
        this.time = time;
        this.balance = balance;
        this.id=id;
        this.date=date;
        this.type=type;
        this.day=day;
        this.month=month;
        this.year=year;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public RecordDataRealm() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
