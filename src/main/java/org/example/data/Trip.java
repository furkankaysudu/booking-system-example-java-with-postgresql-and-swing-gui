package org.example.data;

import org.example.vehicles.Bus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Trip {

    Bus bir = new Bus(1,"benzin",20,10,5);
    Route iki = new Route();
    private Map<Object, Object> trips = new HashMap<>();

    public Trip(Bus bir, Route iki, Map<Object, Object> trips) {
        this.bir = bir;
        this.iki = iki;
        this.trips = trips;
    }

    public Bus getBir() {
        return bir;
    }

    public void setBir(Bus bir) {
        this.bir = bir;
    }

    public Route getIki() {
        return iki;
    }

    public void setIki(Route iki) {
        this.iki = iki;
    }

    public Map<Object, Object> getTrips() {
        return trips;
    }

    public void setTrips(Map<Object, Object> trips) {
        this.trips = trips;
    }
}
