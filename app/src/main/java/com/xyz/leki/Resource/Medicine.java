package com.xyz.leki.Resource;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class Medicine implements Serializable{
    private String name;
    private List<Calendar> alarmTimeList;
    private List<Integer> alarmRequestCodeList;
    private boolean repeat;
    private int quantity;
    private String barcode;

    public Medicine(String name, List<Calendar> alarmTimeList, List<Integer> alarmRequestCodeList, boolean repeat, int quantity, String barcode) {
        this.name = name;
        this.alarmTimeList = alarmTimeList;
        this.alarmRequestCodeList = alarmRequestCodeList;
        this.repeat = repeat;
        this.quantity = quantity;
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Calendar> getAlarmTimeList() {
        return alarmTimeList;
    }

    public void setAlarmTimeList(List<Calendar> alarmTimeList) {
        this.alarmTimeList = alarmTimeList;
    }

    public List<Integer> getAlarmRequestCodeList() {
        return alarmRequestCodeList;
    }

    public void setAlarmRequestCodeList(List<Integer> alarmRequestCodeList) {
        this.alarmRequestCodeList = alarmRequestCodeList;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String toString() {
        return name;
    }
}
