package com.bungabear.inubus;

/**
 * Created by Bunga on 2018-02-01.
 */

public class BusStop {
    public final int NORMAL = 1;
    public final int END = 2;
    public final int RETURN = 3;

    private final String stopId;
    private final String stopName;
    private final int order;
    private final Bus bus;
    private final int type;

    public BusStop(String stopId, String stopName, int order, Bus bus, int type) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.order = order;
        this.bus = bus;
        this.type = type;
    }
}
