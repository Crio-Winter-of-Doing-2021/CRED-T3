package com.cred.cwod.exchanges;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

  @NotNull
  @Min(value = 1, message = "Amount must be greater than 0")
  private Integer amount;
}
