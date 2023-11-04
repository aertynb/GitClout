import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import NavBar from './NavBar';
import { Link } from 'react-router-dom';

class CommitertList extends Component {

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
            return <p>Loading...</p>;
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
                    <h3>Clients</h3>
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
export default CommitertList;