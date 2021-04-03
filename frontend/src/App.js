import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import './App.css';

import Login from './components/login.component';
import UserCards from './components/cards.component';
import AuthService from './services/auth.service';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      currentUser: null
    };
  }

  componentDidMount() {
    const user = AuthService.getCurrentUser();
    if (user) {
      this.setState({
        currentUser : user
      });
    }
  }

  render() {
    return (
      <Router>
        <div className="auth-wrapper">
          <div className="auth-inner">
            <Switch>
              <Route path='/login' exact={true} component={Login} />
              <Route path='/cards' exact={true} component={UserCards} />
            </Switch>
          </div>
        </div>
      </Router>
    )
  }
}

export default App;
