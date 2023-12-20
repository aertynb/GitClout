import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import React, { Component } from 'react';
import CommiterList from './CommiterList';
import Home from './Home';
import DataSender from './DataSender';
import DisplayRepository from './DisplayRepository';

class App extends Component {
  render() {
    return (
        <Router>
          <Routes>
            <Route path='/' exact={true} element={<Home />}/>
            <Route path='/commiters' exact={true} element={<CommiterList />}/>
            <Route path='/clone-repo' exact={true} element={<DataSender />}/>
            <Route path='/repository/:id' exact={true} element={<DisplayRepository />}/>
          </Routes>
        </Router>
    )
  }
}

export default App;
