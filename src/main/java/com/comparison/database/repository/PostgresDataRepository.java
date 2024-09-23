package com.comparison.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.comparison.database.model.PostgresData;

@Repository
public interface PostgresDataRepository extends JpaRepository<PostgresData, Long> {

    @SuppressWarnings("null")
    @Override
    void deleteById(Long postgresId);

    @Query("SELECT SUM(p.TOTAL_NET_AMOUNT) FROM PostgresData p")
    Double sumTotalNetAmount();

    @Query("SELECT AVG(p.TOTAL_NET_AMOUNT) FROM PostgresData p")
    Double averageTotalNetAmount();

    @Query("SELECT MIN(p.TOTAL_NET_AMOUNT) FROM PostgresData p")
    Double minTotalNetAmount();

    @Query("SELECT MAX(p.TOTAL_NET_AMOUNT) FROM PostgresData p")
    Double maxTotalNetAmount();
}
