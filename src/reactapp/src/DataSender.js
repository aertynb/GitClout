import React, { useState } from 'react';
import axios from 'axios';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import LinearProgress from '@mui/material/LinearProgress';
import SendIcon from '@mui/icons-material/Send';

function DataSender() {
    const [link, setLink] = useState('');
    const [isDataAdded, setIsDataAdded] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [progress, setProgress] = useState(0);

    const sendData = () => {
        setLoading(true);
        const config = {
            onUploadProgress: (progressEvent) => {
                const percent = Math.round((progressEvent.loaded * 100) / progressEvent.total);
                setProgress(percent);
            },
            headers: {
                'Content-Type': 'text/plain',
            },
        };
        axios.post('http://localhost:8080/clone-repo/data', link, config)
            .then(response => {
                console.log('Réponse du serveur:', response.data);
                setIsDataAdded(true);
                setError(null);
                setLoading(false);
                if (response.status === 200) {
                    setProgress(0);
                }
            })
            .catch(error => {
                console.error('Erreur lors de l\'envoi des données:', error);
                setIsDataAdded(false);
                setError(error.message || 'Une erreur s\'est produite.');
                setLoading(false);
                setProgress(0);
            });
    };

    return (
        <div>
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
                <Button variant="contained" onClick={sendData} endIcon={<SendIcon />}>Send</Button>
                {loading && (
                    <div>
                        <LinearProgress />
                    </div>
                )}
                {isDataAdded && (
                    <div>
                        <Typography variant="body1" color="#69f0ae">
                            A repository has been added. Please refresh the page to see the changes.
                        </Typography>
                    </div>
                )}

                {error && (
                    <Typography variant="body1" color="error">
                        Error: Please enter a URL.
                    </Typography>
                )}
            </Box>
        </div>
    );
}

export default DataSender;