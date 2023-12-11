import React, { useState } from 'react';
import axios from 'axios';
import NavBar from './NavBar';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

function DataSender() {
    const [link, setLink] = useState('');

    const sendData = () => {
        axios.post('http://localhost:8080/clone-repo/data', link, {
        headers: {
            'Content-Type' : 'text/plain'}
        })
            .then(response => {
                console.log('Réponse du serveur:', response.data);
            })
            .catch(error => {
                console.error('Erreur lors de l\'envoi des données:', error);
            });
    };

    return (
        <div>
            <NavBar/>
            <Box
                component="form"
                sx={{
                    '& > :not(style)': { m: 1, width: '25ch' },
                }}
                noValidate
                autoComplete="off"
            >
                <Typography variant="h4" sx={{ flexGrow: 1}}> Add a repository</Typography>
                <TextField
                    type="text"
                    value={link}
                    onChange={e => setLink(e.target.value)}
                    placeholder="Enter a valid link"
                />
                <Button variant="contained" onClick={sendData}>Send</Button>
            </Box>
        </div>
    );
}

export default DataSender;