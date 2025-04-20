import React, { useState } from "react";
import { request } from "../axios_helper";
import {Button, Box, Typography} from "@mui/material";
import { toast } from "react-hot-toast";
import dayjs from "dayjs";
import TimeSlotList from './TimeSlotList';
import TimeSlotModal from './TimeSlotModal';
import CreateTrainingModal from './CreateTrainingModal';

const getStatusColor = (status) => {
    switch (status) {
        case 'IN_PROGRESS': return 'success.main';
        case 'CANCELLED': return 'error.main';
        case 'COMPLETED': return 'grey.600';
        default: return 'primary.main';
    }
};

export default function CoachTimeSlots({ coachId, timeSlots, setTimeSlots, selectedDate }) {
    const [selectedTimeSlot, setSelectedTimeSlot] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
    const [newTraining, setNewTraining] = useState({
        name: "",
        description: "",
        start: "",
        end: "",
        maxAthletes: ""
    });
    const [errors, setErrors] = useState({});

    const handleStatusChange = (newStatusObj) => {
        const toastId = toast.loading("Обновление статуса...");
        request("PATCH", `trainings/${selectedTimeSlot.id}/status`, newStatusObj)
            .then(() => {
                setTimeSlots(timeSlots.map(slot =>
                    slot.id === selectedTimeSlot.id ? { ...slot, status: newStatusObj.status } : slot
                ));
                toast.success("Статус обновлен!", { id: toastId });
            })
            .catch(() => {
                toast.error("Ошибка обновления", { id: toastId });
            });
    };

    const handleRemoveAthlete = (athleteId) => {
        const toastId = toast.loading("Удаление участника...");
        request("PATCH", `trainings/${selectedTimeSlot.id}/cancel/${athleteId}`)
            .then(() => {
                setTimeSlots(timeSlots.map(slot =>
                    slot.id === selectedTimeSlot.id
                        ? { ...slot, athletes: slot.athletes.filter(a => a.id !== athleteId) }
                        : slot
                ));
                toast.success("Участник удален!", { id: toastId });
            })
            .catch(() => {
                toast.error("Ошибка удаления", { id: toastId });
            });
    };

    const validateForm = () => {
        const newErrors = {};
        if (!newTraining.name.trim()) newErrors.name = "Название обязательно";

        const startRegex = /^([01]?[0-9]|2[0-3]):[0-5][0-9]$/;
        if (!newTraining.start) {
            newErrors.start = "Время начала обязательно";
        } else if (!startRegex.test(newTraining.start)) {
            newErrors.start = "Формат должен быть HH:mm (например, 14:30)";
        }

        if (!newTraining.end) {
            newErrors.end = "Время окончания обязательно";
        } else if (!startRegex.test(newTraining.end)) {
            newErrors.end = "Формат должен быть HH:mm (например, 15:30)";
        } else if (newTraining.start && newTraining.end) {
            const startTime = dayjs(selectedDate.format("YYYY-MM-DD") + " " + newTraining.start);
            const endTime = dayjs(selectedDate.format("YYYY-MM-DD") + " " + newTraining.end);
            if (endTime.isBefore(startTime) || endTime.isSame(startTime)) {
                newErrors.end = "Время окончания должно быть позже времени начала";
            }
        }

        if (!newTraining.maxAthletes) {
            newErrors.maxAthletes = "Укажите количество участников";
        } else if (isNaN(newTraining.maxAthletes) || parseInt(newTraining.maxAthletes) < 1) {
            newErrors.maxAthletes = "Должно быть положительное число";
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleCreateTraining = () => {
        if (!validateForm()) return;

        const toastId = toast.loading("Создание тренировки...");
        const startDateTime = dayjs(selectedDate).format("YYYY-MM-DD") + "T" + newTraining.start + ":00";
        const endDateTime = dayjs(selectedDate).format("YYYY-MM-DD") + "T" + newTraining.end + ":00";

        const trainingData = {
            coachId: coachId,
            name: newTraining.name,
            description: newTraining.description,
            start: startDateTime,
            end: endDateTime,
            maxAthletes: parseInt(newTraining.maxAthletes),
            status: "SCHEDULED"
        };

        request("POST", "trainings", trainingData)
            .then((response) => {
                setTimeSlots([...timeSlots, response.data]);
                toast.success("Тренировка создана!", { id: toastId });
                setIsCreateModalOpen(false);
                setNewTraining({ name: "", description: "", start: "", end: "", maxAthletes: "" });
            })
            .catch((error) => {
                toast.error("Ошибка создания тренировки", { id: toastId });
            });
    };

    return (
        <Box sx={{ width: "100%" }}>
            <Button
                variant="contained"
                color="primary"
                onClick={() => setIsCreateModalOpen(true)}
                sx={{ mb: 3, ml: 2 }}
            >
                Создать тренировку
            </Button>

            {timeSlots.length === 0 && (
                <Box sx={{ width: "100%", p: 4, textAlign: 'center' }}>
                    <Typography variant="h6" color="text.secondary">
                        Нет тренировок в этот день
                    </Typography>
                </Box>
            )}

            <TimeSlotList
                timeSlots={timeSlots}
                selectedDate={selectedDate}
                onTimeSlotClick={(slot) => {
                    setSelectedTimeSlot(slot);
                    setIsModalOpen(true);
                }}
                getStatusColor={getStatusColor}
            />

            <TimeSlotModal
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                selectedTimeSlot={selectedTimeSlot}
                handleStatusChange={handleStatusChange}
                handleRemoveAthlete={handleRemoveAthlete}
            />

            <CreateTrainingModal
                isOpen={isCreateModalOpen}
                onClose={() => setIsCreateModalOpen(false)}
                newTraining={newTraining}
                setNewTraining={setNewTraining}
                errors={errors}
                handleCreateTraining={handleCreateTraining}
            />
        </Box>
    );
}