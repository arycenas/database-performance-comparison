package com.comparison.database.repository;

import com.comparison.database.model.PostgresData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostgresDataRepository extends JpaRepository<PostgresData, Long> {

  @Override
  void deleteById(Long postgresId);

  @Query("SELECT SUM(p.totalNetAmount) FROM PostgresData p")
  Double sumTotalNetAmount();

  @Query("SELECT AVG(p.totalNetAmount) FROM PostgresData p")
  Double averageTotalNetAmount();

  @Query("SELECT MIN(p.totalNetAmount) FROM PostgresData p")
  Double minTotalNetAmount();

  @Query("SELECT MAX(p.totalNetAmount) FROM PostgresData p")
  Double maxTotalNetAmount();
}
