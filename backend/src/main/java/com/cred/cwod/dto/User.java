package com.cred.cwod.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  private String id;

  @NotNull
  private String username;

  @NotNull
  private String password;

  @Pattern(regexp = "\\S+@\\S+\\.\\S+")
  private String email;
}
