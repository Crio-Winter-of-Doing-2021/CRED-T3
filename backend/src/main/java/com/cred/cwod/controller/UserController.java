package com.cred.cwod.controller;

import com.cred.cwod.dto.User;
import com.cred.cwod.exchanges.BaseResponse;
import com.cred.cwod.exchanges.LoginRequest;
import com.cred.cwod.exchanges.LoginResponse;
import com.cred.cwod.services.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@Validated
@Log4j2
public class UserController {

  public static final String SIGNUP_URL = "/signup";
  public static final String LOGIN_URL = "/login";
  public static final String GET_ID = "/getUserId";

  @Autowired
  private UserService userService;

  @ApiResponses({
      @ApiResponse(code = HttpServletResponse.SC_OK, message = "OK", response = BaseResponse.class),
      @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Validation Error"),
      @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Server Error"),
      @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Username & email already exist")
  })
  @PostMapping(SIGNUP_URL)
  public ResponseEntity<BaseResponse<User>> signup(@Valid @RequestBody User user) {
    BaseResponse<User> response;

    User createdUser = userService.createUser(user);
    if (createdUser == null) {
      response = new BaseResponse<>(null, "USER_ALREADY_EXIST");
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    } else {
      response = new BaseResponse<>(createdUser, "USER_SIGNED_UP");
      return ResponseEntity.ok(response);
    }
  }

  @ApiResponses({
      @ApiResponse(code = HttpServletResponse.SC_OK, message = "OK"),
      @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Validation Error"),
      @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Server Error"),
      @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Incorrect Credentials")
  })
  @PostMapping(LOGIN_URL)
  public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {

    User validatedUser = userService.validateUser(loginRequest);
    if (validatedUser != null) {
      return new LoginResponse(validatedUser.getUsername(), validatedUser.getId());
    } else {
      return null;
    }
  }

  @ApiResponses({
      @ApiResponse(code = HttpServletResponse.SC_OK, message = "OK", response = BaseResponse.class),
      @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Validation Error"),
      @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Server Error"),
      @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Incorrect Credentials")
  })
  @GetMapping(GET_ID + "/{username}")
  public ResponseEntity<BaseResponse<String>> getUserId(@PathVariable String username) {
    BaseResponse<String> response;

    User user = userService.findUserId(username);

    if (user != null) {
      response = new BaseResponse<>(user.getId(), "FOUND");
      return ResponseEntity.ok(response);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
