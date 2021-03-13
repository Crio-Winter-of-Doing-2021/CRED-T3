package com.cred.cwod.exchanges;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AddStatementRequest {

  @NotNull
  private MultipartFile statement;

  @NotNull
  private String cardId;
}
