import React from 'react';
import { Box, Modal, Typography, TextField, Button } from "@mui/material";

const modalStyle = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 450,
    bgcolor: "background.paper",
    boxShadow: 24,
    p: 4,
    borderRadius: "16px"
};

const CreateTrainingModal = ({
                                 isOpen,
                                 onClose,
                                 newTraining,
                                 setNewTraining,
                                 errors,
                                 handleCreateTraining
                             }) => {
    return (
        <Modal open={isOpen} onClose={onClose}>
            <Box sx={modalStyle}>
                <Typography variant="h5" sx={{ mb: 3 }}>
                    Создать тренировку
                </Typography>
                <TextField
                    label="Название"
                    value={newTraining.name}
                    onChange={(e) => setNewTraining({ ...newTraining, name: e.target.value })}
                    fullWidth
                    sx={{ mb: 2 }}
                    error={!!errors.name}
                    helperText={errors.name}
                />
                <TextField
                    label="Описание"
                    value={newTraining.description}
                    onChange={(e) => setNewTraining({ ...newTraining, description: e.target.value })}
                    fullWidth
                    multiline
                    rows={3}
                    sx={{ mb: 2 }}
                />
                <TextField
                    label="Время начала (HH:mm)"
                    value={newTraining.start}
                    onChange={(e) => setNewTraining({ ...newTraining, start: e.target.value })}
                    fullWidth
                    sx={{ mb: 2 }}
                    error={!!errors.start}
                    helperText={errors.start}
                />
                <TextField
                    label="Время окончания (HH:mm)"
                    value={newTraining.end}
                    onChange={(e) => setNewTraining({ ...newTraining, end: e.target.value })}
                    fullWidth
                    sx={{ mb: 2 }}
                    error={!!errors.end}
                    helperText={errors.end}
                />
                <TextField
                    label="Максимальное количество участников"
                    type="number"
                    value={newTraining.maxAthletes}
                    onChange={(e) => setNewTraining({ ...newTraining, maxAthletes: e.target.value })}
                    fullWidth
                    sx={{ mb: 2 }}
                    error={!!errors.maxAthletes}
                    helperText={errors.maxAthletes}
                />
                <Box sx={{ display: "flex", justifyContent: "space-between", mt: 2 }}>
                    <Button variant="contained" onClick={handleCreateTraining}>
                        Создать
                    </Button>
                    <Button variant="outlined" onClick={onClose}>
                        Закрыть
                    </Button>
                </Box>
            </Box>
        </Modal>
    );
};

export default CreateTrainingModal;