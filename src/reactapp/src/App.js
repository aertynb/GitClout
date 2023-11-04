import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import React, { Component } from 'react';
import CommiterList from './CommiterList';
import Home from './Home';


class App extends Component {
  render() {
    return (
        <Router>
          <Routes>
            <Route path='/' exact={true} element={<Home />}/>
            <Route path='/commiters' exact={true} element={<CommiterList />}/>
          </Routes>
        </Router>
    )
  }
}

export default App;