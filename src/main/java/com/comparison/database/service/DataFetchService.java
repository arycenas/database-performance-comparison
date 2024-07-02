package com.comparison.database.service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comparison.database.model.MongoData;
import com.comparison.database.model.PostgresData;
import com.comparison.database.repository.MongoDataRepository;
import com.comparison.database.repository.PostgresDataRepository;

@Service
public class DataFetchService {

    @Autowired
    private MongoDataRepository mongoDataRepository;

    @Autowired
    private PostgresDataRepository postgresDataRepository;

    public Map<String, Object> fetchDataAndMeasureTime() {
        Map<String, Object> result = new HashMap<>();

        Instant postgresStart = Instant.now();
        List<PostgresData> postgresData = postgresDataRepository.findAll();
        Instant postgresEnd = Instant.now();
        Duration postgresDuration = Duration.between(postgresStart, postgresEnd);

        Instant mongoStart = Instant.now();
        List<MongoData> mongoData = mongoDataRepository.findAll();
        Instant mongoEnd = Instant.now();
        Duration mongoDuration = Duration.between(mongoStart, mongoEnd);

        result.put("PostgreSQL Data", postgresData);
        result.put("MongoDB Data", mongoData);
        result.put("PostgreSQL Fetch Time (ms)", postgresDuration.toMillis());
        result.put("MongoDB Fetch Time (ms)", mongoDuration.toMillis());

        return result;
    }
}
