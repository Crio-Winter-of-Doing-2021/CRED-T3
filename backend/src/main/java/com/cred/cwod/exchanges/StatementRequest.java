package com.cred.cwod.exchanges;

import com.cred.cwod.dto.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementRequest {
  private List<Transaction> transactions;
}
