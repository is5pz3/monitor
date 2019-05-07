package com.is5pz3.monitor.repositories;

import com.is5pz3.monitor.model.entities.HostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostsRepository extends JpaRepository<HostEntity, String> {

    List<HostEntity> findBySensorId(String sensorId);

    List<HostEntity> findByHostNameAndPlatformAndMetricAndUnit(String hostName, String platform, String metric, String unit);

    List<HostEntity> findByHostNameContaining(String hostName);

}
