package com.armzilla.ha.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by arm on 4/13/15.
 */
public interface DeviceRepository extends ElasticsearchRepository<DeviceDescriptor, String> {
    Page<DeviceDescriptor> findByDeviceType(String type, Pageable request);
    List<DeviceDescriptor> findAll();
    DeviceDescriptor findOne(String id);

}
