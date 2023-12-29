import React, { Component, useState } from 'react';
import { Box, MenuItem, TextField, Typography } from '@mui/material';
import { BarChart, ChartsXAxis, ChartsYAxis, ChartsTooltip } from '@mui/x-charts';

function ContributionBar({ contributions }) {
    const availableMode = ['total lines', 'code lines', 'build files', 'CI files', 'resource files', 'documentation files'];
    const [mode, setMode] = useState('total lines');
    const data = [];

    if (contributions.length === 0) {
        return (
            <div>
                <Typography variant="h6">Contributions:</Typography>
                <Typography>No contributions available.</Typography>
            </div>
        );
    }

    contributions.forEach(contribution => {
        const commiterName = contribution.commiter.name;
        const index = data.findIndex(contribution => contribution.commiterName === commiterName);
        if (index !== -1) {
            data[index].addedLines += contribution.addedLines;
        } else {
            data.push({
                commiterName,
                addedLines: contribution.addedLines,
            });
        }
    });

    return (
        <div>
            <Typography variant="h6">Contributions:</Typography>
            <TextField
                sx={{ minWidth: 100, mr: 5, mt: 2 }}
                id="mode-select"
                select
                label="mode"
                value={mode}
                onChange={(event) => setMode(event.target.value)}
            >
                {availableMode.map((item) => (
                    <MenuItem key={item} value={item}>
                        {item}
                    </MenuItem>
                ))}
            </TextField>
            <BarChart
                xAxis={[{ scaleType: 'band', data: data.map(e => e.commiterName), label: 'Collaborators' }]}
                yAxis={[{ label: 'Number of ' + mode, offset: -20 }]}
                series={[{ data: data.map(e => e.addedLines)}]}
                height={500}
            >
                <ChartsTooltip />
            </BarChart>
        </div>
    );
}

export default ContributionBar;