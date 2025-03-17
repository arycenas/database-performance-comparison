package com.comparison.database.repository;

import com.comparison.database.model.MongoData;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoDataRepository extends MongoRepository<MongoData, ObjectId> {

  @Override
  Optional<MongoData> findById(ObjectId objectId);

  @Override
  void deleteById(ObjectId objectId);

  @Aggregation("{ $group: { _id: null, total: { $sum: { $toDouble: '$TOTAL_NET_AMOUNT' } } } }")
  Double sumTotalNetAmount();

  @Aggregation("{ $group: { _id: null, average: { $avg: { $toDouble: '$TOTAL_NET_AMOUNT' } } } }")
  Double averageTotalNetAmount();

  @Aggregation("{ $group: { _id: null, min: { $min: { $toDouble: '$TOTAL_NET_AMOUNT' } } } }")
  Double minTotalNetAmount();

  @Aggregation("{ $group: { _id: null, max: { $max: { $toDouble: '$TOTAL_NET_AMOUNT' } } } }")
  Double maxTotalNetAmount();
}
