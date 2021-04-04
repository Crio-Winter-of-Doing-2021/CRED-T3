import React, { Component } from "react";
import Card from "react-credit-cards";
import { formatExpirationDate } from "../utils/utils";
import { Alert } from "reactstrap";

import "react-credit-cards/es/styles-compiled.css";
import "../CreditCard.css";
import CardService from "../services/card.service";

export default class AddCard extends Component {
  constructor(props) {
    super(props);
    this.state = {
      userId: this.props.match.params.userId,
      cardNumber: "",
      cardholder: "",
      expiryDate: "",
      focus: "",
      submitting: false,
      success: null,
      errorMessage: null,
    };
  }

  handleInputFocus = ({ target }) => {
    this.setState({
      focused: target.name,
    });
  };

  handleInputChange = ({ target }) => {
    if (target.name === "expiryDate") {
      target.value = formatExpirationDate(target.value);
    }

    this.setState({ [target.name]: target.value });
  };

  handleSubmit = (e) => {
    e.preventDefault();
    const card = {
      cardNumber: this.state.cardNumber,
      cardholder: this.state.cardholder,
      expiryDate: this.state.expiryDate,
      userId: this.state.userId,
    };

    CardService.addCard(card).then(
      () => {
        this.setState({
          success: true,
        });

        this.props.history.push("/cards");
        window.location.reload();
      },
      (error) => {
        if (error.response) {
          this.setState({
            errorMessage: "CARD NOT VALID",
          });
        }
      }
    );
    this.form.reset();
  };

  render() {
    return (
      <div className="row-5">
        <center>
          <h4>Add new card</h4>
        </center>
        <div className="col">
          <Card
            number={this.state.cardNumber || ""}
            name={this.state.cardholder || ""}
            expiry={this.state.expiryDate || ""}
            focused={this.state.focus}
          />
        </div>
        <div className="col">
          <form ref={(c) => (this.form = c)} onSubmit={this.handleSubmit}>
            <div className="form-group">
              <input
                type="tel"
                name="cardNumber"
                className="form-control"
                placeholder="Card number"
                pattern="[\d| ]{16,22}"
                required
                onChange={this.handleInputChange}
                onFocus={this.handleInputFocus}
              />
            </div>
            <div className="form-group">
              <input
                type="text"
                name="cardholder"
                className="form-control"
                placeholder="Your name here"
                required
                onChange={this.handleInputChange}
                onFocus={this.handleInputFocus}
              />
            </div>
            <div className="form-group">
              <input
                type="tel"
                name="expiryDate"
                className="form-control"
                placeholder="Valid thru"
                pattern="^(0[1-9]|1[0-2])\/?([0-9]{2})$"
                required
                onChange={this.handleInputChange}
                onFocus={this.handleInputFocus}
              />
            </div>
            <div className="row">
              <div className="col-6">
                <button
                  className="btn btn-primary"
                  type="submit"
                  disabled={this.state.submitting}
                >
                  Submit
                </button>
              </div>
              <div className="col-6">
                <button
                  className="btn btn-secondary"
                  type="button"
                  onClick={this.handleSubmit}
                  disabled={this.state.submitting}
                >
                  Reset
                </button>
              </div>
            </div>
            <div>
              {this.state.success && (
                <div>
                  <Alert color="success">Card added successfully!!</Alert>
                </div>
              )}
              {this.state.errorMessage && (
                <div className="form-group">
                  <div className="alert alert-danger" role="alert">
                    {this.state.errorMessage}
                  </div>
                </div>
              )}
            </div>
          </form>
        </div>
      </div>
    );
  }
}
