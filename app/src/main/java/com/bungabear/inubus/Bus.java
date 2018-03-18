package com.bungabear.inubus;

import java.util.ArrayList;

/**
 * Created by Bunga on 2018-02-01.
 */

public class Bus {

    public static final int ENGINEER = 0x01;
    public static final int SCIENCE = 0x02;
    public static final int FRONT = 0x04;

    private final String busNo;
    private final int mainStop;
    private int start;
    private int end;
    private int fee;
//    private int fee_student;
    private ArrayList<BusStop> route;
    private boolean fav;
    private int intervalMin;
    private int intervalMax;
    private long arrivalTime;
    private long updateTime;

    public String getBusNo() {
        return busNo;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getFee() {
        return fee;
    }

    public ArrayList<BusStop> getRoute() {
        return route;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public int getIntervalMin() {
        return intervalMin;
    }

    public void setIntervalMin(int intervalMin) {
        this.intervalMin = intervalMin;
    }

    public int getIntervalMax() {
        return intervalMax;
    }

    public void setIntervalMax(int intervalMax) {
        this.intervalMax = intervalMax;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public Bus(String busNo, int start, int end, int fee, ArrayList<BusStop> route, boolean fav, int stopAt) {
        this.busNo = busNo;
        this.start = start;
        this.end = end;
        this.fee = fee;
        this.route = route;
        this.fav = fav;
        this.mainStop = stopAt;
    }
}
