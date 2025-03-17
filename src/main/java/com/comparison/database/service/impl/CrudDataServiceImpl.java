package com.comparison.database.service.impl;

import com.comparison.database.model.MongoData;
import com.comparison.database.model.PostgresData;
import com.comparison.database.repository.MongoDataRepository;
import com.comparison.database.repository.PostgresDataRepository;
import com.comparison.database.service.CrudDataService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrudDataServiceImpl implements CrudDataService {

  private final PostgresDataRepository postgresDataRepository;
  private final MongoDataRepository mongoDataRepository;

  @Value("${file.scan.directory}")
  private String scanDirectory;

  private static final String POSTGRES_TIME = "PostgreSQL Time (ms)";
  private static final String MONGODB_TIME = "MongoDB Time (ms)";

  @Override
  public Map<String, Object> importCsvDataToDatabase() {
    File[] files =
        Optional.ofNullable(new File(scanDirectory).listFiles((_, name) -> name.endsWith(".csv")))
            .orElse(new File[0]);
    int fileCount = files.length;

    List<Map<String, Long>> importResults = Arrays.stream(files).map(this::importCsv).toList();

    long totalPostgresTime =
        importResults.stream()
            .mapToLong(durations -> durations.getOrDefault(POSTGRES_TIME, 0L))
            .sum();

    long totalMongoTime =
        importResults.stream()
            .mapToLong(durations -> durations.getOrDefault(MONGODB_TIME, 0L))
            .sum();

    log.info("CSV import completed: {} files processed", fileCount);

    return resultMapping(totalPostgresTime, totalMongoTime, fileCount);
  }

  @Override
  public Map<String, Object> fetchDataFromDatabase() {
    long postgresTime =
        measureTime(
            () -> {
              List<PostgresData> postgresDataList = postgresDataRepository.findAll();
              log.info("PostgreSQL: {} data found", postgresDataList.size());
            });
    long mongoDbTime =
        measureTime(
            () -> {
              List<MongoData> mongoDataList = mongoDataRepository.findAll();
              log.info("MongoDB: {} data found", mongoDataList.size());
            });

    return resultMapping(postgresTime, mongoDbTime, null);
  }

  @Override
  public Map<String, Object> editDataToDatabase(
      Long postgresId, String mongoId, Map<String, Object> updates) {
    long postgresTime =
        measureTime(
            () ->
                postgresDataRepository
                    .findById(postgresId)
                    .ifPresentOrElse(
                        postgresData -> {
                          updateEntityFields(postgresData, updates);
                          postgresDataRepository.save(postgresData);
                        },
                        () -> log.warn("PostgreSQL Data with ID: {} not found", postgresId)));

    long mongoDbTime =
        measureTime(
            () ->
                mongoDataRepository
                    .findById(new ObjectId(mongoId))
                    .ifPresentOrElse(
                        mongoData -> {
                          updateEntityFields(mongoData, updates);
                          mongoDataRepository.save(mongoData);
                        },
                        () -> log.warn("MongoDB Data with ID: {} not found", mongoId)));

    return resultMapping(postgresTime, mongoDbTime, null);
  }

  @Override
  public Map<String, Object> deleteDataFromDatabase(Long postgresId, String mongoId) {
    // Delete from PostgreSQL
    long postgresTime =
        measureTime(
            () -> {
              if (postgresDataRepository.existsById(postgresId)) {
                postgresDataRepository.deleteById(postgresId);
              } else log.warn("PostgreSQL Data with ID: {} not found", postgresId);
            });

    // Delete from MongoDB
    long mongoDbTime =
        measureTime(
            () -> {
              if (mongoDataRepository.existsById(new ObjectId(mongoId))) {
                mongoDataRepository.deleteById(new ObjectId(mongoId));
              } else log.warn("MongoDB Data with ID: {} not found", mongoId);
            });

    return resultMapping(postgresTime, mongoDbTime, null);
  }

  @Transactional
  protected Map<String, Long> importCsv(File file) {
    List<MongoData> mongoDataList = new ArrayList<>();
    List<PostgresData> postgresDataList = new ArrayList<>();

    try (FileReader fileReader = new FileReader(file);
        CSVReader csvReader = new CSVReaderBuilder(fileReader).withSkipLines(0).build()) {
      String[] headers = csvReader.readNext();
      csvReader.forEach(
          values -> {
            PostgresData postgresData = new PostgresData();
            MongoData mongoData = new MongoData();
            dataMapping(headers, values, postgresData, mongoData);
            postgresDataList.add(postgresData);
            mongoDataList.add(mongoData);
          });
    } catch (Exception e) {
      log.error("Error reading CSV data", e);
    }

    long postgresTime = measureTime(() -> postgresDataRepository.saveAll(postgresDataList));
    long mongoDbTime = measureTime(() -> mongoDataRepository.saveAll(mongoDataList));

    log.info(
        "Data imported from: {}. PostgreSQL time: {} ms, MongoDB time: {} ms.",
        file.getName(),
        postgresTime,
        mongoDbTime);

    Map<String, Long> durations = new LinkedHashMap<>();
    durations.put(POSTGRES_TIME, postgresTime);
    durations.put(MONGODB_TIME, mongoDbTime);

    return durations;
  }

  private void dataMapping(
      String[] headers, String[] values, PostgresData postgresData, MongoData mongoData) {
    for (int i = 0; i < headers.length; i++) {
      String fieldName = headers[i];
      String fieldValue = values[i];

      switch (fieldName) {
        case "COMPANY_CODE" -> {
          postgresData.setCompanyCode(fieldValue);
          mongoData.setCompanyCode(fieldValue);
        }
        case "RECEIVE_NUMBER" -> {
          postgresData.setReceiveNumber(Long.valueOf(fieldValue));
          mongoData.setReceiveNumber(Long.valueOf(fieldValue));
        }
        case "RECEIVE_DATE" -> {
          postgresData.setReceiveDate(fieldValue);
          mongoData.setReceiveDate(fieldValue);
        }
        case "ORDER_LAYOUT_TYPE" -> {
          postgresData.setOrderLayoutType(fieldValue);
          mongoData.setOrderLayoutType(fieldValue);
        }
        case "TOTAL_GROSS_AMOUNT" -> {
          postgresData.setTotalGrossAmount(Double.valueOf(fieldValue));
          mongoData.setTotalGrossAmount(Double.valueOf(fieldValue));
        }
        case "TOTAL_DISCOUNT_AMOUNT" -> {
          postgresData.setTotalDiscountAmount(Double.valueOf(fieldValue));
          mongoData.setTotalDiscountAmount(Double.valueOf(fieldValue));
        }
        case "TOTAL_NET_AMOUNT" -> {
          postgresData.setTotalNetAmount(Double.valueOf(fieldValue));
          mongoData.setTotalNetAmount(Double.valueOf(fieldValue));
        }
        case "TOTAL_TAX_AMOUNT" -> {
          postgresData.setTotalTaxAmount(Double.valueOf(fieldValue));
          mongoData.setTotalTaxAmount(Double.valueOf(fieldValue));
        }
        case "PACKING_POSTING_AMOUNT" -> {
          postgresData.setPackingPostingAmount(Double.valueOf(fieldValue));
          mongoData.setPackingPostingAmount(Double.valueOf(fieldValue));
        }
        case "PACKING_POSTING_TAX_AMOUNT" -> {
          postgresData.setPackingPostingTaxAmount(Double.valueOf(fieldValue));
          mongoData.setPackingPostingTaxAmount(Double.valueOf(fieldValue));
        }
        case "TOTAL_INVOICE_AMOUNT" -> {
          postgresData.setTotalInvoiceAmount(Double.valueOf(fieldValue));
          mongoData.setTotalInvoiceAmount(Double.valueOf(fieldValue));
        }
        case "TOTAL_DETAIL_LINE_COUNT" -> {
          postgresData.setTotalDetailLineCount(Double.valueOf(fieldValue));
          mongoData.setTotalDetailLineCount(Double.valueOf(fieldValue));
        }
        default -> log.warn("Unknown field: {}", fieldName);
      }
    }
  }

  private Map<String, Object> resultMapping(
      Long totalPostgresTime, Long totalMongoTime, Integer fileCount) {
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("PostgreSQL Total Time (ms)", totalPostgresTime);
    result.put("MongoDB Total Time (ms)", totalMongoTime);
    if (fileCount != null) {
      result.put("PostgreSQL Average Time (ms)", calculateAverage(totalPostgresTime, fileCount));
      result.put("MongoDB Average Time (ms)", calculateAverage(totalMongoTime, fileCount));
      result.put("Files Processed", fileCount);
    }

    return result;
  }

  private long calculateAverage(long totalTime, int fileCount) {
    return fileCount > 0 ? totalTime / fileCount : 0;
  }

  private long measureTime(Runnable operation) {
    Instant start = Instant.now();
    operation.run();

    return Duration.between(start, Instant.now()).toMillis();
  }

  private void updateEntityFields(Object entity, Map<String, Object> updates) {
    updates.forEach(
        (key, value) -> {
          try {
            String setterMethodName =
                "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1);
            Method setterMethod = entity.getClass().getMethod(setterMethodName, value.getClass());
            setterMethod.invoke(entity, value);
          } catch (NoSuchMethodException e) {
            log.warn("Setter method {} not found in {}", key, entity.getClass().getSimpleName());
          } catch (IllegalAccessException | InvocationTargetException e) {
            log.warn(
                "Failed to invoke setter for {} in {}", key, entity.getClass().getSimpleName());
          }
        });
  }
}
