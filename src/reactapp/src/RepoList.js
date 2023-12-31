import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Table, TableBody, TableCell, TableContainer, TableRow, TableHead } from '@mui/material';
import { Chip, LinearProgress, CircularProgress, Paper, Typography } from '@mui/material';

function RepoList() {
    const [repositories, setRepositories] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();

    const fetchData = async () => {
        try {
          setIsLoading(true);
          const response = await fetch('/api/repository');
          const data = await response.json();
          setRepositories(data);
          setIsLoading(false);
        } catch (error) {
          console.error('Error fetching data:', error);
          setIsLoading(false);
        }
      };

      useEffect(() => {
          fetchData();
        }, []);


    if (isLoading) {
        return (
            <div>
                <LinearProgress />
                <p>Loading...</p>
                <CircularProgress disableShrink />
            </div>
        );
    }

    const handleRefresh = () => {
        fetchData();
    }

    const handleRowClick = (id) => {
        navigate(`repository/${id}`);
    };

    return (
        <div style={{ margin: '20px auto', maxWidth: 'calc(100% - 60px)' }}>
            <Typography variant="h4" sx={{ flexGrow: 1}}>List of available repositories:</Typography>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow style={{ background: '#212121'}}>
                            <TableCell style={{ color: 'white'}}>Repository</TableCell>
                            <TableCell style={{ color: 'white'}}>Tags</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {repositories.map((repository) => (
                            <TableRow key={repository.id} onClick={() => handleRowClick(repository.id)} style={{ cursor: 'pointer' }}>
                                <TableCell component="th">{repository.name}</TableCell>
                                <TableCell>{repository.tags.map((tag) => (
                                    <Chip label={tag.name} variant="outlined" />
                                ))}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <button onClick={handleRefresh}>Refresh</button>
        </div>
    );
}

export default RepoList;