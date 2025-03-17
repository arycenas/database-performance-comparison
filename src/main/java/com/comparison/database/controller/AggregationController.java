package com.comparison.database.controller;

import com.comparison.database.service.AggregationDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/aggregation")
@Tag(name = "Aggregation Controller", description = "Controller for all aggregation functions")
public class AggregationController {

  private final AggregationDataService aggregationDataService;

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
