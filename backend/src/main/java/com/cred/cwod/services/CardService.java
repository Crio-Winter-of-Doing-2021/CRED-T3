package com.cred.cwod.services;

import com.cred.cwod.dto.Card;
import com.cred.cwod.repository.CardRepository;
import com.cred.cwod.repository.UserRepository;
import com.cred.cwod.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardService implements BaseService {

  @Autowired
  private CardRepository cardRepository;

  @Autowired
  private UserRepository userRepository;

  public static boolean isValidCardNumber(String cardNumber) {

    int noOfDigits = cardNumber.length();
    int nSum = 0;

    boolean isSecond = false;

    for (int i = noOfDigits - 1; i >= 0; i--) {
      int d = cardNumber.charAt(i) - '0';
      if (isSecond) {
        d = d * 2;
      }
      // We add two digits to handle
      // cases that make two digits
      // after doubling
      nSum += d / 10;
      nSum += d % 10;
      isSecond = !isSecond;
    }

    return (nSum % 10 == 0);
  }

  /**
   * Get the specific entity by it's object id
   *
   * @param id - object id of entity to be find.
   * @return entity if it is present
   */
  @Override
  public ResponseEntity<?> findEntityById(String id) {
    if (cardRepository.existsById(id)) {
      return ResponseEntity.ok(cardRepository.findById(id).get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Change status of the specific id as Status.INACTIVE
   *
   * @param id - object id of the entity whose status needed to update
   * @return - updated entity as response
   */
  @Override
  public ResponseEntity<?> deleteEntity(String id) {

    if (cardRepository.existsById(id)) {
      Card currentCard = cardRepository.findById(id).get();

      if (currentCard.getStatus().equals(Status.ACTIVE)) {
        currentCard.setStatus(Status.INACTIVE);
        cardRepository.save(currentCard);
        return ResponseEntity.ok().build();
      } else {
        return ResponseEntity.badRequest().build();
      }

    } else {

      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Change status of the specific id as Status.ACTIVE
   *
   * @param id - object id of the entity whose status needed to update
   * @return - updated entity as response
   */
  @Override
  public ResponseEntity<?> restoreEntity(String id) {

    if (cardRepository.existsById(id)) {
      Card currentCard = cardRepository.findById(id).get();
      if (currentCard.getStatus().equals(Status.INACTIVE)) {
        currentCard.setStatus(Status.ACTIVE);
        cardRepository.save(currentCard);
        return ResponseEntity.ok().build();
      } else {
        return ResponseEntity.notFound().build();
      }
    }
    return ResponseEntity.notFound().build();
  }

  public Card addCard(Card card) {

    List<Card> allCardsByUserId = getAllByUserId(card.getUserId());
    for (Card c : allCardsByUserId) {
      if (c.getCardNumber().equals(card.getCardNumber())) {
        return null;
      }
    }

    card.setStatus(Status.ACTIVE);
    cardRepository.save(card);
    return cardRepository.findByCardNumberAndUserId(card.getCardNumber(), card.getUserId());
  }

  public List<Card> getAllByUserId(String userId) {
    List<Card> cards = new ArrayList<>();

    if (cardRepository.findAllByUserId(userId) != null) {
      cards = cardRepository.findAllByUserId(userId);
    }
    return cards;
  }
}
