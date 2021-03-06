package com.cred.cwod.repository;

import com.cred.cwod.dto.Card;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends MongoRepository<Card, String> {

  Card findByCardNumberAndUserId(String cardNumber, String userId);

  List<Card> findAllByUserId(String userId);

}
