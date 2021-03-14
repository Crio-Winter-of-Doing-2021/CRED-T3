package com.cred.cwod.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cardStatement")
public class CardStatement {

  @Id
  private String id;

  @NotNull
  private String cardId;

  @NotNull
  private List<Transaction> transactions;

  @NotNull
  private String year;

  @NotNull
  private String month;

  @NotNull
  private Integer currentOutstanding;

}
