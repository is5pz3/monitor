package com.is5pz3.monitor.model.data;

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

    private String sensorId;
    private String hostName;
    private String platform;
    private String metric;
    private String unit;
}
