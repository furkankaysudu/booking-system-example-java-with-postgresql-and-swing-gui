package org.example.data;

import java.util.HashMap;
import java.util.Map;

public class Route {

    private String selectedRoute;

    public Route(String selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

    public String getSelectedRoute() {
        return selectedRoute;
    }

    public void setSelectedRoute(String selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

    public int getDistance(){
        switch(getSelectedRoute()){
            case "Istanbul - Kocaeli - Ankara":
                return 500;
            case "Istanbul - Kocaeli - Eskişehir - Konya":
                return 600;
            case "Istanbul - Kocaeli - Bilecik - Eskişehir - Ankara":
                return 375;
            case "Istanbul - Kocaeli - Bilecik - Eskişehir - Konya":
                return 450;
            case "Istanbul - Konya":
                return 300;
            case "Istanbul - Ankara":
                return 250;
            default:
                return 1;
        }
    }
}
