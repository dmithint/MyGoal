import React, {useEffect, useState} from "react";
import "../styles/CoachCard.css";
import {useNavigate} from "react-router-dom";
import {request} from "../axios_helper";
import dayjs from "dayjs";
import {LocalizationProvider, StaticDatePicker} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import {Box} from "@mui/material";
import TimeSlots from "../components/TimeSlots";
import VerticalDivider from "../components/VerticalDivider";
import {useAuth} from "../context/AuthProvider";
import {toast} from "react-hot-toast";

const Trainings = () => {
    const {user, logout} = useAuth();
    const [selectedDate, setSelectedDate] = useState(dayjs());
    const [timeSlots, setTimeSlots] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        if (user === undefined) return;

        if (!user) {
            navigate("/profile");
            return;
        }

        if (user.roles.includes("ATHLETE")) {
            fetchAthleteTimeSlots(selectedDate);
        } else if (user.roles.includes("COACH")) {
            fetchCoachTimeSlots(user.id, selectedDate);
        }
    }, [selectedDate, user, navigate]);

    const fetchCoachTimeSlots = (coachId, date) => {
        const toastId = toast.loading("Загружаем тренировки тренера...");
        request("GET", `trainings/search?coachId=${coachId}&date=${date.format("YYYY-MM-DDTHH:mm:ss")}`)
            .then((response) => {
                setTimeSlots(response.data);
                toast.success("Тренировки загружены!", {id: toastId});
            })
            .catch((error) => {
                console.error("Ошибка при загрузке тренировок:", error);
                toast.error("Ошибка загрузки тренировок!", {id: toastId});
            });
    };

    const fetchAthleteTimeSlots = (date) => {
        const toastId = toast.loading("Загружаем доступные слоты...");
        request("GET", `trainings/search?date=${date.format("YYYY-MM-DDTHH:mm:ss")}`)
            .then((response) => {
                setTimeSlots(response.data);
                toast.success("Слоты загружены!", {id: toastId});
            })
            .catch((error) => {
                console.error("Ошибка при загрузке доступных слотов:", error);
                toast.error("Ошибка загрузки слотов!", {id: toastId});
            });
    };

    const handleDateChange = (date) => {
        setSelectedDate(date);
    };

    if (!user) {
        return <p>Загрузка...</p>;
    }

    return (
        <div style={{height: "90vh"}}>
            <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale="ru">
                <Box sx={{display: "flex", height: "100%"}}>
                    <Box sx={{display: "flex", flexDirection: "column", alignItems: "center", width: "50%"}}>
                        <Box sx={{height: "5%"}}/>
                        <h1>Выберите дату</h1>
                        <Box sx={{height: "3%"}}/>
                        <StaticDatePicker
                            orientation="landscape"
                            value={selectedDate}
                            onChange={handleDateChange}
                            slotProps={{actionBar: {actions: ["today"]}}}
                        />
                    </Box>

                    <VerticalDivider/>

                    <Box sx={{display: "flex", flexDirection: "column", alignItems: "center", width: "50%"}}>
                        <Box sx={{height: "5%"}}/>
                        { user.roles.includes("ATHLETE") ? <h1>Выберите доступный слот</h1> : <></> }
                        <Box sx={{height: "3%"}}/>
                        {timeSlots.length > 0 ? (
                            <TimeSlots timeSlots={timeSlots} setTimeSlots={setTimeSlots} selectedDate={selectedDate}/>
                        ) : (
                            <p>Нет доступных слотов</p>
                        )}
                    </Box>
                </Box>
            </LocalizationProvider>
        </div>
    );
};

export default Trainings;

