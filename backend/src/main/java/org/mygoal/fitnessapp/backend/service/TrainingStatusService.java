package org.mygoal.fitnessapp.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mygoal.fitnessapp.backend.model.TrainingStatus;
import org.mygoal.fitnessapp.backend.model.Training;
import org.mygoal.fitnessapp.backend.repository.TrainingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The service is responsible for updating the status of {@link Training} objects.
 * It runs a scheduled task every 5 minutes to check if the training has started, is ongoing, or has finished.
 */

@Service
@RequiredArgsConstructor
public class TrainingStatusService {

    /**
     * Repository for working with {@link Training} objects.
     */
    private final TrainingRepository trainingRepository;

    /**
     * This method is executed every 20 minutes by the Spring Framework scheduler.
     * It updates the status of all {@link Training} objects in the database.
     */
    @Transactional
    @Scheduled(fixedRate = 1200000L)
    public void updateTrainingStatuses() {
        LocalDateTime now = LocalDateTime.now();

        List<Training> trainingsToUpdate = trainingRepository.findAll();

        for (Training training : trainingsToUpdate) {
            if (training.getStatus().equals(TrainingStatus.CANCELLED)) continue;

            LocalDateTime startTime = training.getStart();
            LocalDateTime endTime = training.getEnd();

            if (now.isAfter(startTime)) {
                if (now.isAfter(endTime)) {
                    training.setStatus(TrainingStatus.COMPLETED);
                } else {
                    training.setStatus(TrainingStatus.IN_PROGRESS);
                }
            } else {
                training.setStatus(TrainingStatus.SCHEDULED);
            }
        }

        trainingRepository.saveAll(trainingsToUpdate);
    }
}
