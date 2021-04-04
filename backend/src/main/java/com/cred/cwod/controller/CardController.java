package com.cred.cwod.controller;

import com.cred.cwod.dto.Card;
import com.cred.cwod.dto.CardStatement;
import com.cred.cwod.dto.User;
import com.cred.cwod.exchanges.*;
import com.cred.cwod.services.CardService;
import com.cred.cwod.services.MediaService;
import com.cred.cwod.services.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@Log4j2
public class CardController {

  public static final String CARD_BASE_URL = "/cards";
  public static final String STATEMENT_URL = "/{id}/statements/{year}/{month}";
  public static final String MAKE_PAYMENT_URL = "/{id}/pay";

  private static final String DOC_TYPE = "pdf";

  @Autowired
  private CardService cardService;

  @Autowired
  private UserService userService;


  @ApiResponses({
      @ApiResponse(code = HttpServletResponse.SC_OK, message = "Saved", response = BaseResponse.class),
      @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Validation Error"),
      @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Server Error")
  })
  @PostMapping(CARD_BASE_URL)
  public ResponseEntity<BaseResponse<Card>> addCard(@Valid @RequestBody Card card) {

    // Check if the card number is valid or not.
    if (!CardService.isValidCardNumber(card.getCardNumber())) {
      return ResponseEntity.badRequest().body(new BaseResponse<>(null, "CARD NOT VALID"));
    }

    Card savedCard = cardService.addCard(card);

    if (savedCard == null) {
      return ResponseEntity.badRequest().body(new BaseResponse<>(null, "ALREADY EXIST"));
    } else {
      return ResponseEntity.ok().body(new BaseResponse<>(savedCard, "SAVED"));
    }
  }

  @ApiResponses({
      @ApiResponse(code = HttpServletResponse.SC_OK, message = "OK", response = CardResponse.class),
      @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Validation Error"),
      @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Server Error"),
      @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Incorrect Credentials")
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

  @ApiResponses({
      @ApiResponse(code = HttpServletResponse.SC_OK, message = "OK", response = CardResponse.class),
      @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Validation Error"),
      @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Server Error"),
      @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Incorrect Credentials")
  })
  @GetMapping(CARD_BASE_URL + "/get" + "/{username}")
  public ResponseEntity<CardResponse> getAllCardsByUsername(@PathVariable String username) {
    CardResponse response = new CardResponse();
    User foundUser = userService.findUserId(username);

    if (foundUser == null) {
      return ResponseEntity.badRequest().body(response);
    }

    List<Card> cards = cardService.getAllByUserId(foundUser.getId());

    if (cards != null && cards.size() > 0) {
      response.setCards(cards);
      return ResponseEntity.ok(response);
    } else {
      return ResponseEntity.badRequest().body(response);
    }
  }


  /**
   * TODO: Create POST /cards/{id}/statements/{year}/{month}
   * As
   */
  @ApiResponses({
      @ApiResponse(code = HttpServletResponse.SC_OK, message = "OK", response = CardStatement.class),
      @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Validation Error"),
      @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Server Error"),
      @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Incorrect Credentials")
  })
  @PostMapping(CARD_BASE_URL + STATEMENT_URL)
  public ResponseEntity<CardStatement> addCardStatement(@PathVariable String id, @PathVariable String year,
                                                        @PathVariable String month,
                                                        @Valid @RequestBody StatementRequest statement) {
    CardStatement response = cardService.updateCardStatement(id, year, month, statement);

    if (response == null) {
      return ResponseEntity.badRequest().body(null);
    } else {
      return ResponseEntity.ok(response);
    }
  }


  /**
   * TODO: Create GET /cards/{id}/statements/{year}/{month}
   * Fetch the statement summary
   */
  @ApiResponses({
      @ApiResponse(code = HttpServletResponse.SC_OK, message = "OK", response = CardStatement.class),
      @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Validation Error"),
      @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Server Error"),
      @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Incorrect Credentials")
  })
  @GetMapping(CARD_BASE_URL + STATEMENT_URL)
  public ResponseEntity<CardStatement> getCardStatement(@PathVariable String id, @PathVariable String year,
                                                        @PathVariable String month) {
    CardStatement response = cardService.getCardStatement(id, year, month);

    if (response == null) {
      return ResponseEntity.badRequest().body(null);
    } else {
      return ResponseEntity.ok(response);
    }
  }

  /**
   * TODO: Create API POST /cards/{id}/pay, to make payments for specific cards using it's id
   *
   *
   */

  @PostMapping(CARD_BASE_URL + MAKE_PAYMENT_URL)
  public ResponseEntity<PaymentResponse> makePayment(@PathVariable String id, @Valid @RequestBody PaymentRequest paymentRequest) {
    // Check if the card id is valid or not.
    return cardService.pay(id, paymentRequest);
  }
}
