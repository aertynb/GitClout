import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Table, TableBody, TableCell, TableContainer, TableRow, TableHead } from '@mui/material';
import { LinearProgress, CircularProgress, Paper } from '@mui/material';

function RepoList() {
    const [repositories, setRepositories] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch('/api/repository');
                const data = await response.json();
                setRepositories(data);
                setIsLoading(false);
            } catch(error) {
                console.error('Error fetching data:', error);
                setIsLoading(false);
            }
        };
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

    return (
        <div style={{ margin: '20px auto', maxWidth: 'calc(100% - 60px)' }}>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Repository</TableCell>
                            <TableCell>Tags</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {repositories.map((repository) => (
                            <TableRow key={repository.id} component={Link} to={`api/repository/${repository.id}`}>
                                <TableCell component="th">{repository.name}</TableCell>
                                <TableCell>{'Tags 1'}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}

export default RepoList;