package com.comparison.database.service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comparison.database.repository.MongoDataRepository;
import com.comparison.database.repository.PostgresDataRepository;

@Service
public class DataDeleteService {

    @Autowired
    private MongoDataRepository mongoDataRepository;

    @Autowired
    private PostgresDataRepository postgresDataRepository;

    public Map<String, Object> deleteData(Long postgresId, String mongoId) {
        Map<String, Object> result = new HashMap<>();

        // Delete from PostgreSQL
        Instant postgresStart = Instant.now();
        postgresDataRepository.deleteById(postgresId);
        Instant postgresEnd = Instant.now();
        Duration postgresDuration = Duration.between(postgresStart, postgresEnd);

        // Delete from MongoDB
        Instant mongoStart = Instant.now();
        mongoDataRepository.deleteById(new ObjectId(mongoId));
        Instant mongoEnd = Instant.now();
        Duration mongoDuration = Duration.between(mongoStart, mongoEnd);

        long postgresDeleteTime = postgresDuration.toMillis();
        long mongoDeleteTime = mongoDuration.toMillis();

        result.put("PostgreSQL Delete Time (ms)", postgresDeleteTime);
        result.put("MongoDB Delete Time (ms)", mongoDeleteTime);

        return result;
    }
}
