import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { DataGrid } from '@mui/x-data-grid';
import { LinearProgress, CircularProgress } from '@mui/material';

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

    const columns = [
        {
            field: 'name',
            headerName: 'Repository',
            width: 300,
        },
        {
            field: 'tags',
            headerName: 'Tags',
            width: 300
        },
    ];

    const rows = repositories.map(repository => ({
        id: repository.id,
        name: repository.name,
        tags: 'Tags 1'
    }));

    return (
        <div style={{ margin: '20px auto', maxWidth: 'calc(100% - 60px)' }}>
            <DataGrid
                rows={rows}
                columns={columns}
                pageSize={10}
                autoHeight
            />
        </div>
    );
}

export default RepoList;