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
public class Host {
    private String id;
    @JsonProperty("sensor_id")
    private String sensorId;
    @JsonProperty("host_name")
    private String hostName;
    private String platform;
    private String metric;
    private String unit;
}
