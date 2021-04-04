import axios from "axios";
import authHeader from "./auth-header";
import { config } from "../utils/constants";

var CARD_API_URL = config.url.CARD_API_URL;

class CardService {
  async getAllCardsByUsername(username) {
    const response = await axios.get(CARD_API_URL + "get/" + username, {
      headers: authHeader(),
    });
    console.log(response);
    return response.data;
  }

  async makeCardPayment(cardId, amount) {
    return await axios
      .post(
        CARD_API_URL + cardId + "/pay",
        {
          amount: amount,
        },
        {
          headers: authHeader(),
        }
      )
      .then((response) => {
        return response.data;
      });
  }

  async getCardStatement(cardId, month, year) {
    return await axios
      .get( CARD_API_URL + cardId + '/statements/' + year + '/' + month,
        {
          headers: authHeader(),
        }
      )
      .then((response) => {
        return response.data;
      });
  }

  async addCard(card) {
    return await axios
      .post( CARD_API_URL,
        {
          cardNumber: card.cardNumber,
          cardholder: card.cardholder,
          expiryDate: card.expiryDate,
          userId: card.userId,
        },
        {
          headers: authHeader(),
        }
      )
      .then((response) => {
        return response;
      });
  }
}

export default new CardService();
