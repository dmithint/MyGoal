import React, { useEffect, useState } from "react";
import { request } from "../axios_helper";
import {
    Box,
    Button,
    Card,
    Grid,
    IconButton,
    Typography,
    TextField,
    CircularProgress,
    Modal,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import StarIcon from "@mui/icons-material/Star";
import { useAuth } from "../context/AuthProvider";
import { toast } from "react-hot-toast";
import {getStatusText} from "../util/ui";

const VisitHistory = () => {
    const { user, logout } = useAuth();
    const [trainings, setTrainings] = useState([]);
    const [openModal, setOpenModal] = useState(false);
    const [selectedRating, setSelectedRating] = useState(0);
    const [comment, setComment] = useState("");
    const [selectedTraining, setSelectedTraining] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        if (user === undefined) return;

        if (!user) {
            navigate("/profile");
            return;
        }

        const isAthlete = user.roles.includes("ATHLETE");

        if (!isAthlete) {
            navigate("/");
            return;
        }

        const toastId = toast.loading("Загружаем историю тренировок...");
        const endpoint = `trainings/search?athleteId=${user.id}`

        request("GET", endpoint)
            .then((response) => {
                setTrainings(response.data);
                toast.success("История тренировок загружена!", { id: toastId });
            })
            .catch((error) => {
                console.error("Ошибка при загрузке истории тренировок:", error);
                toast.error("Ошибка загрузки истории тренировок!", { id: toastId });
                logout();
            });
    }, [user, navigate, logout]);

    const handleSubmitReview = () => {
        if (!selectedTraining || !user) return;

        const toastId = toast.loading("Отправка отзыва...");
        request(
            "PATCH",
            `trainings/${selectedTraining.id}/rate/${user.id}?rating=${selectedRating}&comment=${encodeURIComponent(
                comment
            )}`
        )
            .then(() => {
                toast.success("Отзыв успешно отправлен!", { id: toastId });
                setOpenModal(false);
                setSelectedRating(0);
                setComment("");
                return request("GET", `trainings/search?athleteId=${user.id}`);
            })
            .then((response) => setTrainings(response.data))
            .catch((error) => {
                console.error("Ошибка при отправке отзыва:", error);
                toast.error("Ошибка отправки отзыва!", { id: toastId });
            });
    };

    const handleCardClick = (training) => {
        const isAthlete = user.roles.includes("ATHLETE");
        if (isAthlete && training.status.toLowerCase() === "completed") {
            setSelectedTraining(training);
            setOpenModal(true);
        }
    };

    const getStatusColor = (status) =>
        ({
            completed: "#54b35a",
            in_progress: "#4682b4",
            scheduled: "#ff9800",
            cancelled: "#d32f2f",
        }[status.toLowerCase()] || "#757575");


    const formatDateTime = (dateTime) => {
        const date = new Date(dateTime);
        return {
            date: date.toLocaleDateString("ru-RU"),
            time: date.toLocaleTimeString("ru-RU", {
                hour: "2-digit",
                minute: "2-digit",
            }),
        };
    };

    if (!user) {
        return (
            <Box
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    height: "100vh",
                }}
            >
                <CircularProgress size={60} />
            </Box>
        );
    }

    const isAthlete = user.roles.includes("ATHLETE");

    return (
        <Box
            sx={{
                minHeight: "90vh",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                padding: "2rem"
            }}
        >
            <Modal
                open={openModal}
                onClose={() => setOpenModal(false)}
                sx={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                }}
            >
                <Box
                    sx={{
                        backgroundColor: "white",
                        padding: "2rem",
                        borderRadius: "8px",
                        width: "90%",
                        maxWidth: "500px",
                    }}
                >
                    <Typography variant="h6" sx={{ mb: 2 }}>
                        Отзыв о тренировке "{selectedTraining?.name}"
                    </Typography>
                    <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                        <Box>
                            <Typography>Оценка:</Typography>
                            <Box sx={{ display: "flex", gap: 1 }}>
                                {[1, 2, 3, 4, 5].map((star) => (
                                    <IconButton
                                        key={star}
                                        onClick={() => setSelectedRating(star)}
                                        color={selectedRating >= star ? "primary" : "default"}
                                    >
                                        <StarIcon />
                                    </IconButton>
                                ))}
                            </Box>
                        </Box>

                        <TextField
                            label="Комментарий"
                            multiline
                            rows={4}
                            value={comment}
                            onChange={(e) => setComment(e.target.value)}
                            fullWidth
                        />

                        <Button
                            variant="contained"
                            onClick={handleSubmitReview}
                            disabled={selectedRating === 0}
                            sx={{ mt: 2 }}
                        >
                            Отправить отзыв
                        </Button>
                    </Box>
                </Box>
            </Modal>

            <Typography
                variant="h4"
                sx={{
                    mb: 4,
                    color: "#333",
                    fontWeight: 600,
                    textShadow: "1px 1px 2px rgba(0,0,0,0.1)",
                }}
            >
                История тренировок
            </Typography>

            {trainings.length > 0 ? (
                <Box sx={{ width: "100%", maxWidth: "1200px" }}>
                    {trainings.map((training) => {
                        const start = formatDateTime(training.start);
                        const end = formatDateTime(training.end);
                        const statusText = getStatusText(training.status);

                        return (
                            <Card
                                key={training.id}
                                onClick={() => handleCardClick(training)}
                                sx={{
                                    mb: 2,
                                    p: 2,
                                    backgroundColor: getStatusColor(training.status),
                                    borderRadius: "10px",
                                    boxShadow: "0 4px 6px rgba(0,0,0,0.1)",
                                    transition: "transform 0.2s",
                                    "&:hover": {
                                        transform:
                                            isAthlete && training.status.toLowerCase() === "completed"
                                                ? "translateY(-2px)"
                                                : "none",
                                        cursor:
                                            isAthlete && training.status.toLowerCase() === "completed"
                                                ? "pointer"
                                                : "default",
                                    },
                                }}
                            >
                                <Grid container spacing={2} sx={{ color: "white" }}>
                                    <Grid item xs={12}>
                                        <Typography variant="h6">{training.name}</Typography>
                                    </Grid>

                                    <Grid item xs={12} sm={3}>
                                        <Typography>
                                            <strong>Дата:</strong> {start.date}
                                        </Typography>
                                    </Grid>

                                    <Grid item xs={12} sm={3}>
                                        <Typography>
                                            <strong>Начало:</strong> {start.time}
                                        </Typography>
                                        <Typography>
                                            <strong>Окончание:</strong> {end.time}
                                        </Typography>
                                    </Grid>

                                    {isAthlete && (
                                        <Grid item xs={12} sm={3}>
                                            <Typography>
                                                <strong>Тренер:</strong> {training.coach.firstName}{" "}
                                                {training.coach.lastName}
                                            </Typography>
                                        </Grid>
                                    )}
                                </Grid>
                            </Card>
                        );
                    })}
                </Box>
            ) : (
                <Typography sx={{ color: "#666" }}>Тренировок пока нет</Typography>
            )}
        </Box>
    );
};

export default VisitHistory;