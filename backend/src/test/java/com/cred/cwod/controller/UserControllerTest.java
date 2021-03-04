package com.cred.cwod.controller;

import com.cred.cwod.RunApplication;
import com.cred.cwod.dto.User;
import com.cred.cwod.exchanges.LoginRequest;
import com.cred.cwod.security.SecurityConstants;
import com.cred.cwod.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.cred.cwod.controller.UserController.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {RunApplication.class})
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource("file:backend/src/main/resources/application.properties")
public class UserControllerTest {

  private String userId;

  private String token;

  private User signupUser;

  private User loginUser;

  private int portNumber = 8081;

  private RequestSpecification specification;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @BeforeClass
  public void setup() {
    mapper = new ObjectMapper();

    signupUser = new User();
    signupUser.setUsername("user");
    signupUser.setPassword("user");

    MockitoAnnotations.initMocks(this);

    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @BeforeClass
  public void authorization() {
    loginUser = new User();
    loginUser.setEmail("email@mail.com");
    loginUser.setUsername("test");
    loginUser.setPassword("test");
    loginUser.setId("120faw34fgty");

    userId = loginUser.getId();
    token =
        given()
            .basePath(LOGIN_URL)
            .port(portNumber)
            .contentType("application/json")
            .body(loginUser)
            .when()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .header(SecurityConstants.HEADER_STRING);

    specification =
        new RequestSpecBuilder()
            .addHeader(SecurityConstants.HEADER_STRING, token)
            .setBasePath(GET_ID + "/" + loginUser.getUsername())
            .setPort(portNumber)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
  }

  @AfterClass
  public void cleanUp() {

  }


  @Test
  public void userSignupTest() throws Exception {
    // If user provide a valid email id then it should return SUCCESS.
    signupUser.setEmail("user@test.com");
    Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(signupUser);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(SIGNUP_URL)
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8").content(this.mapper.writeValueAsBytes(signupUser));

    mockMvc.perform(builder).andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.data.username", is(signupUser.getUsername())));
  }

  @Test
  public void userSignupEmailValidationError() throws Exception {
    // If user provide a invalid email id then it should return CLIENT_SIDE_ERROR.
    signupUser.setEmail("");
    Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(signupUser);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(SIGNUP_URL)
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8").content(this.mapper.writeValueAsBytes(signupUser));

    mockMvc.perform(builder).andExpect(status().is4xxClientError());
  }

  @Test
  public void userLoginMustGenerateJwtToken() throws Exception {
    loginUser = new User();
    loginUser.setPassword("test");
    loginUser.setUsername("test");

    Mockito.when(userService.validateUser(Mockito.any(LoginRequest.class))).thenReturn(loginUser);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(LOGIN_URL)
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8").content(this.mapper.writeValueAsBytes(loginUser));

    mockMvc.perform(builder).andExpect(status().is2xxSuccessful());
  }


  @Test
  public void getUserIdWithJwtToken() throws Exception {

    Mockito.when(userService.findUserId(Mockito.any(String.class))).thenReturn(loginUser);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(GET_ID + "/" + loginUser.getUsername())
        .accept(MediaType.APPLICATION_JSON)
        .header(SecurityConstants.HEADER_STRING, token);

    mockMvc.perform(builder).andExpect(status().is2xxSuccessful());
  }


  @Test
  public void getUserIdWithoutJwtTokenMustReturnUnauthorizedError() {

    given()
        .basePath(GET_ID + "/" + loginUser.getUsername())
        .port(portNumber)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .get()
        .then()
        .statusCode(HttpStatus.FORBIDDEN.value());
  }
}
