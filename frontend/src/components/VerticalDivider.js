import React from 'react';
import '../styles/Footer.css';

function VerticalDivider() {
    return (
        <div
            style={{
                width: '3px',
                maxHeight: '100%',
                backgroundColor: 'grey',
                margin: '0 10px',
                zIndex: '-1',
            }}
        />
    );
}


export default VerticalDivider;