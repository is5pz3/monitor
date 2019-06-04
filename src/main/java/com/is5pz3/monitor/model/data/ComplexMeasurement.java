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
public class ComplexMeasurement {
    @JsonProperty("complex_measurement_id")
    private String id;

    private List<Measurement> measurements;


}
