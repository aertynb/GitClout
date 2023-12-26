import React, { Component } from 'react';
import axios from 'axios';
import { Paper, Typography, Box } from '@mui/material';
import NavBar from './NavBar';
import { LinearProgress, CircularProgress } from '@mui/material';

class DisplayRepository extends Component {
    constructor(props) {
        super(props);
        this.state = {
            idRepository: 1,
            tags: [],
            contributors: [],
            selectedTag: null
        };
    }

    componentDidMount() {
        this.getData();
    }

    getData = () => {
        axios.get('http://localhost:8080/api/repository/1')
             .then(response => {
                console.log("RÉUSSI", response.data);
                this.setState({
                    tags: [],
                    contributors: [],
                });
             })
             .catch(error => {
                console.error('Erreur getData: ', error);
             });
    };

    render() {
        return (
            <div>
                <NavBar />
                <Paper>
                    <Typography variant="h4">DisplayRepository Details</Typography>
                    <Typography variant="h6">Tags:</Typography>
                </Paper>
            </div>
        );
    }
}

export default DisplayRepository;