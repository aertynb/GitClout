import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import React, { Component } from 'react';
import CommiterList from './CommiterList';
import Home from './Home';
import DataSender from './DataSender'
//import { MatButton, MatProgressSpinner, MatToolbar } from '@angular/material';

class App extends Component {
  render() {
    return (
        <Router>
          <Routes>
            //<mat-toolbar color="primary">
            <Route path='/' exact={true} element={<Home />}/>
            //</mat-toolbar>
            <Route path='/commiters' exact={true} element={<CommiterList />}/>
            /*<Route path='/commiters' exact={true} element={
              <div>
                <MatButton onClick={() => {}}>
                  List commiters
                </MatButton>
                {this.state.showCommiterList && <CommiterList />}
              </div>
            }/>*/
            <Route path='/clone-repo' exact={true} element={<DataSender />}/>
          </Routes>
        </Router>
    )
  }
}

export default App;
