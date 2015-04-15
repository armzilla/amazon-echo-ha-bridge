package com.armzilla.ha.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by arm on 4/13/15.
 */
public interface DeviceRepository extends ElasticsearchRepository<DeviceDescriptor, String> {
    List<DeviceDescriptor> findByDeviceType(String type);
    List<DeviceDescriptor> findAll();
    DeviceDescriptor findById(String id);

}
