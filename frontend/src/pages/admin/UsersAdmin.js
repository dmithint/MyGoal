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
import {ClearIcon} from "@mui/x-date-pickers";

export default function UsersAdmin() {
    const { user, logout } = useAuth();
    const [athletes, setAthletes] = useState([]);
    const [coaches, setCoaches] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        if (!user || !user.roles.includes("ADMIN")) {
            navigate("/");
            return;
        }

        const fetchUsers = async () => {
            try {
                const athletesResponse = await request("GET", "/athletes");
                const coachesResponse = await request("GET", "/coaches");

                setAthletes(athletesResponse.data);
                setCoaches(coachesResponse.data);
                setLoading(false);
            } catch (error) {
                console.error("Ошибка при загрузке пользователей:", error);
                toast.error("Ошибка загрузки данных");
                logout();
            }
        };

        fetchUsers();
    }, [user, navigate, logout]);

    const handleDeleteUser = async (userId, type) => {
        const toastId = toast.loading("Удаление пользователя...");
        try {
            if (type == "athlete") {
                await request("DELETE", `/athletes/${userId}`);
                setAthletes(athletes.filter(athlete => athlete.id !== userId));
            } else {
                await request("DELETE", `/athletes/${userId}`);
                setCoaches(coaches.filter(coach => coach.id !== userId));
            }
            toast.success("Пользователь успешно удален", { id: toastId });
        } catch (error) {
            console.error("Ошибка при удалении:", error);
            toast.error("Ошибка при удалении пользователя", { id: toastId });
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
                Управление пользователями
            </Typography>

            <Box sx={{ mb: 6 }}>
                <Typography
                    variant="h5"
                    sx={{ mb: 2, color: "#34495e", fontWeight: 600 }}
                >
                    Спортсмены
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
                            <TableRow sx={{ backgroundColor: "#3498db" }}>
                                <TableCell sx={{ color: "white", fontWeight: 600 }}>Имя</TableCell>
                                <TableCell sx={{ color: "white", fontWeight: 600 }}>Фамилия</TableCell>
                                <TableCell sx={{ color: "white", fontWeight: 600 }}>Email</TableCell>
                                <TableCell sx={{ color: "white", fontWeight: 600 }}>Действия</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {athletes.map((athlete) => (
                                <TableRow
                                    key={athlete.id}
                                    sx={{
                                        '&:hover': {
                                            backgroundColor: "#f8f9fa",
                                            transition: "background-color 0.2s"
                                        }
                                    }}
                                >
                                    <TableCell>{athlete.firstName}</TableCell>
                                    <TableCell>{athlete.lastName}</TableCell>
                                    <TableCell>{athlete.email}</TableCell>
                                    <TableCell>
                                        <IconButton
                                            color="error"
                                            onClick={() => handleDeleteUser(athlete.id, "athlete")}
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

            <Box>
                <Typography
                    variant="h5"
                    sx={{ mb: 2, color: "#34495e", fontWeight: 600 }}
                >
                    Тренеры
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
                            <TableRow sx={{ backgroundColor: "#2ecc71" }}>
                                <TableCell sx={{ color: "white", fontWeight: 600 }}>Имя</TableCell>
                                <TableCell sx={{ color: "white", fontWeight: 600 }}>Фамилия</TableCell>
                                <TableCell sx={{ color: "white", fontWeight: 600 }}>Email</TableCell>
                                <TableCell sx={{ color: "white", fontWeight: 600 }}>Действия</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {coaches.map((coach) => (
                                <TableRow
                                    key={coach.id}
                                    sx={{
                                        '&:hover': {
                                            backgroundColor: "#f8f9fa",
                                            transition: "background-color 0.2s"
                                        }
                                    }}
                                >
                                    <TableCell>{coach.firstName}</TableCell>
                                    <TableCell>{coach.lastName}</TableCell>
                                    <TableCell>{coach.email}</TableCell>
                                    <TableCell>
                                        <IconButton
                                            color="error"
                                            onClick={() => handleDeleteUser(coach.id, "coach")}
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
        </Box>
    );
};