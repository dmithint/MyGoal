package org.mygoal.fitnessapp.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "athletes")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class Athlete extends User {

    @OneToOne(mappedBy = "athlete", cascade = CascadeType.ALL, orphanRemoval = true)
    private BodyMeasurements bodyMeasurements;

    @ManyToMany(mappedBy = "athletes")
    private List<Training> trainings = new ArrayList<>();
}
