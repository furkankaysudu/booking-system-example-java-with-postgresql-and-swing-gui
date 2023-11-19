package org.example.data;

import java.util.HashMap;
import java.util.Map;

public class Route {

    private Map<String, Map<String, Integer>> busDistance = new HashMap<>();
    private Map<String, Map<String, Integer>> trainDistance = new HashMap<>();
    private Map<String, Map<String, Integer>> airplaneDistance = new HashMap<>();

    public Route() {
        // Default constructor
    }

    public Map<String, Map<String, Integer>> getBusDistance() {
        return busDistance;
    }

    public void setBusDistance(String source, String destination, int distance) {
        busDistance.computeIfAbsent(source, k -> new HashMap<>()).put(destination, distance);
    }

    public Map<String, Map<String, Integer>> getTrainDistance() {
        return trainDistance;
    }

    public void setTrainDistance(String source, String destination, int distance) {
        trainDistance.computeIfAbsent(source, k -> new HashMap<>()).put(destination, distance);
    }

    public Map<String, Map<String, Integer>> getAirplaneDistance() {
        return airplaneDistance;
    }

    public void setAirplaneDistance(String source, String destination, int distance) {
        airplaneDistance.computeIfAbsent(source, k -> new HashMap<>()).put(destination, distance);
    }

}
