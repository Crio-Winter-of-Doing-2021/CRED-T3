import React, { Component } from "react";
import { Button, ButtonGroup } from "reactstrap";
import { Link } from "react-router-dom";
import "react-credit-cards/es/styles-compiled.css";

import Cards from "react-credit-cards";

import "../CreditCard.css";
import axios from "axios";

import AuthService from "../services/auth.service";
import CardService from "../services/card.service";

export default class UserCards extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: AuthService.getCurrentUser(),
      userId: "",
      cardList: [],
      isEmpty: true,
      isLoading: true,
    };
  }

  async componentDidMount() {
    try {
      const [userId, cardResponse] = await axios.all([
        AuthService.getUserIdByUsername(this.state.username),
        CardService.getAllCardsByUsername(this.state.username),
      ]);

      this.setState({
        isEmpty: false,
        userId: userId,
        cardList: cardResponse.cards,
        isLoading: false,
      });
    } catch (error) {
      console.log(error.message);
    }
  }

  getUserId = () => sessionStorage.getItem("userId");

  render() {
    const { cardList, isLoading, isEmpty } = this.state;

    if (isLoading) {
      if (!isEmpty) {
        return (
          <div align="center">
            <h4>Fetching Cards...</h4>
            <div class="spinner-border"></div>
          </div>
        );
      } else {
        return (
          <div>
            <span>No Saved Cards</span>
            <ButtonGroup style={{ marginLeft: "100px" }}>
              <Button
                size="sm"
                color="black"
                tag={Link}
                to={"/addCard/" + sessionStorage.getItem("userId")}
              >
                Add new card
              </Button>
            </ButtonGroup>
          </div>
        );
      }
    }

    const fetchedCards = cardList.map((card) => {
      return (
        <div>
          <p></p>
          <ButtonGroup style={{ marginLeft: "200px" }}>
            <Button
              size="sm"
              color="primary"
              tag={Link}
              to={"/statements/" + card.id}
            >
              Statement
            </Button>
          </ButtonGroup>
          <p></p>
          <Cards
            expiry={card.expiryDate}
            name={card.cardholder}
            number={card.cardNumber}
          />
          <hr />
        </div>
      );
    });

    return (
      <div>
        <ButtonGroup style={{ marginLeft: "100px" }}>
          <Button
            size="sm"
            color="black"
            tag={Link}
            to={"/addCard/" + sessionStorage.getItem("userId")}
          >
            Add new card
          </Button>
        </ButtonGroup>
        <hr />
        <h4>Saved Cards</h4>
        {fetchedCards}
      </div>
    );
  }
}
