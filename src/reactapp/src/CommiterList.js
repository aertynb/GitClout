import React, { Component } from 'react';
import { Container, Table } from 'reactstrap';
import NavBar from './NavBar';
import LinearProgress from '@mui/material/LinearProgress';
import CircularProgress from '@mui/material/CircularProgress';
import { DataGrid } from '@mui/x-data-grid';

class CommiterList extends Component {

    constructor(props) {
        super(props);
        this.state = {commiters: []};
    }

    componentDidMount() {
        fetch('/api/commiters')
            .then(response => response.json())
            .then(data => this.setState({commiters: data}));
    }

    render() {
        const {commiters, isLoading} = this.state;
        const columns = [
            { field: 'name', headerName: "Name", width: 300},
            { field: 'email', headerName: "Email", width: 300}
        ];
        const rows = commiters.map(commiter => ({
            id: commiter.id,
            name: commiter.name,
            email: commiter.email,
        }));

        if (isLoading) {
            return (
            <div>
                <NavBar />
                <LinearProgress />
                <p>Loading...</p>;
                <CircularProgress disableShrink />
            </div>
            );
        }

        return (
            <div>
                <NavBar/>
                <Container fluid>
                    <h3>Commiters</h3>
                    <div style= {{ margin: '20px auto', maxWidth: 'calc(100% - 60px)' }} >
                        <DataGrid
                            columns={columns}
                            rows={rows}
                            pageSize={5}
                        />
                    </div>
                </Container>
            </div>
        );
    }
}
export default CommiterList;