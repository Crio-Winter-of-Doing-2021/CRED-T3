import React, { Component } from "react";
import { BrowserRouter as Router, Route, Switch, Link } from "react-router-dom";

import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import Login from "./components/login.component";
import UserCards from "./components/cards.component";
import AuthService from "./services/auth.service";
import Payment from "./components/payment.component";
import CardStatement from "./components/statement.component";
import AddCard from "./components/addCard.component";
import Register from "./components/register.component";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      currentUser: null,
    };
  }

  componentDidMount() {
    const user = AuthService.getCurrentUser();
    if (user) {
      this.setState({
        currentUser: user
      });
    }
  }

  render() {
    return (
      <Router>
      <div>
        <nav className="navbar navbar-expand navbar-dark bg-dark">
          {this.state.currentUser ? (
            <div className="navbar-nav ml-auto">
              <li className="nav-item">
                <Link to={"/cards"} className="nav-link"></Link>
              </li>
              <li className="nav-item">
                <a href="/" className="nav-link" onClick={AuthService.logout}>
                  LogOut
                </a>
              </li>
            </div>
          ) : (
            <div className="navbar-nav ml-auto">
              <li className="nav-item">
                <Link to={"/"} className="nav-link">
                  Login
                </Link>
              </li>
              <li className="nav-item">
                <Link to={"/signup"} className="nav-link">
                  Sign Up
                </Link>
              </li>
            </div>
          )}
        </nav>

        
          <div className="auth-wrapper">
            <div className="auth-inner">
              <Switch>
                <Route path="/" exact={true} component={Login} />
                <Route path="/signup" exact={true} component={Register} />
                <Route path="/cards" exact={true} component={UserCards} />
                <Route
                  path="/statements/:id"
                  exact={true}
                  component={CardStatement}
                />
                <Route
                  path="/payments/:id/:outstanding"
                  exact={true}
                  component={Payment}
                />
                <Route
                  path="/addCard/:userId"
                  exact={true}
                  component={AddCard}
                />
              </Switch>
            </div>
          </div>
        
      </div>
      </Router>
    );
  }
}

export default App;
