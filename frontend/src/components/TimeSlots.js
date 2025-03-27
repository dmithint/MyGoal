import CoachTimeSlots from "./CoachTimeSlots";
import AthleteTimeSlots from "./AthleteTimeSlots";
import { useAuth } from "../context/AuthProvider";

export default function TimeSlots({ timeSlots, setTimeSlots, selectedDate }) {
    const { user } = useAuth();

    if (user?.roles?.includes("COACH")) {
        return (
            <CoachTimeSlots
                coachId={user.id}
                timeSlots={timeSlots}
                setTimeSlots={setTimeSlots}
                selectedDate={selectedDate}
            />
        );
    }

    return (
        <AthleteTimeSlots
            timeSlots={timeSlots}
            setTimeSlots={setTimeSlots}
        />
    );
}