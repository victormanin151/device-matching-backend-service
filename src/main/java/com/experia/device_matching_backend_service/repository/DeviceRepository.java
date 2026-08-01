package com.experia.device_matching_backend_service.repository;

import com.experia.device_matching_backend_service.model.Device;
import org.springframework.data.aerospike.query.QueryParam;
import org.springframework.data.aerospike.repository.AerospikeRepository;

import java.util.Optional;


public interface DeviceRepository
        extends AerospikeRepository<Device, String> {

    Optional<Device> findByOsNameAndOsVersionAndBrowserNameAndBrowserVersion(
            QueryParam osName,
            QueryParam osVersion,
            QueryParam browserName,
            QueryParam browserVersion
    );
}
