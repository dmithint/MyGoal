package org.mygoal.fitnessapp.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "body_measurements")
@Getter @Setter
public class BodyMeasurements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double height;
    private Double weight;
    private Double fat;
    private Double shoulderWidth;
    private Double shoulderCircumference;
    private Double chestCircumference;
    private Double waistCircumference;
    private Double hipCircumference;
    private Double calfCircumference;

    @OneToOne
    @JoinColumn(name = "athlete_id", nullable = false)
    private Athlete athlete;
}
