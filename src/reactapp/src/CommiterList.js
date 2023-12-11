import React, { Component } from 'react';
import { Container, Table } from 'reactstrap';
import NavBar from './NavBar';
import LinearProgress from '@mui/material/LinearProgress';
import CircularProgress from '@mui/material/CircularProgress';

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

        const commiterList = commiters.map(commiters => {
            return <tr key={commiters.id}>
                <td style={{whiteSpace: 'nowrap'}}>{commiters.name}</td>
                <td>{commiters.email}</td>
            </tr>
        });

        return (
            <div>
                <NavBar/>
                <Container fluid>
                    <h3>Commiters</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="30%">Name</th>
                            <th width="30%">Email</th>
                        </tr>
                        </thead>
                        <tbody>
                        {commiterList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}
export default CommiterList;