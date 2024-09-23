package com.comparison.database.service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comparison.database.repository.MongoDataRepository;
import com.comparison.database.repository.PostgresDataRepository;

@Service
public class DataAggregateService {

    @Autowired
    private MongoDataRepository mongoDataRepository;

    @Autowired
    private PostgresDataRepository postgresDataRepository;

    public Map<String, Object> aggregateSumAndMeasureTime() {
        Map<String, Object> result = new HashMap<>();

        // PostgreSQL Sum Aggregation
        Instant postgresStart = Instant.now();
        Double postgresSum = postgresDataRepository.sumTotalNetAmount();
        Instant postgresEnd = Instant.now();
        Duration postgresDuration = Duration.between(postgresStart, postgresEnd);

        // MongoDB Sum Aggregation
        Instant mongoStart = Instant.now();
        Double mongoSum = mongoDataRepository.sumTotalNetAmount();
        Instant mongoEnd = Instant.now();
        Duration mongoDuration = Duration.between(mongoStart, mongoEnd);

        result.put("PostgreSQL Aggregation Time (ms)", postgresDuration.toMillis());
        result.put("MongoDB Aggregation Time (ms)", mongoDuration.toMillis());
        result.put("PostgreSQL Total Net Amount Sum", postgresSum);
        result.put("MongoDB Total Net Amount Sum", mongoSum);

        return result;
    }

    public Map<String, Object> aggregateAverageAndMeasureTime() {
        Map<String, Object> result = new HashMap<>();

        // PostgreSQL Average Aggregation
        Instant postgresStart = Instant.now();
        Double postgresAverage = postgresDataRepository.averageTotalNetAmount();
        Instant postgresEnd = Instant.now();
        Duration postgresDuration = Duration.between(postgresStart, postgresEnd);

        // MongoDB Average Aggregation
        Instant mongoStart = Instant.now();
        Double mongoAverage = mongoDataRepository.averageTotalNetAmount();
        Instant mongoEnd = Instant.now();
        Duration mongoDuration = Duration.between(mongoStart, mongoEnd);

        result.put("PostgreSQL Aggregation Time (ms)", postgresDuration.toMillis());
        result.put("MongoDB Aggregation Time (ms)", mongoDuration.toMillis());
        result.put("PostgreSQL Total Net Amount Average", postgresAverage);
        result.put("MongoDB Total Net Amount Average", mongoAverage);

        return result;
    }

    public Map<String, Object> minAverageAndMeasureTime() {
        Map<String, Object> result = new HashMap<>();

        // PostgreSQL Average Aggregation
        Instant postgresStart = Instant.now();
        Double postgresAverage = postgresDataRepository.minTotalNetAmount();
        Instant postgresEnd = Instant.now();
        Duration postgresDuration = Duration.between(postgresStart, postgresEnd);

        // MongoDB Average Aggregation
        Instant mongoStart = Instant.now();
        Double mongoAverage = mongoDataRepository.minTotalNetAmount();
        Instant mongoEnd = Instant.now();
        Duration mongoDuration = Duration.between(mongoStart, mongoEnd);

        result.put("PostgreSQL Aggregation Time (ms)", postgresDuration.toMillis());
        result.put("MongoDB Aggregation Time (ms)", mongoDuration.toMillis());
        result.put("PostgreSQL Total Net Amount Min", postgresAverage);
        result.put("MongoDB Total Net Amount Min", mongoAverage);

        return result;
    }

    public Map<String, Object> maxAverageAndMeasureTime() {
        Map<String, Object> result = new HashMap<>();

        // PostgreSQL Average Aggregation
        Instant postgresStart = Instant.now();
        Double postgresAverage = postgresDataRepository.maxTotalNetAmount();
        Instant postgresEnd = Instant.now();
        Duration postgresDuration = Duration.between(postgresStart, postgresEnd);

        // MongoDB Average Aggregation
        Instant mongoStart = Instant.now();
        Double mongoAverage = mongoDataRepository.maxTotalNetAmount();
        Instant mongoEnd = Instant.now();
        Duration mongoDuration = Duration.between(mongoStart, mongoEnd);

        result.put("PostgreSQL Aggregation Time (ms)", postgresDuration.toMillis());
        result.put("MongoDB Aggregation Time (ms)", mongoDuration.toMillis());
        result.put("PostgreSQL Total Net Amount Max", postgresAverage);
        result.put("MongoDB Total Net Amount Max", mongoAverage);

        return result;
    }
}
