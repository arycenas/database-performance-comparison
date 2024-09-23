package com.comparison.database.controller;

import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comparison.database.model.MongoData;
import com.comparison.database.model.PostgresData;
import com.comparison.database.service.DataAggregateService;
import com.comparison.database.service.DataDeleteService;
import com.comparison.database.service.DataEditService;
import com.comparison.database.service.DataFetchService;
import com.comparison.database.service.ImportCsvService;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private ImportCsvService importCsvService;

    @Autowired
    private DataFetchService dataFetchService;

    @Autowired
    private DataEditService dataEditService;

    @Autowired
    private DataDeleteService dataDeleteService;

    @Autowired
    private DataAggregateService dataAggregateService;

    @PostMapping("/import")
    public Map<String, Object> importData() {
        return importCsvService.scanAndImport();
    }

    @GetMapping("/fetch")
    public Map<String, Object> fetchData() {
        return dataFetchService.fetchDataAndMeasureTime();
    }

    @PatchMapping("/edit/{postgresId}/{mongoId}")
    public Map<String, Object> editData(@PathVariable Long postgresId, @PathVariable String mongoId,
            @RequestBody Map<String, Object> updates) {
        MongoData newMongoData = new MongoData();
        PostgresData newPostgresData = new PostgresData();

        updates.forEach((key, value) -> {
            try {
                Field mongoField = MongoData.class.getDeclaredField(key);
                mongoField.setAccessible(true);
                mongoField.set(newMongoData, value);

                Field postgresField = PostgresData.class.getDeclaredField(key);
                postgresField.setAccessible(true);
                postgresField.set(newPostgresData, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
            }
        });

        return dataEditService.editData(postgresId, mongoId, newMongoData, newPostgresData);
    }

    @DeleteMapping("/delete/{postgresId}/{mongoId}")
    public Map<String, Object> deleteData(@PathVariable Long postgresId, @PathVariable String mongoId) {
        return dataDeleteService.deleteData(postgresId, mongoId);
    }

    @GetMapping("/sum")
    public Map<String, Object> getSumAggregation() {
        return dataAggregateService.aggregateSumAndMeasureTime();
    }

    @GetMapping("/average")
    public Map<String, Object> getAverageAggregation() {
        return dataAggregateService.aggregateAverageAndMeasureTime();
    }

    @GetMapping("/min")
    public Map<String, Object> getMinAggregation() {
        return dataAggregateService.minAverageAndMeasureTime();
    }

    @GetMapping("/max")
    public Map<String, Object> getMaxAggregation() {
        return dataAggregateService.maxAverageAndMeasureTime();
    }
}
