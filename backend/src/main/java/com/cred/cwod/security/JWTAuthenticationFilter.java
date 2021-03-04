package com.cred.cwod.security;

import com.auth0.jwt.JWT;
import com.cred.cwod.dto.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.cred.cwod.security.SecurityConstants.*;

/**
 * JSON Web Tokens, are tokens that are used to authenticate users on applications. It enables backends to accept
 * requests simply by validating the contents of these JWTS.
 * <p>
 * In this class we have implemented an authentication filter to issue JWTS to users sending credentials.
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req,
                                              HttpServletResponse res) throws AuthenticationException {
    try {
      User user = new ObjectMapper()
          .readValue(req.getInputStream(), User.class);

      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              user.getUsername(),
              user.getPassword(),
              new ArrayList<>())
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req,
                                          HttpServletResponse res,
                                          FilterChain chain,
                                          Authentication auth) {


    String token = JWT.create()
        .withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .sign(HMAC512(SECRET.getBytes()));

    res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
  }
}

