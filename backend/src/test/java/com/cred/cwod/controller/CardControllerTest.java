package com.cred.cwod.controller;

import com.cred.cwod.RunApplication;
import com.cred.cwod.dto.Card;
import com.cred.cwod.dto.User;
import com.cred.cwod.services.CardService;
import com.cred.cwod.utils.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.cred.cwod.controller.CardController.CARD_BASE_URL;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {RunApplication.class})
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CardControllerTest {

  private final int portNumber = 8081;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mvc;

  @Mock
  private CardService cardService;

  @InjectMocks
  private CardController cardController;

  private Card card;

  private List<Card> listOfCards;

  private String userId;

  private String token;


  @BeforeClass
  public void initialSetup() {
    mapper = new ObjectMapper();

    MockitoAnnotations.initMocks(this);
    mvc = MockMvcBuilders.standaloneSetup(cardController).build();

    User loginUser = new User();
    loginUser.setEmail("email@mail.com");
    loginUser.setUsername("test");
    loginUser.setPassword("test");
    loginUser.setId("120faw34fgty");

    userId = loginUser.getId();

    // Initialize the card
    card = new Card();
    card.setStatus(Status.ACTIVE);
    card.setCardNumber("4024007113290893");
    card.setCardholder(loginUser.getUsername());
    card.setExpiryDate("11/21");
    card.setId("qwedfg56hy09f");
    card.setUserId(loginUser.getId());

    listOfCards = new ArrayList<>();
    listOfCards.add(card);
  }

  @Test
  public void addCardTest() throws Exception {

    card.setCardNumber("4024007113290893"); // If the card number if valid

    Mockito.when(cardService.addCard(Mockito.any(Card.class))).thenReturn(card);
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .post(CARD_BASE_URL)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8")
        .content(this.mapper.writeValueAsBytes(card));

    mvc.perform(builder)
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.data.cardNumber", is(card.getCardNumber())));
  }

  @Test
  public void addCardNotValidCardNumber_is4xx() throws Exception {

    card.setCardNumber("123456789189135");

    Mockito.when(cardService.addCard(Mockito.any(Card.class))).thenReturn(card);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .post(CARD_BASE_URL)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8")
        .content(this.mapper.writeValueAsBytes(card));

    mvc.perform(builder)
        .andExpect(status().is4xxClientError());

  }

  @Test
  public void getAllCardsByUserIdTest() throws Exception {

    Mockito.when(cardService.getAllByUserId(Mockito.any(String.class))).thenReturn(listOfCards);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .get(CARD_BASE_URL + "/" + userId)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON);

    mvc.perform(builder)
        .andExpect(status().is2xxSuccessful());
  }
}
