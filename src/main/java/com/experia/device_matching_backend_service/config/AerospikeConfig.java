package com.experia.device_matching_backend_service.config;

import com.aerospike.client.Host;
import com.experia.device_matching_backend_service.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.config.AbstractAerospikeDataConfiguration;
import org.springframework.data.aerospike.repository.config.EnableAerospikeRepositories;

import java.util.Collection;
import java.util.List;

@Configuration
@EnableAerospikeRepositories(basePackageClasses = DeviceRepository.class)
public class AerospikeConfig extends AbstractAerospikeDataConfiguration {

    @Value("${aerospike.host}")
    private String host;

    @Value("${aerospike.port}")
    private int port;

    @Value("${aerospike.namespace}")
    private String namespace;

    @Override
    protected Collection<Host> getHosts() {
        return List.of(new Host(host, port));
    }

    @Override
    protected String nameSpace() {
        return namespace;
    }
}