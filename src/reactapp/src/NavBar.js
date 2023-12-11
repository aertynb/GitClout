import React, {Component} from 'react';
import {Navbar, NavbarBrand} from 'reactstrap';
import {Link} from 'react-router-dom';

import AppBar from '@mui/material/AppBar';
import Container from '@mui/material/Container';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';

const pages = ['Home', 'Commiters']

export default class NavBar extends Component {

    constructor(props) {
        super(props);
        this.state = {isOpen: false};
        this.toggle = this.toggle.bind(this);
    }

    toggle() {
        this.setState({
            isOpen: !this.state.isOpen
        });
    }

    render() {
        return (
        <div>
            <AppBar position="static">
                 <Container maxWidth="xl">
                     <Toolbar>
                        <Typography variant="h5" sx={{ flexGrow: 1}}>
                            GitClout
                        </Typography>
                        <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'flex'} }}>
                            <Menu>
                                {pages.map((page) => (
                                    <MenuItem key={page}>
                                        <Typography textAlign="center">{page}</Typography>
                                    </MenuItem>
                                ))}
                            </Menu>
                        </Box>
                     </Toolbar>
                 </Container>
            </AppBar>
            <Navbar color="dark" dark expand="md">
                <NavbarBrand tag={Link} to="/">Home</NavbarBrand>
            </Navbar>
        </div>
        );
    }
}