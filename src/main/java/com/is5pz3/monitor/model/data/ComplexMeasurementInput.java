package com.is5pz3.monitor.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ComplexMeasurementInput {
    @JsonProperty("sensor_id")
    private String sensorId;
    @JsonProperty("time_window")
    private Integer timeWindow;
    @JsonProperty("calculation_frequency")
    private Integer calculationFrequency;
    private String token;
}
