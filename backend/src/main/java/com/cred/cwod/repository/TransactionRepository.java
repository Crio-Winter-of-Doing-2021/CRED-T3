package com.cred.cwod.repository;

import com.cred.cwod.dto.CardStatement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<CardStatement, String> {

  CardStatement getAllByCardIdAndYearAndMonth(String cardId, String year, String month);
  CardStatement getByCardId(String cardId);
}
