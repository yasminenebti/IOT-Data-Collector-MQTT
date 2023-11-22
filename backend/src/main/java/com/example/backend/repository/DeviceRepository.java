package com.example.backend.repository;

import com.example.backend.entity.DeviceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceData,Long> {
    int countByTopic(String topic);

}
