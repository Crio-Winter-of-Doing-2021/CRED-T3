import React, { Component } from 'react';
import { Button, ButtonGroup } from 'reactstrap';
import { Link, withRouter} from 'react-router-dom';
import { MDBDataTableV5 } from 'mdbreact';

import 'react-credit-cards/es/styles-compiled.css';
import 'mdbreact/dist/css/mdb.css';

import CardService from '../services/card.service';

class CardStatement extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            statement: {
                columns: [
                    {
                        label: 'Date (YYYY-MM-DD)',
                        field: 'date',
                        width: 100,
                        sort: 'desc'
                    },
                    {
                        label: 'Description',
                        field: 'details',
                        width: 100,
                        sort: 'disabled'
                    },
                    {
                        label: 'Amount',
                        field: 'amount',
                        width: 100
                    },
                    {
                        label: 'Credit/Debit',
                        field: 'transactionType',
                        width: 100,
                        sort: 'disabled'
                    }
                ],
                rows: []
            },
            transactions: [],
            outstanding: null,            
            isLoading: true,
            message: ''
        };
    }

    async componentDidMount() {
        try {
            const month = '01';
            const year = '2021';
            await CardService.getCardStatement(this.state.id, month, year)
                .then(response => {
                    this.setState({
                        transactions: response.transactions,
                        outstanding: response.currentOutstanding,
                        isLoading: false
                    });
                },
                error => {
                    if (error.response) {
                        this.setState({
                            isLoading: false,
                            message: "No Statements Found",
                            outstanding: 'N/A'
                          })
                    } else {
                        this.setState({
                            isLoading: false,
                            message: "Unable to fetch statement",
                            outstanding: 'N/A'
                          })
                    }
                });
        } catch (error) {
            console.log(error.message);
        }
    }

    getUserId = () => sessionStorage.getItem('userId');
    
    render() {
        const {statement, transactions, outstanding, isLoading } = this.state;
        statement.rows = transactions;

        if (isLoading) {
            return <p>Fetching Card Statement...</p>;
        }

        return (
            
            <div className="App">
                {this.state.message && (
                    <div className="form-group">
                        <div className="alert alert-danger" role="alert">
                            {this.state.message}
                        </div>
                    </div>
                )}

            <span>
                <h4>Card Statement</h4>
                <div align="right">Current outstanding: Rs. {outstanding}
                <ButtonGroup style={{ marginLeft: "500px" }}>
                        <Button size="sm" color="primary" tag={Link} to={"/payments/" + this.state.id + '/' + this.state.outstanding}>Pay now</Button>
                </ButtonGroup>
                </div>
                <MDBDataTableV5
                    hover
                    entriesOptions={[5, 10, 20, 25]} 
                    entries={5}
                    striped
                    bordered
                    materialSearch
                    order={['date', 'desc']}
                    data={statement}
                />
            </span>
            </div>
          );
    }
}

export default withRouter(CardStatement);