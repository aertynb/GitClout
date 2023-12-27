import React, { Component } from 'react';
import axios from 'axios';
import { Chip, Paper, Typography, Box } from '@mui/material';
import NavBar from './NavBar';
import { LinearProgress, CircularProgress } from '@mui/material';
import LocalOffer from '@mui/icons-material/LocalOffer';

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
                    tags: response.data.tags,
                    contributors: [],
                });
             })
             .catch(error => {
                console.error('Erreur getData: ', error);
             });
    };

    firstSectionStyle = {
        overflowX: 'auto',
        padding: '200px',
    };

    handleTagClick = (tag) => {
        this.setState({ selectedTag: tag });
        console.log("Tag sélectionné : ", tag);
    };

    render() {
        const {tags} = this.state;
        return (
            <div>
                <NavBar />
                <Typography variant="h4" style={{ textAlign: 'center'}}>asm</Typography>
                <br/>
                <div style={this.firstSectionStyle}>
                    <Typography variant="h6">Tags:</Typography>
                    <Paper style={{ overflow: 'hidden', padding: '8px' }}>
                            <div style={{ overflowX: 'auto', display: 'flex', alignItems: 'center', padding: '4px'}}>
                                {tags.map((tag) => (
                                    <Chip icon={<LocalOffer />} key={tag.id} label={tag.name} style={{ margin: '0 4px'}} onClick={() => this.handleTagClick(tag)} />
                                ))}
                            </div>
                    </Paper>
                </div>
            </div>
        );
    }
}

export default DisplayRepository;