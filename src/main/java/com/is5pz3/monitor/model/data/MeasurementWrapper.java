package com.is5pz3.monitor.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MeasurementWrapper {
    private List<Measurement> measurements;

    @JsonProperty("complex_measurements")
    private ComplexMeasurement complexMeasurement;
}
