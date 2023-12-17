import React, {Component} from 'react';
import {Navbar, NavbarBrand} from 'reactstrap';
import {Link} from 'react-router-dom';

import {
    AppBar,
    Button,
    Container,
    Toolbar,
    Typography,
    Box,
} from '@mui/material';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import WbCloudyOutlinedIcon from '@mui/icons-material/WbCloudyOutlined';

const pages = ['Home', 'Commiters']

export default class NavBar extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isOpen: false,
        };
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
                        <IconButton
                            size="large"
                            edge="start"
                            color="inherit"
                            aria-label="menu"
                            sx={{ mr: 2}}
                        >
                            <MenuIcon />
                        </IconButton>
                        <WbCloudyOutlinedIcon sx={{ mr: 2, display: { xs: 'flex', md: 'flex' }, fontFamily: 'monospace' }} />
                        <Button
                            component={Link}
                            to={"/"}
                        >
                            <Typography variant="h5" sx={{ flexGrow: 1, color: 'white', letterSpacing: '.3rem'}}>
                                GitClout
                            </Typography>
                        </Button>
                        <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'flex'} }}>
                            {pages.map((page) => (
                                <Button
                                  key={page}
                                  component={Link}
                                  to={`/${page === 'Home' ? '' : page.toLowerCase()}`}
                                  sx={{ my: 2, color: 'white', display: 'block' }}
                                >
                                    {page}
                                </Button>
                            ))}
                        </Box>
                     </Toolbar>
                 </Container>
            </AppBar>
        </div>
        );
    }
}