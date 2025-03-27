import React, { useEffect, useState } from "react";
import { request } from "../../axios_helper";
import {
    Box,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Typography,
    CircularProgress,
    IconButton
} from "@mui/material";
import { useAuth } from "../../context/AuthProvider";
import { toast } from "react-hot-toast";
import { useNavigate } from "react-router-dom";
import { ClearIcon } from "@mui/x-date-pickers";

export default function TrainingsAdmin() {
    const { user, logout } = useAuth();
    const [trainings, setTrainings] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        if (!user || !user.roles.includes("ADMIN")) {
            navigate("/");
            return;
        }

        const fetchTrainings = async () => {
            try {
                const response = await request("GET", "/trainings");
                const sortedTrainings = response.data.sort((a, b) =>
                    new Date(a.start) - new Date(b.start)
                );
                setTrainings(sortedTrainings);
                setLoading(false);
            } catch (error) {
                console.error("Ошибка при загрузке тренировок:", error);
                toast.error("Ошибка загрузки данных");
                logout();
            }
        };

        fetchTrainings();
    }, [user, navigate, logout]);

    const handleDeleteTraining = async (trainingId) => {
        const toastId = toast.loading("Удаление тренировки...");
        try {
            await request("DELETE", `/trainings/${trainingId}`);
            setTrainings(trainings.filter(training => training.id !== trainingId));
            toast.success("Тренировка успешно удалена", { id: toastId });
        } catch (error) {
            console.error("Ошибка при удалении:", error);
            toast.error("Ошибка при удалении тренировки", { id: toastId });
        }
    };

    const formatDateRange = (start, end) => {
        const startDate = new Date(start);
        const endDate = new Date(end);

        const pad = (num) => String(num).padStart(2, '0');

        const startTime = `${pad(startDate.getHours())}:${pad(startDate.getMinutes())}`;
        const endTime = `${pad(endDate.getHours())}:${pad(endDate.getMinutes())}`;
        const date = `${pad(startDate.getDate())}.${pad(startDate.getMonth() + 1)}.${startDate.getFullYear()}`;

        return `${startTime} - ${endTime} ${date}`;
    };

    // Function to get background color based on status
    const getStatusColor = (status) => {
        switch (status?.toLowerCase()) {
            case 'planned':
                return 'rgba(46, 204, 113, 0.1)'; // Light green
            case 'completed':
                return 'rgba(52, 152, 219, 0.1)'; // Light blue
            case 'cancelled':
                return 'rgba(231, 76, 60, 0.1)'; // Light red
            default:
                return 'rgba(149, 165, 166, 0.1)'; // Light grey for unknown status
        }
    };

    if (loading) {
        return (
            <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh" }}>
                <CircularProgress size={60} />
            </Box>
        );
    }

    return (
        <Box sx={{
            minHeight: "90vh",
            padding: "2rem",
            background: "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)"
        }}>
            <Typography
                variant="h4"
                sx={{
                    mb: 4,
                    textAlign: "center",
                    color: "#2c3e50",
                    fontWeight: 700,
                    textTransform: "uppercase",
                    letterSpacing: "1px"
                }}
            >
                Управление тренировками
            </Typography>

            <TableContainer
                component={Paper}
                sx={{
                    borderRadius: "12px",
                    boxShadow: "0 4px 15px rgba(0,0,0,0.1)",
                    overflow: "hidden"
                }}
            >
                <Table>
                    <TableHead>
                        <TableRow sx={{ backgroundColor: "#e74c3c" }}>
                            <TableCell sx={{ color: "white", fontWeight: 600 }}>Название</TableCell>
                            <TableCell sx={{ color: "white", fontWeight: 600 }}>Статус</TableCell>
                            <TableCell sx={{ color: "white", fontWeight: 600 }}>Тренер</TableCell>
                            <TableCell sx={{ color: "white", fontWeight: 600 }}>Дата</TableCell>
                            <TableCell sx={{ color: "white", fontWeight: 600 }}>Макс. участников</TableCell>
                            <TableCell sx={{ color: "white", fontWeight: 600 }}>Рейтинг</TableCell>
                            <TableCell sx={{ color: "white", fontWeight: 600 }}>Действия</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {trainings.map((training) => (
                            <TableRow
                                key={training.id}
                                sx={{
                                    backgroundColor: getStatusColor(training.status),
                                    '&:hover': {
                                        backgroundColor: "#f8f9fa",
                                        transition: "background-color 0.2s"
                                    }
                                }}
                            >
                                <TableCell>{training.name}</TableCell>
                                <TableCell>{training.status}</TableCell>
                                <TableCell>{training.coach?.firstName} {training.coach?.lastName}</TableCell>
                                <TableCell>{formatDateRange(training.start, training.end)}</TableCell>
                                <TableCell>{training.maxAthletes}</TableCell>
                                <TableCell>{training.averageRating?.toFixed(1) || '-'}</TableCell>
                                <TableCell>
                                    <IconButton
                                        color="error"
                                        onClick={() => handleDeleteTraining(training.id)}
                                    >
                                        <ClearIcon />
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    );
}