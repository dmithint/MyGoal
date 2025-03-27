package org.mygoal.fitnessapp.backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mygoal.fitnessapp.backend.dto.AthleteDto;
import org.mygoal.fitnessapp.backend.dto.CoachDto;
import org.mygoal.fitnessapp.backend.dto.PersonalDataDto;
import org.mygoal.fitnessapp.backend.exceptions.AppException;
import org.mygoal.fitnessapp.backend.model.Athlete;
import org.mygoal.fitnessapp.backend.model.Coach;
import org.mygoal.fitnessapp.backend.repository.CoachRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for working with coaches.
 */
@RequiredArgsConstructor
@Service
public class CoachService {

    private final CoachRepository coachRepository;

    public CoachDto updateCoachPersonal(Long id, PersonalDataDto personalData) {
        Coach coach = coachRepository.findById(id)
                .orElseThrow(() -> new AppException("Coach not found", HttpStatus.NOT_FOUND));

        if (personalData.getFirstName() != null) coach.setFirstName(personalData.getFirstName());
        if (personalData.getLastName() != null) coach.setLastName(personalData.getLastName());
        if (personalData.getEmail() != null) coach.setEmail(personalData.getEmail());

        coachRepository.save(coach);
        return toCoachDto(coach);
    }

    public void deleteCoachById(Long id) {
        Coach coach = coachRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coach not found with id: " + id));

        coachRepository.delete(coach);
    }

    /**
     * Method for getting a list of all coaches.
     *
     * @return List of all coaches.
     */
    public List<CoachDto> getCoachesList() {
        return coachRepository.findAll().stream()
                .map(coach -> new CoachDto(
                        coach.getId(),
                        coach.getFirstName(),
                        coach.getLastName(),
                        coach.getEmail(),
                        coach.getAverageRating(),
                        coach.getSpecialization(),
                        coach.getExperience()
                )).toList();
    }

    public CoachDto getCoachById(Long id) {
        return coachRepository.findById(id)
                .map(coach -> new CoachDto(
                        coach.getId(),
                        coach.getFirstName(),
                        coach.getLastName(),
                        coach.getEmail(),
                        coach.getAverageRating(),
                        coach.getSpecialization(),
                        coach.getExperience()
                ))
                .orElseThrow(() -> new EntityNotFoundException("Coach not found with id: " + id));
    }


    private CoachDto toCoachDto(Coach coach) {
        return CoachDto.builder()
                .id(coach.getId())
                .email(coach.getEmail())
                .firstName(coach.getFirstName())
                .lastName(coach.getLastName())
                .experience(coach.getExperience())
                .specialization(coach.getSpecialization())
                .averageRating(coach.getAverageRating())
                .build();
    }

}