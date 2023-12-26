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
                <br/>
                <DataSender/>
                <RepoList/>
            </div>
        );
    }
}
export default Home;