import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { DataGrid } from '@mui/x-data-grid';
import { LinearProgress, CircularProgress } from '@mui/material';

const columns = [
    {
        field: 'repository',
        headerName: 'Repository',
        width: 300,
        renderCell: (params) => <Link to={`/repository/${params.row.id}`}>{params.row.name}</Link>
    },
    { field: 'tags', headerName: 'Tags', width: 300},
];

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

    const rows = repositories.map(repo => ({
        id: repo.id,
        repository: repo.name,
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