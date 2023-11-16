import React, { useState } from 'react';
import axios from 'axios';
import NavBar from './NavBar';

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
            <input
                type="text"
                value={link}
                onChange={e => setLink(e.target.value)}
                placeholder="Enter a valid link"
            />
            <button onClick={sendData}>Envoyer</button>
        </div>
    );
}

export default DataSender;