package org.mygoal.fitnessapp.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "training_feedbacks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    @ManyToOne
    @JoinColumn(name = "athlete_id", nullable = false)
    private Athlete athlete;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 500)
    private String comment;

}
