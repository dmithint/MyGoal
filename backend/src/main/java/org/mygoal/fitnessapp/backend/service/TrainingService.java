package org.mygoal.fitnessapp.backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mygoal.fitnessapp.backend.dto.TrainingDto;
import org.mygoal.fitnessapp.backend.dto.TrainingFeedbackDto;
import org.mygoal.fitnessapp.backend.dto.TrainingRequest;
import org.mygoal.fitnessapp.backend.exceptions.AppException;
import org.mygoal.fitnessapp.backend.model.*;
import org.mygoal.fitnessapp.backend.repository.AthleteRepository;
import org.mygoal.fitnessapp.backend.repository.CoachRepository;
import org.mygoal.fitnessapp.backend.repository.TrainingFeedbackRepository;
import org.mygoal.fitnessapp.backend.repository.TrainingRepository;
import org.mygoal.fitnessapp.backend.service.util.TrainingSpecifications;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The service is responsible for working with {@link Training} model objects.
 * Provides methods for getting all {@link Training} objects, filtering them by various parameters, as well as enrolling and canceling {@link User}s in {@link Training}s.
 */
@RequiredArgsConstructor
@Service
public class TrainingService {

    /**
     * Repository for working with {@link Training} objects.
     */
    private final TrainingRepository trainingRepository;

    private final TrainingFeedbackRepository trainingFeedbackRepository;

    /**
     * Repository for working with {@link User} objects.
     */
    private final AthleteRepository athleteRepository;

    private final CoachRepository coachRepository;

    private final EmailService emailService;

    public List<TrainingDto> getTrainingsWithFilter(Long athleteId, Long coachId, LocalDateTime date) {
        Sort sortByTrainingStartDesc = Sort.by(Sort.Direction.DESC, "start");
        List<Training> trainings = trainingRepository.findAll(TrainingSpecifications.filter(athleteId, coachId, date), sortByTrainingStartDesc);
        return trainings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TrainingDto mapToDto(Training training) {
        TrainingDto dto = new TrainingDto();
        dto.setId(training.getId());
        dto.setName(training.getName());
        dto.setDescription(training.getDescription());
        dto.setStatus(training.getStatus().name());
        dto.setMaxAthletes(training.getMaxAthletes());
        dto.setStart(training.getStart());
        dto.setEnd(training.getEnd());
        dto.setAverageRating(training.getAverageRating());

        if (training.getCoach() != null) {
            TrainingDto.CoachDto coachDto = new TrainingDto.CoachDto();
            Coach coach = training.getCoach();
            coachDto.setId(coach.getId());
            coachDto.setFirstName(coach.getFirstName());
            coachDto.setLastName(coach.getLastName());
            coachDto.setSpecialization(coach.getSpecialization());
            coachDto.setExperience(coach.getExperience());
            coachDto.setAverageRating(coach.getAverageRating());
            dto.setCoach(coachDto);
        }

        if (!training.getAthletes().isEmpty()) {
            List<TrainingDto.AthleteDto> athleteDtos = training.getAthletes().stream()
                    .map(athlete -> {
                        TrainingDto.AthleteDto athleteDto = new TrainingDto.AthleteDto();
                        athleteDto.setId(athlete.getId());
                        athleteDto.setFirstName(athlete.getFirstName());
                        athleteDto.setLastName(athlete.getLastName());
                        return athleteDto;
                    })
                    .collect(Collectors.toList());
            dto.setAthletes(athleteDtos);
        } else {
            dto.setAthletes(Collections.emptyList());
        }

        return dto;
    }




    public Training createTraining(TrainingRequest trainingRequest) {
        Coach coach = coachRepository.findById(trainingRequest.getCoachId())
                .orElseThrow(() -> new IllegalArgumentException("Coach not found"));

        Training training = new Training();
        training.setName(trainingRequest.getName());
        training.setDescription(trainingRequest.getDescription());
        training.setStatus(trainingRequest.getStatus());
        training.setMaxAthletes(trainingRequest.getMaxAthletes());
        training.setStart(trainingRequest.getStart());
        training.setEnd(trainingRequest.getEnd());
        training.setCoach(coach);

        return trainingRepository.save(training);
    }


    @Transactional
    public void enrollAthleteInTraining(Long athleteId, Long trainingId) {
        Athlete athlete = athleteRepository.findById(athleteId)
                .orElseThrow(() -> new RuntimeException("Athlete not found with ID: " + athleteId));

        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found with ID: " + trainingId));

        if (training.getAthletes().size() >= training.getMaxAthletes()) {
            throw new RuntimeException("Training is already full.");
        }

        training.getAthletes().add(athlete);
        trainingRepository.save(training);
    }

    @Transactional
    public void cancelAthleteInTraining(Long athleteId, Long trainingId) {
        Athlete athlete = athleteRepository.findById(athleteId)
                .orElseThrow(() -> new RuntimeException("Athlete not found with ID: " + athleteId));

        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found with ID: " + trainingId));

        trainingRepository.save(training);
        training.getAthletes().remove(athlete);
        emailService.sendTrainingCancellationEmail(athlete, training);
    }


    @Transactional
    public void deleteTraining(Long trainingId) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found with ID: " + trainingId));

        trainingRepository.delete(training);
    }

    @Transactional
    public void updateTrainingStatus(Long trainingId, TrainingStatus newStatus) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new EntityNotFoundException("Training not found"));

        for (Athlete athlete : training.getAthletes()) {
            emailService.sendTrainingStatusUpdateEmail(athlete, training, newStatus.toString());
        }

        training.setStatus(newStatus);
        trainingRepository.save(training);
    }

    @Transactional
    public void rateTraining(Long athleteId, Long trainingId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Athlete athlete = athleteRepository.findById(athleteId)
                .orElseThrow(() -> new RuntimeException("Athlete not found with ID: " + athleteId));

        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found with ID: " + trainingId));

        if (!training.getAthletes().contains(athlete)) {
            throw new RuntimeException("Athlete is not enrolled in this training.");
        }

        Optional<TrainingFeedback> existingFeedback = training.getFeedbacks().stream()
                .filter(r -> r.getAthlete().getId().equals(athleteId))
                .findFirst();

        if (existingFeedback.isPresent()) {
            existingFeedback.get().setRating(rating);
            existingFeedback.get().setComment(comment);
        } else {
            TrainingFeedback newFeedback = new TrainingFeedback();
            newFeedback.setTraining(training);
            newFeedback.setAthlete(athlete);
            newFeedback.setRating(rating);
            newFeedback.setComment(comment);
            training.getFeedbacks().add(newFeedback);
        }

        trainingRepository.save(training);
    }


    public List<TrainingFeedbackDto> getAllFeedbacks() {
        return trainingFeedbackRepository.findAll().stream()
                .map(this::convertToFeedbackDto)
                .collect(Collectors.toList());
    }

    public void deleteTrainingFeedback(Long feedbackId) {
        TrainingFeedback feedback = trainingFeedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new AppException("Feedback not found", HttpStatus.NOT_FOUND));
        trainingFeedbackRepository.delete(feedback);
    }


    private TrainingFeedbackDto convertToFeedbackDto(TrainingFeedback feedback) {
        Training training = feedback.getTraining();
        TrainingFeedbackDto.Athlete athleteDto = new TrainingFeedbackDto.Athlete(
                feedback.getAthlete().getId(),
                feedback.getAthlete().getFirstName(),
                feedback.getAthlete().getLastName()
        );

        TrainingFeedbackDto.Coach coachDto = training.getCoach() != null ?
                new TrainingFeedbackDto.Coach(
                        training.getCoach().getId(),
                        training.getCoach().getFirstName(),
                        training.getCoach().getLastName()
                ) : null;

        return new TrainingFeedbackDto(
                feedback.getId(),
                training.getId(),
                athleteDto,
                feedback.getRating(),
                feedback.getComment(),
                training.getName(),
                coachDto
        );
    }

}
