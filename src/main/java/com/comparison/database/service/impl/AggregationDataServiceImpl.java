package com.comparison.database.service.impl;

import com.comparison.database.repository.MongoDataRepository;
import com.comparison.database.repository.PostgresDataRepository;
import com.comparison.database.service.AggregationDataService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AggregationDataServiceImpl implements AggregationDataService {

  private static final String POSTGRES_TIME = "PostgreSQL Aggregation Time (ms)";
  private static final String MONGO_TIME = "MongoDB Aggregation Time (ms)";
  private static final String POSTGRES_RESULT = "PostgreSQL Result";
  private static final String MONGO_RESULT = "MongoDB Result";
  private final PostgresDataRepository postgresDataRepository;
  private final MongoDataRepository mongoDataRepository;

  @Override
  public Map<String, Object> sumOperations() {
    return executeAggregation(
        postgresDataRepository::sumTotalNetAmount, mongoDataRepository::sumTotalNetAmount);
  }

  @Override
  public Map<String, Object> averageOperations() {
    return executeAggregation(
        postgresDataRepository::averageTotalNetAmount, mongoDataRepository::averageTotalNetAmount);
  }

  @Override
  public Map<String, Object> minOperations() {
    return executeAggregation(
        postgresDataRepository::minTotalNetAmount, mongoDataRepository::minTotalNetAmount);
  }

  @Override
  public Map<String, Object> maxOperations() {
    return executeAggregation(
        postgresDataRepository::maxTotalNetAmount, mongoDataRepository::maxTotalNetAmount);
  }

  private Map<String, Object> executeAggregation(
      AggregationFunction postgresFunction, AggregationFunction mongoFunction) {
    Map<String, Object> result = new HashMap<>();

    long postgresStart = System.currentTimeMillis();
    Double postgresResult = postgresFunction.execute();
    long postgresTime = System.currentTimeMillis() - postgresStart;

    long mongoStart = System.currentTimeMillis();
    Double mongoResult = mongoFunction.execute();
    long mongoTime = System.currentTimeMillis() - mongoStart;

    result.put(POSTGRES_TIME, postgresTime);
    result.put(MONGO_TIME, mongoTime);
    result.put(POSTGRES_RESULT, postgresResult != null ? postgresResult : 0.0);
    result.put(MONGO_RESULT, mongoResult != null ? mongoResult : 0.0);

    return result;
  }

  @FunctionalInterface
  private interface AggregationFunction {
    Double execute();
  }
}
