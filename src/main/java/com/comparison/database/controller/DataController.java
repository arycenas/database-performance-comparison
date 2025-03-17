package com.comparison.database.controller;

import com.comparison.database.service.AggregationDataService;
import com.comparison.database.service.CrudDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/data")
@Tag(
    name = "Data Controller",
    description = "Controller for all operations both CRUD and Aggregation")
public class DataController {

  private final CrudDataService crudDataService;
  private final AggregationDataService aggregationDataService;

  private static final String ERROR_MESSAGE = "Error";

  @PostMapping("/import")
  @Operation(summary = "Import Data from CSV to Database")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Data imported successfully",
            content = @Content(schema = @Schema(implementation = String.class))),
      })
  public ResponseEntity<Map<String, Object>> importData() {
    return ResponseEntity.ok(crudDataService.importCsvDataToDatabase());
  }

  @GetMapping("/fetch")
  @Operation(summary = "Fetch Data from Database")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Data fetched successfully",
            content = @Content(schema = @Schema(implementation = String.class))),
      })
  public ResponseEntity<Map<String, Object>> fetchData() {
    return ResponseEntity.ok(crudDataService.fetchDataFromDatabase());
  }

  @PatchMapping("/edit/{postgresId}/{mongoId}")
  @Operation(summary = "Edit Data from both PostgreSQL and MongoDB")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Data updated successfully",
            content = @Content(schema = @Schema(implementation = String.class))),
      })
  public ResponseEntity<Map<String, Object>> editData(
      @PathVariable Long postgresId,
      @PathVariable String mongoId,
      @RequestBody Map<String, Object> updates) {
    if (updates == null || updates.isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of(ERROR_MESSAGE, "Updates cannot be empty"));
    }

    try {
      Map<String, Object> result = crudDataService.editDataToDatabase(postgresId, mongoId, updates);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException e) {
      log.warn("Invalid input for Postgres ID: {} and Mongo ID: {}", postgresId, mongoId, e);
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    } catch (Exception e) {
      log.error("Error updating data for Postgres ID: {} and Mongo ID: {}", postgresId, mongoId, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "Failed to update data"));
    }
  }

  @DeleteMapping("/delete/{postgresId}/{mongoId}")
  @Operation(summary = "Delete Data from both PostgreSQL and MongoDB")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Data deleted successfully",
            content = @Content(schema = @Schema(implementation = String.class))),
      })
  public ResponseEntity<Map<String, Object>> deleteData(
      @PathVariable Long postgresId, @PathVariable String mongoId) {
    return ResponseEntity.ok(crudDataService.deleteDataFromDatabase(postgresId, mongoId));
  }

  @GetMapping("/sum")
  @Operation(summary = "Sum Operation for Total Net Amount Column from Both PostgreSQL and MongoDB")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Data operation success",
            content = @Content(schema = @Schema(implementation = String.class))),
      })
  public ResponseEntity<Map<String, Object>> getSumAggregation() {
    return ResponseEntity.ok(aggregationDataService.sumOperations());
  }

  @GetMapping("/average")
  @Operation(
      summary = "Average Operation for Total Net Amount Column from Both PostgreSQL and MongoDB")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Data operation success",
            content = @Content(schema = @Schema(implementation = String.class))),
      })
  public ResponseEntity<Map<String, Object>> getAverageAggregation() {
    return ResponseEntity.ok(aggregationDataService.averageOperations());
  }

  @GetMapping("/min")
  @Operation(summary = "Min Operation for Total Net Amount Column from Both PostgreSQL and MongoDB")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Data operation success",
            content = @Content(schema = @Schema(implementation = String.class))),
      })
  public ResponseEntity<Map<String, Object>> getMinAggregation() {
    return ResponseEntity.ok(aggregationDataService.minOperations());
  }

  @GetMapping("/max")
  @Operation(summary = "Max Operation for Total Net Amount Column from Both PostgreSQL and MongoDB")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Data operation success",
            content = @Content(schema = @Schema(implementation = String.class))),
      })
  public ResponseEntity<Map<String, Object>> getMaxAggregation() {
    return ResponseEntity.ok(aggregationDataService.maxOperations());
  }
}
