import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import { request } from "../axios_helper";
import { useAuth } from "../context/AuthProvider";
import CoachCard from '../components/CoachCard';

function Coaches() {
    const [coaches, setCoaches] = useState([]);
    const navigate = useNavigate();
    const {user} = useAuth();

    useEffect(() => {
        if (user === undefined) return;
        if (!user) {
            navigate("/");
        } else {
            request("GET", "coaches")
                .then((response) => {
                    setCoaches(response.data);
                })
                .catch((error) => {
                    console.error("Ошибка загрузки данных тренеров:", error);
                });
        }
    }, [user, navigate]);

    return (
        <div className="coach-page" style={{padding: '20px', height: '90vh', overflowY: 'auto'}}>
                <div style={{
                    display: 'grid',
                    gridTemplateColumns: 'repeat(3, 1fr)',
                    gap: '20px',
                }}>
                    {coaches.map((coach) => (
                        <CoachCard
                            key={coach.id}
                            id={coach.id}
                            firstName={coach.firstName}
                            lastName={coach.lastName}
                            rating={coach.averageRating}
                        />
                    ))}
                </div>
            </div>
            );
            }

            export default Coaches;