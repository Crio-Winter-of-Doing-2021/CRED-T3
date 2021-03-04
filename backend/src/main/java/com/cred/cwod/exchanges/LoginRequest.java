package com.cred.cwod.exchanges;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginRequest {

  @NotNull
  private String username;

  @NotNull
  private String password;
}
