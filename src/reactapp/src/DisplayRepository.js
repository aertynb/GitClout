import React, { Component } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { Paper, Typography, Box } from '@mui/material';
import NavBar from './NavBar';
import { LinearProgress, CircularProgress } from '@mui/material';

class DisplayRepository extends Component {
    constructor(props) {
        super(props);
        this.state = {
            idRepository: this.props.match.params.id,
            tags: [],
            contributors: [],
            selectedTag: null
        };
    }

    componentDidMount() {
        fetch('/api/repository/${this.state.idRepository}')
            .then(response => response.json())
            .then(data => this.setState({tags: data}));
    }

    render() {
        const { tags, contributors, selectedTag } = this.state;

        return (
            <div>
                <NavBar />

            </div>
        );
    }
}

export default DisplayRepository;