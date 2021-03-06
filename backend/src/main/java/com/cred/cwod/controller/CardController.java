package com.cred.cwod.controller;

import com.cred.cwod.dto.Card;
import com.cred.cwod.exchanges.BaseResponse;
import com.cred.cwod.exchanges.CardResponse;
import com.cred.cwod.services.CardService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@Log4j2
public class CardController {

  public static final String CARD_BASE_URL = "/cards";
  public static final String STATEMENT_URL = "/{id}/statements/{year}/{month}";

  @Autowired
  private CardService cardService;

  @ApiResponses({
      @ApiResponse(code = 200, message = "OK", response = BaseResponse.class),
      @ApiResponse(code = 400, message = "Validation Error"),
      @ApiResponse(code = 500, message = "Server Error")
  })
  @PostMapping(CARD_BASE_URL)
  public ResponseEntity<BaseResponse<Card>> addCard(@Valid @RequestBody Card card) {
    Card savedCard = cardService.addCard(card);

    if (!CardService.isValidCardNumber(card.getCardNumber())) {
      return ResponseEntity.badRequest().body(new BaseResponse<>(null, "CARD NOT VALID"));
    } else if (savedCard == null) {
      return ResponseEntity.badRequest().body(new BaseResponse<>(null, "ALREADY EXIST"));
    } else {
      return ResponseEntity.ok().body(new BaseResponse<>(savedCard, "SAVED"));
    }
  }

  @ApiResponses({
      @ApiResponse(code = 200, message = "OK", response = CardResponse.class),
      @ApiResponse(code = 400, message = "Validation Error"),
      @ApiResponse(code = 500, message = "Server Error"),
      @ApiResponse(code = 403, message = "Incorrect Credentials")
  })
  @GetMapping(CARD_BASE_URL + "/{userId}")
  public ResponseEntity<CardResponse> getAllCardsByUserId(@PathVariable String userId) {
    CardResponse response = new CardResponse();
    List<Card> cards = cardService.getAllByUserId(userId);
    if (cards != null && cards.size() > 0) {
      response.setCards(cards);
      return ResponseEntity.ok(response);
    } else {
      return ResponseEntity.badRequest().body(response);
    }
  }
}
