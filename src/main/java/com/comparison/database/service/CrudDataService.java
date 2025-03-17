package com.comparison.database.service;

import java.util.Map;

public interface CrudDataService {

  Map<String, Object> importCsvDataToDatabase();

  Map<String, Object> fetchDataFromDatabase();

  Map<String, Object> editDataToDatabase(
      Long postgresId, String mongoId, Map<String, Object> updates);

  Map<String, Object> deleteDataFromDatabase(Long postgresId, String mongoId);
}
