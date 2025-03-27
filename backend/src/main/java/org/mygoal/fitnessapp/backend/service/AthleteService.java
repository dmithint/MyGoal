package org.mygoal.fitnessapp.backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mygoal.fitnessapp.backend.dto.BodyMeasurementsDto;
import org.mygoal.fitnessapp.backend.dto.AthleteDto;
import org.mygoal.fitnessapp.backend.dto.PersonalDataDto;
import org.mygoal.fitnessapp.backend.exceptions.AppException;
import org.mygoal.fitnessapp.backend.model.BodyMeasurements;
import org.mygoal.fitnessapp.backend.model.Athlete;
import org.mygoal.fitnessapp.backend.repository.AthleteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AthleteService {

    private final AthleteRepository athleteRepository;

    public List<AthleteDto> getAllAthletes() {
        return athleteRepository.findAll().stream()
                .map(this::toAthleteDto)
                .collect(Collectors.toList());
    }

    public Optional<Athlete> findAthleteById(Long id) {
        return athleteRepository.findById(id);
    }

    public Athlete getAthleteById(Long id) {
        return findAthleteById(id)
                .orElseThrow(() -> new AppException("Athlete not found", HttpStatus.NOT_FOUND));
    }

    public AthleteDto getAthleteDtoById(Long id) {
        return findAthleteById(id)
                .map(this::toAthleteDto)
                .orElseThrow(() -> new AppException("Athlete not found", HttpStatus.NOT_FOUND));
    }

    public AthleteDto findByEmail(String email) {
        Athlete athlete = athleteRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Athlete with email " + email + " not found", HttpStatus.NOT_FOUND));
        return toAthleteDto(athlete);
    }

    public AthleteDto updateAthletePersonal(Long id, PersonalDataDto personalData) {
        Athlete athlete = athleteRepository.findById(id)
                .orElseThrow(() -> new AppException("Athlete not found", HttpStatus.NOT_FOUND));

        if (personalData.getFirstName() != null) athlete.setFirstName(personalData.getFirstName());
        if (personalData.getLastName() != null) athlete.setLastName(personalData.getLastName());
        if (personalData.getEmail() != null) athlete.setEmail(personalData.getEmail());

        athleteRepository.save(athlete);
        return toAthleteDto(athlete);
    }

    public void deleteAthleteById(Long id) {
        Athlete athlete = athleteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Athlete not found with id: " + id));

        athleteRepository.delete(athlete);
    }


    public AthleteDto updateAthleteBMParams(Long id, BodyMeasurementsDto parameters) {
        Athlete athlete = athleteRepository.findById(id)
                .orElseThrow(() -> new AppException("Athlete not found", HttpStatus.NOT_FOUND));

        BodyMeasurements measurements = athlete.getBodyMeasurements();
        if (measurements == null) {
            measurements = new BodyMeasurements();
            measurements.setAthlete(athlete);
            athlete.setBodyMeasurements(measurements);
        }

        if (parameters.getHeight() != null) measurements.setHeight(parameters.getHeight());
        if (parameters.getWeight() != null) measurements.setWeight(parameters.getWeight());
        if (parameters.getFat() != null) measurements.setFat(parameters.getFat());
        if (parameters.getShoulderWidth() != null) measurements.setShoulderWidth(parameters.getShoulderWidth());
        if (parameters.getShoulderCircumference() != null)
            measurements.setShoulderCircumference(parameters.getShoulderCircumference());
        if (parameters.getChestCircumference() != null)
            measurements.setChestCircumference(parameters.getChestCircumference());
        if (parameters.getWaistCircumference() != null)
            measurements.setWaistCircumference(parameters.getWaistCircumference());
        if (parameters.getHipCircumference() != null)
            measurements.setHipCircumference(parameters.getHipCircumference());
        if (parameters.getCalfCircumference() != null)
            measurements.setCalfCircumference(parameters.getCalfCircumference());

        athleteRepository.save(athlete);

        return toAthleteDto(athlete);
    }

    private AthleteDto toAthleteDto(Athlete athlete) {
        return AthleteDto.builder()
                .id(athlete.getId())
                .email(athlete.getEmail())
                .firstName(athlete.getFirstName())
                .lastName(athlete.getLastName())
                .bodyMeasurements(toBodyMeasurementsDto(athlete.getBodyMeasurements()))
                .build();
    }

    private BodyMeasurementsDto toBodyMeasurementsDto(BodyMeasurements measurements) {
        if (measurements == null) {
            return null;
        }
        return BodyMeasurementsDto.builder()
                .height(measurements.getHeight())
                .weight(measurements.getWeight())
                .fat(measurements.getFat())
                .shoulderWidth(measurements.getShoulderWidth())
                .shoulderCircumference(measurements.getShoulderCircumference())
                .chestCircumference(measurements.getChestCircumference())
                .waistCircumference(measurements.getWaistCircumference())
                .hipCircumference(measurements.getHipCircumference())
                .calfCircumference(measurements.getCalfCircumference())
                .build();
    }

}
