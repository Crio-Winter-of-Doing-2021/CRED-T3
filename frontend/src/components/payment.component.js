import React, { Component } from "react";
import "react-credit-cards/es/styles-compiled.css";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import CardService from "../services/card.service";

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        Amount cannot be empty!
      </div>
    );
  } else if (value < 1) {
    return (
      <div className="alert alert-danger" role="alert">
        Amount must be greater than zero!
      </div>
    );
  }
};

export default class Payment extends Component {
  constructor(props) {
    super(props);

    this.onChangeAmount = this.onChangeAmount.bind(this);
    this.handlePayment = this.handlePayment.bind(this);

    this.state = {
      id: this.props.match.params.id,
      outstanding: this.props.match.params.outstanding,
      amount: null,
      message: "",
    };
  }

  onChangeAmount(e) {
    this.setState({
      amount: e.target.value,
    });
  }

  handlePayment(e) {
    e.preventDefault();

    this.form.validateAll();

    const currOutstanding = this.state.outstanding;
    if (this.checkBtn.context._errors.length === 0) {
      CardService.makeCardPayment(this.state.id, this.state.amount).then(
        (response) => {
          this.setState({
            outstanding: currOutstanding - response.amountPaid,
            message: "Payment Successful",
          });
          this.props.match.params.outstanding = this.state.outstanding;
        },
        (error) => {
          if (error.response) {
            this.setState({
              loading: false,
              message: "Payment failed!",
            });
          } else {
            this.setState({
              loading: false,
              message: "Retry",
            });
          }
        }
      );
    } else {
      this.setState({
        amount: 0,
      });
    }
  }

  render() {
    return (
      <div>
        <span>Pay now</span>
        <hr />
        <span>Outstanding amount: Rs. {this.state.outstanding}</span>
        <Form
          class="form-inline"
          onSubmit={this.handlePayment}
          ref={(c) => {
            this.form = c;
          }}
        >
          <div class="form-group">
            <label for="inlineFormCustomSelectPref" class="my-1 mr-2">
              Rs:{" "}
            </label>
            <Input
              type="number"
              class="form-control form-control-sm"
              name="amount"
              value={this.state.amount}
              onChange={this.onChangeAmount}
              validations={[required]}
              placeholder="enter amount"
            />
          </div>
          <div>
            <button type="submit" class="btn btn-primary btn-sm">
              <span>pay</span>
            </button>
          </div>
          {this.state.message && (
            <div className="form-group">
              <div className="alert alert-danger" role="alert">
                {this.state.message}
              </div>
            </div>
          )}
          <CheckButton
            style={{ display: "none" }}
            ref={(c) => {
              this.checkBtn = c;
            }}
          />
        </Form>
      </div>
    );
  }
}
