package com.cred.cwod.exchanges;

import com.cred.cwod.dto.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResponse {
  private List<Card> cards;
}
