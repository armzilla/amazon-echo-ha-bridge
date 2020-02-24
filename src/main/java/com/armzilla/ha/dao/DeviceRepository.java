package com.armzilla.ha.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by arm on 4/13/15.
 */
public interface DeviceRepository extends CrudRepository<DeviceDescriptor, String> {
    Page<DeviceDescriptor> findByDeviceType(String type, Pageable request);
    List<DeviceDescriptor> findAll();
    DeviceDescriptor findOne(String id);

}
