package com.cred.cwod.dto;

import com.cred.cwod.utils.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Document(collection = "cards")
@AllArgsConstructor
@NoArgsConstructor
public class Card {

  @Id
  private String id;

  @NotNull
  private String cardNumber;

  @NotNull
  @Pattern(regexp = "^(0[1-9]|1[0-2])\\/?([0-9]{2})$")
  private String expiryDate;

  @NotNull
  private String cardholder;

  @NotNull
  private String userId;

  @JsonIgnoreProperties
  private Status status;

}
