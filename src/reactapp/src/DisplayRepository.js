import React, { Component } from 'react';
import axios from 'axios';
import { Chip, Paper, Typography, Box } from '@mui/material';
import NavBar from './NavBar';
import LocalOffer from '@mui/icons-material/LocalOffer';
import ContributionBar from './ContributionBar';

class DisplayRepository extends Component {
    constructor(props) {
        super(props);
        this.state = {
            idRepository: props.idRepository,
            tags: [],
            contributors: [],
            selectedTag: null
        };
    }

    componentDidMount() {
        this.getData();
    }

    getData = () => {
        const idRepository = window.location.pathname.split('/').pop();

        console.log(idRepository)

        axios.get(`http://localhost:8080/api/repository/${idRepository}`)
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

    sectionStyle = {
        overflowX: 'auto',
        padding: '200px',
    };

    handleTagClick = (tag) => {
        this.setState({ selectedTag: tag }, () => {
            console.log("Tag sélectionné : ", this.state.selectedTag);
            console.log("Contributions du tag sélectionné: ", this.state.selectedTag.contributions);
        });
    };

    render() {
        const {tags, selectedTag } = this.state;
        return (
            <div>
                <NavBar />
                <Typography variant="h4" style={{ textAlign: 'center'}}>asm</Typography>
                <br/>
                <div style={this.sectionStyle}>
                    <Typography variant="h6">Tags:</Typography>
                    <Paper style={{ overflow: 'hidden', padding: '8px' }}>
                            <div style={{ overflowX: 'auto', display: 'flex', alignItems: 'center', padding: '4px'}}>
                                {tags.map((tag) => (
                                    <Chip icon={<LocalOffer />} key={tag.id} label={tag.name} style={{ margin: '0 4px'}} onClick={() => this.handleTagClick(tag)} />
                                ))}
                            </div>
                    </Paper>
                    <br/>
                    {selectedTag ? (
                        <ContributionBar contributions={ selectedTag.contributions } />
                    ) : null}
                </div>
            </div>
        );
    }
}

export default DisplayRepository;