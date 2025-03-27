import React, { useState, useEffect } from 'react';
import defaultCoachImg from '../assets/coaches/default-coach.png';
import '../styles/CoachCard.css';

export default function CoachCard({ id, firstName, lastName, rating }) {
    const [imgSrc, setImgSrc] = useState(defaultCoachImg);

    useEffect(() => {
        try {
            const image = require(`../assets/coaches/coach${id}.png`);
            setImgSrc(image);
        } catch {
            setImgSrc(defaultCoachImg);
        }
    }, [id]);

    return (
        <div className="coach-card">
            <img
                src={imgSrc}
                alt={`${firstName} ${lastName}`}
                className="coach-photo"
                onError={() => setImgSrc(defaultCoachImg)}
            />
            <div className="coach-info">
                <h3 className="coach-name">{firstName} {lastName}</h3>
                <p className="coach-rating">Рейтинг: {rating.toFixed(1)}</p>
            </div>
        </div>
    );
}