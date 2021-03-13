package com.cred.cwod.controller;

import com.cred.cwod.dto.Card;
import com.cred.cwod.exchanges.BaseResponse;
import com.cred.cwod.exchanges.CardResponse;
import com.cred.cwod.exchanges.UploadMediaResponse;
import com.cred.cwod.services.CardService;
import com.cred.cwod.services.MediaService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@Log4j2
public class CardController {

  public static final String CARD_BASE_URL = "/cards";
  public static final String STATEMENT_URL = "/{id}/statements/{year}/{month}";

  private static final String DOC_TYPE = "pdf";

  @Autowired
  private CardService cardService;

  @Autowired
  private MediaService mediaService;


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


  /**
   * TODO: Create POST /cards/{id}/statements/{year}/{month}
   * Add monthly pdf statement for a card by its id.
   * Request body should contain - PDF Monthly Media
   *
   */
  @ApiResponses({
      @ApiResponse(code = HttpServletResponse.SC_OK, message = "OK", response = UploadMediaResponse.class),
      @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Validation Error"),
      @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Server Error"),
      @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Incorrect Credentials")
  })
  @PostMapping(CARD_BASE_URL + STATEMENT_URL)
  public ResponseEntity<UploadMediaResponse> addCardStatement(@PathVariable String id, @PathVariable String year,
                                                            @PathVariable String month,
                                                            @RequestBody MultipartFile statement) {

    String fileName = mediaService.storeFile(statement, id, DOC_TYPE);

    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/statement/").path(fileName)
        .toUriString();

    UploadMediaResponse response = new UploadMediaResponse(fileName, fileDownloadUri, statement.getContentType());

    return ResponseEntity.ok(response);
  }


  /**
   * TODO: Create GET /cards/{id}/statements/{year}/{month}
   * Fetch the statement summary
   */
}
