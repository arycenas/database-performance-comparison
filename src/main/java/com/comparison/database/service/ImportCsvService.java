package com.comparison.database.service;

import java.io.File;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.comparison.database.model.MongoData;
import com.comparison.database.model.PostgresData;
import com.comparison.database.repository.MongoDataRepository;
import com.comparison.database.repository.PostgresDataRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@Service
public class ImportCsvService {

    @Value("${file.scan.directory}")
    private String scanDirectory;

    @Autowired
    private MongoDataRepository mongoDataRepository;

    @Autowired
    private PostgresDataRepository postgresDataRepository;

    @Scheduled(fixedRate = 10000)
    public Map<String, Object> scanAndImport() {
        long totalPostgresTime = 0;
        long totalMongoTime = 0;
        int fileCount = 0;

        try {
            File directory = new File(scanDirectory);
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".csv"));

            if (files != null) {
                fileCount = files.length;
                for (File file : files) {
                    Map<String, Long> durations = importCsv(file);
                    totalPostgresTime += durations.getOrDefault("PostgreSQL Time (ms)", 0L);
                    totalMongoTime += durations.getOrDefault("MongoDB Time (ms)", 0L);
                }
            } else {
                System.out.println("No CSV Files");
                System.out.println(scanDirectory);
            }
        } catch (Exception e) {
        }

        long avgPostgresTime = fileCount > 0 ? totalPostgresTime / fileCount : 0;
        long avgMongoTime = fileCount > 0 ? totalMongoTime / fileCount : 0;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("MongoDB Average Time (ms)", avgMongoTime);
        result.put("PostgreSQL Total Time (ms)", totalPostgresTime);
        result.put("MongoDB Total Time (ms)", totalMongoTime);
        result.put("Files Processed", fileCount);
        result.put("PostgreSQL Average Time (ms)", avgPostgresTime);

        return result;
    }

    public Map<String, Long> importCsv(File file) {
        List<MongoData> mongoDataList = new ArrayList<>();
        List<PostgresData> postgresDataList = new ArrayList<>();
        Map<String, Long> durations = new LinkedHashMap<>();

        try (FileReader fileReader = new FileReader(file);
                CSVReader csvReader = new CSVReaderBuilder(fileReader).withSkipLines(0).build()) {
            String[] headers = csvReader.readNext();
            String[] values;

            while ((values = csvReader.readNext()) != null) {
                PostgresData postgresData = new PostgresData();
                MongoData mongoData = new MongoData();
                for (int i = 0; i < headers.length; i++) {
                    String fieldName = headers[i];
                    String fieldValue = values[i];

                    switch (fieldName) {
                        case "COMPANY_CODE" -> {
                            postgresData.setCOMPANY_CODE(fieldValue);
                            mongoData.setCOMPANY_CODE(fieldValue);
                        }
                        case "RECEIVE_NUMBER" -> {
                            postgresData.setRECEIVE_NUMBER(Long.valueOf(fieldValue));
                            mongoData.setRECEIVE_NUMBER(Long.valueOf(fieldValue));
                        }
                        case "RECEIVE_DATE" -> {
                            postgresData.setRECEIVE_DATE(fieldValue);
                            mongoData.setRECEIVE_DATE(fieldValue);
                        }
                        case "ORDER_LAYOUT_TYPE" -> {
                            postgresData.setORDER_LAYOUT_TYPE(fieldValue);
                            mongoData.setORDER_LAYOUT_TYPE(fieldValue);
                        }
                        case "TOTAL_GROSS_AMOUNT" -> {
                            postgresData.setTOTAL_GROSS_AMOUNT(Double.valueOf(fieldValue));
                            mongoData.setTOTAL_GROSS_AMOUNT(Double.valueOf(fieldValue));
                        }
                        case "TOTAL_DISCOUNT_AMOUNT" -> {
                            postgresData.setTOTAL_DISCOUNT_AMOUNT(Double.valueOf(fieldValue));
                            mongoData.setTOTAL_DISCOUNT_AMOUNT(Double.valueOf(fieldValue));
                        }
                        case "TOTAL_NET_AMOUNT" -> {
                            postgresData.setTOTAL_NET_AMOUNT(Double.valueOf(fieldValue));
                            mongoData.setTOTAL_NET_AMOUNT(Double.valueOf(fieldValue));
                        }
                        case "TOTAL_TAX_AMOUNT" -> {
                            postgresData.setTOTAL_TAX_AMOUNT(Double.valueOf(fieldValue));
                            mongoData.setTOTAL_TAX_AMOUNT(Double.valueOf(fieldValue));
                        }
                        case "PACKING_POSTING_AMOUNT" -> {
                            postgresData.setPACKING_POSTING_AMOUNT(Double.valueOf(fieldValue));
                            mongoData.setPACKING_POSTING_AMOUNT(Double.valueOf(fieldValue));
                        }
                        case "PACKING_POSTING_TAX_AMOUNT" -> {
                            postgresData.setPACKING_POSTING_TAX_AMOUNT(Double.valueOf(fieldValue));
                            mongoData.setPACKING_POSTING_TAX_AMOUNT(Double.valueOf(fieldValue));
                        }
                        case "TOTAL_INVOICE_AMOUNT" -> {
                            postgresData.setTOTAL_INVOICE_AMOUNT(Double.valueOf(fieldValue));
                            mongoData.setTOTAL_INVOICE_AMOUNT(Double.valueOf(fieldValue));
                        }
                        case "TOTAL_DETAIL_LINE_COUNT" -> {
                            postgresData.setTOTAL_DETAIL_LINE_COUNT(Double.valueOf(fieldValue));
                            mongoData.setTOTAL_DETAIL_LINE_COUNT(Double.valueOf(fieldValue));
                        }
                        default -> {
                        }
                    }
                }
                postgresDataList.add(postgresData);
                mongoDataList.add(mongoData);
            }
        } catch (Exception e) {
            return durations;
        }

        Instant start = Instant.now();
        postgresDataRepository.saveAll(postgresDataList);
        Instant postgresEnd = Instant.now();

        mongoDataRepository.saveAll(mongoDataList);
        Instant mongoEnd = Instant.now();

        Duration postgresDuration = Duration.between(start, postgresEnd);
        Duration mongoDuration = Duration.between(postgresEnd, mongoEnd);

        durations.put("PostgreSQL Time (ms)", postgresDuration.toMillis());
        durations.put("MongoDB Time (ms)", mongoDuration.toMillis());

        System.out.println("Data imported from: " + file.getName() + ". PostgreSQL time: " + postgresDuration.toMillis()
                + " ms, MongoDB time: " + mongoDuration.toMillis() + " ms.");

        return durations;
    }
}