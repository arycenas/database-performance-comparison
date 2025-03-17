package com.comparison.database.service;

import java.util.Map;

public interface AggregationDataService {

  Map<String, Object> sumOperations();

  Map<String, Object> averageOperations();

  Map<String, Object> minOperations();

  Map<String, Object> maxOperations();
}
