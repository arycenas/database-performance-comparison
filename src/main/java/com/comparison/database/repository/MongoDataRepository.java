package com.comparison.database.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.comparison.database.model.MongoData;

@Repository
public interface MongoDataRepository extends MongoRepository<MongoData, ObjectId> {

    @SuppressWarnings("null")
    @Override
    public Optional<MongoData> findById(ObjectId objectId);

    @SuppressWarnings("null")
    @Override
    public void deleteById(ObjectId objectId);

    @Aggregation("{ $group: { _id: null, total: { $sum: { $toDouble: '$TOTAL_NET_AMOUNT' } } } }")
    Double sumTotalNetAmount();

    @Aggregation("{ $group: { _id: null, average: { $avg: { $toDouble: '$TOTAL_NET_AMOUNT' } } } }")
    Double averageTotalNetAmount();
}
