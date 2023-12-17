import React, { Component } from 'react';
import './App.css';
import NavBar from './NavBar';
import DataSender from './DataSender';
import RepoList from './RepoList';
import { Link } from 'react-router-dom';

class Home extends Component {
    render() {
        return (
            <div>
                <NavBar/>
                <h2 style={{ textAlign: 'center'}}>GitClout</h2>
                <br/>
                <DataSender/>
                <h3>List of available repositories:</h3>
                <RepoList/>
            </div>
        );
    }
}
export default Home;