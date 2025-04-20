package org.mygoal.fitnessapp.backend.service.util;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.mygoal.fitnessapp.backend.model.Athlete;
import org.mygoal.fitnessapp.backend.model.Training;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * Class containing specifications for filtering workouts.
 */

public class TrainingSpecifications {

    public static Specification<Training> filter(Long athleteId, Long coachId, LocalDateTime dateTime) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            query.distinct(true);

            if (athleteId != null) {
                Join<Training, Athlete> athletesJoin = root.join("athletes");
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(athletesJoin.get("id"), athleteId));
            }

            if (coachId != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("coach").get("id"), coachId));
            }

            // Фильтр по дате
            if (dateTime != null) {
                LocalDateTime startOfDay = dateTime.toLocalDate().atStartOfDay();
                LocalDateTime endOfDay = dateTime.toLocalDate().atTime(23, 59, 59);

                // Фильтруем тренировки, которые начинаются в указанный день
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.between(root.get("start"), startOfDay, endOfDay));

                // Дополнительно: фильтр для исключения прошедших тренировок на текущий день
                if (dateTime.toLocalDate().equals(LocalDate.now())) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.greaterThanOrEqualTo(root.get("start"), LocalDateTime.now()));
                }
            }

            return predicate;
        };
    }
}