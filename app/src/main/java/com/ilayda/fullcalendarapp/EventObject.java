package com.ilayda.fullcalendarapp;

public class EventObject {
    private String eventName;
    private String startTime;
    private String finishTime;

    private String detail;
    private int startHour;
    private int startMinute;
    private int finishHour;
    private int finishMinute;
    private int remindHour;
    private int remindMinute;
    private int repeat;
    private String address;

    private int ID;

    private int year;
    private int month;
    private int dayOfMonth;

    public EventObject(String eventName, String startTime, String finishTime, String detail, int startHour, int startMinute, int finishHour, int finishMinute,
                       int remindHour, int remindMinute, int repeat, String address, int ID, int year, int month, int dayOfMonth) {
        this.eventName = eventName;
        this.startTime = startTime;
        this.finishTime = finishTime;

        this.detail = detail;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.finishHour = finishHour;
        this.finishMinute = finishMinute;
        this.remindHour = remindHour;
        this.remindMinute = remindMinute;
        this.repeat = repeat;
        this.address = address;
        this.ID = ID;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getFinishHour() {
        return finishHour;
    }

    public void setFinishHour(int finishHour) {
        this.finishHour = finishHour;
    }

    public int getFinishMinute() {
        return finishMinute;
    }

    public void setFinishMinute(int finishMinute) {
        this.finishMinute = finishMinute;
    }

    public int getRemindHour() {
        return remindHour;
    }

    public void setRemindHour(int remindHour) {
        this.remindHour = remindHour;
    }

    public int getRemindMinute() {
        return remindMinute;
    }

    public void setRemindMinute(int remindMinute) {
        this.remindMinute = remindMinute;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}
