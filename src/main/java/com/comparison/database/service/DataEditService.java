package com.comparison.database.service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comparison.database.model.MongoData;
import com.comparison.database.model.PostgresData;
import com.comparison.database.repository.MongoDataRepository;
import com.comparison.database.repository.PostgresDataRepository;

@Service
public class DataEditService {

    @Autowired
    private MongoDataRepository mongoDataRepository;

    @Autowired
    private PostgresDataRepository postgresDataRepository;

    public Map<String, Object> editData(Long postgresId, String mongoId, MongoData newMongoData,
            PostgresData newPostgresData) {
        Map<String, Object> result = new HashMap<>();

        // Edit PostgreSQL data
        Instant postgresStart = Instant.now();
        Optional<PostgresData> optionalPostgresData = postgresDataRepository.findById(postgresId);
        if (optionalPostgresData.isPresent()) {
            PostgresData postgresData = optionalPostgresData.get();
            copyNonNullProperties(newPostgresData, postgresData);
            postgresDataRepository.save(postgresData);
        }
        Instant postgresEnd = Instant.now();
        Duration postgresDuration = Duration.between(postgresStart, postgresEnd);

        // Edit MongoDB data
        Instant mongoStart = Instant.now();
        ObjectId objectId = new ObjectId(mongoId);
        Optional<MongoData> optionalMongoData = mongoDataRepository.findById(objectId);
        if (optionalMongoData.isPresent()) {
            MongoData mongoData = optionalMongoData.get();
            copyNonNullProperties(newMongoData, mongoData);
            mongoDataRepository.save(mongoData);
        }
        Instant mongoEnd = Instant.now();
        Duration mongoDuration = Duration.between(mongoStart, mongoEnd);

        long postgresEditTime = postgresDuration.toMillis();
        long mongoEditTime = mongoDuration.toMillis();

        result.put("PostgreSQL Edit Time (ms)", postgresEditTime);
        result.put("MongoDB Edit Time (ms)", mongoEditTime);

        return result;
    }

    private void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
