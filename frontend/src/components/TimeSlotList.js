import React from 'react';
import { Button, Box, Typography } from "@mui/material";
import dayjs from "dayjs";
import { getStatusText } from "../util/ui";

const TimeSlotList = ({ timeSlots, selectedDate, onTimeSlotClick, getStatusColor }) => {
    const groupedByDate = timeSlots.reduce((acc, slot) => {
        const date = dayjs(slot.start).format("YYYY-MM-DD");
        if (!acc[date]) acc[date] = [];
        acc[date].push(slot);
        return acc;
    }, {});

    const formatTimeRange = (start, end) => `${dayjs(start).format("HH:mm")} - ${dayjs(end).format("HH:mm")}`;

    return (
        <Box sx={{ width: "100%", p: 2 }}>
            {Object.entries(groupedByDate).map(([date, slots]) => (
                <Box key={date} sx={{ mb: 4 }}>
                    <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
                        {dayjs(date).format("DD.MM.YYYY")}
                    </Typography>
                    <Box sx={{ display: "flex", flexWrap: "wrap", gap: 1.5 }}>
                        {slots.map((slot) => (
                            <Button
                                key={slot.id}
                                variant="outlined"
                                onClick={() => onTimeSlotClick(slot)}
                                sx={{
                                    m: 0.5,
                                    padding: '8px 16px',
                                    borderRadius: '10px',
                                    borderColor: getStatusColor(slot.status),
                                    color: getStatusColor(slot.status)
                                }}
                            >
                                {slot.name} ({formatTimeRange(slot.start, slot.end)} - {getStatusText(slot.status)})
                            </Button>
                        ))}
                    </Box>
                </Box>
            ))}
        </Box>
    );
};

export default TimeSlotList;