import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import React, { Component } from 'react';
import CommiterList from './CommiterList';
import Home from './Home';
import DataSender from './DataSender'

class App extends Component {
  render() {
    return (
        <Router>
          <Routes>
            <Route path='/' exact={true} element={<Home />}/>
            <Route path='/commiters' exact={true} element={<CommiterList />}/>
            <Route path='/clone-repo' exact={true} element={<DataSender />}/>
          </Routes>
        </Router>
    )
  }
}

export default App;
