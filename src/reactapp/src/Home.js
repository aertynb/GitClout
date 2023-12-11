import React, { Component } from 'react';
import './App.css';
import NavBar from './NavBar';
import { Link } from 'react-router-dom';
import { Button, Container } from 'reactstrap';

class Home extends Component {
    render() {
        return (
            <div>
                <NavBar/>
                <h2>GitClout</h2>
            </div>
        );
    }
}
export default Home;