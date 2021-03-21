package com.cred.cwod.dto;

import com.cred.cwod.utils.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @NotNull(message = "Date & Time of transaction must be in UTC")
  private Date date;

  @NotNull
  private String details;

  @NotNull
  private Integer amount;

  @NotNull
  private TransactionType transactionType;

}
