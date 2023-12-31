import React, { Component, useState } from 'react';
import { Box, MenuItem, Stack, TextField, Typography } from '@mui/material';
import { BarChart, ChartsTooltip } from '@mui/x-charts';

function ContributionBar({ contributions }) {
    const availableMode = ['total lines', 'code lines', 'build files', 'CI files', 'resource files', 'documentation files'];
    const [mode, setMode] = useState('total lines');
    const languages = ['JAVA', 'PYTHON', 'JAVASCRIPT', 'TYPESCRIPT', 'CSHARP', 'OTHER'];
    let data = [];
    let seriesX = [];
    let getSeries = [];

    if (contributions.length === 0) {
        return (
            <div>
                <Typography variant="h6">Contributions:</Typography>
                <Typography>No contributions available.</Typography>
            </div>
        );
    }

    if (mode === 'total lines') {
        seriesX = [];
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
                seriesX.push(commiterName);
            }
        });
        getSeries = [{ data: data.map(e => e.addedLines)}];
    } else if (mode === 'code lines') {
        seriesX = [];
        data = languages.map(l => ({ data: [], stack: 'total', label: l}) );
        let tabCommiter = [];
        contributions.forEach(contribution => {
            const lang = contribution.language;
            const commiterName = contribution.commiter.name;
            const langData = data.find(d => d.label === lang);
            if (langData) {
                const indexCommiter = tabCommiter[commiterName];
                if (indexCommiter !== undefined) {
                    langData.data[indexCommiter] += contribution.addedLines;
                } else {
                    languages.forEach(l => data.find(d => d.label === l).data.push(0));
                    langData.data[tabCommiter.length] += contribution.addedLines;
                    tabCommiter[commiterName] = tabCommiter.length;
                    seriesX.push(commiterName);
                }
            } else {
                console.log("Langue non trouvée :", lang);
            }
        });
        getSeries = data;
    } else {
        return (
            <div>
                <Typography variant="h6">Contributions:</Typography>
                <Box width='250px'>
                    <TextField
                        id="mode-select"
                        select
                        label="mode"
                        value={mode}
                        onChange={(event) => setMode(event.target.value)}
                        fullWidth
                    >
                        {availableMode.map((item) => (
                            <MenuItem value={item}>
                                {item}
                            </MenuItem>
                        ))}
                    </TextField>
                </Box>
                <Typography>In development</Typography>
            </div>
        );
    }

    return (
        <div>
            <Typography variant="h6">Contributions:</Typography>
            <Box width='250px'>
                <Stack direction="row">
                    <TextField
                        id="mode-select"
                        select
                        label="mode"
                        value={mode}
                        onChange={(event) => setMode(event.target.value)}
                        fullWidth
                    >
                        {availableMode.map((item) => (
                            <MenuItem key={item} value={item}>
                                {item}
                            </MenuItem>
                        ))}
                    </TextField>
                </Stack>
            </Box>
            <BarChart
                xAxis={[{ scaleType: 'band', data: seriesX, label: 'Collaborators' }]}
                yAxis={[{ label: 'Number of ' + mode, offset: -20 }]}
                series={getSeries}
                height={500}
            >
                <ChartsTooltip />
            </BarChart>
        </div>
    );
}

export default ContributionBar;