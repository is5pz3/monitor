package com.is5pz3.monitor.dto;

public class ComplexMeasurementDTO {
    private String sensor_id;
    private Integer time_window;
    private Integer calculation_frequency;
    private String token;

    public ComplexMeasurementDTO() {
    }

    public ComplexMeasurementDTO(String sensor_id, Integer time_window, Integer calculation_frequency, String token) {
        this.sensor_id = sensor_id;
        this.time_window = time_window;
        this.calculation_frequency = calculation_frequency;
        this.token = token;
    }

    public String getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(String sensor_id) {
        this.sensor_id = sensor_id;
    }

    public Integer getTime_window() {
        return time_window;
    }

    public void setTime_window(Integer time_window) {
        this.time_window = time_window;
    }

    public Integer getCalculation_frequency() {
        return calculation_frequency;
    }

    public void setCalculation_frequency(Integer calculation_frequency) {
        this.calculation_frequency = calculation_frequency;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
