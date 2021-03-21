package com.cred.cwod.exchanges;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

  private Integer amountPaid;

  private String message;
}
