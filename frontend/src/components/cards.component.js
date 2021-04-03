import React, { Component } from 'react';
import { Button, ButtonGroup, Container } from 'reactstrap';
import { Link } from 'react-router-dom';
import 'react-credit-cards/es/styles-compiled.css';

import Cards from 'react-credit-cards';

import '../CreditCard.css';
import axios from 'axios';

import AuthService from '../services/auth.service';
import CardService from '../services/card.service';

export default class UserCards extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: AuthService.getCurrentUser(),
            userId: '',
            cardList: [],
            isReloaded: false,
            isLoading: true
        };
    }

    async componentDidMount() {
        try {
            const [userId, cardResponse] = 
                await axios.all([AuthService.getUserIdByUsername(this.state.username),
                 CardService.getAllCardsByUsername(this.state.username)]);
                this.setState({
                    userId: userId,
                    cardList: cardResponse,
                    isLoading: false
                })

            console.log(cardResponse);

        } catch (error) {
            console.log(error.message);
        }
    }

    getUserId = () => sessionStorage.getItem('userId');
    
    render() {
        const { cardList, isLoading } = this.state;
        
        if (isLoading) {
            return <p>Fetching Cards...</p>;
        }

        const fetchedCards = cardList.map(card => {
            return (
                <div>
                    <p></p>
                    <ButtonGroup style={{ marginLeft: "200px" }}>
                        <Button size="sm" color="primary" tag={Link} to={"/payments/" + card.id}>Make Payment</Button>
                    </ButtonGroup>
                    <p></p>
                    <Cards
                        expiry={card.expiryDate}
                        name={card.cardholder}
                        number={card.cardNumber}
                    />
                    <hr />
                    <p></p>
                    <ButtonGroup style={{ marginLeft: "200px" }}>
                        <Button size="sm" color="primary" tag={Link} to={"/payments/" + card.id}>Make Payment</Button>
                    </ButtonGroup>
                    <p></p>
                    <Cards
                        expiry={card.expiryDate}
                        name={card.cardholder}
                        number={card.cardNumber}
                    />
                    <hr />
                    
                </div>
            )
        });

        return (
            <div id="PaymentForm">
                <h4>Saved Cards</h4>
                {fetchedCards}
            </div>
        );
    }
}